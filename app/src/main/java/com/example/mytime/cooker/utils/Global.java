package com.example.mytime.cooker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by My time on 2017/10/14
 */

public class Global {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss.000+0000").create();
    public static Gson getGson(){
        return gson;
    }
}
