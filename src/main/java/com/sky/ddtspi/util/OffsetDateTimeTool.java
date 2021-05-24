package com.sky.ddtspi.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

public class OffsetDateTimeTool {

    //private static final ZoneOffset OFFSET_ID = ZoneOffset.of("+08:00");
    private static final ZoneOffset OFFSET_ID = getOFFSET_ID();
    /**
     * String  转 OffsetDateTime
     */
    public static String getZoneStringDateTime(CharSequence text){
        return formatDateTime(OffsetDateTime.of(LocalDateTime.parse(text.toString()),OFFSET_ID)
                ,TimeFormat.LONG_DATE_PATTERN_WITH_TIME_ZONE.value());
    }
    /**
     * String 转 OffsetDateTime 转 long 转 OffsetDateTime 格式化 String
     */
    public static String getStringDateTime(CharSequence text) {
        return formatDateTime(OffsetDateTime.ofInstant(Instant.ofEpochMilli(getLongDateTime(text
                ,TimeFormat.LONG_DATE_PATTERN_WITH_TIME_ZONE.value(),getOffset(text.toString()))),OFFSET_ID)
                ,TimeFormat.LONG_DATE_PATTERN_LINE.value());
    }
    /**
     * String 转 OffsetDateTime 转 long
     */
    public static long getLongDateTime(CharSequence text, String patten, String offsetId) {
        return getLong(parse(text,patten,offsetId))*1000;
    }
    /**
     * OffsetDateTime 转 long
     */
    public static long getLong(OffsetDateTime offsetDateTime) {
        return offsetDateTime.getLong(ChronoField.INSTANT_SECONDS);
    }
    /**
     * 根据字符串设置日期
     *
     * @return OffsetDateTime
     */
    public static OffsetDateTime parse(CharSequence text, String patten, String offsetId) {
        LocalDateTime localDateTime = LocalDateTime.parse(text, DateTimeFormatter.ofPattern(patten));
        return OffsetDateTime.of(localDateTime, ZoneOffset.of(offsetId));
    }
    /**
     * 格式化时间
     */
    public static String formatDateTime(OffsetDateTime dateTime, String patten) {
        return dateTime.format(DateTimeFormatter.ofPattern(patten));
    }
    /**
     * 获取系统时区
     */
    public static ZoneOffset getOFFSET_ID(){
        return LocalDateTime.now(ZoneOffset.systemDefault()).atZone(ZoneOffset.systemDefault()).getOffset();
    }
    /**
     * 获取string时区
     */
    public static String getOffset(String date){
        return date.substring(date.length()-6,date.length());
    }
    /**
     * 获取当前时间 在ISO-8601日历系统中的UTC /格林威治偏移的日期时间，如2017-12-03T10:15:30+08:00
     */
    public static OffsetDateTime getDateTimeNow() {
        return OffsetDateTime.now();
    }
    /**
     * 获取时间（只设置年月日）
     */
    public static OffsetDateTime getDateTime(int year, int month, int day , String offsetId) {
        return getDateTime(year, month, day, 0, 0, 0, 0 , ZoneOffset.of(offsetId));
    }
    /**
     * 获取时间
     *
     * @param year
     *            年
     * @param month
     *            月
     * @param day
     *            日
     * @param hour
     *            时
     * @param minute
     *            分
     * @param second
     *            秒
     * @param nanoOfSecond
     *            纳秒
     * @param zoneOffset
     *            时区
     * @return OffsetDateTime
     */
    public static OffsetDateTime getDateTime(int year, int month, int day, int hour, int minute, int second,
                                             int nanoOfSecond, ZoneOffset zoneOffset) {
        return OffsetDateTime.of(year, month, day, hour, minute, second, nanoOfSecond, zoneOffset);

    }

    public static OffsetDateTime getOffsetDateTime(Date dateTime) {
        return OffsetDateTime.of(dateTime.getYear(),dateTime.getMonth(),dateTime.getDay(),dateTime.getHours(),dateTime.getMinutes(),dateTime.getSeconds(),0,ZoneOffset.UTC);
    }

    /**
     * 内部枚举类
     */
    public enum TimeFormat {
        //短时间格式 年月日
        /**
         *时间格式：yyyy-MM-dd HH:mm:ss
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),

        LONG_DATE_PATTERN_WITH_TIME_ZONE("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        private String value = "";

        TimeFormat(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
    public static void main(String args[]) {
        System.out.println("getLongDateTime : "
                + getLongDateTime("2018-06-05T08:00:00.000+08:00"
                ,TimeFormat.LONG_DATE_PATTERN_WITH_TIME_ZONE.value(),getOffset("2018-06-05T04:00:00.000+08:00") ));
        System.out.println("getLongDateTime : "
                + getLongDateTime("2018-06-05T04:00:00.000+04:00"
                ,TimeFormat.LONG_DATE_PATTERN_WITH_TIME_ZONE.value(),getOffset("2018-06-05T04:00:00.000+04:00") ));
        System.out.println("getStringDateTime :" + getStringDateTime("2018-06-05T04:00:00.000-12:00"));
        System.out.println("getZoneStringDateTime:" + getZoneStringDateTime("2018-06-05T04:00:00"));
        System.out.println("getOffsetDateTime:"+getOffsetDateTime(new Date()));
    }
}