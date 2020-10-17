package com.xuexiang.cdaccount.database;

import androidx.annotation.NonNull;

public class ChartDataEntry implements Comparable<ChartDataEntry>{
    public String dataName;
    public double dataMoney;

    public ChartDataEntry(String name, double s){
        this.dataName = name;
        this.dataMoney = s;
    }

    public String getDataName() {
        return dataName;
    }

    public double getDataMoney() {
        return dataMoney;
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
