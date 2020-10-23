package com.xuexiang.cdaccount.ExpanableBill;

import java.util.List;

public class BillDataDay {
    private boolean mDaySelected = false;
    private boolean mExpanded;


    private String mYear;
    private String mMonth;
    private String mDay;
    private Double mDayIncome;
    private Double mDayOutcome;

    private List<BillDataItem> mBillDataItemList;

    public BillDataDay(String year, String month, String day, Double dayIncome, Double dayOutcome, List<BillDataItem> billDataItem) {
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        this.mDayIncome = dayIncome;
        this.mDayOutcome = dayOutcome;
        mBillDataItemList = billDataItem;
    }


    public boolean ismDaySelected() {
        return mDaySelected;
    }

    public void setmDaySelected(boolean mDaySelected) {
        this.mDaySelected = mDaySelected;
    }

    public boolean ismExpanded() {
        return mExpanded;
    }

    public void setmExpanded(boolean mExpanded) {
        this.mExpanded = mExpanded;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public String getmMonth() {
        return mMonth;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public Double getmDayIncome() {
        return mDayIncome;
    }

    public void setmDayIncome(Double mDayIncome) {
        this.mDayIncome = mDayIncome;
    }

    public Double getmDayOutcome() {
        return mDayOutcome;
    }

    public void setmDayOutcome(Double mDayOutcome) {
        this.mDayOutcome = mDayOutcome;
    }

    public List<BillDataItem> getmBillDataItemList() {
        return mBillDataItemList;
    }

    public void setmBillDataItemList(List<BillDataItem> mBillDataItemList) {
        this.mBillDataItemList = mBillDataItemList;
    }
}
