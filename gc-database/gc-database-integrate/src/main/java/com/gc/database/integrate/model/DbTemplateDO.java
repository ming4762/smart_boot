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
 * 2021/2/12 8:45 下午
 */
@Getter
@Setter
@TableName("db_template")
@ToString
public class DbTemplateDO extends BaseModel {
    private static final long serialVersionUID = -3919849941490596203L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long templateId;

    private String templateName;

    private String templateContent;

    private String remark;

    private LocalDateTime createTime;
}
