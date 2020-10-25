package com.xuexiang.cdaccount.arimaprediction;


import java.util.Random;
import java.util.Vector;

import Jama.Matrix;

import static java.lang.Math.log;

public class ARMAMethod
{
	public ARMAMethod()
	{
		
	}
	
	/**
	 * @param originalData
	 * @return ��ֵ
	 */
	public double avgData(double [] originalData)
	{
		return this.sumData(originalData) / originalData.length;
	}
	
	/**
	 * @param originalData
	 * @return ���
	 */
	public double sumData(double [] originalData)
	{
		double sum = 0.0;

		for (double originalDatum : originalData) {
			sum += originalDatum;
		}
		return sum;
	}
	
	/**
	 * �����׼�� sigma = sqrt(var);
	 * @param originalData
	 * @return ��׼��
	 */
	public double stdErrData(double [] originalData)
	{
		return Math.sqrt(this.varErrData(originalData));
	}
	
	
	/**
	 * ���㷽�� var = sum(x - mu) ^2 / N;
	 * @param originalData
	 * @return ����
	 */
	public double varErrData(double [] originalData)
	{
		if (originalData.length <= 1)
			return 0.0;
		
		double var = 0.0;
		double mu = this.avgData(originalData);

		for (double originalDatum : originalData) {
			var += (originalDatum - mu) * (originalDatum - mu);
		}
		var /= (originalData.length - 1);		//�������ƫ����
		
		return var;
	}
	
	/**
	 * ��������غ���(ϵ��) rou(k) = C(k) / C(0);
	 * ���� C(k) = sum((x(t) - mu)*(x(t - k) - mu)) / (N - k),
	 * C(0) = var =  sum(x(t) - mu) ^2 / N;
	 * @param originalData
	 * @param order
	 * @return ����غ���(rou(k))
	 */
	public double [] autoCorrData(double [] originalData, int order)
	{
		double [] autoCov = this.autoCovData(originalData, order);
		double [] autoCorr = new double[order + 1];		//Ĭ�ϳ�ʼ��Ϊ0
		double var = this.varErrData(originalData);
		
		if (var != 0)
		{
			for (int i = 0; i < autoCorr.length; ++i)
			{
				autoCorr[i] = autoCov[i] / var;
			}
		}
		
		return autoCorr;
	}
	
	/**
	 * @param dataFir
	 * @param dataSec
	 * @return Ƥ��ѷ���ϵ��(�����)
	 */
	public double mutalCorr(double [] dataFir, double [] dataSec)
	{
		double sumX = 0.0;
		double sumY = 0.0;
		double sumXY = 0.0;
		double sumXSq = 0.0;
		double sumYSq = 0.0;
		int len = 0;
		
		if (dataFir.length != dataSec.length)
		{
			len = Math.min(dataFir.length, dataSec.length);
		}
		else
		{
			len = dataFir.length;
		}
		for (int i = 0; i < len; ++i)
		{
			sumX += dataFir[i];
			sumY += dataSec[i];
			sumXY += dataFir[i] * dataSec[i];
			sumXSq += dataFir[i] * dataFir[i];
			sumYSq += dataSec[i] * dataSec[i];
		}		
		
		double numerator = sumXY - sumX * sumY / len;
		double denominator = Math.sqrt((sumXSq - sumX * sumX / len) * (sumYSq - sumY * sumY / len));
		
		if (denominator == 0)
		{
			return 0.0;
		}
		
		return numerator/ denominator;
	}
	
	//
	
	/**
	 * @param data
	 * @return		����ؾ���
	 */
	public double [][] computeMutalCorrMatrix(double [][] data)
	{
		double [][] result = new double[data.length][data.length];
		for (int i = 0; i < data.length; ++i)
		{
			for (int j = 0; j < data.length; ++j)
			{
				result[i][j] = this.mutalCorr(data[i], data[j]);
			}
		}
		
		return result;
	}
	
	/**
	 * ������Э���C(k) = sum((x(t) - mu)*(x(t - k) - mu)) / (N - k);
	 * @param originalData
	 * @param order
	 * @return ��Э����(gama(k))-->��Ϊ�������ϵ��
	 */
	public double [] autoCovData(double [] originalData, int order)
	{
		double mu = this.avgData(originalData);
		double [] autoCov = new double[order + 1];
		
		for (int i = 0; i <= order; ++i)
		{
			autoCov[i] = 0.0;
			for (int j = 0; j < originalData.length - i; ++j)
			{
				autoCov[i] += (originalData[i + j] - mu) * (originalData[j] - mu);
			}
			autoCov[i] /= (originalData.length - i);
		}
		return autoCov;
	}
	
