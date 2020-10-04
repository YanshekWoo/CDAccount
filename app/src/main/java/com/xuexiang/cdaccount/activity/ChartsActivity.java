/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.cdaccount.activity;


import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;



import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.charts.MyBarChart;
import com.xuexiang.cdaccount.charts.MyLineChart;
import com.xuexiang.cdaccount.charts.MyPieChart;
import com.xuexiang.cdaccount.core.BaseActivity;

import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xutil.data.DateUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:21
 */
public class ChartsActivity extends BaseActivity {


    @BindView(R.id.lineChart2)
    LineChart mLineChart;

    @BindView(R.id.barChart2)
    BarChart mBarChart;

    @BindView(R.id.pieChart2)
    PieChart mPieChart;

    @BindView(R.id.btn_date_start2)
    XUIAlphaButton Btn_date_start;

    @BindView(R.id.btn_date_end2)
    XUIAlphaButton Btn_date_end;

    @BindView(R.id.chart_title)
    TitleBar mTitleBar;

    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;


    // 日期选择器
    private TimePickerView mDatePickerStart;
    private TimePickerView mDatePickerEnd;
    private Date mDateStart;
    private Date mDateEnd;

    //图表定义类
    MyBarChart myBarChart;
    MyPieChart myPieChart;
    MyLineChart myLineChart;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_charts;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }


    @Override
    protected boolean isSupportSlideBack() {
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void initViews() {
        initTitleBar();
        initTimePicker();
        initChart();
        selectChart(0);
    }



    protected void initTitleBar() {
        String category = getIntent().getStringExtra("category");
        mTitleBar.setTitle(category);
        mTitleBar.setLeftClickListener(view -> {
            finish();
        });
        mTitleBar.setCenterClickListener(v -> {
            XToastUtils.toast("点击标题");
        });
    }



    /**
     *  初始化图表
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void initChart() {
        myBarChart = new MyBarChart();
        myPieChart = new MyPieChart();
        myLineChart = new MyLineChart();

        //柱状图
        mBarChart = myBarChart.initBarChart(mBarChart);
        //饼图
        mPieChart = myPieChart.initPieChart(mPieChart);
        //折线图
        mLineChart = myLineChart.initLineChart(mLineChart);

        refreshCharts();
    }







    /**
     * 更新所有图表数据
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void refreshCharts() {
        // 柱状图
        BarData barData = myBarChart.setBardata();
        mBarChart.setData(barData);
        mBarChart.animateXY(1500, 1500);
        mBarChart.invalidate();

        //饼图
        PieData pieData = myPieChart.setPiedata(mPieChart);
        mPieChart.setData(pieData);
        mPieChart.animateXY(1500, 1500);
        mPieChart.invalidate();

        //折线图
        LineData lineData = myLineChart.setLinedata();
        mLineChart.setData(lineData);
        mLineChart.animateXY(1500, 1500);
        mLineChart.invalidate();

    }



    /**
     * 初始化日期选择控件
     */
    protected void initTimePicker() {
        mDateEnd = DateUtils.string2Date(getIntent().getStringExtra("dateend"), DateUtils.yyyyMMdd.get());
        Btn_date_end.setText(DateUtils.date2String(mDateEnd, DateUtils.yyyyMMdd.get()));

        mDateStart = DateUtils.string2Date(getIntent().getStringExtra("datestart"), DateUtils.yyyyMMdd.get());
        Btn_date_start.setText(DateUtils.date2String(mDateStart, DateUtils.yyyyMMdd.get()));

    }


    /**
     * 日期选择控件
     * @param view
     * 当前视图
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.btn_date_start2, R.id.btn_date_end2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_date_start2:
                showDatePickerStart();
                break;
            case R.id.btn_date_end2:
                showDatePickerEnd();
                break;
            default:
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDatePickerStart() {
        if (mDatePickerStart == null) {
            //选择器初始化
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDateStart);
            mDatePickerStart = new TimePickerBuilder(this, (date, v) ->
            {
                //选择器监听
                mDateStart = date;
                Btn_date_start.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
                if(mDateStart.after(mDateEnd)) {
                    XToastUtils.error("开始日期不能晚于结束日期");
                }
                refreshCharts();
            })
                    .setDate(calendar)  //默认日期为前一个页面的日期
                    .setTimeSelectChangeListener(date -> {
                        if(date.after(mDateEnd)) {
                            XToastUtils.error("开始日期不能晚于结束日期");
                        }
                    })

                    .setTitleText("开始日期")
                    .build();
        }
        mDatePickerStart.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDatePickerEnd() {
        if (mDatePickerEnd == null) {
            //选择器初始化
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mDateEnd);
            mDatePickerEnd = new TimePickerBuilder(this, (date, v) ->
            {
                //选择器监听
                mDateEnd = date;
                Btn_date_end.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
                if(mDateStart.after(mDateEnd)) {
                    XToastUtils.error("结束日期不能早于开始日期");
                }
                refreshCharts();
            })
                    .setDate(calendar)  //默认日期为前一个页面的日期
                    .setTimeSelectChangeListener(date -> {
                        if(date.before(mDateStart)) {
                            XToastUtils.error("结束日期不能早于开始日期");
                        }
                    })
                    .setTitleText("结束日期")
                    .build();
        }
        mDatePickerEnd.show();
    }



    /**
     * 隐藏切换图表
     * @param choice
     * 悬浮按钮选择
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void selectChart(int choice) {
        switch (choice) {
            case 0:
                mPieChart.setVisibility(View.VISIBLE);
                mBarChart.setVisibility(View.GONE);
                mLineChart.setVisibility(View.GONE);
                break;
            case 1:
                mPieChart.setVisibility(View.GONE);
                mBarChart.setVisibility(View.VISIBLE);
                mLineChart.setVisibility(View.GONE);
                break;
            case 2:
                mPieChart.setVisibility(View.GONE);
                mBarChart.setVisibility(View.GONE);
                mLineChart.setVisibility(View.VISIBLE);
                break;
        }
        refreshCharts();
    }


    /**
     * 多选按钮监听
     * @param v
     * 控件视图
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.fab_piechart, R.id.fab_barchart, R.id.fab_linechart})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_piechart:
                selectChart(0);
                break;
            case R.id.fab_barchart:
                selectChart(1);
                break;
            case R.id.fab_linechart:
                selectChart(2);
                break;
            default:
                selectChart(0);
                break;
        }
        mFloatingActionMenu.toggle(false);
    }

}
