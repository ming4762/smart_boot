package com.smart.common.utils

import org.springframework.util.StringUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 时间工具类
 * @author ming
 * 2019/6/12 上午9:43
 */
object DateUtil {
    /**
     * 以小时作为事件间隔
     */
    val INTERVAL_TYPE_HOUR = "hour"

    /**
     * 格式化日期
     * @param dateStr String 字符型日期
     * @param format String 格式
     * @return Date 日期
     */
    @JvmStatic
    @Throws(ParseException::class)
    fun parseDate(dateStr: String, format: String): Date {
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.parse(dateStr)
    }

    /**
     * 时间格式化
     * @param date 需要格式化的时间
     * @param formatValue 格式化
     * @return 格式化后的时间字符串
     */
    @JvmStatic
    fun formatTime(date: Date, formatValue: String): String {
        val dateList = ArrayList<Date>()
        dateList.add(date)
        return batchFormatTime(dateList, formatValue)[0]
    }

    /**
     * 字符串转为日期
     */
    @JvmStatic
    fun convertDate(dateStr: String?): Date? {
        var isUTC = false
        if (StringUtils.isEmpty(dateStr)) return null
        dateStr as String
        if (dateStr.contains("CST")) {
            return SimpleDateFormat().parse(dateStr)
        }
        var dateDealStr = dateStr.replace("年", "-").replace("月", "-").replace("日", "")
                .replace("/", "-").replace("\\.", "-").trim()
        var dateFormatStr = ""
        // 确定日期个税
        if (Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}.*").matcher(dateDealStr).matches()) {
            dateFormatStr = "yyyy-MM-dd"
        } else if (Pattern.compile("^[0-9]{4}-[0-9]{1}-[0-9]+.*||^[0-9]{4}-[0-9]+-[0-9]{1}.*").matcher(dateDealStr).matches()) {
            dateFormatStr = "yyyy-M-d"
        } else if (Pattern.compile("^[0-9]{2}-[0-9]{2}-[0-9]{2}.*").matcher(dateDealStr).matches()) {
            dateFormatStr = "yy-MM-dd"
        } else if (Pattern.compile("^[0-9]{2}-[0-9]{1}-[0-9]+.*||^[0-9]{2}-[0-9]+-[0-9]{1}.*").matcher(dateDealStr).matches()) {
            dateFormatStr = "yy-M-d"
        }

        //确定时间格式
        if(Pattern.compile(".*[ ][0-9]{2}").matcher(dateDealStr).matches()){
            dateFormatStr += " HH"
        }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}").matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm"
        }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}").matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm:ss"
        }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{0,3}").matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm:ss:sss"
        }

        if (Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{0,3}Z").matcher(dateDealStr).matches()) {
            dateDealStr = dateDealStr.replace("Z", " UTC")
            dateFormatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS Z"
            isUTC = true
        }
        if (!StringUtils.isEmpty(dateFormatStr)) {
            val format = SimpleDateFormat(dateFormatStr)
            if (isUTC) {
                format.timeZone = TimeZone.getTimeZone("UTC")
            }
            return format.parse(dateDealStr)
        }
        return null
    }


    /**
     * 批量格式化格式
     * @param dateList 时间列表
     * @param formatValue 格式化
     * @return 格式化后的时间字符串集合
     */
    @JvmStatic
    fun batchFormatTime(dateList: List<Date>, formatValue: String): List<String> {
        val simpleDateFormat = SimpleDateFormat(formatValue)
        val result = ArrayList<String>()
        for (date in dateList) {
            result.add(simpleDateFormat.format(date))
        }
        return result
    }

    /**
     * 生成时间间隔
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param intervalNumber 间隔数
     * @param type 事件间隔类型。小时/分组/天/周/月/年
     * TODO zhognming  待完善，intervalNumber、type可变
     * @return
     */
    @JvmStatic
    fun createHourInterval(startTime: Date, endTimePara: Date, intervalNumber: Int?, type: String = DateUtil.INTERVAL_TYPE_HOUR): List<Date> {
        var endTime = endTimePara
        val result = ArrayList<Date>()
        //获取第一个时间
        val calendar = Calendar.getInstance()
        calendar.time = startTime
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        while (calendar.time.before(endTime)) {
            result.add(calendar.time)
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + intervalNumber!!)

        }
        endTime = convertDateHour(endTime)
        if (result.size > 0 && result[result.size - 1].before(endTime)) {
            result.add(endTime)
        }
        return result
    }

    /**
     * 转换时间
     * 从小时之后全为0
     * @param date
     * @return
     */
    @JvmStatic
    private fun convertDateHour(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}