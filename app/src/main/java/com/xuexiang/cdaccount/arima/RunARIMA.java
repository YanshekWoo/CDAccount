package com.xuexiang.cdaccount.arima;

import com.xuexiang.cdaccount.database.ChartDataEntry;

import java.util.List;

public class RunARIMA {

	public double runPrediction(List<ChartDataEntry> chartDataEntryList) {
		double[] dataArray = new double[chartDataEntryList.size()];
		for (int i = 0; i < chartDataEntryList.size(); i++) {
			dataArray[i] = chartDataEntryList.get(i).getDataMoney();
		}


		ARIMA arima = new ARIMA(dataArray);

		int[] model = arima.getARIMAmodel();

		return arima.aftDeal(arima.predictValue(model[0], model[1]));
	}
}
