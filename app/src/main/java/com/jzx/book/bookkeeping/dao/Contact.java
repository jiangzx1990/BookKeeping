package com.jzx.book.bookkeeping.dao;

/**
 * Created by Jzx on 2019/1/17
 */
public class Contact {
    private long id;
    private String name;
    private String contact_tell;

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

    public String getContact_tell() {
        return contact_tell;
    }

    public void setContact_tell(String contact_tell) {
        this.contact_tell = contact_tell;
    }
}
