package com.gc.system.service;

import com.gc.starter.crud.service.BaseService;
import com.gc.system.constants.FunctionTypeConstants;
import com.gc.system.model.SysFunctionPO;
import com.gc.system.model.SysRolePO;
import com.gc.system.model.SysUserPO;
import com.gc.system.pojo.dto.user.UserSetRoleDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author jackson
 * 2020/1/24 3:49 下午
 */
public interface SysUserService extends BaseService<SysUserPO> {

    /**
     * 查询用户的角色
     * @param userId 用户Id
     * @return 角色列表
     */
    @NonNull
    List<SysRolePO> listRole(@NonNull Long userId);

    /**
     * 设置人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    <T> void setWithUser(@NonNull List<T> resource);

    /**
     * 设置创建人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    <T> void setWithCreateUser(@NonNull List<T> resource);


    /**
     * 设置更新人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    <T> void setWithUpdateUser(@NonNull List<T> resource);

    /**
     * 查询用户菜单信息
     * @return 菜单列表
     */
    @NonNull
    List<SysFunctionPO> listCurrentUserMenu();

    /**
     * 查询用户功能
     * @param userId 用户ID
     * @param types 查询的功能类型
     * @return 用户ID表
     */
    List<SysFunctionPO> listUserFunction(@NonNull Long userId, List<FunctionTypeConstants> types);

    /**
     * 设置角色
     * @param parameter 参数
     * @return 是否这是成功
     */
    boolean setRole(@NonNull UserSetRoleDTO parameter);
}
