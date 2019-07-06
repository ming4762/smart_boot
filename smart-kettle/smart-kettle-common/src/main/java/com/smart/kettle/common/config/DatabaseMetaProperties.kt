package com.smart.kettle.common.config

/**
 *
 * kettle资源库连接信息
 * @author ming
 * 2019/7/3 下午8:24
 */
class DatabaseMetaProperties {

    var name = ""

    var type = "mysql"

    var access = "jdbc"

    lateinit var host: String

    lateinit var db: String

    var port = "3306"

    lateinit var dbUser: String

    lateinit var dbPassword: String

    var resUser = "admin"

    var resPassword = "admin"
}