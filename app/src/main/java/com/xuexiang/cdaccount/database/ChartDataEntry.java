package com.xuexiang.cdaccount.database;

import androidx.annotation.NonNull;

public class ChartDataEntry implements Comparable<ChartDataEntry>{
    public String dataName;
    public double dataMoney;
    private double sumMoney = 1000000;

    public ChartDataEntry(String name, double s){
        this.dataName = name;
        this.dataMoney = s;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public double getDataMoney() {
        return dataMoney;
    }

    public void setDataMoney(double dataMoney) {
        this.dataMoney = dataMoney;
    }

    public double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(double sumMoney) {
        this.sumMoney = sumMoney;
    }

    @NonNull
    @Override
    public String toString() {
        return dataName+dataMoney;
    }

    @Override
    public int compareTo(ChartDataEntry chartDataEntry) {
        return (int) (chartDataEntry.getDataMoney() - this.dataMoney);
    }


}
