package com.jzx.book.bookkeeping.dao;

/**
 * Created by Jzx on 2019/1/17
 */
public class Flow {
    private long id;
    private long contactId;
    private double amount;
    private long payTypeId;
    private long payFlowId;
    private String remark;
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(long payTypeId) {
        this.payTypeId = payTypeId;
    }

    public long getPayFlowId() {
        return payFlowId;
    }

    public void setPayFlowId(long payFlowId) {
        this.payFlowId = payFlowId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
