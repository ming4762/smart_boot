package com.gc.database.message.pojo.bo;

import com.gc.database.message.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 主键业务类
 * @author shizhongming
 * 2020/1/20 8:54 下午
 */
@Getter
@Setter
@ToString
public class PrimaryKeyBO extends AbstractTableBaseBO {
    private static final long serialVersionUID = 3645913071310589071L;


    @DatabaseField("COLUMN_NAME")
    private String columnName;

    /**
     * 主键序号
     */
    @DatabaseField("KEY_SEQ")
    private Integer keySeq;

    /**
     * 主键名称
     */
    @DatabaseField("PK_NAME")
    private String pkName;
}