	//
	
	/**
	 * @param vec		ģ�͵�ϵ��
	 * @param data		����
	 * @param type		ѡ����ģ��
	 * @return
	 */
	public double getModelAIC(Vector<double []>vec, double [] data, int type)
	{
		int n = data.length;
		int p = 0, q = 0;
		double tmpAR = 0.0, tmpMA = 0.0;
		double sumErr = 0.0;
		Random random = new Random();
		
		/* MA */	
		if (type == 1)
		{
			double [] maCoe = vec.get(0);
			q = maCoe.length;
			double [] errData = new double[q];
			
			for (int i = q - 1; i < n; ++i)
			{
				tmpMA = 0.0;
				for (int j = 1; j < q; ++j)
				{
					tmpMA += maCoe[j] * errData[j];
				}

				if (q - 1 >= 0) System.arraycopy(errData, 0, errData, 1, q - 1);
				errData[0] = random.nextGaussian() * Math.sqrt(maCoe[0]);
				sumErr += (data[i] - tmpMA) * (data[i] - tmpMA);
			}
//			return Math.log(sumErr) + (q + 1) * 2 / n;
			return (n - (q - 1)) * log(sumErr / (n - (q - 1))) + (q + 1) * 2;
			// return  (n-(q-1))*Math.log(sumErr/(n-(q-1)))+(q)*Math.log(n-(q-1));		//AIC ��С���˹���
		}
		/* AR */
		else if (type == 2)
		{
			double [] arCoe = vec.get(0);
			p = arCoe.length;
			
			for (int i = p - 1; i < n; ++i)
			{
				tmpAR = 0.0;
				for (int j = 0; j < p - 1; ++j)
				{
					tmpAR += arCoe[j] * data[i - j - 1];
				}
				sumErr += (data[i] - tmpAR) * (data[i] - tmpAR);
			}
//			return Math.log(sumErr) + (p + 1) * 2 / n;
			return (n - (p - 1)) * log(sumErr / (n - (p - 1))) + (p + 1) * 2;
			// return (n-(p-1))*Math.log(sumErr/(n-(p-1)))+(p)*Math.log(n-(p-1));		//AIC ��С���˹���
		}
		/* ARMA */
		else
		{
			double [] arCoe = vec.get(0);
			double [] maCoe = vec.get(1);
			p = arCoe.length;
			q = maCoe.length;
			double [] errData = new double[q];
			
			for (int i = p - 1; i < n; ++i)
			{
				tmpAR = 0.0;
				for (int j = 0; j < p - 1; ++j)
				{
					tmpAR += arCoe[j] * data[i - j - 1];
				}
				tmpMA = 0.0;
				for (int j = 1; j < q; ++j)
				{
					tmpMA += maCoe[j] * errData[j];
				}

				if (q - 1 >= 0) System.arraycopy(errData, 0, errData, 1, q - 1);
				errData[0] = random.nextGaussian() * Math.sqrt(maCoe[0]);
				
				sumErr += (data[i] - tmpAR - tmpMA) * (data[i] - tmpAR - tmpMA);
			}
//			return Math.log(sumErr) + (q + p + 1) * 2 / n;
			return (n - (q + p - 1)) * log(sumErr / (n - (q + p - 1))) + (p + q) * 2;
			// return (n-(p-1))*Math.log(sumErr/(n-(p-1)))+(p+q-1)*Math.log(n-(p-1));		//AIC ��С���˹���
		}
	}

