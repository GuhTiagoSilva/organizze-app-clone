package com.example.organizze.helper;

import java.text.SimpleDateFormat;

public class DateUtil {


    public static String getCurrentDate(){
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static String monthYearChosenDate(String date){
        String dateReturn [] = date.split("/");
        String day = dateReturn[0];//day
        String month = dateReturn[1];//month
        String year = dateReturn[2];//year

        String monthYear = month + year;

        return monthYear;

    }


}
