package com.smart.starter.fluorite.service

import com.smart.starter.fluorite.constants.LiveUrlConstants
import com.smart.starter.fluorite.utils.FluoriteRestUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 *
 * @author ming
 * 2019/8/6 下午4:08
 */
@Service
class LiveService {

    @Autowired
    private lateinit var fluoriteService: FluoriteService

    /**
     * 获取用户下直播视频列表
     */
    fun videoList(startPage: Int?, pageSize: Int?): Map<String, Any> {
        // 获取url
        var url = this.fluoriteService.getUrlWithToken(LiveUrlConstants.USER_LIVE_VIDEO_LIST.url)
        startPage?.let {
            url += "&startPage=$it"
        }
        pageSize?.let {
            url += "&startPage=$it"
        }
        return FluoriteRestUtil.post(url)
    }
}