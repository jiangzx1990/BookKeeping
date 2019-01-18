package com.jzx.book.bookkeeping.utils;

/**
 * Created by Jzx on 2019/1/17
 */
public class SafeString {
    public static String handleStringIfNull(String str){
        return str == null ? "" : str;
    }
}
