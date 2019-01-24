package com.jzx.book.bookkeeping.dao;

import android.support.annotation.NonNull;

/**
 * Created by Jzx on 2019/1/17
 */
public class PayType {
    private long id;
    private String name;
    private String des;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @NonNull
    @Override
    public String toString() {
        return name == null ? "":name;
    }
}
