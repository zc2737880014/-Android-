package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
//public final class TodoContract {
//
//    public static final String SQL_CREATE_NOTES =
//            "CREATE TABLE " + TodoNote.TABLE_NAME
//                    + "(" + TodoNote._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + TodoNote.COLUMN_DATE + " INTEGER, "
//                    + TodoNote.COLUMN_STATE + " INTEGER, "
//                    + TodoNote.COLUMN_CONTENT + " TEXT, "
//                    + TodoNote.COLUMN_PRIORITY + " INTEGER)";
//
//    public static final String SQL_ADD_PRIORITY_COLUMN =
//            "ALTER TABLE " + TodoNote.TABLE_NAME + " ADD " + TodoNote.COLUMN_PRIORITY + " INTEGER";
//
//    private TodoContract() {
//    }
//
//    public static class TodoNote implements BaseColumns {
//        public static final String TABLE_NAME = "note";
//
//        public static final String COLUMN_DATE = "date";
//        public static final String COLUMN_STATE = "state";
//        public static final String COLUMN_CONTENT = "content";
//        public static final String COLUMN_PRIORITY = "priority";
//    }
//
//}

//重写TodoContract
public final class TodoContract{
    public TodoContract(){
        //由于没有私有内容，因而初始化函数无内容
    }
    //定义字符组
    public class TodoNote implements BaseColumns{
        public static final String TABLE_NAME = "note";

//        public static final String COLUMN_ID = "id";
        //由于BaseColums里有默认_ID项因此注释掉了自定义id
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_PRIORITY = "priority";
    }
    //定义创建表的语句
    public static final String SQL_CREATE_NOTES = "create table" +" " + TodoNote.TABLE_NAME
            + "(" + TodoNote._ID + " " +"integer primary key autoincrement,"
            + " " + TodoNote.COLUMN_CONTENT + " " + "text,"
            + " " + TodoNote.COLUMN_DATE + " "+ "date,"
            + " " + TodoNote.COLUMN_STATE + " " + "integer,"
            + " " + TodoNote.COLUMN_PRIORITY + " " + "integer)";
    //虽然在create表语句中已经包含了PRIORITY列了，但是由于之后涉及到了DateBase的UpDate所以仍需一个ALTER
    public static final String SQL_ADD_PRIORITY_COLUMN = "alter table" + " " + TodoNote.TABLE_NAME
            + " " + "add" + " " + TodoNote.COLUMN_PRIORITY + " " +"integer";
}