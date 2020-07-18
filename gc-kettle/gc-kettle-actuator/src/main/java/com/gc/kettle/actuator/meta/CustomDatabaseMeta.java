package com.gc.kettle.actuator.meta;

import org.pentaho.di.core.database.DatabaseMeta;

/**
 * @author shizhongming
 * 2020/7/16 8:24 下午
 */
public class CustomDatabaseMeta extends DatabaseMeta {

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
        if ("org.gjt.mm.mysql.Driver".equals(driverClass)) {
            driverClass = "com.mysql.cj.jdbc.Driver";
        }
        return driverClass;
    }
}
