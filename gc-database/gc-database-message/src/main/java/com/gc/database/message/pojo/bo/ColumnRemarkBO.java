package com.gc.database.message.pojo.bo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shizhongming
 * 2020/7/5 8:32 下午
 */
@ToString
@Setter
@Getter
public class ColumnRemarkBO extends AbstractDatabaseBaseBO {
    private static final long serialVersionUID = 374924028298986492L;

    @DatabaseField("TABLE_NAME")
    private String tableName;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    @DatabaseField("REMARK")
    private String remark;
}
