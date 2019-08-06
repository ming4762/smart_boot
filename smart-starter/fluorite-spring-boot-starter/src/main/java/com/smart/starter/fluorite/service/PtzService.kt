package com.smart.starter.fluorite.service

import com.smart.starter.fluorite.constants.PTZUrlConstants
import com.smart.starter.fluorite.model.po.PtzPO
import com.smart.starter.fluorite.utils.FluoriteRestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 云台控制服务层
 * @author ming
 * 2019/8/6 下午4:07
 */
@Service
class PtzService {

    @Autowired
    private lateinit var fluoriteService: FluoriteService

    /**
     * 开始云台控制
     */
    fun start(ptzPO: PtzPO) {
        val url = FluoriteRestUtil.splicingUrl(this.fluoriteService.getUrlWithToken(PTZUrlConstants.PTZ_START.url), mapOf(
                "deviceSerial" to ptzPO.deviceSerial!!,
                "channelNo" to ptzPO.channelNo!!,
                "direction" to ptzPO.direction!!,
                "speed" to ptzPO.speed
        ))
        FluoriteRestUtil.post(url)
    }

    /**
     * 停止云台控制
     */
    fun stop(ptzPO: PtzPO) {
        val parameter = mutableMapOf<String, Any>(
                "deviceSerial" to ptzPO.deviceSerial!!,
                "channelNo" to ptzPO.channelNo!!
        )
        ptzPO.direction?.let {
            parameter["direction"] = it
        }
        val url = FluoriteRestUtil.splicingUrl(this.fluoriteService.getUrlWithToken(PTZUrlConstants.PTZ_STOP.url), parameter)
        FluoriteRestUtil.post(url)
    }
}