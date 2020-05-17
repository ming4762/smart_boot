package com.gc.common.base.utils;

import com.google.common.collect.Lists;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
public final class DateUtil {

    private static final String CST_DATE_STR = "CST";

    private static final Pattern YYYY_MM_DD = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}.*");

    private static final Pattern YYYY_M_D = Pattern.compile("^[0-9]{4}-[0-9]{1}-[0-9]+.*||^[0-9]{4}-[0-9]+-[0-9]{1}.*");

    private static final Pattern YY_MM_DD = Pattern.compile("^[0-9]{2}-[0-9]{2}-[0-9]{2}.*");

    private static final Pattern YY_M_D = Pattern.compile("^[0-9]{2}-[0-9]{1}-[0-9]+.*||^[0-9]{2}-[0-9]+-[0-9]{1}.*");

    private static final Pattern HH = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}.*");

    private static final Pattern HH_MM = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}");

    private static final Pattern HH_MM_SS = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}");

    private static final Pattern HH_MM_SS_SSS = Pattern.compile(".*[ ][0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{0,3}");

    private static final Pattern YYYY_MM_DD_HH_MM_SS_SSS_Z = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{0,3}Z");

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    @NonNull
    public static Date parse(@NonNull String dateStr, @NonNull String pattern) {
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
    @NonNull
    public static String format(@NonNull Date date, @NonNull String pattern) {
        return DateUtil.batchFormat(Lists.newArrayList(date), pattern).get(0);
    }

    /**
     * 批量格式化时间
     * @param dateList 时间集合
     * @param pattern 格式
     * @return 时间字符串
     */
    public static List<String> batchFormat(@NonNull List<Date> dateList, @NonNull String pattern) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
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
        boolean isUtc = false;
        if (org.springframework.util.StringUtils.isEmpty(dateStr)) {
            return null;
        }
        if (dateStr.contains(CST_DATE_STR)) {
            return new SimpleDateFormat().parse(dateStr);
        }
        String dateDealStr = dateStr.replace("年", "-").replace("月", "-").replace("日", "")
                .replace("/", "-").replace("\\.", "-").trim();
        String dateFormatStr = "";
        // 确定日期个税
        if (YYYY_MM_DD.matcher(dateDealStr).matches()) {
            dateFormatStr = "yyyy-MM-dd";
        } else if (YYYY_M_D.matcher(dateDealStr).matches()) {
            dateFormatStr = "yyyy-M-d";
        } else if (YY_MM_DD.matcher(dateDealStr).matches()) {
            dateFormatStr = "yy-MM-dd";
        } else if (YY_M_D.matcher(dateDealStr).matches()) {
            dateFormatStr = "yy-M-d";
        }

        //确定时间格式
        if(HH.matcher(dateDealStr).matches()){
            dateFormatStr += " HH";
        }else if(HH_MM.matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm";
        }else if(HH_MM_SS.matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm:ss";
        }else if(HH_MM_SS_SSS.matcher(dateDealStr).matches()){
            dateFormatStr += " HH:mm:ss:sss";
        }

        if (YYYY_MM_DD_HH_MM_SS_SSS_Z.matcher(dateDealStr).matches()) {
            dateDealStr = dateDealStr.replace("Z", " UTC");
            dateFormatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS Z";
            isUtc = true;
        }
        if (!org.springframework.util.StringUtils.isEmpty(dateFormatStr)) {
            final SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
            if (isUtc) {
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
        return Lists.newArrayList();
    }
}
