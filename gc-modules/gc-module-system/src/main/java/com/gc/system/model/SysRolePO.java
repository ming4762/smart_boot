package com.gc.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jackson
 * 2020/1/24 2:18 下午
 */
@TableName("sys_role")
@Getter
@Setter
public class SysRolePO extends BaseModelUserTime {

    private static final long serialVersionUID = 6200571838296972907L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 角色类型
     */
    private String roleType;


    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 序号
     */
    private Integer seq;
}
