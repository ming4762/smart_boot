package com.gc.database.message.pojo.bo;

import com.gc.database.message.constants.DatabaseTypeConstant;
import com.gc.database.message.constants.ExceptionConstant;
import com.gc.database.message.exception.SmartDatabaseException;
import com.gc.database.message.executor.DatabaseExecutor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;
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

    public Driver doGetDriver() {
        final String driverClass = this.doGetDriverClass();
        try {
            final Class clazz = Class.forName(driverClass);
            return (Driver) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new SmartDatabaseException(ExceptionConstant.DIRVER_CLASS_NOT_FOUND, driverClass);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new SmartDatabaseException(ExceptionConstant.DIRVER_CLASS_INSTANCE, driverClass);
        }
    }

    public String doGetDriverClass() {
        return this.getType().getDriverClass();
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
     * TODO: 目前是每次创建，后台改为spring管理或使用缓存获取
     * @return 数据库执行器
     */
    @SneakyThrows
    public DatabaseExecutor getDatabaseExecutor() {
        return this.getDatabaseExecutorClass().newInstance();
    }
}
