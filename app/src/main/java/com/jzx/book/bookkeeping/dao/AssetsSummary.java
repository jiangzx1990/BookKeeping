package com.jzx.book.bookkeeping.dao;

import java.math.BigDecimal;

/**
 * Created by Jzx on 2019/1/21
 */
public class AssetsSummary {
    private double borrowOutTotal;//总借出金额
    private double borrowOutBackTotal;//借出已归还金额

    private double borrowInTotal;//总借入金额
    private double borrowInBackTotal;//已还款金额

    public void setBorrowOutTotal(double borrowOutTotal) {
        this.borrowOutTotal = borrowOutTotal;
    }

    public void setBorrowOutBackTotal(double borrowOutBackTotal) {
        this.borrowOutBackTotal = borrowOutBackTotal;
    }

    public double getBorrowOutTotal() {
        return borrowOutTotal;
    }

    public double getBorrowOutBackTotal() {
        return borrowOutBackTotal;
    }

    public double getBorrowInTotal() {
        return borrowInTotal;
    }

    public double getBorrowInBackTotal() {
        return borrowInBackTotal;
    }

    public double getBorrowOutBalance() {
        return new BigDecimal(borrowOutTotal)
                .subtract(new BigDecimal(borrowOutBackTotal))
                .doubleValue();
    }

    public void setBorrowInTotal(double borrowInTotal) {
        this.borrowInTotal = borrowInTotal;
    }

    public void setBorrowInBackTotal(double borrowInBackTotal) {
        this.borrowInBackTotal = borrowInBackTotal;
    }

    public double getBorrowInBalance() {
        return new BigDecimal(borrowInTotal)
                .subtract(new BigDecimal(borrowInBackTotal))
                .doubleValue();
    }
}
