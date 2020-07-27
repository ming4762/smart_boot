package com.gc.database.message.pojo.dbo;

import com.gc.database.message.annotation.DatabaseField;
import com.gc.database.message.converter.BigDecimalToIntegerConverter;
import lombok.Getter;
import lombok.Setter;

/**
 * 字段DO
 * @author ShiZhongMing
 * 2020/7/25 17:17
 * @since 0.0.6
 */
@Getter
@Setter
public class ColumnDO extends AbstractTableBaseDO {
    private static final long serialVersionUID = -617702057646747452L;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    @DatabaseField(value = "DATA_TYPE", converter = BigDecimalToIntegerConverter.class)
    private Integer dataType;

    @DatabaseField("TYPE_NAME")
    private String typeName;

    @DatabaseField(value = "COLUMN_SIZE", converter = BigDecimalToIntegerConverter.class)
    private Integer columnSize;

    @DatabaseField(value = "BUFFER_LENGTH", converter = BigDecimalToIntegerConverter.class)
    private Integer bufferLength;

    @DatabaseField(value = "DECIMAL_DIGITS", converter = BigDecimalToIntegerConverter.class)
    private Integer decimalDigits;

    @DatabaseField(value = "NUM_PREC_RADIX", converter = BigDecimalToIntegerConverter.class)
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
}
