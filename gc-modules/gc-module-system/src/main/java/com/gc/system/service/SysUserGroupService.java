package com.gc.system.service;

import com.gc.common.auth.model.SysUserPO;
import com.gc.starter.crud.service.BaseService;
import com.gc.system.model.SysUserGroupPO;
import com.gc.system.pojo.dto.UserGroupUserSaveDTO;
import com.gc.system.pojo.dto.UserUserGroupSaveDTO;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jackson
 * 2020/1/24 3:04 下午
 */
public interface SysUserGroupService extends BaseService<SysUserGroupPO> {

    /**
     * 查询用户组ID包含的用户id集合
     * @param groupIds 用户组ID
     * @return
     */
    @NotNull
    Map<Long, List<Long>> listUserIdByIds(@NotNull Collection<Long> groupIds);

    /**
     * 查询用户组ID包含的用户集合
     * @param groupIds 用户组ID
     * @return
     */
    @NotNull
    Map<Long, List<SysUserPO>> listUserByIds(@NotNull Collection<Long> groupIds);

    /**
     * 保存用户组的用户信息
     * @param parameter
     * @return
     */
    boolean saveUserGroupByGroupId(@NotNull UserGroupUserSaveDTO parameter);

    /**
     * 保存用户的用户组信息
     * @param parameter
     * @return
     */
    boolean saveUserGroupByUserId(@NotNull UserUserGroupSaveDTO parameter);
}
