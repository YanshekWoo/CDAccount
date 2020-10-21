package com.xuexiang.cdaccount.ExpanableBill;

import java.util.List;

public class BillDataYear {
    private boolean mYearSelected = true;
    private boolean mExpanded;

    private String mYear;
    private Double mYearIncome;
    private Double mYearOutcome;

    private List<BillDataMonth> mBillDataMonthList;


    public BillDataYear(String year, Double yearIncome, Double yearOutcome, List<BillDataMonth> billDataMonthList) {
        this.mYear = year;
        this.mYearIncome = yearIncome;
        this.mYearOutcome = yearOutcome;
        mBillDataMonthList = billDataMonthList;
    }

    public boolean ismYearSelected() {
        return mYearSelected;
    }

    public void setmYearSelected(boolean mYearSelected) {
        this.mYearSelected = mYearSelected;
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

    public Double getmYearIncome() {
        return mYearIncome;
    }

    public void setmYearIncome(Double mYearIncome) {
        this.mYearIncome = mYearIncome;
    }

    public Double getmYearOutcome() {
        return mYearOutcome;
    }

    public void setmYearOutcome(Double mYearOutcome) {
        this.mYearOutcome = mYearOutcome;
    }

    public List<BillDataMonth> getmBillDataMonthList() {
        return mBillDataMonthList;
    }

    public void setmBillDataMonthList(List<BillDataMonth> mBillDataMonthList) {
        this.mBillDataMonthList = mBillDataMonthList;
    }
}
