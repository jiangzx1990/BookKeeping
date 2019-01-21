package com.jzx.book.bookkeeping.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jzx.book.bookkeeping.dao.AssetsSummary;
import com.jzx.book.bookkeeping.dao.Flow;
import com.jzx.book.bookkeeping.helper.ContextProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzx on 2019/1/18
 */
public class FlowOperator {
    public static boolean addFlow(double amount,
                               long contactId,
                               long payTypeId,
                               long payWayId,
                               String date,
                               String remark){
        boolean success = false;
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()){
            try{
                ContentValues values = new ContentValues();
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_D,amount);
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.CONTACT_I,contactId);
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_I,payTypeId);
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_WAY_I,payWayId);
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.DATE_S,date);
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.REMARK_S,remark);
                long id = db.insert(SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME,null,values);
                if(id != -1){
                    success = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                db.close();
            }
        }
        return success;
    }


    public static AssetsSummary getAssetsSummary(){
        AssetsSummary summary = new AssetsSummary();
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        if(db.isOpen()){
            try{
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME,
                        new String[]{SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_D},
                        SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_I + "=?",
                        new String[]{String.valueOf(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.BORROW_OUT)},
                        null,null,null);
                //计算总借出金额
                BigDecimal borrowOut = new BigDecimal(0);
                BigDecimal temp;
                while (cursor.moveToNext()){
                    temp = new BigDecimal(cursor.getDouble(0));
                    borrowOut = borrowOut.add(temp);
                }
                cursor.close();
                cursor = null;
                summary.setBorrowOutTotal(borrowOut.doubleValue());
                //计算借出已归还金额
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME,
                        new String[]{SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_D},
                        SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_I + "=?",
                        new String[]{String.valueOf(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.BORROW_OUT_BACK)},
                        null,null,null);
                BigDecimal borrowOutBack = new BigDecimal(0);
                while (cursor.moveToNext()){
                    temp = new BigDecimal(cursor.getDouble(0));
                    borrowOutBack = borrowOutBack.add(temp);
                }
                cursor.close();
                cursor = null;
                summary.setBorrowOutBackTotal(borrowOutBack.doubleValue());
                //计算总借入金额
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME,
                        new String[]{SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_D},
                        SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_I + "=?",
                        new String[]{String.valueOf(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.BORROW_IN)},
                        null,null,null);
                BigDecimal borrowIn = new BigDecimal(0);
                while (cursor.moveToNext()){
                    temp = new BigDecimal(cursor.getDouble(0));
                    borrowIn = borrowIn.add(temp);
                }
                cursor.close();
                cursor = null;
                summary.setBorrowInTotal(borrowIn.doubleValue());
                //计算已归还金额
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME,
                        new String[]{SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_D},
                        SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_I + "=?",
                        new String[]{String.valueOf(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.BORROW_IN_BACK)},
                        null,null,null);
                BigDecimal borrowInBack = new BigDecimal(0);
                while (cursor.moveToNext()){
                    temp = new BigDecimal(cursor.getDouble(0));
                    borrowInBack = borrowInBack.add(temp);
                }
                cursor.close();
                cursor = null;
                summary.setBorrowInBackTotal(borrowInBack.doubleValue());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(cursor != null){
                    cursor.close();
                }
                db.close();
            }
        }
        return summary;
    }


    public static List<Flow> queryFlow(long contactId,long contactType){
        //todo
        List<Flow> flows = new ArrayList<>();
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = null;
            String sql = "select " + SQL.DB_BOOK_KEEPING.TABLE_FLOW.ID_L + ","
                     + SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_D + ","
                    + SQL.DB_BOOK_KEEPING.TABLE_FLOW.REMARK_S;
            if(contactId > 0 && contactType > 0){
            }else{

            }
        }
        return flows;
    }
}
