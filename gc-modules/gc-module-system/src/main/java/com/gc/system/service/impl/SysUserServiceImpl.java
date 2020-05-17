package com.gc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.auth.model.SysUserPO;
import com.gc.common.base.constants.TransactionManagerConstants;
import com.gc.common.base.exception.IllegalAccessRuntimeException;
import com.gc.common.base.exception.InvocationTargetRuntimeException;
import com.gc.common.base.exception.NoSuchMethodRuntimeException;
import com.gc.starter.crud.constants.UserPropertyConstants;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.gc.system.mapper.SysUserGroupRoleMapper;
import com.gc.system.mapper.SysUserGroupUserMapper;
import com.gc.system.mapper.SysUserMapper;
import com.gc.system.mapper.SysUserRoleMapper;
import com.gc.system.model.SysRolePO;
import com.gc.system.model.SysUserGroupRolePO;
import com.gc.system.model.SysUserGroupUserPO;
import com.gc.system.model.SysUserRolePO;
import com.gc.system.service.SysRoleService;
import com.gc.system.service.SysUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/23 7:44 下午
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserPO> implements SysUserService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserGroupUserMapper sysUserGroupUserMapper;

    @Autowired
    private SysUserGroupRoleMapper sysUserGroupRoleMapper;

    @Autowired
    private SysRoleService sysRoleService;

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
        final Set<Long> userRoleIdSet = this.sysUserRoleMapper.selectList(
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
}
