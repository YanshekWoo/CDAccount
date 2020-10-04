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

package com.xuexiang.cdaccount.charts;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.arima.RunARIMA;

import java.util.ArrayList;
import java.util.List;

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
        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        //无数据时显示
        lineChart.setNoDataText(getResources().getString(R.string.no_data));
        //设置动画
        lineChart.animateXY(1500, 1500);

        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        Legend legend = lineChart.getLegend();
        setLineChartAxis(xAxis, yAxisLeft, yAxisRight, legend);
        return lineChart;
    }

    public void setLineChartAxis(XAxis xAxis, YAxis yAxisLeft, YAxis yAxisRight, Legend legend) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);

        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setAxisLineWidth(1);
        yAxisLeft.setEnabled(true);

        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(true);
        yAxisRight.setAxisLineWidth(1);
        yAxisRight.setEnabled(true);

        //设置Lengend位置
        legend.setTextColor(getResources().getColor(R.color.app_color_theme_5)); //设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LineData setLinedata() {
        //设置数据
        List<Entry> entries = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            entries.add(new Entry(i, (float) (Math.random()) * 80));
//        }

        entries.add(new Entry(0, (float) 2285));
        entries.add(new Entry(1, (float) 1670));
        entries.add(new Entry(2, (float) 2341));
        entries.add(new Entry(3, (float) 1781));
        entries.add(new Entry(4, (float) 1737));
        entries.add(new Entry(5, (float) 2210));
        entries.add(new Entry(6, (float) 2824));
        entries.add(new Entry(7, (float) 3141));
        entries.add(new Entry(8, (float) 2089));
        entries.add(new Entry(9, (float) 1989));
        //预测 predict
        RunARIMA ra = new RunARIMA();
        for(int i=10; i < 13; i++) {
            float predict = ra.predictNext(entries);
            entries.add(new Entry(i, predict));
        }


        LineDataSet lineDataSet = new LineDataSet(entries, "折线图数据");
        lineDataSet.setColor(getResources().getColor(R.color.app_color_theme_5));
        lineDataSet.setCircleColor(getResources().getColor(R.color.app_color_theme_7));
        lineDataSet.setLineWidth(2f);
        //设置填充
        //设置允许填充，渐变
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(80);
        lineDataSet.setFillDrawable(getResources().getDrawable(R.drawable.line_gradient_bg_shape));

        LineData linedata = new LineData(lineDataSet);
        linedata.setValueTextSize(11f);
        return linedata;
    }

}
