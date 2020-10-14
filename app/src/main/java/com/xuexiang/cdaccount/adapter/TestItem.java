package com.xuexiang.cdaccount.adapter;

public class TestItem {
    private int mflag;
    private boolean mrefresh;

    public TestItem(int flag, boolean refresh){
        mflag = flag;
        mrefresh = refresh;
    }

    public int getFlag() {
        return mflag;
    }

    public boolean getRefresh() {
        return mrefresh;
    }
}
