package com.gc.database.integrate.service.impl;

import com.gc.database.integrate.mapper.DbTemplateMapper;
import com.gc.database.integrate.model.DbTemplateDO;
import com.gc.database.integrate.service.DbTemplateService;
import com.gc.starter.crud.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author shizhongming
 * 2021/2/12 8:48 下午
 */
@Service
public class DbTemplateServiceImpl extends BaseServiceImpl<DbTemplateMapper, DbTemplateDO> implements DbTemplateService {
}
