package com.gc.database.message.pojo.dbo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;

/**
 * 表格信息基础类
 * @author ShiZhongMing
 * 2020/7/25 17:05
 * @since 0.0.6
 */
@Getter
@Setter
public class AbstractTableBaseDO extends AbstractDatabaseBaseDO {
    private static final long serialVersionUID = -3056893832662371382L;

    @DatabaseField("TABLE_CAT")
    private String tableCat;

    @DatabaseField("TABLE_SCHEM")
    private String tableSchem;

    @DatabaseField("TABLE_NAME")
    private String tableName;
}
