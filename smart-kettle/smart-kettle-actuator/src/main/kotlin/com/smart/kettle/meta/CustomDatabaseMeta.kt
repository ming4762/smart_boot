package com.smart.kettle.meta

import org.pentaho.di.core.database.DatabaseMeta

/**
 *
 * @author ming
 * 2019/7/12 下午2:51
 */
class CustomDatabaseMeta : DatabaseMeta {

    constructor(): super()

    constructor(name: String, type: String, access: String, host: String, db: String, port: String, user: String, pass: String): super(name, type, access, host, db, port, user, pass)

    /**
     * 重写获取数据库驱动
     * 解决mysql驱动包版本冲突
     */
    override fun getDriverClass(): String {
        var driverClass = super.getDriverClass()
        if (driverClass == "org.gjt.mm.mysql.Driver") {
            driverClass = "com.mysql.cj.jdbc.Driver"
        }
        return driverClass
    }

    /**
     * 重写获取url方法，设置时区
     */
    override fun getURL(): String {
        var url = super.getURL()
        if (this.databaseInterface.pluginName == "MySQL") {
            url += "&serverTimezone=GMT%2B8"
        }
        return url
    }
}