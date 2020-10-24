package com.xuexiang.cdaccount.arima;

import org.jetbrains.annotations.NotNull;

import Jama.Matrix;

public class ARMAMath
{
	public double avgData(double[] dataArray)
	{
		return this.sumData(dataArray)/dataArray.length;
	}
	
	public double sumData(@NotNull double[] dataArray)
	{
		double sumData=0;
		for (double v : dataArray) {
			sumData += v;
		}
		return sumData;
	}
	
	public double stderrData(double[] dataArray)
	{
		return Math.sqrt(this.varerrData(dataArray));
	}
	
	public double varerrData(double[] dataArray)
	{
		double variance=0;
		double avgsumData=this.avgData(dataArray);
		
		for(int i=0;i<dataArray.length;i++)
		{
			dataArray[i]-=avgsumData;
			variance+=dataArray[i]*dataArray[i];
		}
		return variance/dataArray.length;//variance error;
	}
	
	/**
	 * ��������صĺ��� Tho(k)=Grma(k)/Grma(0)
	 * @param dataArray ����
	 * @param order ����
	 * @return  autoCor
	 */
	public double[] autocorData(double[] dataArray,int order)
	{
		double[] autoCor=new double[order+1];
		double varData=this.varerrData(dataArray);//��׼������ķ���
		
		for(int i=0;i<=order;i++)
		{
			autoCor[i]=0;
			for(int j=0;j<dataArray.length-i;j++)
			{
				autoCor[i]+=dataArray[j+i]*dataArray[j];
			}
			autoCor[i]/=dataArray.length;
			autoCor[i]/=varData;
		}
		return autoCor;
	}
	
/**
 * Grma
 * @param dataArray  数据数组
 * @param order  序列数
 * @return 序列的自相关系数
 */
	public double[] autocorGrma(double[] dataArray,int order)
	{
		double[] autoCor=new double[order+1];
		for(int i=0;i<=order;i++)
		{
			autoCor[i]=0;
			for(int j=0;j<dataArray.length-i;j++)
			{
				autoCor[i]+=dataArray[j+i]*dataArray[j];
			}
			
			autoCor[i]/=(dataArray.length-i);
		}
		return autoCor;
	}
	
/**
 * ��ƫ�����ϵ��
 * @param dataArray  数据数组
 * @param order 序列数
 * @return  parautocor
 */
	public double[] parautocorData(double[] dataArray,int order)
	{
		double[] parautocor =new double[order];
		
		for(int i=1;i<=order;i++)
	    {
			parautocor[i-1]=this.parcorrCompute(dataArray, i,0)[i-1];
	    }
		return parautocor;
	}
/**
 * ����Toplize����
 * @param dataArray  数据数组
 * @param order 序列数
 * @return  toplizeMatrix
 */
	public double[][] toplize(double[] dataArray,int order)
	{
		double[][] toplizeMatrix=new double[order][order];
		double[] atuocorr=this.autocorData(dataArray,order);

		for(int i=1;i<=order;i++)
		{
			int k=1;
			for(int j=i-1;j>0;j--)
			{
				toplizeMatrix[i-1][j-1]=atuocorr[k++];
			}
			toplizeMatrix[i-1][i-1]=1;
			int kk=1;
			for(int j=i;j<order;j++)
			{
				toplizeMatrix[i-1][j]=atuocorr[kk++];
			}
		}
		return toplizeMatrix;
	}

	/**
	 * ��MAģ�͵Ĳ���
	 * @param autocorData  自相关数据
	 * @param q 差分
	 * @return  maPara
	 */
	public double[] getMApara(double[] autocorData,int q)
	{
		double[] maPara=new double[q+1];//��һ�������������������q�����ma����sigma2,ma1,ma2...
		double[] tempmaPara=maPara;
		double temp=0;
		boolean iterationFlag=true;
		//�ⷽ����
		//�������ⷽ����
		maPara[0]=1;//��ʼ��
		while(iterationFlag)
		{
			for(int i=1;i<maPara.length;i++)
			{
				temp+=maPara[i]*maPara[i];
			}
			tempmaPara[0]=autocorData[0]/(1+temp);
		
			for(int i=1;i<maPara.length;i++)
			{
				temp=0;
				for(int j=1;j<maPara.length-i;j++)
				{
					temp+=maPara[j]*maPara[j+i];
				}
				tempmaPara[i]=-(autocorData[i]/maPara[0]-temp);
			}
			iterationFlag=false;
			for(int i=0;i<maPara.length;i++)
			{
				if(maPara[i]!=tempmaPara[i])
				{
					iterationFlag=true;
					break;
				}
			}
			
			maPara=tempmaPara;
		}
		
		return maPara;
	}
	/**
	 * �����Իع�ϵ��
	 * @param dataArray 数据数组
	 * @param p 差分
	 * @param q ,
	 * @return result
	 */
	public double[] parcorrCompute(double[] dataArray,int p,int q)
	{
		double[][] toplizeArray=new double[p][p];//p��toplize����
		
		double[] atuocorr=this.autocorData(dataArray,p+q);//����p+q�׵�����غ���
		double[] autocorrF=this.autocorGrma(dataArray, p+q);//����p+q�׵������ϵ����
		for(int i=1;i<=p;i++)
		{
			int k=1;
			for(int j=i-1;j>0;j--)
			{
				toplizeArray[i-1][j-1]=atuocorr[q+k++];
			}
			toplizeArray[i-1][i-1]=atuocorr[q];
			int kk=1;
			for(int j=i;j<p;j++)
			{
				toplizeArray[i-1][j]=atuocorr[q+kk++];
			}
		}
		
	    Matrix toplizeMatrix = new Matrix(toplizeArray);//�ɶ�λ����ת���ɶ�ά����
	    Matrix toplizeMatrixinverse=toplizeMatrix.inverse();//������������
		
	    double[] temp=new double[p];
		if (p >= 0) System.arraycopy(atuocorr, q + 1, temp, 0, p);
	    
		Matrix autocorrMatrix=new Matrix(temp, p);
		Matrix parautocorDataMatrix=toplizeMatrixinverse.times(autocorrMatrix);
		
		double[] result=new double[parautocorDataMatrix.getRowDimension()+1];
		for(int i=0;i<parautocorDataMatrix.getRowDimension();i++)
		{
			result[i]=parautocorDataMatrix.get(i,0);
		}
		
		//����sigmat2
		double sum2=0;
		for(int i=0;i<p;i++)
			for(int j=0;j<p;j++)
			{
				sum2+=result[i]*result[j]*autocorrF[Math.abs(i-j)];
			}
		result[result.length-1]=autocorrF[0]-sum2; //result
		
		
			return result;
	}

	
	}
