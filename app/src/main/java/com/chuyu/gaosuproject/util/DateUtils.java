package com.chuyu.gaosuproject.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/15.
 */

public class DateUtils {
   public static SimpleDateFormat sf=null;
    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getCurrentDate(){
        Date date = new Date();
         sf=new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time){
        Date date = new Date(time);
        sf=new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time){
        sf=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date=sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getSystemDate(){
        Date date = new Date();
        sf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(date);
    }
    public static String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    public static String getNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
    /**
     * 比较日期
     *
     * @param DATE1 选择日期
     * @param DATE2 被比较日期
     * @return
     */
    public static boolean compare_date(String DATE1, String DATE2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date dt1 = simpleDateFormat.parse(DATE1);
            Date dt2 = simpleDateFormat.parse(DATE2);
            if (dt1.getTime() < dt2.getTime()) {
                //date 1小于 date 2
                return true;
            } else if (dt1.getTime() > dt2.getTime()) {
                //date 1大于 date 2
                return false;
            } else {
                //date 1等于 date 2
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
