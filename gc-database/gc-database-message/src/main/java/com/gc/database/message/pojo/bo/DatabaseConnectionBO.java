package com.gc.database.message.pojo.bo;

import com.gc.database.message.constants.DatabaseTypeConstant;
import com.gc.database.message.constants.ExceptionConstant;
import com.gc.database.message.exception.SmartDatabaseException;
import lombok.Builder;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * 数据库连接业务类
 * @author shizhongming
 * 2020/1/18 8:56 下午
 */
@Builder
@ToString
public class DatabaseConnectionBO extends AbstractDatabaseConnectionBaseBO {

    private static final long serialVersionUID = 8846621337726036907L;

    /**
     * 创建数据库连接key
     * @return 数据库连接的key
     */
    @NotNull
    public String createConnectionKey() {
        Assert.notNull(this.getDatabaseName(), "数据库名称不能为null");
        Assert.notNull(this.getUrl(), "数据库连接url不能为null");
        Assert.notNull(this.getUsername(), "数据库用户名不能为null");
        Assert.notNull(this.getPassword(), "数据库密码不能为null");
        return MessageFormat.format("{0}_{1}_{2}_{3}", this.getDatabaseName(), this.getUrl(), this.getUsername(), this.getPassword());
    }

    /**
     * 连接数据库
     * @return 数据库连接
     */
    public Connection connection() throws SQLException {
        final Driver driver = this.doGetDriver();
        final Properties properties = new Properties();
        properties.setProperty("user", this.getUsername());
        properties.setProperty("password", this.getPassword());
        final String url = String.format("%s/%s", this.getUrl(), this.getDatabaseName());
        return driver.connect(url, properties);
    }

    public Driver doGetDriver() throws SmartDatabaseException {
        final String driverClass = this.doGetDriverClass();
        try {
            final Class clazz = Class.forName(driverClass);
            return (Driver) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new SmartDatabaseException(ExceptionConstant.DIRVER_CLASS_NOT_FOUND, driverClass);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new SmartDatabaseException(ExceptionConstant.DIRVER_CLASS_INSTANCE, driverClass);
        }
    }

    public String doGetDriverClass() {
        return DatabaseTypeConstant.valueOf(this.getType()).getDriverClass();
    }
}
