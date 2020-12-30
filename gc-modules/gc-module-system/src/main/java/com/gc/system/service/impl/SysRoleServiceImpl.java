package com.gc.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gc.auth.core.utils.AuthUtils;
import com.gc.common.base.constants.TransactionManagerConstants;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.gc.system.mapper.SysRoleMapper;
import com.gc.system.model.SysRoleFunctionPO;
import com.gc.system.model.SysRolePO;
import com.gc.system.pojo.dto.role.RoleMenuSaveDTO;
import com.gc.system.service.SysRoleFunctionService;
import com.gc.system.service.SysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/24 2:20 下午
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRolePO> implements SysRoleService {

    private final SysRoleFunctionService sysRoleFunctionService;

    public SysRoleServiceImpl(SysRoleFunctionService sysRoleFunctionService) {
        this.sysRoleFunctionService = sysRoleFunctionService;
    }

    /**
     * 保存角色的菜单信息
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = TransactionManagerConstants.SYSTEM_MANAGER)
    public boolean saveRoleMenu(RoleMenuSaveDTO parameter) {
        // 删除
        this.sysRoleFunctionService.remove(
                new QueryWrapper<SysRoleFunctionPO>().lambda()
                .eq(SysRoleFunctionPO :: getRoleId, parameter.getRoleId())
        );
        // 保存
        if (CollectionUtils.isNotEmpty(parameter.getFunctionIdList())) {
            return this.sysRoleFunctionService.saveBatchWithUser(
                parameter.getFunctionIdList().stream().map(item -> {
                    final SysRoleFunctionPO sysRoleFunction = new SysRoleFunctionPO();
                    sysRoleFunction.setFunctionId(item);
                    sysRoleFunction.setRoleId(parameter.getRoleId());
                    return sysRoleFunction;
                }).collect(Collectors.toList()),
                    AuthUtils.getCurrentUserId()
            );
        }
        return true;
    }
}
