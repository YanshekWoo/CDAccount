package com.xuexiang.cdaccount.arima;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class RunARIMA
{
	public RunARIMA() {
	}

	public float predictNext(List<Entry> entries)
	{
		//获取entries数据
		ArrayList<Double> al=new ArrayList<Double>();
		for(Entry e : entries) {
			al.add((double) e.getY());
		}

		double [] data = new double[al.size()];
		for (int i = 0; i < data.length; ++i)
		{
			data[i] = al.get(i);
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
			System.out.println("BestModel is " + bestModel[0] + " " + bestModel[1]);
			list.add(bestModel);
		}
		al.clear();
		double sumPredict = 0.0;
		for (int k = 0; k < cnt; ++k)
		{
			sumPredict += (double)tmpPredict[k] / (double)cnt;
		}
		int predict = (int)Math.round(sumPredict);
		System.out.println("Predict value="+predict);
		return (float) predict;
	}
}
