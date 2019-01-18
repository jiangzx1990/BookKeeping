package com.jzx.book.bookkeeping.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jzx.book.bookkeeping.dao.PayWay;
import com.jzx.book.bookkeeping.helper.ContextProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzx on 2019/1/18
 */
public class PayWayOperator {
    public static List<PayWay> getAllPayWay(){
        List<PayWay> result = new ArrayList<>();
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = null;
            try{
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                PayWay p;
                while (cursor.moveToNext()){
                    p = new PayWay();
                    p.setId(cursor.getLong(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.ID_L)));
                    p.setName(cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.NAME_S)));
                    result.add(p);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(cursor != null){
                    cursor.close();
                }
                db.close();
            }
        }
        return result;
    }

    public static PayWay queryPayWayByName(String payWayName){
        PayWay payWay = null;
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = null;
            try{
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME,
                        null,
                        SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.NAME_S+"=?",
                        new String[]{payWayName},
                        null,
                        null,
                        null);
                if(cursor.moveToNext()){
                    payWay = new PayWay();
                    payWay.setId(cursor.getLong(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.ID_L)));
                    payWay.setName(cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.NAME_S)));
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(cursor != null){
                    cursor.close();
                }
                db.close();
            }
        }
        return payWay;
    }

    public static PayWay addPayWay(String payWayName){
        PayWay payWay = null;
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()){
            try{
                ContentValues values = new ContentValues();
                values.put(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.NAME_S,payWayName);
                long id = db.insert(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME,null,values);
                if(id != -1){
                    payWay = new PayWay();
                    payWay.setId(id);
                    payWay.setName(payWayName);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                db.close();
            }
        }
        return payWay;
    }

    public static boolean deletePayWay(long id){
        boolean deleted = false;
            DbHelper helper = new DbHelper(ContextProvider.getContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            if(db.isOpen()){
                try{
                    deleted = db.delete(SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME,
                            SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.ID_L + "=?",
                            new String[]{String.valueOf(id)}) > 0;
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.close();
                }
            }
        return deleted;
    }
}
