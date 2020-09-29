package com.gc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.auth.core.RestUserDetails;
import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.constants.TransactionManagerConstants;
import com.gc.common.base.exception.IllegalAccessRuntimeException;
import com.gc.common.base.exception.InvocationTargetRuntimeException;
import com.gc.common.base.exception.NoSuchMethodRuntimeException;
import com.gc.common.base.utils.security.Md5Utils;
import com.gc.starter.crud.constants.UserPropertyConstants;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.gc.system.constants.FunctionTypeConstants;
import com.gc.system.mapper.SysUserGroupRoleMapper;
import com.gc.system.mapper.SysUserGroupUserMapper;
import com.gc.system.mapper.SysUserMapper;
import com.gc.system.model.*;
import com.gc.system.pojo.dto.user.UserSetRoleDTO;
import com.gc.system.service.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/23 7:44 下午
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserPO> implements SysUserService {

    private final SysUserRoleService sysUserRoleService;

    private final SysUserGroupUserMapper sysUserGroupUserMapper;

    private final SysUserGroupRoleMapper sysUserGroupRoleMapper;

    private final SysRoleService sysRoleService;

    private final SysRoleFunctionService sysRoleFunctionService;

    @Autowired
    private SysFunctionService sysFunctionService;

    public SysUserServiceImpl(SysUserRoleService sysUserRoleService, SysUserGroupUserMapper sysUserGroupUserMapper, SysUserGroupRoleMapper sysUserGroupRoleMapper, SysRoleService sysRoleService, SysRoleFunctionService sysRoleFunctionService) {
        this.sysUserRoleService = sysUserRoleService;
        this.sysUserGroupUserMapper = sysUserGroupUserMapper;
        this.sysUserGroupRoleMapper = sysUserGroupRoleMapper;
        this.sysRoleService = sysRoleService;
        this.sysRoleFunctionService = sysRoleFunctionService;
    }

    /**
     * 查询用户的角色列表
     * @param userId 用户Id
     * @return 角色列表
     */
    @Override
    @Transactional(value = TransactionManagerConstants.SYSTEM_MANAGER, readOnly = true)
    public @NonNull
    List<SysRolePO> listRole(@NonNull Long userId) {
        final Set<Long> roleIdSet = Sets.newHashSet();
        // 1、查询用户对应的角色
        final Set<Long> userRoleIdSet = this.sysUserRoleService.list(
                new QueryWrapper<SysUserRolePO>().lambda()
                        .eq(SysUserRolePO :: getUserId, userId)
                        .eq(SysUserRolePO :: getEnable, Boolean.TRUE)
        ).stream().map(SysUserRolePO :: getRoleId).collect(Collectors.toSet());
        roleIdSet.addAll(userRoleIdSet);
        // 2、查询用户组的角色
        // 查询用户组
        final Set<Long> userGroupIdSet = this.sysUserGroupUserMapper.selectList(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                    .eq(SysUserGroupUserPO :: getUserId, userId)
                    .eq(SysUserGroupUserPO :: getEnable, Boolean.TRUE)
        ).stream().map(SysUserGroupUserPO :: getUserGroupId).collect(Collectors.toSet());
        // 通过用户组查询角色ID
        if (!userGroupIdSet.isEmpty()) {
            final Set<Long> groupRoleIdSet = this.sysUserGroupRoleMapper.selectList(
                    new QueryWrapper<SysUserGroupRolePO>().lambda()
                    .in(SysUserGroupRolePO :: getGroupId, userGroupIdSet)
                    .eq(SysUserGroupRolePO :: getEnable, Boolean.TRUE)
            ).stream().map(SysUserGroupRolePO :: getRoleId).collect(Collectors.toSet());
            roleIdSet.addAll(groupRoleIdSet);
        }
        if (roleIdSet.isEmpty()) {
            return Lists.newArrayList();
        }
        return this.sysRoleService.list(
                new QueryWrapper<SysRolePO>().lambda()
                .in(SysRolePO :: getRoleId, roleIdSet)
                .eq(SysRolePO :: getEnable, Boolean.TRUE)
        );
    }

    /**
     * 重写删除方法：删除用户关系
     * @param idList ID列表
     * @return 是否删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = TransactionManagerConstants.SYSTEM_MANAGER)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 删除用户与用户组管理
        this.sysUserGroupUserMapper.delete(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                .in(SysUserGroupUserPO :: getUserId, idList)
        );
        // 删除用户与角色关系
        this.sysUserRoleService.remove(
                new QueryWrapper<SysUserRolePO>().lambda()
                .in(SysUserRolePO::getUserId, idList)
        );
        return super.removeByIds(idList);
    }

    /**
     * 重新保存方法设置密码
     * @param entity 实体类
     * @return
     */
    @Override
    public boolean save(@NonNull SysUserPO entity) {
        entity.setPassword(Md5Utils.md5(entity.getUsername()));
        return super.save(entity);
    }

    /**
     * 设置人员信息
     * @param resource 原
     * @param <T> 目标类型
     * @return list目标类型
     */
    @Override
    public <T> void setWithUser(@NonNull List<T> resource) {
        this.setWithUser(resource, true, true);
    }

    /**
     * 设置创建人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    @Override
    public <T> void setWithCreateUser(@NonNull List<T> resource) {
        this.setWithUser(resource, true, false);
    }

    /**
     * 设置更新人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    @Override
    public <T> void setWithUpdateUser(@NonNull List<T> resource) {
        this.setWithUser(resource, false, true);
    }

    /**
     * 设置人员信息
     * @param resource 原
     * @param <T> 目标类型
     */
    private <T> void setWithUser(@NonNull List<T> resource, boolean withCreateUser, boolean withUpdateUser) {
        if (!resource.isEmpty()) {
            Set<Long> userIdSet = Sets.newHashSet();
            try {
                for (T item : resource) {
                    if (withCreateUser) {
                        // 获取创建人员ID
                        userIdSet.add((Long) PropertyUtils.getProperty(item, UserPropertyConstants.CREATE_USER_ID.getName()));
                    }
                    if (withUpdateUser) {
                        // 获取创建人员ID
                        userIdSet.add((Long) PropertyUtils.getProperty(item, UserPropertyConstants.UPDATE_USER_ID.getName()));
                    }
                }
                userIdSet = userIdSet.stream().filter(item -> !Objects.isNull(item)).collect(Collectors.toSet());
                if (!userIdSet.isEmpty()) {
                    // 查询人员信息
                    Map<Long, SysUserPO> userMap = this.listByIds(userIdSet).stream()
                            .collect(Collectors.toMap(SysUserPO :: getUserId, item -> item));
                    for (T item : resource) {
                        if (withCreateUser) {
                            Long createUserId = (Long) PropertyUtils.getProperty(item, UserPropertyConstants.CREATE_USER_ID.getName());
                            PropertyUtils.setProperty(item, UserPropertyConstants.CREATE_USER.getName(), userMap.get(createUserId));
                        }
                        if (withUpdateUser) {
                            Long updateUserId = (Long) PropertyUtils.getProperty(item, UserPropertyConstants.UPDATE_USER_ID.getName());
                            PropertyUtils.setProperty(item, UserPropertyConstants.UPDATE_USER.getName(), userMap.get(updateUserId));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new InvocationTargetRuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodRuntimeException(e);
            }
        }
    }

    /**
     * 查询用户菜单信息
     * @return 菜单列表
     */
    @NonNull
    @Override
    @Transactional(value = TransactionManagerConstants.SYSTEM_MANAGER, readOnly = true, rollbackFor = Exception.class)
    public List<SysFunctionPO> listCurrentUserMenu() {
        // 获取当前用户的角色信息
        final RestUserDetails userDetails = AuthUtils.getCurrentUser();
        if (Objects.isNull(userDetails)) {
            return Lists.newArrayList();
        }
        return this.listUserFunction(userDetails.getUserId(), ImmutableList.of(FunctionTypeConstants.CATALOG, FunctionTypeConstants.MENU));
    }

    /**
     * 查询用户功能
     * @param userId 用户ID
     * @param types 查询的功能类型
     * @return 用户ID表
     */
    @Override
    @Transactional(value = TransactionManagerConstants.SYSTEM_MANAGER, readOnly = true, rollbackFor = Exception.class)
    public List<SysFunctionPO> listUserFunction(@NonNull Long userId, @NonNull List<FunctionTypeConstants> types) {
        // 查询角色信息
        final Set<Long> roleIds = this.listRole(userId)
                .stream().map(SysRolePO::getRoleId)
                .collect(Collectors.toSet());
        if (roleIds.isEmpty()) {
            return Lists.newArrayList();
        }
        // 查询功能ID
        final Set<Long> functionIds = this.sysRoleFunctionService.list(
                new QueryWrapper<SysRoleFunctionPO>().lambda()
                        .select(SysRoleFunctionPO :: getFunctionId)
                        .in(SysRoleFunctionPO :: getRoleId, roleIds)
        ).stream().map(SysRoleFunctionPO :: getFunctionId).collect(Collectors.toSet());
        if (functionIds.isEmpty()) {
            return Lists.newArrayList();
        }
        // 查询功能信息
        final LambdaQueryWrapper<SysFunctionPO> queryWrapper = new QueryWrapper<SysFunctionPO>().lambda()
                .in(SysFunctionPO :: getFunctionId, functionIds);
        if (CollectionUtils.isNotEmpty(types)) {
            queryWrapper.in(SysFunctionPO :: getFunctionType, types.stream().map(FunctionTypeConstants :: getValue).collect(Collectors.toList()));
        }
        return this.sysFunctionService.list(queryWrapper);
    }

    /**
     * 设置角色
     * @param parameter 参数
     * @return 是否这是成功
     */
    @Override
    @Transactional(value = TransactionManagerConstants.SYSTEM_MANAGER, rollbackFor = Exception.class)
    public boolean setRole(@NonNull UserSetRoleDTO parameter) {
        // 删除用户角色关系
        this.sysUserRoleService.remove(
                new QueryWrapper<SysUserRolePO>().lambda().eq(SysUserRolePO :: getUserId, parameter.getUserId())
        );
        // 保存用户角色关系
        if (CollectionUtils.isNotEmpty(parameter.getRoleIdList())) {
            this.sysUserRoleService.saveBatchWithUser(
                    parameter.getRoleIdList().stream().map(roleId -> SysUserRolePO.builder()
                            .roleId(roleId)
                            .enable(Boolean.TRUE)
                            .userId(parameter.getUserId())
                            .build()).collect(Collectors.toList()),
                    AuthUtils.getCurrentUserId()
            );
        }
        return true;
    }
}
