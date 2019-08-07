package com.smart.portal.service.impl

import com.smart.portal.mapper.PortalPicMapper
import com.smart.portal.model.PortalPicDO
import com.smart.portal.service.PortalPicService
import com.smart.starter.crud.service.impl.BaseServiceImpl
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/8/1 下午3:46
 */
@Service
class PortalPicServiceImpl : BaseServiceImpl<PortalPicMapper, PortalPicDO>(), PortalPicService {
}