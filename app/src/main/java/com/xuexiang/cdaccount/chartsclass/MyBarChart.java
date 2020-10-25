/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.cdaccount.chartsclass;

import android.annotation.SuppressLint;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.model.GradientColor;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.database.ChartDataEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.xuexiang.xutil.XUtil.getContext;
import static com.xuexiang.xutil.XUtil.getResources;

public class MyBarChart {

    /**
     * 初始化柱状图
     * @param barChart
     * 柱状图
     * @return barChart
     */
    public BarChart initBarChart(BarChart barChart) {
        barChart.setDescription(null);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setNoDataText(getResources().getString(R.string.no_data));
        // Y轴禁止缩放
        barChart.setScaleYEnabled(false);
        // 禁止高亮
        barChart.setHighlightFullBarEnabled(false);
        barChart.setHighlightPerTapEnabled(false);
        //设置动画
        barChart.animateXY(1500, 1500);

        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        Legend legend = barChart.getLegend();
        setBarChartAxis(xAxis, yAxisLeft, yAxisRight, legend);

        return barChart;
    }

    public void setBarChartAxis(XAxis xAxis, YAxis yAxisLeft, YAxis yAxisRight, Legend legend) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setGranularity(1f);
//        xAxis.setTextSize(6f);
//        xAxis.setLabelRotationAngle(-70f);
//        xAxis.setAxisMinimum(0);

        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setAxisLineWidth(1);
        yAxisLeft.setEnabled(true);
        yAxisLeft.setAxisMinimum(0);

        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(true);
        yAxisRight.setAxisLineWidth(1);
        yAxisRight.setEnabled(true);
        yAxisRight.setAxisMinimum(0);

        //设置Lengend位置
        legend.setTextColor(getContext().getColor(R.color.colorAccent)); //设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setForm(Legend.LegendForm.NONE);
    }


    /**
     * 设置图表数据
     * @return bardata
     */
    public BarData setBardata(BarChart barChart, List<ChartDataEntry> chartDataEntries, String legendLable) {
        List<BarEntry> entries = new ArrayList<>();
        final int length = chartDataEntries.size();
        for(int i = 0; i < length; i++) {
            entries.add(new BarEntry(i, (float) chartDataEntries.get(i).getDataMoney(), chartDataEntries.get(i).getDataName()));
        }

        BarDataSet barDataSet = new BarDataSet(entries, legendLable);

        barDataSet.setDrawIcons(false);
        //双色柱状图
        int startColor1 = ContextCompat.getColor(Objects.requireNonNull(getContext()), android.R.color.holo_orange_light);
        int startColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
        int startColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
        int startColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_light);
        int startColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
        int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
        int endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
        int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
        int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);

        List<GradientColor> gradientColors = new ArrayList<>();
        gradientColors.add(new GradientColor(startColor1, endColor1));
        gradientColors.add(new GradientColor(startColor2, endColor2));
        gradientColors.add(new GradientColor(startColor3, endColor3));
        gradientColors.add(new GradientColor(startColor4, endColor4));
        gradientColors.add(new GradientColor(startColor5, endColor5));

        //设置渐变色
        barDataSet.setGradientColors(gradientColors);
        // Y值显示样式
        barDataSet.setValueFormatter(new ValueFormatter(){
            @SuppressLint("DefaultLocale")
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        // set text size according to length of datas
        float textSize;
        if(length>19){
            textSize = 2f;
        }
        else if(length>15){
            textSize = 4f;
        }
        else if(length>11){
            textSize = 6f;
        }
        else if(length>7){
            textSize = 8f;

        }
        else {
            textSize = 10f;
        }
        barChart.getXAxis().setTextSize(textSize);
//            barChart.getXAxis().setLabelRotationAngle(0);
        barChart.getXAxis().setLabelCount(length);
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int intValue = (int) value;
                if(intValue < length && intValue>=0 && intValue==value) {
                    return chartDataEntries.get(intValue).getDataName();
                }
                else {
                    return "";
                }
            }
        });


        BarData bardata = new BarData(barDataSet);
        bardata.setValueTextSize(textSize);


        return bardata;
    }
}
