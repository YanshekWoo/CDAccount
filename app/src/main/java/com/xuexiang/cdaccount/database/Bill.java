package com.xuexiang.cdaccount.database;
public class Bill {
    private int Bill_ID, Bill_TYPE, Bill_SubCategory, Bill_Account, Bill_toAccount, Bill_Mumber;
    private double Bill_Money;
    private String year, month, day, time, Bill_Remark;

    public Bill(int Bill_ID, int Bill_TYPE, int Bill_SubCategory, int Bill_Account, int Bill_toAccount, int Bill_Member, String year, String month, String day, String time, String Bill_Remark, double Bill_Money) {
        this.Bill_Account = Bill_Account;
        this.Bill_ID = Bill_ID;
        this.Bill_TYPE = Bill_TYPE;
        this.Bill_SubCategory = Bill_SubCategory;
        this.Bill_toAccount = Bill_toAccount;
        this.Bill_Mumber = Bill_Member;
        this.Bill_Money = Bill_Money;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.Bill_Remark = Bill_Remark;
    }
}
