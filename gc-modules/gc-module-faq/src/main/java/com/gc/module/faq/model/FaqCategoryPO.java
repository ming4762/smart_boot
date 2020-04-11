package com.gc.module.faq.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shizhongming
 * 2020/4/11 11:32 上午
 */
@Getter
@Setter
@TableName("faq_category")
public class FaqCategoryPO extends BaseModelCreateUserTime {
    private static final long serialVersionUID = 6127265174027913760L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long categoryId;

    private String categoryName;

    private Long parentId;

    private Date createTime;

    private Date updateTime;

    private Integer seq;

    private Boolean enable;
}