	// Y-W�������
	/**
	 * @param garma	����������ݵ�Э����
	 * @return ���ؾ���Y-W�������Ľ�������������������һ��Ԫ�ش洢����ģ���е���������
	 */
	public double [] YWSolve(double [] garma)
	{
		int order = garma.length - 1;
		double [] garmaPart = new double[order];
		System.arraycopy(garma, 1, garmaPart, 0, order);
		
		// ��Э����ת��Ϊ�������ʽ
		double [][] garmaArray = new double[order][order];
		for (int i = 0; i < order; ++i)
		{
			// �Խ���
			garmaArray[i][i] = garma[0];
			
			//������
			int subIndex = i;
			for (int j = 0; j < i; ++j)
			{
				garmaArray[i][j] = garma[subIndex--];
			}
			
			//������
			int topIndex = i;
			for (int j = i + 1; j < order; ++j)
			{
				garmaArray[i][j] = garma[++topIndex];
			}
		}
		
		/* ������juma������ʵ���˴󲿷ֶԾ���Ĳ��� */
		/* ���ܻ���ھ��󲻿����������ھ��󲻿���ʱ����ͨ�����Խ���Ԫ��ȫ������1e-6������ */
		Matrix garmaMatrix = new Matrix(garmaArray);
		Matrix garmaMatrixInverse = garmaMatrix.inverse();
		Matrix autoReg = garmaMatrixInverse.times(new Matrix(garmaPart, order));
		
		double [] result = new double[autoReg.getRowDimension() + 1];
		for (int i = 0; i < autoReg.getRowDimension(); ++i)
		{
			result[i] = autoReg.get(i, 0);
		}
		
		double sum = 0.0;
		for (int i = 0; i < order; ++i)
		{
			sum += result[i] * garma[i];
		}
		result[result.length - 1] = garma[0] - sum;
		return result;
	}

	// Levinson �������
	
	/**
	 * @param garma  ����������ݵ�Э����
	 * @return	���ؽ���ĵ�һ��Ԫ�ش�������ڵ��������еķ��
	 * �����Ԫ�ش�����ǵ��������д洢��ϵ��
	 */
	public double [][] LevinsonSolve(double [] garma)
	{
		int order = garma.length - 1;
		double [][] result = new double[order + 1][order + 1];
		double [] sigmaSq = new double[order + 1];
		
		sigmaSq[0] = garma[0];
		result[1][1] = garma[1] / sigmaSq[0];
		sigmaSq[1] = sigmaSq[0] * (1.0 - result[1][1] * result[1][1]);
		for (int k = 1; k < order; ++k)
		{
			double sumTop = 0.0;
			double sumSub = 0.0;
			for (int j = 1; j <= k; ++j)
			{
				sumTop += garma[k + 1 - j] * result[k][j];
				sumSub += garma[j] * result[k][j];
			}
			result[k + 1][k + 1] = (garma[k + 1] - sumTop) / (garma[0] - sumSub);
			for (int j = 1; j <= k; ++j)
			{
				result[k + 1][j] = result[k][j] - result[k + 1][k + 1] * result[k][k + 1 - j];
			} 
			sigmaSq[k + 1] = sigmaSq[k] * (1.0 - result[k + 1][k + 1] * result[k + 1][k + 1]);
		}
		result[0] = sigmaSq;
		
		return result;
	}

	// ���AR(p)��ϵ��
	
	/**
	 * @param originalData	ԭʼ����
	 * @param p		ģ�͵Ľ���
	 * @return		ARģ�͵�ϵ��
	 */
	public double [] computeARCoe(double [] originalData, int p)
	{
		double [] garma = this.autoCovData(originalData, p);		//p+1
		
		double [][] result = this.LevinsonSolve(garma);		//(p + 1) * (p + 1)
		double [] ARCoe = new double[p + 1];
		if (p >= 0) System.arraycopy(result[p], 1, ARCoe, 0, p);
		ARCoe[p] = result[0][p];		//��������
		
//		 return this.YWSolve(garma);
		return ARCoe;
	}

	// ���MA(q)��ϵ��
	
