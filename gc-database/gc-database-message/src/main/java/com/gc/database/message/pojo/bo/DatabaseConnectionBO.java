package com.gc.database.message.pojo.bo;

import com.gc.common.base.spring.ApplicationContextRegister;
import com.gc.database.message.constants.DatabaseTypeConstant;
import com.gc.database.message.executor.DatabaseExecutor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.text.MessageFormat;

/**
 * 数据库连接业务类
 * @author shizhongming
 * 2020/1/18 8:56 下午
 */
@ToString
public class DatabaseConnectionBO extends AbstractDatabaseConnectionBaseBO {

    private static final long serialVersionUID = 8846621337726036907L;

    public DatabaseConnectionBO(String databaseName, DatabaseTypeConstant type, String url, String username, String password, String tableSchema) {
        super(databaseName, type, url, username, password, tableSchema);
    }

    /**
     * 创建数据库连接key
     * @return 数据库连接的key
     */
    @NonNull
    public String createConnectionKey() {
        Assert.notNull(this.getDatabaseName(), "数据库名称不能为null");
        Assert.notNull(this.getUrl(), "数据库连接url不能为null");
        Assert.notNull(this.getUsername(), "数据库用户名不能为null");
        Assert.notNull(this.getPassword(), "数据库密码不能为null");
        return MessageFormat.format("{0}_{1}_{2}_{3}", this.getDatabaseName(), this.getUrl(), this.getUsername(), this.getPassword());
    }

    /**
     * 获取数据库执行器类型
     * @return 数据库执行器Class
     */
    public Class<? extends DatabaseExecutor> getDatabaseExecutorClass() {
        return this.getType().getExecuterClass();
    }

    /**
     * 获取数据库执行器
     * @return 数据库执行器
     */
    @SneakyThrows
    public DatabaseExecutor getDatabaseExecutor() {
        final DatabaseExecutor databaseExecutor = ApplicationContextRegister.getBean(this.getDatabaseExecutorClass());
        Assert.notNull(databaseExecutor, "获取数据库执行器失败，spring上下文中未找到bean：" + this.getDatabaseExecutorClass());
        return databaseExecutor;
    }
}
