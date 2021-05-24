package com.sky.ddtspi.util;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import java.util.Calendar;
import java.util.Date;

public class OffsetDateTimeTool2 {


    public static OffsetDateTime getOffsetDateTime(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH)+1;//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR_OF_DAY);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        return OffsetDateTime.of(year,month,day,hour,minute,second,0, ZoneOffset.UTC);
    }
    public static void main(String args[]) {
        System.out.println("getOffsetDateTime:"+getOffsetDateTime(new Date()));
    }
}