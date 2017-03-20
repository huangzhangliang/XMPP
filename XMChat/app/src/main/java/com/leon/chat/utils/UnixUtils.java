package com.leon.chat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换工具
 */
public class UnixUtils {

    /**
     * 字符串转时间戳
     **/
    public static long date2TimestampAll(String value){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();

    }

    /**
     * 时间戳转字符串-年月
     **/
    public static String timestamp2StringAll(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }


    /**
     * 时间戳转字符串-年月
     **/
    public static String timestamp2StringYM(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy年MM月");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }

    /**
     * 时间戳转字符串-年月日
     **/
    public static String timestamp2StringYMD(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy年MM月dd日");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }

    /**
     * 时间戳转字符串-年月日
     **/
    public static String timestamp2StringYMDEN(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }

    /**
     * 时间戳转字符串-月日
     **/
    public static String timestamp2StringMD(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("MM月dd日");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }

    /**
     * 时间戳转字符串
     **/
    public static String timestamp2StringCH(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("MM月dd日 HH时mm分");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }


    /**
     * 时间戳转字符串
     **/
    public static String timestamp2StringEN(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd HH:mm");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }

    /**
     * 字符串获取小时
     **/
    public static String timestampGetHour(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("HH");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }

    /**
     * 字符串获取时分
     **/
    public static String timestampGetHourMinute(long value){
        SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm");
        long lValue = value * 1000;
        return sdf.format(lValue);
    }


    /**
     * 比较日期
     **/
    public static String compareTime(long startTime, long endTime){
        String s = timestamp2StringYMD(startTime);
        String e = timestamp2StringYMD(endTime);
        String sH = timestampGetHourMinute(startTime);
        String eH = timestampGetHourMinute(endTime);
        String compareValue;
        if (s.equals(e)){
            if (sH.equals(eH)){
                compareValue = timestamp2StringMD(startTime) + " " + sH ;
            }else{
                compareValue = timestamp2StringMD(startTime) + " " + sH + " ~ " + eH ;
            }
//            compareValue = "03月19日 23:00~24:00" ;
        }else{
            compareValue = timestamp2StringEN(startTime) + " ~ " + timestamp2StringEN(endTime) ;
        }
        return compareValue;
    }


    /**
     * UT时间戳转字符串
     **/
    public static String calculationDiffer(long startTime){
        long beginTime = System.currentTimeMillis() / 1000;
        LogUtils.sf("startTime:"+startTime+"---beginTime:"+beginTime);
        long betweenDays = (long)((startTime - beginTime) / (60 * 60 *24) + 0.5);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, (int) betweenDays);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        String week = "";
        switch (weekDay){
            case Calendar.MONDAY:
                week = "周一";
                break;
            case Calendar.TUESDAY:
                week = "周二";
                break;
            case Calendar.WEDNESDAY:
                week = "周三";
                break;
            case Calendar.THURSDAY:
                week = "周四";
                break;
            case Calendar.FRIDAY:
                week = "周五";
                break;
            case Calendar.SATURDAY:
                week = "周六";
                break;
            case Calendar.SUNDAY:
                week = "周日";
                break;
        }
        String time = timestampGetHour(startTime);
        int iTime = Integer.valueOf(time);
        int simplifyTime = 0;
        String timeBucket;
        if (iTime > 12){
            timeBucket = "下午";
            simplifyTime = iTime % 12;
        }else{
            timeBucket = "上午";
            simplifyTime = iTime;
        }
        String tempData = "";
        if (betweenDays == 0){
            tempData = "今天";
        }else if (betweenDays > 0){
            tempData = betweenDays + "天后";
        }else if (betweenDays < 0){
            tempData = -betweenDays + "天前";
        }
        return tempData +timeBucket+ simplifyTime +"点 | "+week;
    }


}
