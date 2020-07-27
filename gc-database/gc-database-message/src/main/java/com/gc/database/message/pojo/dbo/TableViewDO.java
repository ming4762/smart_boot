package com.gc.database.message.pojo.dbo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;

/**
 * table view DO类
 * @author ShiZhongMing
 * 2020/7/25 16:30
 * @since 1.0
 */
@Getter
@Setter
public class TableViewDO extends AbstractTableBaseDO {
    private static final long serialVersionUID = -5083921132717774974L;

    @DatabaseField("TABLE_TYPE")
    private String tableType;

    @DatabaseField("REMARKS")
    private String remarks;

    @DatabaseField("TYPE_CAT")
    private String typeCat;

    @DatabaseField("TYPE_SCHEM")
    private String typeSchem;

    @DatabaseField("TYPE_NAME")
    private String typeName;

    @DatabaseField("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @DatabaseField("REF_GENERATION")
    private String refGeneration;
}
