package com.gc.database.message.pojo.bo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shizhongming
 * 2020/1/20 8:53 下午
 */
@Getter
@Setter
public abstract class AbstractTableBaseBO extends AbstractDatabaseBaseBO {

    private static final long serialVersionUID = 9010293595189493582L;
    @DatabaseField("TABLE_CAT")
    private String tableCat;

    @DatabaseField("TABLE_SCHEM")
    private String tableSchem;

    @DatabaseField("TABLE_NAME")
    private String tableName;
}
