package com.gc.database.message.pojo.bo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 索引信息
 * @author shizhongming
 * 2020/1/20 9:11 下午
 */
@Getter
@Setter
@ToString
public class IndexBO extends AbstractTableBaseBO {
    private static final long serialVersionUID = -6827286891209056960L;

    @DatabaseField("NON_UNIQUE")
    private Integer nonUnique;

    @DatabaseField("INDEX_QUALIFIER")
    private String indexQualifier;

    @DatabaseField("INDEX_NAME")
    private String indexName;

    @DatabaseField("TYPE")
    private Integer type;

    @DatabaseField("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    @DatabaseField("ASC_OR_DESC")
    private String ascOrDesc;

    @DatabaseField("CARDINALITY")
    private Integer cardinality;

    @DatabaseField("PAGES")
    private Integer pages;

    @DatabaseField("FILTER_CONDITION")
    private String filterCondition;
}
