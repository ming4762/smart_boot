package com.gc.kettle.actuator.meta;

import com.gc.kettle.actuator.constants.DatabaseTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;

/**
 * @author shizhongming
 * 2020/7/16 8:24 下午
 */
public class CustomDatabaseMeta extends DatabaseMeta {
    /**
     * 旧的mysql驱动
     */
    private final String OLD_MYSQL_DRIVER = "org.gjt.mm.mysql.Driver";

    /**
     * 新的mysql驱动
     */
    private final String NEW_MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * mysql servertimezone
     */
    private final String MYSQL_TIME_ZONE = "&serverTimeZone=GMT%2B8";


//    private String name;
//
//    private String type;
//    private String access;
//    private String host;
//    private String db;
//
//    private String port;
//    private String user;
//    private String pass;

    public CustomDatabaseMeta(String name, String type, String access, String host, String db, String port, String user, String pass) {
        super(name, type, access, host, db, port, user, pass);
//        this.name = name;
//        this.type = type;
//        this.access = access;
//        this.host = host;
//        this.db = db;
//        this.port = port;
//        this.pass = pass;
    }

    /**
     * 重写获取数据库驱动
     * 解决mysql驱动包版本冲突
     */
    @Override
    public String getDriverClass() {
        String driverClass = super.getDriverClass();
        if (OLD_MYSQL_DRIVER.equals(driverClass)) {
            driverClass = NEW_MYSQL_DRIVER;
        }
        return driverClass;
    }

    /**
     * 重写getURL函数，解决mysql serverTimeZone问题
     * @return url
     * @throws KettleDatabaseException KettleDatabaseException
     */
    @Override
    public String getURL() throws KettleDatabaseException {
        String url = super.getURL();
        if (StringUtils.equals(this.getDatabaseInterface().getPluginName(), DatabaseTypeEnum.MySql.name())) {
            url += MYSQL_TIME_ZONE;
        }
        return url;
    }
}