	/**
	 * @param originalData   ԭʼ����
	 * @param q			ģ�ͽ���
	 * @return			MAϵ��
	 */
	public double [] computeMACoe(double [] originalData, int q)
	{
		// ȷ����ѵ�p
//		int p = 0;
//		double minAIC = Double.MAX_VALUE;
//		int len = originalData.length;
//		for (int i = 1; i < len; ++i)
//		{
//			double [] garma = this.autoCovData(originalData, i);
//			double [][] result = this.LevinsonSolve(garma);
//			
//			double [] ARCoe = new double[i + 1];
//			for (int k = 0; k < i; ++k)
//			{
//				ARCoe[k] = result[i][k + 1];
//			}
//			ARCoe[i] = result[0][i];
////			double [] ARCoe = this.YWSolve(garma);
//			
//			Vector<double []> vec = new Vector<>();
//			vec.add(ARCoe);
//			double aic = this.getModelAIC(vec, originalData, 2);
//			if (aic < minAIC)
//			{
//				minAIC = aic;
//				p = i;
//			}	
//		}
		
		int p = Math.max((int) log(originalData.length), 1);
		
//		System.out.println("The best p is " + p);
		// ��ȡϵ��
		double [] bestGarma = this.autoCovData(originalData, p);
		double [][] bestResult = this.LevinsonSolve(bestGarma);
		
		double [] alpha = new double[p + 1];
		alpha[0] = -1;
		if (p >= 0) System.arraycopy(bestResult[p], 1, alpha, 1, p);

//		double [] result = this.YWSolve(bestGarma);
//		double [] alpha = new double[p + 1];
//		alpha[0] = -1;
//		for (int i = 1; i <= p; ++i)
//		{
//			alpha[i] = result[i - 1];
//		}
		double [] paraGarma = new double[q + 1];
		for (int k = 0; k <= q; ++k)
		{
			double sum = 0.0;
			for (int j = 0; j <= p - k; ++j)
			{
				sum += alpha[j] * alpha[k + j];
			}
			paraGarma[k] = sum / bestResult[0][p];
		}
		
		double [][] tmp = this.LevinsonSolve(paraGarma);
		double [] MACoe = new double[q + 1];
		for (int i = 1; i < MACoe.length; ++i)
		{
			MACoe[i] = -tmp[q][i];
		}
		MACoe[0] = 1 / tmp[0][q];		//��������
		
//		double [] tmp = this.YWSolve(paraGarma);
//		double [] MACoe = new double[q + 1];
//		System.arraycopy(tmp, 0, MACoe, 1, tmp.length - 1);
//		MACoe[0] = tmp[tmp.length - 1];
		
		return MACoe;
	}

	// ���ARMA(p, q)��ϵ��
	
	/**
	 * @param originalData		ԭʼ����
	 * @param p			ARģ�ͽ���
	 * @param q			MAģ�ͽ���
	 * @return			ARMAģ��ϵ��
	 */
	public double [] computeARMACoe(double [] originalData, int p, int q)
	{
		double [] allGarma = this.autoCovData(originalData, p + q);
		double [] garma = new double[p + 1];
		System.arraycopy(allGarma, q, garma, 0, garma.length);
		double [][] arResult = this.LevinsonSolve(garma);
		
		// AR
		double [] ARCoe = new double[p + 1];
		if (p >= 0) System.arraycopy(arResult[p], 1, ARCoe, 0, p);
		ARCoe[p] = arResult[0][p];
//		double [] ARCoe = this.YWSolve(garma);
		
		// MA
		double [] alpha = new double[p + 1];
		alpha[0] = -1;
		System.arraycopy(ARCoe, 0, alpha, 1, p);
		
		double [] paraGarma = new double[q + 1];
		for (int k = 0; k <= q; ++k)
		{
			double sum = 0.0;
			for (int i = 0; i <= p; ++i)
			{
				for (int j = 0; j <= p; ++j)
				{
					sum += alpha[i] * alpha[j] * allGarma[Math.abs(k + i - j)];
				}
			}
			paraGarma[k] = sum;
		}
		double [][] maResult = this.LevinsonSolve(paraGarma);
		double [] MACoe = new double[q + 1];
		if (q >= 0) System.arraycopy(maResult[q], 1, MACoe, 1, q);
		MACoe[0] = maResult[0][q];
		
//		double [] tmp = this.YWSolve(paraGarma);
//		double [] MACoe = new double[q + 1];
//		System.arraycopy(tmp, 0, MACoe, 1, tmp.length - 1);
//		MACoe[0] = tmp[tmp.length - 1];
		
		double [] ARMACoe = new double[p + q + 2];
		for (int i = 0; i < ARMACoe.length; ++i)
		{
			if (i < ARCoe.length)
			{
				ARMACoe[i] = ARCoe[i];
			}
			else
			{
				ARMACoe[i] = MACoe[i - ARCoe.length];
			}
		}
		return ARMACoe;
	}
}
