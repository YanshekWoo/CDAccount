package com.xuexiang.cdaccount.dbclass;

public class AccountDataEntry{
    private String Name;
    private double InMoney;
    private double OutMoney;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getInMoney() {
        return InMoney;
    }

    public void setInMoney(double inMoney) {
        InMoney = inMoney;
    }

    public double getOutMoney() {
        return OutMoney;
    }

    public void setOutMoney(double outMoney) {
        OutMoney = outMoney;
    }

    public AccountDataEntry(String name, double s, double t){
        this.Name = name;
        this.InMoney = s;
        this.OutMoney = t;
    }
}
