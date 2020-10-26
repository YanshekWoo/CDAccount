package com.xuexiang.cdaccount.arima;

import com.xuexiang.cdaccount.database.ChartDataEntry;

import java.util.List;

public class RunARIMA {

	public static double runPrediction(List<ChartDataEntry> chartDataEntryList) {
		final int length = chartDataEntryList.size();
		double[] dataArray = new double[length];
		for (int i = 0; i < length; i++) {
			dataArray[i] = chartDataEntryList.get(i).getDataMoney();
		}


		ARIMA arima = new ARIMA(dataArray);

		int[] model = arima.getARIMAmodel();

		return arima.aftDeal(arima.predictValue(model[0], model[1]));
	}
}
