package com.smart.common.utils

import com.smart.common.model.Gps

/**
 * GPS 工具类
 * @author ming
 * 2019/6/12 上午10:02
 */
object GpsUtil {

    private val pi = 3.1415926535897932384626
    private val a = 6378245.0
    private val ee = 0.00669342162296594323


    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     */
    fun gcj02ToBd09(gg_lat: Double, gg_lon: Double): Gps {
        val z = Math.sqrt(gg_lon * gg_lon + gg_lat * gg_lat) + 0.00002 * Math.sin(gg_lat * pi)
        val theta = Math.atan2(gg_lat, gg_lon) + 0.000003 * Math.cos(gg_lon * pi)
        val bd_lon = z * Math.cos(theta) + 0.0065
        val bd_lat = z * Math.sin(theta) + 0.006
        return Gps(bd_lat, bd_lon)
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 BD-09 坐标转换成GCJ-02 坐标
     */
    fun bd09ToGcj02(bd_lat: Double, bd_lon: Double): Gps {
        val x = bd_lon - 0.0065
        val y = bd_lat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi)
        val gg_lon = z * Math.cos(theta)
        val gg_lat = z * Math.sin(theta)
        return Gps(gg_lat, gg_lon)
    }

    /**
     * 地球坐标系 (WGS84) 与火星坐标系 (GCJ-02) 的转换算法 将 WGS84 坐标转换成 GCJ-02 坐标
     */
    fun gps84ToGcj02(lat: Double, lon: Double): Gps? {
        if (outOfChina(lat, lon)) {
            return null
        }
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = Math.sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLon = dLon * 180.0 / (a / sqrtMagic * Math.cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return Gps(mgLat, mgLon)
    }

    /**
     * 地球坐标系 (WGS84) 与火星坐标系 (GCJ-02) 的转换算法 将 GCJ-02 坐标转换成 WGS84 坐标
     */
    fun gcjToGps84(lat: Double, lon: Double): Gps {
        val gps = transform(lat, lon)
        val lontitude = lon * 2 - gps.wgLon
        val latitude = lat * 2 - gps.wgLat
        return Gps(latitude, lontitude)
    }

    /**
     * 百度坐标转为地球坐标系
     * @param lat
     * @param lon
     * @return
     */
    fun bd09ToGps84(lat: Double, lon: Double): Gps {
        val gps = bd09ToGcj02(lat, lon)
        return gcjToGps84(gps.wgLat, gps.wgLon)
    }

    fun outOfChina(lat: Double, lon: Double): Boolean {
        if (lon < 72.004 || lon > 137.8347) return true
        return lat < 0.8293 || lat > 55.8271
    }

    private fun transform(lat: Double, lon: Double): Gps {
        if (outOfChina(lat, lon)) return Gps(lat, lon)
        var dLat = transformLat(lon - 105.0, lat - 35.0)
        var dLon = transformLon(lon - 105.0, lat - 35.0)
        val radLat = lat / 180.0 * pi
        var magic = Math.sin(radLat)
        magic = 1 - ee * magic * magic
        val sqrtMagic = Math.sqrt(magic)
        dLat = dLat * 180.0 / (a * (1 - ee) / (magic * sqrtMagic) * pi)
        dLon = dLon * 180.0 / (a / sqrtMagic * Math.cos(radLat) * pi)
        val mgLat = lat + dLat
        val mgLon = lon + dLon
        return Gps(mgLat, mgLon)
    }

    private fun transformLat(x: Double, y: Double): Double {
        var ret = (-100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x)))
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0
        return ret
    }

    private fun transformLon(x: Double, y: Double): Double {
        var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x))
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0
        return ret
    }
}