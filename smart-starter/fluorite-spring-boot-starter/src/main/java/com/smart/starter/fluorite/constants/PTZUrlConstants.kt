package com.smart.starter.fluorite.constants

/**
 * 云台控制url
 */
enum class PTZUrlConstants(var url: String) {
    // 开始云台控制
    PTZ_START("https://open.ys7.com/api/lapp/device/ptz/start"),
    // 停止云台控制
    PTZ_STOP("https://open.ys7.com/api/lapp/device/ptz/stop"),
    // 镜像翻转
    PTZ_MIRROR("https://open.ys7.com/api/lapp/device/ptz/mirror"),
    // 添加预置点
    PRESENT_ADD("https://open.ys7.com/api/lapp/device/preset/add"),
    // 调用预置点
    PRESENT_MOVE("https://open.ys7.com/api/lapp/device/preset/move"),
    // 清除预置点
    PRESENT_CLEAR("https://open.ys7.com/api/lapp/device/preset/clear")
}