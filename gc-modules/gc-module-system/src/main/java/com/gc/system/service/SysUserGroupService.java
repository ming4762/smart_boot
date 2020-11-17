package com.gc.system.service;

import com.gc.starter.crud.service.BaseService;
import com.gc.system.model.SysUserGroupPO;
import com.gc.system.model.SysUserPO;
import com.gc.system.pojo.dto.UserGroupUserSaveDTO;
import com.gc.system.pojo.dto.UserUserGroupSaveDTO;
import org.springframework.lang.NonNull;

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
     * @return 用户组ID包含的用户id集合
     */
    @NonNull
    Map<Long, List<Long>> listUserIdByIds(@NonNull Collection<Long> groupIds);

    /**
     * 查询用户组ID包含的用户集合
     * @param groupIds 用户组ID
     * @return 用户组ID包含的用户集合
     */
    @NonNull
    Map<Long, List<SysUserPO>> listUserByIds(@NonNull Collection<Long> groupIds);

    /**
     * 保存用户组的用户信息
     * @param parameter 查询参数
     * @return 用户组的用户信息
     */
    boolean saveUserGroupByGroupId(@NonNull UserGroupUserSaveDTO parameter);

    /**
     * 保存用户的用户组信息
     * @param parameter 参数
     * @return 结果
     */
    boolean saveUserGroupByUserId(@NonNull UserUserGroupSaveDTO parameter);
}
