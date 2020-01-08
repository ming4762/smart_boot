package com.gc.common.utils;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 日期时间工具类
 * @author shizhongming
 * 2020/1/8 8:27 下午
 */
public class DateUtil {

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    @NotNull
    public static Date parse(@NotNull String dateStr, @NotNull String pattern) {
        final LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        final Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 格式化时间
     * @param date 时间
     * @param pattern 格式
     * @return 时间字符串
     */
    @NotNull
    public static String format(@NotNull Date date, @NotNull String pattern) {
        return DateUtil.batchFormat(Lists.newArrayList(date), pattern).get(0);
    }

    /**
     * 批量格式化时间
     * @param dateList 时间集合
     * @param pattern 格式
     * @return 时间字符串
     */
    public static List<String> batchFormat(@NotNull List<Date> dateList, @NotNull String pattern) {
        final var formatter = DateTimeFormatter.ofPattern(pattern);
        return dateList
                .stream()
                .map(date -> {
                    final Instant instant = date.toInstant();
                    final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    return localDateTime.format(formatter);
                }).collect(Collectors.toList());
    }

    /**
     * 字符串转为时间
     * @param dateStr 字符串
     * @return 时间
     * @throws ParseException
     */
    @Nullable
    public static Date convertDate(String dateStr) throws ParseException {
        var isUTC = false;
        if (StringUtils.isEmpty(dateStr)) return null;
        if (dateStr.contains("CST")) {
            return new SimpleDateFormat().parse(dateStr);
        }
        var dateDealStr = dateStr.replace("年", "-").replace("月", "-").replace("日", "")
                .replace("/", "-").replace("\\.", "-").trim();
        var dateFormatStr = "";
        // 确定日期个税
        if (Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}.*").matcher(dateDealStr).matches()) {
            dateFormatStr = "yyyy-MM-dd";
        } else if (Pattern.compile("^[0-9]{4}-[0-9]{1}-[0-9]+.*||^[0-9]{4}-[0-9]+-[0-9]{1}.*").matcher(dateDealStr).matches()) {
            dateFormatStr = "yyyy-M-d";
        } else if (Pattern.compile("^[0-9]{2}-[0-9]{2}-[0-9]{2}.*").matcher(dateDealStr).matches()) {
            dateFormatStr = "yy-MM-dd";
        } else if (Pattern.compile("^[0-9]{2}-[0-9]{1}-[0-9]+.*||^[0-9]{2}-[0-9]+-[0-9]{1}.*").matcher(dateDealStr).matches()) {
            dateFormatStr = "yy-M-d";
        }

        //确定时间格式
        if(Pattern.compile(".*[ ][0-9]{2}").matcher(dateDealStr).matches()){
            dateFormatStr += " HH";
        }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}").matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm";
        }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}").matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm:ss";
        }else if(Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{0,3}").matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm:ss:sss";
        }

        if (Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{0,3}Z").matcher(dateDealStr).matches()) {
            dateDealStr = dateDealStr.replace("Z", " UTC");
            dateFormatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";
            isUTC = true;
        }
        if (!StringUtils.isEmpty(dateFormatStr)) {
            final var format = new SimpleDateFormat(dateFormatStr);
            if (isUTC) {
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            return format.parse(dateDealStr);
        }
        return null;
    }

    /**
     * todo: 待开发
     * @return
     */
    public static List<Date> createHourInterval() {
        return null;
    }
}
