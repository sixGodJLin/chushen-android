package com.example.mytime.cooker.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by My time on 2017/10/19
 */

public class TimeUtil {
    public static String formatTimestamp(Timestamp timestamp){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            //方法一
            return sdf.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String DateToString(Timestamp timestamp){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            //方法一
            return sdf.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
