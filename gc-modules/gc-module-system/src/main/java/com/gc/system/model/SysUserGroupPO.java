package com.gc.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author jackson
 * 2020/1/24 2:56 下午
 */
@TableName("sys_user_group")
@Getter
@Setter
public class SysUserGroupPO extends BaseModelUserTime {


    private static final long serialVersionUID = 7943564843713775352L;

    private static final String PREMISSION_GROUP = "sys:userGroup";
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long groupId;

    /**
     * 用户组名称
     */
    @NotNull(message = "用户组名称不能为空")
    private String groupName;

    /**
     * 用户组编码
     */
    @NotNull(message = "用户组编码不能为空")
    private String groupCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 是否启用
     */
    private Boolean enable;
}