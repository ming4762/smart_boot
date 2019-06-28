package com.smart.common.model

/**
 * GPS实体类
 * @author ming
 * 2019/6/12 上午10:02
 */
class Gps(wgLat: Double, wgLon: Double) {

    var wgLat: Double = 0.toDouble()
    var wgLon: Double = 0.toDouble()

    init {
        this.wgLat = wgLat
        this.wgLon = wgLon
    }

    override fun toString(): String {
        return "$wgLat,$wgLon"
    }
}