package com.jzx.book.bookkeeping.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jzx.book.bookkeeping.dao.PayType;
import com.jzx.book.bookkeeping.helper.ContextProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzx on 2019/1/17
 */
public class PayTypeOperator {
    public static List<PayType> getAllPayFlow() {
        List<PayType> result = new ArrayList<>();
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = null;
            try {
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME,
                        new String[]{SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.ID_L,
                                SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TYPE_I,
                                SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S,
                                SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.DES_S},
                        null,
                        null,
                        null,
                        null,
                        null);
                PayType flow;
                while (cursor.moveToNext()){
                    flow = new PayType();
                    flow.setId(cursor.getLong(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.ID_L)));
                    flow.setType(cursor.getInt(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TYPE_I)));
                    flow.setName(cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S)));
                    flow.setDes(cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.DES_S)));
                    result.add(flow);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(cursor != null){
                    cursor.close();
                }
                db.close();
            }
        }
        return result;
    }
}
