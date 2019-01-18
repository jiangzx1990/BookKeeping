package com.jzx.book.bookkeeping.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.jzx.book.bookkeeping.helper.ContextProvider;

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
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_F,amount);
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
}
