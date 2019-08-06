package com.smart.starter.fluorite.model.po

class PtzPO {

    /**
     * 设备号
     */
    var deviceSerial: String? = null

    // 通道号
    var channelNo: Int? = null

    // 操作命令：0-上，1-下，2-左，3-右，4-左上，5-左下，6-右上，7-右下，8-放大，9-缩小，10-近焦距，11-远焦距
    var direction: Int? = null

    // 云台速度：0-慢，1-适中，2-快
    var speed: Int = 1
}