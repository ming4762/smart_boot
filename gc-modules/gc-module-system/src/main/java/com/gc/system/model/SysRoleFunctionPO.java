package com.gc.system.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gc.starter.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shizhongming
 * 2020/9/22 8:45 下午
 */
@Getter
@Setter
@TableName("sys_role_function")
public class SysRoleFunctionPO extends BaseModelCreateUserTime {
    private static final long serialVersionUID = -8303994182948294185L;

    @TableId
    private Long roleId;

    @TableId
    private Long functionId;
}
