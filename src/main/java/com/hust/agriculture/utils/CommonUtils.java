package com.hust.agriculture.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils {

    private static final String REAL_ENSCAPE = "\"";

    public static boolean isSearchExactly(String key){
        if(key.startsWith(REAL_ENSCAPE) && key.endsWith(REAL_ENSCAPE)){
            return true;
        }
        return false;
    }

    public static Date add(Integer day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    public static String fromObject(Object object, Logger logger){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
