package com.xuexiang.cdaccount.ExpanableBill;

public class BillDataItem {
    private int Bill_TYPE;
    private String Bill_SubCategory, Bill_Account, Bill_toAccount, Bill_Mumber;
    private double Bill_Money;
    private String year, month, day, time, Bill_Remark;

    public BillDataItem(int Bill_TYPE, String Bill_SubCategory, String Bill_Account, String Bill_toAccount, String Bill_Member, String year, String month, String day, String time, double Bill_Money, String Bill_Remark) {
        this.Bill_TYPE = Bill_TYPE;
        this.Bill_SubCategory = Bill_SubCategory;
        this.Bill_Account = Bill_Account;
        this.Bill_toAccount = Bill_toAccount;
        this.Bill_Mumber = Bill_Member;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.Bill_Money = Bill_Money;
        this.Bill_Remark = Bill_Remark;
    }


    public int getBill_TYPE() {
        return Bill_TYPE;
    }

    public void setBill_TYPE(int bill_TYPE) {
        Bill_TYPE = bill_TYPE;
    }

    public String getBill_SubCategory() {
        return Bill_SubCategory;
    }

    public void setBill_SubCategory(String bill_SubCategory) {
        Bill_SubCategory = bill_SubCategory;
    }

    public String getBill_Account() {
        return Bill_Account;
    }

    public void setBill_Account(String bill_Account) {
        Bill_Account = bill_Account;
    }

    public String getBill_toAccount() {
        return Bill_toAccount;
    }

    public void setBill_toAccount(String bill_toAccount) {
        Bill_toAccount = bill_toAccount;
    }

    public String getBill_Mumber() {
        return Bill_Mumber;
    }

    public void setBill_Mumber(String bill_Mumber) {
        Bill_Mumber = bill_Mumber;
    }

    public double getBill_Money() {
        return Bill_Money;
    }

    public void setBill_Money(double bill_Money) {
        Bill_Money = bill_Money;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBill_Remark() {
        return Bill_Remark;
    }

    public void setBill_Remark(String bill_Remark) {
        Bill_Remark = bill_Remark;
    }
}
