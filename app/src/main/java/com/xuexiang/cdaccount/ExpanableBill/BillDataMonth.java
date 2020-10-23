package com.xuexiang.cdaccount.ExpanableBill;

import java.util.List;

public class BillDataMonth {

    private boolean mMonthSelected = false;
    private boolean mExpanded;

    private String mYear;
    private String mMonth;
    private Double mMonthIncome;
    private Double mMonthOutcome;

    private List<BillDataDay> mBillDataDayList;

    public BillDataMonth(String year, String month, Double monthIncome, Double monthOutcome, List<BillDataDay> billDataDayList) {
        this.mYear = year;
        this.mMonth = month;
        this.mMonthIncome = monthIncome;
        this.mMonthOutcome = monthOutcome;
        mBillDataDayList = billDataDayList;
    }



    public boolean ismMonthSelected() {
        return mMonthSelected;
    }

    public void setmMonthSelected(boolean mMonthSelected) {
        this.mMonthSelected = mMonthSelected;
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

    public Double getmMonthIncome() {
        return mMonthIncome;
    }

    public void setmMonthIncome(Double mMonthIncome) {
        this.mMonthIncome = mMonthIncome;
    }

    public Double getmMonthOutcome() {
        return mMonthOutcome;
    }

    public void setmMonthOutcome(Double mMonthOutcome) {
        this.mMonthOutcome = mMonthOutcome;
    }

    public List<BillDataDay> getmBillDataDayList() {
        return mBillDataDayList;
    }

    public void setmBillDataDayList(List<BillDataDay> mBillDataDayList) {
        this.mBillDataDayList = mBillDataDayList;
    }
}
