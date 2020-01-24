package com.gc.system.controller;

import com.gc.common.auth.utils.AuthUtils;
import com.gc.common.base.message.Result;
import com.gc.starter.crud.controller.BaseController;
import com.gc.system.model.SysUserGroupPO;
import com.gc.system.service.SysUserGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户组层
 * @author jackson
 * 2020/1/24 3:10 下午
 */
@RestController
@RequestMapping("sys/userGroup")
public class SysUserGroupController extends BaseController<SysUserGroupService, SysUserGroupPO> {

    @PostMapping("saveUpdate")
    @PreAuthorize("hasPermission('#{SysUserGroupPO.PREMISSION_GROUP}', 'save') or hasPermission('#{SysUserGroupPO.PREMISSION_GROUP}', 'update')")
    protected Result<Boolean> saveUpdate(@RequestBody @Valid SysUserGroupPO model, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return Result.failure(result);
        }
        return Result.success(this.service.saveOrUpdateWithAllUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PreAuthorize("hasPermission('#{SysUserGroupPO.PREMISSION_GROUP}', 'delete')")
    @PostMapping("batchDelete")
    protected Result<Integer> batchDelete(List<SysUserGroupPO> modelList) throws Exception {
        return super.batchDelete(modelList);
    }

    /**
     * 执行删除
     * @param idList
     * @return
     */
    @PreAuthorize("hasPermission('#{SysUserGroupPO.PREMISSION_GROUP}', 'delete')")
    @PostMapping("batchDeleteById")
    public Result<Boolean> batchDeleteById(List<Long> idList) {
        return Result.success(this.service.removeByIds(idList));
    }

    @Override
    @PostMapping("list")
    @PreAuthorize("hasPermission('#{SysUserGroupPO.PREMISSION_GROUP}', 'query')")
    protected Result<Object> list(Map<String, Object> parameter) {
        return super.list(parameter);
    }
}
