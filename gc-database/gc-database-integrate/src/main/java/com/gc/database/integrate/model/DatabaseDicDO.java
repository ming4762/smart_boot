package com.gc.database.integrate.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author shizhongming
 * 2020/11/1 5:27 下午
 */
@Getter
@Setter
@ToString
@TableName("gc_database_dic")
public class DatabaseDicDO extends BaseModel {
    private static final long serialVersionUID = -7127357033431026697L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String dicName;

    private Long templateId;

    private Long databaseId;

    private LocalDateTime createTime;

    private String remark;
}
