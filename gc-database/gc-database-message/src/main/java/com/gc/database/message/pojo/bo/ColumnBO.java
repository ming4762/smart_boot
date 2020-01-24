package com.gc.database.message.pojo.bo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;

/**
 * 列业务类
 * @author shizhongming
 * 2020/1/20 8:56 下午
 */
@Getter
@Setter
public class ColumnBO extends TableBaseBO {
    private static final long serialVersionUID = 6189047510460416526L;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    @DatabaseField("DATA_TYPE")
    private Integer dataType;

    @DatabaseField("TYPE_NAME")
    private String typeName;

    @DatabaseField("COLUMN_SIZE")
    private Integer columnSize;

    @DatabaseField("BUFFER_LENGTH")
    private Integer bufferLength;

    @DatabaseField("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @DatabaseField("NUM_PREC_RADIX")
    private Integer numPrecRadix;

    @DatabaseField("NULLABLE")
    private Integer nullable;

    @DatabaseField("REMARKS")
    private String remarks;

    @DatabaseField("COLUMN_DEF")
    private String columnDef;

    @DatabaseField("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @DatabaseField("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @DatabaseField("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @DatabaseField("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @DatabaseField("IS_NULLABLE")
    private String isNullable;

    @DatabaseField("SS_IS_SPARSE")
    private Integer ssIsSparse;

    @DatabaseField("SS_IS_COLUMN_SET")
    private Integer ssIsColumnSet;

    @DatabaseField("SS_IS_COMPUTED")
    private Integer ssIsComputed;

    @DatabaseField("IS_AUTOINCREMENT")
    private String autoincrement;

    @DatabaseField("SS_UDT_CATALOG_NAME")
    private String ssUdtCatalogName;

    @DatabaseField("SS_UDT_SCHEMA_NAME")
    private String ssUdtSchemaName;

    @DatabaseField("SS_UDT_ASSEMBLY_TYPE_NAME")
    private String ssUdtAssemblyTypeName;

    @DatabaseField("SS_XML_SCHEMACOLLECTION_CATALOG_NAME")
    private String ssXmlSchemacollectionCatalogName;

    @DatabaseField("SS_XML_SCHEMACOLLECTION_SCHEMA_NAME")
    private String ssXmlSchemacollectionSchemaName;

    @DatabaseField("SS_XML_SCHEMACOLLECTION_NAME")
    private String ssXmlSchemacollectionName;

    @DatabaseField("SS_DATA_TYPE")
    private Boolean ssDataType;

    // --------- 主键信息 -----------
    /**
     * 是否是主键
     */
    private Boolean primaryKey = Boolean.FALSE;

    /**
     * 主键序号
     */
    private Integer keySeq;

    /**
     * 主键名称
     */
    private String pkName;

    // -------- 外键信息 --------
    private Boolean importKey = Boolean.FALSE;

    private String importPkName;

    // -------- 索引信息 ---------

    // 是否是索引
    private Boolean indexed = Boolean.FALSE;

    // 是否是唯一索引
    private Boolean unique = Boolean.FALSE;

    // 索引类型
    private Integer indexType;

    // ------- 其他信息 -----------
    // java类型
    private String javaType;

    private String simpleJavaType;
}
