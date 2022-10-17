package com.delivce.managenyama.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeToday {
    public String getDateTimeToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDateTime = sdf.format(c.getTime());

        return strDateTime;
    }
    public String getDateToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(c.getTime());

        return strDate;
    }
}
