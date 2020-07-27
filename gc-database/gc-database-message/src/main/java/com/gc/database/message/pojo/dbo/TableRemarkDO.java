package com.gc.database.message.pojo.dbo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;

/**
 * 表注释
 * @author ShiZhongMing
 * 2020/7/25 17:10
 * @since 1.0
 */
@Getter
@Setter
public class TableRemarkDO extends AbstractDatabaseBaseDO {
    private static final long serialVersionUID = 3904911515668284196L;

    @DatabaseField("TABLE_NAME")
    private String tableName;

    @DatabaseField("REMARK")
    private String remark;
}
