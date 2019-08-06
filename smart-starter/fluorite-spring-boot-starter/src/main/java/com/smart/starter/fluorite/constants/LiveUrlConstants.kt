package com.smart.starter.fluorite.constants

/**
 * 海康直播接口地址
 */
enum class LiveUrlConstants(var url: String) {

    // 获取用户下直播视频列表
    USER_LIVE_VIDEO_LIST("https://open.ys7.com/api/lapp/live/video/list"),

    // 获取指定有效期的直播地址
    LIVE_ADDRESS_LIMITED("https://open.ys7.com/api/lapp/live/address/limited"),

    // 开通直播功能
    LIVE_VIDEO_OPEN("https://open.ys7.com/api/lapp/live/video/open"),

    // 获取直播地址
    LIVE_ADDRESS_GET("https://open.ys7.com/api/lapp/live/address/get"),

    // 关闭直播功能
    LIVE_VIDEO_CLOSE("https://open.ys7.com/api/lapp/live/video/close")
}