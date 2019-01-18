package com.jzx.book.bookkeeping.utils;

/**
 * Created by Jzx on 2019/1/17
 */
public class RegUtils {
    public static boolean isCellphoneSimple(String number){
        boolean isCellphone = false;
        if(number != null){
            isCellphone = number.matches("^1[0-9]{10}$");
        }
        return isCellphone;
    }
}
