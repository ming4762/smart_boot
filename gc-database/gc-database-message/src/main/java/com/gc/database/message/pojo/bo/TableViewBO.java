package com.gc.database.message.pojo.bo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 数据库表实体
 * @author shizhongming
 * 2020/1/18 8:52 下午
 */
@Getter
@Setter
@ToString
public class TableViewBO extends AbstractTableBaseBO {
    private static final long serialVersionUID = 763710787360425049L;

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

    private List<ColumnBO> columnList;
}
