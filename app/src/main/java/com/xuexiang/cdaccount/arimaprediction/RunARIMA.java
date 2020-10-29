package com.xuexiang.cdaccount.arimaprediction;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.xuexiang.cdaccount.dbclass.ChartDataEntry;

import java.util.ArrayList;
import java.util.List;

public class RunARIMA
{
	public RunARIMA() {
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	public double predictNext(List<ChartDataEntry> entries)
	{
		double [] data = new double[entries.size()];
		for (int i = 0; i < data.length; ++i)
		{
			data[i] = entries.get(i).getDataMoney();
		}

		ARIMAModel arima = new ARIMAModel(data);

		ArrayList<int []> list = new ArrayList<>();
		int period = 7;
		int modelCnt = 5, cnt = 0;			//ͨ�����Ԥ���ƽ��ֵ��ΪԤ��ֵ
		int [] tmpPredict = new int [modelCnt];
		for (int k = 0; k < modelCnt; ++k)			//����ͨ��������������м������յĽ��
		{
			int [] bestModel = arima.getARIMAModel(period, list, k != 0);
			if (bestModel.length == 0)
			{
				tmpPredict[k] = (int)data[data.length - period];
				cnt++;
				break;
			}
			else
			{
				int predictDiff = arima.predictValue(bestModel[0], bestModel[1], period);
				tmpPredict[k] = arima.aftDeal(predictDiff, period);
				cnt++;
			}
//			System.out.println("BestModel is " + bestModel[0] + " " + bestModel[1]);
//			Log.i("bestModule", "BestModel is " + bestModel[0] + " " + bestModel[1]);
			list.add(bestModel);
		}

		double sumPredict = 0.0;
		for (int k = 0; k < cnt; ++k)
		{
			sumPredict += (double)tmpPredict[k];
		}

		return sumPredict / cnt;
	}
}
