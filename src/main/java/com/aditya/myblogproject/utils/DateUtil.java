package com.aditya.myblogproject.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public  static String getCurrentDateTime(){
        Date date = Calendar.getInstance().getTime();
        return DATE_FORMAT.format(date);
    }

    public static Date createDateFromDateString(String dateString){
        Date date = null;
        if(null != dateString){
            try{
                date = DATE_FORMAT.parse(dateString);
            }catch(ParseException pe){
                date = new Date();
            }
        }else{
            date = new Date();
        }
        return date;
    }
}
