package com.xuexiang.cdaccount.arima;
import java.util.*;

public class AR {
	
	double[] stdoriginalData;
	int p;
	ARMAMath armamath=new ARMAMath();
	
	/**
	 * ARģ��
	 * @param  stdoriginalData, p
	 * @param p //pΪMAģ�ͽ���
	 */
	public AR(double [] stdoriginalData,int p)
	{
		this.stdoriginalData=stdoriginalData;
		this.p=p;
	}
/**
 * ����ARģ�Ͳ���
 * @return  v
 */
	public Vector<double[]> ARmodel()
	{
		Vector<double[]> v= new Vector<>();
		v.add(armamath.parcorrCompute(stdoriginalData, p, 0));
		return v;
	}
	
}
