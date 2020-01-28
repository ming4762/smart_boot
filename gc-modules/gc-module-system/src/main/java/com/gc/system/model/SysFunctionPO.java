package com.gc.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jackson
 * 2020/1/27 12:13 下午
 */
@TableName("sys_function")
@Getter
@Setter
public class SysFunctionPO extends BaseModelUserTime {


    private static final long serialVersionUID = -4732658608405383250L;
    /**
     * 功能ID
     */
    private Long functionId;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 功能名称
     */
    private Long functionName;

    /**
     * 功能类型（10：目录 20：菜单 30：功能）
     */
    private String functionType;

    /**
     * 图标
     */
    private String icon;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * url
     */
    private String url;

    /**
     * 权限
     */
    private String premission;

    /**
     * 是否菜单
     */
    @TableField("is_menu")
    private Boolean menuIs;

    /**
     * 外链菜单打开方式 0/内部打开 1/外部打开
     */
    private Boolean internalOrExternal;

    /**
     * 是否配置数据权限
     */
    private Boolean dataRule;
}
