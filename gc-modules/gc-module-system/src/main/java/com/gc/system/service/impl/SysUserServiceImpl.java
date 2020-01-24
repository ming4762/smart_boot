package com.gc.system.service.impl;

import com.gc.common.auth.model.SysUserPO;
import com.gc.common.auth.service.SysUserService;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import com.gc.system.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
 * @author jackson
 * 2020/1/23 7:44 下午
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUserPO> implements SysUserService {
}
