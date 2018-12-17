package com.zgx.tools;

import java.text.DateFormat;  
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
  
  
/**
 *  DateTime.java: 用于实现，日期、字符串、long之间的相互转换。
 * 
 * 
 */  
public class DateTimeTools  
{  
    /** main */  
    public static void main(String[] args)  
    {  
        String currentTime = currentTime();                 // 获取当前时间的字符串形式  
        long currentLong = currentTimeLong();               // 获取当前时间的long型  
          
        System.out.println("示例输出：");  
          
        System.out.println("currentTime: " + currentTime);  
        System.out.println("currentLong: " + currentLong);  
          
        Date stringToDate = toDate(currentTime);            // 日期字符串转化为日期  
        Date longToDate = toDate(currentLong);              // long转化为日期  
          
        System.out.println("stringToDate: " + toString(stringToDate));  // 显示为日期串  
        System.out.println("longToDate: " + toString(longToDate));      // 显示为日期串  
    }  
      
    // 获取当前时间，long型  
    public static long currentTimeLong()  
    {  
        return new Date().getTime();  
    }  
      
    // 获取当前时间，字符串形式  
    public static String currentTime()  
    {  
        Date date = new Date();  
        return toString(date);  
    }  
      
    // 从字符串, 获取日期, 如time = "2016-3-16 4:12:16"  
    public static Date toDate(String time)  
    {  
        try  
        {  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            Date date = sdf.parse(time);  
              
            return date;  
        }  
        catch (ParseException e)  
        {  
            return null;  
        }  
    }  
      
    // 从long, 获取日期  
    @SuppressWarnings("unused")  
    public static Date toDate(long millSec)  
    {  
        return new Date(millSec);  
    }  
      
    // 日期转化为字符串形式  
    public static String toString(Date date)  
    {  
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        return format.format(date);  
    }  
}  