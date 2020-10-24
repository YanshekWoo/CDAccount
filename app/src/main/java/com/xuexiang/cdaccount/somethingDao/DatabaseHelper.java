package com.xuexiang.cdaccount.somethingDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     *  @param context
     *
     */
    private Context mContext;
    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DATA_BASE_NAME, null, Constants.VERSION_CODE);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sql;
//        sql = "create table "+"Bill"+"(Bill_ID int, Bill_TYPE int, Bill_SubCategory int, Bill_Account int, Bill_toAccount int, Bill_Member int, year varchar(5), month varchar(3), day varchar(3), time varchar(15), Bill_Remark varchar(255), Bill_Money double)";
//        db.execSQL(sql);
//
//        sql = "create table "+"OutTopCategory"+"(OutTopCategory_ID int, OutTopCategory_Name String)";
//        db.execSQL(sql);
//
//        sql = "create table "+"OutSubCategory"+"(OutSubCategory_ID int, OutSubCategory_Parent, OutSubCategory_Name varchar(15))";
//        db.execSQL(sql);
//
//        sql = "create table "+"InTopCategory"+"(InTopCategory_ID int, InTopCategory_Name varchar(15))";
//        db.execSQL(sql);
//
//        sql = "create table "+"InSubCategory"+"(InSubCategory_ID int, InSubCategory_Parent, InSubCategory_Name)";
//        db.execSQL(sql);
//
//        sql = "create table "+"Account"+"(Account_ID int, Account_Name varchar(15), Account_InMoney double, Account_OutMoney)";
//        db.execSQL(sql);
//
//        sql = "create table "+"Member"+"(Member_ID int, Member_Name varchar(15))";
//        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}