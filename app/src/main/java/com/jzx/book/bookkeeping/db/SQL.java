package com.jzx.book.bookkeeping.db;

/**
 * Created by Jzx on 2019/1/16
 */
public class SQL {
    public static class DB_BOOK_KEEPING {
        static final String DB_NAME = "book_keeping";

        static class TABLE_CONTACT {
            //交易人员表
            static final String TABLE_NAME = "contact";
            static final String ID_L = "id";
            static final String NAME_S = "name";
            static final String CONTACT_S = "contact_tell";
            //建表语句
            static final String CREATE_TABLE_SQL = "create table if not exists " +
                    TABLE_NAME + "(" + ID_L + " INTEGER primary key autoincrement," +
                    NAME_S + " TEXT not null default \"\"," +
                    CONTACT_S + " TEXT not null default \"\")";
        }

        static class TABLE_PAY_WAY {
            //交易方式表
            static final String TABLE_NAME = "pay_way";
            static final String ID_L = "id";
            static final String NAME_S = "name";
            //建表语句
            static final String CREATE_TABLE_SQL = "create table if not exists " +
                    TABLE_NAME + "(" + ID_L + " INTEGER primary key autoincrement," +
                    NAME_S + " TEXT not null default \"\")";
        }

        static class TABLE_PAY_TYPE {
            //交易类型表
            public static final String NAME_BORROW_OUT_S = "借出";
            public static final String NAME_BORROW_IN_S = "借入";
            public static final String NAME_BORROW_OUT_BACK_S = "借出还款";
            public static final String NAME_BORROW_IN_BACK_S = "借入还款";
            static final String TABLE_NAME = "pay_type";
            static final String ID_L = "id";
            static final String NAME_S = "name";
            static final String DES_S = "des";
            //建表语句
            static final String CREATE_TABLE_SQL = "create table if not exists " +
                    TABLE_NAME + "(" + ID_L + " INTEGER primary key autoincrement ," +
                    NAME_S + " TEXT not null default \"\"," +
                    DES_S + " TEXT not null default \"\" )";

        }

        static class TABLE_FLOW{
            //交易流水表
            static final String TABLE_NAME = "flow";//表名
            static final String ID_L = "id";//id
            static final String AMOUNT_D = "amount";//交易金额
            static final String CONTACT_L = "contact_id";//联系人id
            static final String PAY_TYPE_ID_L = "pay_type_id";//支付方式id
            static final String PAY_WAY_L = "pay_way_id";//流水类型
            static final String REMARK_S = "remark";
            static final String DATE_S = "date";// yyyy年MM月dd日
            //建表语句
            static final String CREATE_TABLE_SQL = "create table if not exists " +
                    TABLE_NAME + "(" + ID_L + " INTEGER primary key autoincrement, " +
                    CONTACT_L + " INTEGER not null," +
                    AMOUNT_D + " DOUBLE not null," +
                    PAY_TYPE_ID_L + " INTEGER not null ," +
                    PAY_WAY_L + " INTEGER not null," +
                    REMARK_S + " TEXT," +
                    DATE_S + " TEXT )";
        }
    }
}
