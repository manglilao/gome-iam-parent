package com.gome.iam.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 基于 JDK 7 的时间工具类
 * Created by qiaowentao on 2017/6/24.
 */
public class Time7Util {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Long MILLISECONDS = 60*1000L;

    public static String  reverseDateToTime(Date date){
        String time =  "";
        try{
            time = sdf.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return time;
    }

    public static Date  reverseStrToDate(String time){
        Date date = null;
        try{
            date = sdf.parse(time);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取指定分钟数前的时间
     * @param minutes
     * @return
     */
    public static Date getScheduleTime(Date date,int minutes){
        Date beforeDate = null;
        try{
            beforeDate = new Date(date.getTime()-minutes * MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return beforeDate;
    }

    public static void main(String[] args) {
        Date date = getScheduleTime(new Date(),-30);
        System.out.println(date);

        LocalDateTime lockedEndTime = LocalDateTime.now().plus(1, ChronoUnit.HOURS);
        System.out.println(lockedEndTime);

    }

}
