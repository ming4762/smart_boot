package com.smart.portal.service.impl

import com.smart.portal.mapper.PortalMaterialGtMapper
import com.smart.portal.model.PortalMaterialGtDO
import com.smart.portal.service.PortalMaterialGtService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/8/1 下午2:46
 */
@Service
class PortalMaterialGtServiceImpl : BaseServiceImpl<PortalMaterialGtMapper, PortalMaterialGtDO>(), PortalMaterialGtService {
}