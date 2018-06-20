package com.ucar.zkdoctor.util.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: 时间转化工具类
 * Created on 2018/1/26 10:51
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class DateUtil {

    /**
     * 时间字符格式
     */
    private final static String DATE_FORMAT_YMD = "yyyy-MM-dd";
    private final static String DATE_FORMAT_HMS = "yyyy-MM-dd HH:mm:ss";
    private final static String DATE_FORMAT_HM = "yyyy-MM-dd HH:mm";

    /**
     * 将yyyy-MM-dd HH:mm格式的日期字符串转化为
     *
     * @param dateStr 日期字符串
     * @return
     * @throws ParseException
     */
    public static Date parseYYYYMMddHHmm(String dateStr) throws ParseException {
        return parse(dateStr, DATE_FORMAT_HM);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式的日期字符串转化为
     *
     * @param dateStr 日期字符串
     * @return
     * @throws ParseException
     */
    public static Date parseYYYYMMddHHmmss(String dateStr) throws ParseException {
        return parse(dateStr, DATE_FORMAT_HMS);
    }

    /**
     * 根据日期字符串以及格式，将其转化为Date类型
     *
     * @param dateStr 日期字符串
     * @param format  格式
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }

    /**
     * 将日期转化为yyyy-MM-dd HH:mm:ss格式的字符串
     *
     * @param date 日期
     * @return
     */
    public static String formatYYYYMMddHHMMss(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_HMS);
        return sdf.format(date);
    }

    /**
     * 将日期转化为yyyy-MM-dd 格式的字符串
     *
     * @param date 日期
     * @return
     */
    public static String formatYYYYMMdd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YMD);
        return sdf.format(date);
    }

    /**
     * 获取intervalTimeMs时间差的日期值
     *
     * @param date           基准日期
     * @param intervalTimeMs 时间间隔，单位:ms
     * @return
     */
    public static Date getIntervalDate(Date date, long intervalTimeMs) {
        return new Date(date.getTime() + intervalTimeMs);
    }

    /**
     * 判断监控的状态信息，是否符合监控时间要求：最近的修改时间/该状态的创建时间在intervalTimeMs毫秒以内
     *
     * @param now            当前时间
     * @param createTime     状态信息的创建时间
     * @param modifyTime     状态信息的最新更改时间
     * @param intervalTimeMs 时间间隔，单位:ms
     * @return
     */
    public static boolean isMonitorTime(Date now, Date createTime, Date modifyTime, long intervalTimeMs) {
        Date beforeTime = DateUtil.getIntervalDate(now, intervalTimeMs);
        return (modifyTime == null && createTime != null && createTime.after(beforeTime)) ||
                (modifyTime != null && modifyTime.after(beforeTime));
    }
}
