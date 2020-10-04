package com.xuexiang.cdaccount.arima;

import java.util.Vector;

public class ARMAModel
{
	private double [] data = {};
	private int p;		//AR����
	private int q;		//MA����
	
	public ARMAModel(double [] data, int p, int q)
	{
		this.data = data;
		this.p = p;
		this.q = q;
	}
	
	/**
	 * ��ARMAģ���У����ȸ���ԭʼ�������ARģ�͵��Իع�ϵ��(ARϵ��)
	 * ����ARϵ����ԭʼ���ݣ����Ĳв����У����ݲв����е���Э�����������ARMA��MAϵ��
	 * @return ar, ma
	 */
	public Vector<double []> solveCoeOfARMA()
	{
		Vector<double []>vec = new Vector<>();
		
		//ARMAģ��
		double [] armaCoe = new ARMAMethod().computeARMACoe(this.data, this.p, this.q);
		//ARϵ��
		double [] arCoe = new double[this.p + 1];
		System.arraycopy(armaCoe, 0, arCoe, 0, arCoe.length);
		//MAϵ��
		double [] maCoe = new double[this.q + 1];
		System.arraycopy(armaCoe, (this.p + 1), maCoe, 0, maCoe.length);
		
		vec.add(arCoe);
		vec.add(maCoe);
		
		return vec;
	}
}
