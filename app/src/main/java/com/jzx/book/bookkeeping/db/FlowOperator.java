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
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.CONTACT_L,contactId);
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_ID_L,payTypeId);
                values.put(SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_WAY_L,payWayId);
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
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME,
                        new String[]{SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.ID_L,
                                SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S},
                        null,null,null,null,null);
                long borrowOutId = -1L;
                long bowworOutBackId = -1L;
                long borrowInId = -1L;
                long borrowInBackId = -1L;
                String name;
                long id;
                while (cursor.moveToNext()){
                    name = cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S));
                    id = cursor.getLong(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.ID_L));
                    if (SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_BORROW_OUT_S.equals(name)){
                        borrowOutId = id;
                        continue;
                    }
                    if(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_BORROW_OUT_BACK_S.equals(name)){
                        bowworOutBackId = id;
                        continue;
                    }
                    if(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_BORROW_IN_S.equals(name)){
                        borrowInId = id;
                        continue;
                    }
                    if(SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_BORROW_IN_BACK_S.equals(name)){
                        borrowInBackId = id;
                    }
                }
                cursor.close();
                cursor = null;
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME,
                        new String[]{SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_D},
                        SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_ID_L + "=?",
                        new String[]{String.valueOf(borrowOutId)},
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
                        SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_ID_L + "=?",
                        new String[]{String.valueOf(bowworOutBackId)},
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
                        SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_ID_L + "=?",
                        new String[]{String.valueOf(borrowInId)},
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
                        SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_ID_L + "=?",
                        new String[]{String.valueOf(borrowInBackId)},
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


    /**
     * 根据交易人id，支付类型id查找交易流水，都传0时表示查全部
     * @param contactId 交易人id
     * @param payTypeValue 交易类型
     */
    public static List<Flow> queryFlow(long contactId,long payTypeValue){
        List<Flow> flows = new ArrayList<>();
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = null;
            final String ID = "id";
            final String AMOUNT = "amount";
            final String REMARK = "remark";
            final String DATE = "date";
            final String CONTACT = "contact";
            final String PAY_TYPE = "payType";
            final String PAY_WAY = "payWay";
            String sql = "select " +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.ID_L + " as " +
                    ID + "," +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.AMOUNT_D + " as " +
                    AMOUNT + "," +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.REMARK_S +" as " +
                    REMARK + "," +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME +"." +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.DATE_S +" as " +
                    DATE + "," +
                    SQL.DB_BOOK_KEEPING.TABLE_CONTACT.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_CONTACT.NAME_S + " as " +
                    CONTACT + "," +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.NAME_S +" as " +
                    PAY_TYPE + "," +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME +"." +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.NAME_S + " as "+
                    PAY_WAY + " from " +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + " inner join " +
                    SQL.DB_BOOK_KEEPING.TABLE_CONTACT.TABLE_NAME + " on " +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.CONTACT_L + " = " +
                    SQL.DB_BOOK_KEEPING.TABLE_CONTACT.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_CONTACT.ID_L + " inner join " +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME + " on " +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_ID_L +" = " +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_TYPE.ID_L + " inner join " +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME +" on " +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_WAY_L + " = " +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.TABLE_NAME + "." +
                    SQL.DB_BOOK_KEEPING.TABLE_PAY_WAY.ID_L;
            String where  = "";
            String[] condition = null;
            if(contactId > 0 || payTypeValue > 0){
                if(contactId > 0 && payTypeValue >0){
                    where = " where " +SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                            SQL.DB_BOOK_KEEPING.TABLE_FLOW.CONTACT_L + "=? and " +
                            SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                            SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_ID_L + "=?";
                    condition = new String[]{String.valueOf(contactId),String.valueOf(payTypeValue)};
                }else{
                    if(contactId > 0){
                        where = " where " + SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                                SQL.DB_BOOK_KEEPING.TABLE_FLOW.CONTACT_L + "=?";
                        condition = new String[]{String.valueOf(contactId)};
                    }else{
                        where = " where " + SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME + "." +
                                SQL.DB_BOOK_KEEPING.TABLE_FLOW.PAY_TYPE_ID_L + "=?";
                        condition = new String[]{String.valueOf(payTypeValue)};
                    }
                }
            }
            try{
                sql = sql + where;
                cursor = db.rawQuery(sql,condition);
                Flow flow;
                while (cursor.moveToNext()){
                    flow = new Flow();
                    flow.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                    flow.setAmount(cursor.getDouble(cursor.getColumnIndex(AMOUNT)));
                    flow.setRemark(cursor.getString(cursor.getColumnIndex(REMARK)));
                    flow.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                    flow.setContact(cursor.getString(cursor.getColumnIndex(CONTACT)));
                    flow.setPayType(cursor.getString(cursor.getColumnIndex(PAY_TYPE)));
                    flow.setPayWay(cursor.getString(cursor.getColumnIndex(PAY_WAY)));
                    flows.add(flow);
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
        return flows;
    }



    public static boolean deleteFlows(List<Long> ids){
        boolean deleted = false;
        if(ids != null && ids.size() > 0){
            DbHelper helper = new DbHelper(ContextProvider.getContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            if(db.isOpen()){
                try{
                    db.beginTransaction();
                    boolean error = false;
                    for(int i=0,size=ids.size();i<size;i++){
                         int num = db.delete(SQL.DB_BOOK_KEEPING.TABLE_FLOW.TABLE_NAME,
                                "id = ?",
                                new String[]{String.valueOf(ids.get(i))});
                         if(num == 0){
                             error = true;
                             break;
                         }
                    }
                    if(!error){
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        deleted = true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.close();
                }
            }
        }
        return deleted;
    }
}
