package com.jzx.book.bookkeeping.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jzx.book.bookkeeping.helper.ContextProvider;
import com.jzx.book.bookkeeping.dao.Contact;
import com.jzx.book.bookkeeping.utils.SafeString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzx on 2019/1/17
 */
public class ContactOperator {
    public static List<Contact> getAllContacts(){
        ArrayList<Contact> result = new ArrayList<>();
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = null;
            try{
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.TABLE_NAME,
                        new String[]{SQL.DB_BOOK_KEEPING.TABLE_CONTACT.ID_L,
                                SQL.DB_BOOK_KEEPING.TABLE_CONTACT.NAME_S,
                                SQL.DB_BOOK_KEEPING.TABLE_CONTACT.CONTACT_S},
                        null,
                        null,
                        null,
                        null,null);
                Contact contact;
                while (cursor.moveToNext()){
                    contact = new Contact();
                    contact.setId(cursor.getLong(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.ID_L)));
                    contact.setName(cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.NAME_S)));
                    contact.setContact_tell(cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.CONTACT_S)));
                    result.add(contact);
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

    public static Contact addContact(String name,String tell) {
        Contact t = null;
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            try {
                ContentValues c = new ContentValues();
                c.put(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.NAME_S, name);
                c.put(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.CONTACT_S,
                        SafeString.handleStringIfNull(tell));
                long id = db.insert(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.TABLE_NAME,
                        null,
                        c);
                if (id != -1) {
                    t = new Contact();
                    t.setContact_tell(tell);
                    t.setName(name);
                    t.setId(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }

        return t;
    }

    public static Contact queryContactByName(String contactName){
        Contact contact = null;
        DbHelper helper = new DbHelper(ContextProvider.getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = null;
            try{
                cursor = db.query(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.TABLE_NAME,
                        null,
                        SQL.DB_BOOK_KEEPING.TABLE_CONTACT.NAME_S + "=?",
                        new String[]{contactName},
                        null,
                        null,
                        null);
                if (cursor.moveToNext()){
                    contact = new Contact();
                    contact.setId(cursor.getLong(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.ID_L)));
                    contact.setName(cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.NAME_S)));
                    contact.setContact_tell(cursor.getString(cursor.getColumnIndex(SQL.DB_BOOK_KEEPING.TABLE_CONTACT.CONTACT_S)));
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
        return contact;
    }

}
