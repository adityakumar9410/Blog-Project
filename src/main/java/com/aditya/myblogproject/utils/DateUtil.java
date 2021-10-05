package com.aditya.myblogproject.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public  static String getDateStringFromDate(Date date){
        return DATE_FORMAT.format(date);
    }

    public static String getCurrentInstanceOfDate(){
        Date date = Calendar.getInstance().getTime();
        return DATE_FORMAT.format(date);
    }
    public static Date getDateFromDateString(String dateString){
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
