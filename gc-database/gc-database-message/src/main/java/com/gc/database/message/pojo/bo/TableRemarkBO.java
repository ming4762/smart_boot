package com.gc.database.message.pojo.bo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * table 备注实体
 * @author shizhongming
 * 2020/7/5 6:37 下午
 */
@Getter
@Setter
@ToString
public class TableRemarkBO extends AbstractDatabaseBaseBO {
    private static final long serialVersionUID = 2047545478082450561L;

    @DatabaseField("TABLE_NAME")
    private String tableName;

    @DatabaseField("REMARK")
    private String remark;
}
