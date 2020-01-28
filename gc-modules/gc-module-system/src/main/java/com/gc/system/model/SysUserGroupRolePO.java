package com.gc.system.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jackson
 * 2020/1/24 3:45 下午
 */
@TableName("sys_user_group_role")
@Getter
@Setter
public class SysUserGroupRolePO extends BaseModelCreateUserTime {

    private static final long serialVersionUID = -1506076132970865247L;
    /**
     * 用户组ID
     */
    @TableId
    private long groupId;

    /**
     * 角色ID
     */
    @TableId
    private long roleId;

    /**
     * 是否启用
     */
    private int enable;
}
