package com.smart.portal.model

/**
 *
 * @author ming
 * 2019/8/23 上午9:55
 */
class NewsDTO : NewsDO() {

    var module: PortalModuleDO? = null

    // 附件列表
    var attachmentFileIdList: List<String>? = null
}