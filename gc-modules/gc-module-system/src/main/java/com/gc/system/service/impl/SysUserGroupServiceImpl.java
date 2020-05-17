package com.gc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.common.auth.model.SysUserPO;
import com.gc.common.base.constants.TransactionManagerConstants;
import com.gc.common.base.utils.BeanUtils;
import com.gc.starter.crud.constants.CrudConstants;
import com.gc.starter.crud.query.PageQueryParameter;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.gc.system.mapper.SysUserGroupMapper;
import com.gc.system.model.SysUserGroupPO;
import com.gc.system.model.SysUserGroupUserPO;
import com.gc.system.pojo.bo.SysUserGroupBO;
import com.gc.system.pojo.dto.UserGroupUserSaveDTO;
import com.gc.system.pojo.dto.UserUserGroupSaveDTO;
import com.gc.system.service.SysUserGroupService;
import com.gc.system.service.SysUserGroupUserService;
import com.gc.system.service.SysUserService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/24 3:05 下午
 */
@Service
public class SysUserGroupServiceImpl extends BaseServiceImpl<SysUserGroupMapper, SysUserGroupPO> implements SysUserGroupService {

    @Autowired
    private SysUserGroupUserService sysUserGroupUserService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询用户组ID包含的用户id集合
     * @param groupIds 用户组ID
     * @return
     */
    @Override
    @NonNull
    public Map<Long, List<Long>> listUserIdByIds(@NonNull Collection<Long> groupIds) {
        if (groupIds.isEmpty()) {
            return Maps.newHashMap();
        }
        // 查询用户组-用户信息
        final List<SysUserGroupUserPO> sysUserGroupUserList = this.sysUserGroupUserService.list(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                .in(SysUserGroupUserPO :: getUserGroupId, groupIds)
        );
        if (!sysUserGroupUserList.isEmpty()) {
            // 分组转换
            return sysUserGroupUserList.stream()
                    .collect(Collectors.groupingBy(SysUserGroupUserPO :: getUserGroupId, Collectors.mapping(SysUserGroupUserPO::getUserId, Collectors.toList())));
        }
        return Maps.newHashMap();
    }

    /**
     * 重写查询方法
     * @param queryWrapper 查询参数
     * @param parameter 参数
     * @param paging 是否分页
     * @return
     */
    @Override
    public @NonNull List<SysUserGroupPO> list(@NonNull QueryWrapper<SysUserGroupPO> queryWrapper, @NonNull PageQueryParameter<String, Object> parameter, @NonNull Boolean paging) {
        List<SysUserGroupPO> list =  super.list(queryWrapper, parameter, paging);
        if (list.isEmpty()) {
            return list;
        }
        Object withAll = parameter.get(CrudConstants.WITH_ALL.name());
        if (Objects.equals(withAll, Boolean.TRUE)) {
            List boList = BeanUtils.copyCollection(list, SysUserGroupBO.class);
            this.sysUserService.setWithUser(boList);
            return boList;
        }
        return list;
    }

    /**
     * 查询用户组ID包含的用户集合
     * @param groupIds 用户组ID
     * @return
     */
    @Override
    @NonNull
    public Map<Long, List<SysUserPO>> listUserByIds(@NonNull Collection<Long> groupIds) {
        // 查询用户ID信息
        final Map<Long, List<Long>> idResult = this.listUserIdByIds(groupIds);
        if (!idResult.isEmpty()) {
            // 获取人员ID
            final Set<Long> userIds = Sets.newHashSet();
            idResult.forEach((key, value) -> userIds.addAll(value));
            if (!userIds.isEmpty()) {
                // 查询人员信息并转为map
                final Map<Long, SysUserPO> userMap = this.sysUserService.listByIds(userIds)
                        .stream()
                        .collect(Collectors.toMap(SysUserPO::getUserId, item -> item));
                if (!userMap.isEmpty()) {
                    final Map<Long, List<SysUserPO>> result = Maps.newHashMap();
                    // 通过idResult， userMap组装结果
                    idResult.forEach((key, value) -> result.put(key,
                            value.stream().
                                    map(userMap::get)
                                    .filter(ObjectUtils :: isNotEmpty)
                                    .collect(Collectors.toList())));
                    return result;
                }
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 保存用户组的用户信息
     * @param parameter
     * @return
     */
    @Override
    @Transactional(value = TransactionManagerConstants.SYSTEM_MANAGER, rollbackFor = Exception.class, readOnly = true)
    public boolean saveUserGroupByGroupId(@NonNull UserGroupUserSaveDTO parameter) {
        // 删除用户组用户信息信息
        this.sysUserGroupUserService.remove(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                .eq(SysUserGroupUserPO :: getUserGroupId, parameter.getGroupId())
        );
        // 保存用户的用户组信息
        final List<SysUserGroupUserPO> sysUserGroupUserList = parameter.getUserIdList().stream()
                .distinct()
                .map(item -> SysUserGroupUserPO.builder()
                        .userGroupId(parameter.getGroupId())
                        .userId(item)
                        .enable(Boolean.TRUE)
                        .build()
                ).collect(Collectors.toList());
        return this.sysUserGroupUserService.saveBatch(sysUserGroupUserList);
    }

    /**
     * 保存用户的用户组信息
     * @param parameter
     * @return
     */
    @Override
    @Transactional(value = TransactionManagerConstants.SYSTEM_MANAGER, rollbackFor = Exception.class)
    public boolean saveUserGroupByUserId(@NonNull UserUserGroupSaveDTO parameter) {
        // 删除用户组用户信息信息
        this.sysUserGroupUserService.remove(
                new QueryWrapper<SysUserGroupUserPO>().lambda()
                    .eq(SysUserGroupUserPO :: getUserId, parameter.getUserId())
        );
        // 保存用户的用户组信息
        final List<SysUserGroupUserPO> sysUserGroupUserList = parameter.getGroupIdList().stream()
                .distinct()
                .map(item -> SysUserGroupUserPO.builder()
                        .userId(parameter.getUserId())
                        .userGroupId(item)
                        .enable(Boolean.TRUE)
                        .build()
                ).collect(Collectors.toList());
        return this.sysUserGroupUserService.saveBatch(sysUserGroupUserList);
    }
}
