package com.jzx.book.bookkeeping.utils;

public class TimeUtils {
    public static String convertTimeStr(int hour,int minute){
        StringBuilder sb = new StringBuilder();
        if(hour < 10){
            sb.append(0);
        }
        sb.append(hour);
        sb.append(":");
        if(minute < 10){
            sb.append(0);
        }
        sb.append(minute);
        return sb.toString();
    }
}
