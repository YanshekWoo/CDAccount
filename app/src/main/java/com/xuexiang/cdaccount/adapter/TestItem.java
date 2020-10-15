package com.xuexiang.cdaccount.adapter;

import java.util.Arrays;
import java.util.Collection;

public class TestItem {
    private boolean myear,mmonth,mday;
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
}
