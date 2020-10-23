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
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.database.ChartDataEntry;

import java.util.ArrayList;
import java.util.List;

import static com.xuexiang.xutil.XUtil.getContext;
import static com.xuexiang.xutil.XUtil.getResources;

public class MyLineChart {

    /**
     * 初始化折线图
     * @param lineChart
     * 折线图
     * @return lineChart
     */
    public LineChart initLineChart(LineChart lineChart) {
        lineChart.setDescription(null);
        lineChart.setDrawGridBackground(false);
        // 开启手势触摸
        lineChart.setTouchEnabled(true);
        // Y轴禁止缩放
        lineChart.setScaleYEnabled(false);
        // enable scaling and dragging
        lineChart.setDragEnabled(true);
//        // 禁止高亮
        lineChart.setHighlightPerDragEnabled(false);
//        lineChart.setHighlightPerTapEnabled(false);
        //无数据时显示
        lineChart.setNoDataText(getResources().getString(R.string.no_data));
        //设置动画
        lineChart.animateXY(1500, 1500);

        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        Legend legend = lineChart.getLegend();
        setLineChartAxis(xAxis, yAxisLeft, yAxisRight, legend);


        CustomMPLineChartMarkerView mv = new CustomMPLineChartMarkerView(getContext());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);

        return lineChart;
    }

    public void setLineChartAxis(XAxis xAxis, YAxis yAxisLeft, YAxis yAxisRight, Legend legend) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(4f);
        xAxis.setLabelRotationAngle(-70f);
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
        legend.setTextColor(getResources().getColor(R.color.colorAccent)); //设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setForm(Legend.LegendForm.NONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LineData setLinedata(LineChart lineChart, List<ChartDataEntry> chartDataEntries, String legendLable) {
        //设置数据
        List<Entry> entries = new ArrayList<>();
        int length = chartDataEntries.size();
        for (int i = 0; i < length; i++) {
            entries.add(new Entry(i, (float) chartDataEntries.get(i).getDataMoney()));
        }

        // X轴样式
        lineChart.getXAxis().setLabelCount(length);
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int mod = length / 15 + 1;
                int intValue = Math.round(value);
                if(intValue < length && intValue>=length-3 && intValue==value) {
                    return "预测值";
                }
                if(intValue < length-3 && intValue>=0 && intValue==value && intValue % mod==0) {
                    String date = chartDataEntries.get(intValue).getDataName();
                    if(date.length() >= 8) {
                        String year = date.substring(0, 4);
                        String month = date.substring(4, 6);
                        String day = date.substring(6, 8);
                        return "   "+year+"-"+month+"-"+day;
                    }
                    else {
                        return "";
                    }
                }
                else {
                    return "";
                }
            }
        });


        LineDataSet lineDataSet = new LineDataSet(entries, legendLable);
//        lineDataSet.setHighLightColor(R.color.app_color_theme_6);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setDrawCircles(true);
        //设置填充'
        //设置允许填充，渐变
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(150);
        lineDataSet.setFillDrawable(getResources().getDrawable(R.drawable.line_gradient_bg_shape));
        //设置圆滑线
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        lineDataSet.setValueFormatter(new ValueFormatter(){
            @SuppressLint("DefaultLocale")
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        // 折线颜色
//        if(length>=6) {
//            int[] s = new int[length];
//            for(int i=0; i<length-3; i++) {
//                s[i] = getResources().getColor(R.color.app_color_theme_5);
//            }
//            s[length-3] = getResources().getColor(R.color.app_color_theme_1);
//            s[length-2] = getResources().getColor(R.color.app_color_theme_1);
//            s[length-1] = getResources().getColor(R.color.app_color_theme_1);
//            lineDataSet.setColors(s);
//        }
//        else {
//            lineDataSet.setColor(getResources().getColor(R.color.app_color_theme_5));
//        }
        lineDataSet.setColor(getResources().getColor(R.color.app_color_theme_5));
        if(length>=7) {
            int[] s = new int[length];
            for(int i=0; i<length-3; i++) {
                s[i] = getResources().getColor(R.color.app_color_theme_5);
            }
            s[length-3] = getResources().getColor(R.color.app_color_theme_1);
            s[length-2] = getResources().getColor(R.color.app_color_theme_1);
            s[length-1] = getResources().getColor(R.color.app_color_theme_1);
            lineDataSet.setCircleColors(s);
        }
        else {
            lineDataSet.setCircleColor(getResources().getColor(R.color.app_color_theme_5));
        }


        LineData linedata = new LineData(lineDataSet);
        linedata.setValueTextSize(6f);
        return linedata;
    }

}
