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

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.database.ChartDataEntry;

import java.util.ArrayList;
import java.util.List;

import static com.xuexiang.xutil.XUtil.getContext;
import static com.xuexiang.xutil.XUtil.getResources;

public class MyPieChart {

    /**
     * 初始化折线图
     * @param pieChart  饼图
     * @return 饼图
     */
    public PieChart initPieChart(PieChart pieChart) {
        //设置百分比显示
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        //设置图标中心空白，空心
        pieChart.setDrawHoleEnabled(true);
        // 禁止高亮
        pieChart.setHighlightPerTapEnabled(false);
        //设置空心圆的弧度百分比，最大100
        pieChart.setHoleRadius(45f);
        pieChart.setHoleColor(Color.WHITE);
        //设置透明弧的样式
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setTransparentCircleRadius(52f);
        //无数据显示
        pieChart.setNoDataText(getResources().getString(R.string.no_data));
        //标签颜色
        pieChart.setEntryLabelColor(Color.BLACK);
        //设置动画
        pieChart.animateXY(1500, 1500);

        Legend legend = pieChart.getLegend();
        setPieChartAxis(legend);

        return pieChart;
    }


    public void setPieChartAxis(Legend legend) {
        //设置Lengend位置
        legend.setTextColor(getContext().getColor(R.color.colorAccent)); //设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setWordWrapEnabled(true);
    }

    public PieData setPiedata(PieChart pieChart, List<ChartDataEntry> chartDataEntries, String legendLable) {
        //设置数据
        List<PieEntry> entries = new ArrayList<>();
        PieEntry others = new PieEntry((float) 0.00, "其他");
        int othersCount = 0;
        final int lenght = chartDataEntries.size();
        for (int i = 0; i < lenght; i++) {
            float money = (float) chartDataEntries.get(i).getDataMoney();
            if(chartDataEntries.get(i).getSumMoney() / money > 20) {
                money += others.getValue();
                others.setY(money);
                others.setLabel(chartDataEntries.get(i).getDataName());
                othersCount++;
            }
            else {
                entries.add(new PieEntry(money, chartDataEntries.get(i).getDataName()));
            }
        }
        if(othersCount == 1){
            entries.add(others);
        }
        else if(othersCount > 1) {
            others.setLabel("其他");
            entries.add(others);
        }

        List<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());


        PieDataSet pieDataSet = new PieDataSet(entries, legendLable);
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setSelectionShift(4f);
        pieDataSet.setColors(colors);
//        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData piedata = new PieData(pieDataSet);
        piedata.setValueFormatter(new PercentFormatter(pieChart));
        piedata.setDrawValues(true);
        piedata.setValueTextSize(12f);
        piedata.setValueTextColor(getContext().getColor(R.color.black));
        return piedata;
    }

}
