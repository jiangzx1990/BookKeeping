package com.jzx.book.bookkeeping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jzx on 2019/1/16
 */
public class DbHelper extends SQLiteOpenHelper {

    DbHelper(Context context) {
        super(context, SQL.DB_BOOK_KEEPING.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(!db.isReadOnly()){
            db.execSQL(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.CREATE_TABLE_SQL);
            db.execSQL(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.CREATE_TABLE_SQL);
            db.execSQL(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.CREATE_TABLE_SQL);
            db.execSQL(SQL.DB_BOOK_KEEPING.TABLE_FLOW.CREATE_TABLE_SQL);
            //初始化交易方式表
            db.beginTransaction();
            ContentValues wx = new ContentValues();
            wx.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.NAME_S, "微信");
            ContentValues aliPay = new ContentValues();
            aliPay.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.NAME_S, "支付宝");
            db.insert(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME, null, wx);
            db.insert(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME, null, aliPay);
            //初始化交易流向表
            ContentValues pay1 = new ContentValues();
            pay1.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S,
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_BORROW_OUT_S);
            pay1.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.DES_S,"借给TA");
            ContentValues pay2 = new ContentValues();
            pay2.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S,
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_BORROW_IN_S);
            pay2.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.DES_S,"找TA借");
            ContentValues pay4 = new ContentValues();
            pay4.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S,
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_BORROW_OUT_BACK_S);
            pay4.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.DES_S,"TA还自己钱");
            ContentValues pay8 = new ContentValues();
            pay8.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S,
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_BORROW_IN_BACK_S);
            pay8.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.DES_S,"自己还TA钱");
            db.insert(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME,null,pay1);
            db.insert(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME,null,pay2);
            db.insert(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME,null,pay4);
            db.insert(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME,null,pay8);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
