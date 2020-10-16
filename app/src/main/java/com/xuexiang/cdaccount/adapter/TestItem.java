package com.xuexiang.cdaccount.adapter;

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

//    public List<String> getMonth(int year){
//
//    }
//
//    public List<String> getDay (int year, int month){
//
//    }
//
//    public
}
