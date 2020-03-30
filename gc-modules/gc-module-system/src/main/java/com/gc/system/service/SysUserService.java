package com.gc.system.service;

import com.gc.common.auth.service.AuthUserService;
import com.gc.system.model.SysRolePO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author jackson
 * 2020/1/24 3:49 下午
 */
public interface SysUserService extends AuthUserService {

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
}
