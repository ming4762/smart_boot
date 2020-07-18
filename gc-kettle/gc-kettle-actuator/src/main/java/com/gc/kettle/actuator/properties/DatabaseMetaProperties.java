package com.gc.kettle.actuator.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shizhongming
 * 2020/7/16 8:33 下午
 */
@Getter
@Setter
public class DatabaseMetaProperties {

    private String type = "mysql";

    private String access = "jdbc";
    private String name = "";

    private String host = "localhost";

    private String db = "";

    private String port = "3306";
    private String dbUser = "root";
    private String dbPassword = "";

    private String resUser = "admin";

    private String resPassword = "admin";
}
