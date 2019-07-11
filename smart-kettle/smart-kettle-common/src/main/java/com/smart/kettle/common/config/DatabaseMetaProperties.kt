package com.smart.kettle.common.config

/**
 *
 * kettle资源库连接信息
 * @author ming
 * 2019/7/3 下午8:24
 */
class DatabaseMetaProperties {

    var type = "mysql"
    var access = "jdbc"
    var name = ""
    var host: String = "localhost"

    var db: String = ""

    var port = "3306"

    var dbUser: String = "root"

    var dbPassword: String = ""

    var resUser = "admin"

    var resPassword = "admin"
}