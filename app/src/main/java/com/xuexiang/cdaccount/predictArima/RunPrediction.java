package com.xuexiang.cdaccount.predictArima;

import com.xuexiang.cdaccount.database.ChartDataEntry;

import java.util.List;

public class RunPrediction {

	public double predictNext(List<ChartDataEntry> entries)
	{
//		Scanner ino=null;
//
//		try {
//			ArrayList<Double> arraylist=new ArrayList<Double>();
//			ino=new Scanner(new File(System.getProperty("user.dir")+"/data/ceshidata.txt"));
//			while(ino.hasNext())
//			{
//				arraylist.add(Double.parseDouble(ino.next()));
//			}
//			double[] dataArray=new double[arraylist.size()-1];
//			for(int i=0;i<arraylist.size()-1;i++)
//				dataArray[i]=arraylist.get(i);
//
//			//System.out.println(arraylist.size());
//
//			ARIMA arima=new ARIMA(dataArray);
//
//			int []model=arima.getARIMAmodel();
//			System.out.println("Best model is [p,q]="+"["+model[0]+" "+model[1]+"]");
//			System.out.println("Predict value="+arima.aftDeal(arima.predictValue(model[0],model[1])));
//			System.out.println("Predict error="+(arima.aftDeal(arima.predictValue(model[0],model[1]))-arraylist.get(arraylist.size()-1))/arraylist.get(arraylist.size()-1)*100+"%");
//
//		//	String[] str = (String[])list1.toArray(new String[0]);
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			ino.close();
//		}

//		ArrayList<Double> arraylist=new ArrayList<Double>();
//		for(int i=0; i<entries.size(); i++) {
//			arraylist.add(entries.get(i).getDataMoney());
//		}


		double[] dataArray=new double[entries.size()];
		for(int i=0; i<entries.size(); i++) {
			dataArray[i] = entries.get(i).getDataMoney();
		}
		ARIMA arima=new ARIMA(dataArray);
		int []model=arima.getARIMAmodel();
		return arima.aftDeal(arima.predictValue(model[0],model[1]));
	}
	
	
}
