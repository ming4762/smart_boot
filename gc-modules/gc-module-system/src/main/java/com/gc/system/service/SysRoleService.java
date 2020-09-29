package com.gc.system.service;

import com.gc.starter.crud.service.BaseService;
import com.gc.system.model.SysRolePO;
import com.gc.system.pojo.dto.role.RoleMenuSaveDTO;

/**
 * @author jackson
 * 2020/1/24 2:20 下午
 */
public interface SysRoleService extends BaseService<SysRolePO> {

    /**
     * 保存角色的菜单信息
     * @param parameter 参数
     * @return 是否保存成功
     */
    boolean saveRoleMenu(RoleMenuSaveDTO parameter);
}
