package com.xuexiang.cdaccount.adapter;

import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestItem {
    private boolean myear = true, mmonth = false, mday = false;
    private boolean mrefresh;

    public TestItem(){
    }

    public void addYear(boolean year) {
        myear = year;
    }

    public boolean getYear() {
        return myear;
    }

    public void addMonth(boolean month) {
        mmonth = month;
    }

    public boolean getMonth() {
        return mmonth;
    }

    public void addDay(boolean day) {
        mday = day;
    }

    public boolean getDay() {
        return mday;
    }

    public void addRefresh(boolean refreash) {
        mrefresh = refreash;
    }
    public boolean getRefresh() {
        return mrefresh;
    }

    public  Collection<String> getDemoData1() {
        return Arrays.asList("1", "2", "3", "4", "5");
//        return Arrays.asList("1", "2", "3");
    }

//    //输入年份，返回该年份中有记录的月份
//    public List<String> getMonthByYear(int year){
//
//    }
//
//    //输入年份，返回该年份总收入
//    public int getIncomeByYear (int year) {
//
//    }
//
//    //输入年份，返回该年份总支出
//    public int getOutComeByYear (int year) {
//
//    }
//
//    //输入年份和月份，返回该年该月中有记录的天
//    public List<String> getDayByMonth (int year, int month){
//
//    }
//
//    //输入年份和月份，返回该月总收入
//    public int getIncomeByMonth (int year, int month){
//
//    }
//
//    //输入年份和月份，返回该月总支出
//    public int getOutcomeByMonth (int year, int month){
//
//    }
//
//    //输入年份月份天，返回该日总收入
//    public int getIncomeByDay (int year, int month ,int day){
//
//    }
//
//    //输入年份月份天，返回该日总支出
//    public int getOutcomeByDay (int year, int month, int day){
//
//    }
//
//    //返回流水
//    public List<Bill> getBillByDay (int year, int month, int day, String mainCatagory, String subCatagory, String member, String account, String startTime, String endTime){
//
//    }
//
//    //返回全部账户名
//    public List<String> getAccountName () {
//
//    }
//
//    //返回全部账户金额
//    public List<String> getAccountMoney() {
//
//    }
//
//    //查找账户，存在返回true
//    boolean FindAccountName (String name){
//
//    }
}
