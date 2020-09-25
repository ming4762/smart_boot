package com.gc.system.pojo.bo;

import com.gc.system.model.SysUserGroupPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shizhongming
 * 2020/3/30 8:23 下午
 */
@Getter
@Setter
@ToString
public class SysUserGroupBO extends SysUserGroupPO {
    private static final long serialVersionUID = -331636901915341763L;

    private SysUserPO createUser;

    private SysUserPO updateUser;
}
