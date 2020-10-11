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

package com.xuexiang.cdaccount.fragment.charts;

import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.RequiresApi;


import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.ChartsActivity;
import com.xuexiang.cdaccount.charts.MyBarChart;
import com.xuexiang.cdaccount.charts.MyLineChart;
import com.xuexiang.cdaccount.charts.MyPieChart;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.tabbar.TabControlView;
import com.xuexiang.xutil.data.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author Xinshuo Hu
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class ChartsFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    @BindView(R.id.chart_tab)
    TabLayout mTabLayout;

    @BindView(R.id.lineChart)
    LineChart mLineChart;

    @BindView(R.id.barChart)
    BarChart mBarChart;

    @BindView(R.id.pieChart)
    PieChart mPieChart;

    @BindView(R.id.btn_date_start)
    XUIAlphaButton Btn_date_start;

    @BindView(R.id.btn_date_end)
    XUIAlphaButton Btn_date_end;

    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;

    @BindView(R.id.inout_select)
    TabControlView mTabControlView;


    // 日期选择器
    private TimePickerView mDatePickerStart;
    private TimePickerView mDatePickerEnd;
    private Date mDateStart;
    private Date mDateEnd;


    //图表定义类
    MyBarChart myBarChart;
    MyPieChart myPieChart;
    MyLineChart myLineChart;


    //tab选择项
    private CharSequence tabSelected;



    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }


    /**
     * 布局的资源id
     * @return LayoutId
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_charts;
    }


    /**
     * 初始化控件
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initViews() {
        initTabControlView();
        init_tab();
        initTimePicker();
        initChart();
        selectChart(0);
    }


    /**
     * 初始化收入支出单选按钮
     */
    private void initTabControlView() {
        try {
            mTabControlView.setItems(ResUtils.getStringArray(R.array.inout_param_option), ResUtils.getStringArray(R.array.inout_param_value));
            mTabControlView.setDefaultSelection(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTabControlView.setOnTabSelectionChangedListener((title, value) -> {
            XToastUtils.toast("选中了：" + title + ", 选中的值为：" + value);
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
        mBarChart = setBarChartClickListener(mBarChart);
        //饼图
        mPieChart = myPieChart.initPieChart(mPieChart);
        mPieChart = setPieChartClickListener(mPieChart);
        //折线图
        mLineChart = myLineChart.initLineChart(mLineChart);

        refreshCharts();
    }



    /**
     * 设置柱状图点击事件
     * @param barChart
     * 柱状图
     * @return barChart
     */
    protected BarChart setBarChartClickListener(BarChart barChart) {
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e == null) {
                    return;
                }
                if(mTabLayout.getSelectedTabPosition() == 0) {
                    int selecedindex = (int) h.getX();
                    XToastUtils.toast(Integer.toString(selecedindex));
                    //跳转Acticity
                    Intent intent = new Intent(getContext(), ChartsActivity.class);
                    intent.putExtra("category", Integer.toString(selecedindex));
                    intent.putExtra("datestart", DateUtils.date2String(mDateStart, DateUtils.yyyyMMdd.get()));
                    intent.putExtra("dateend", DateUtils.date2String(mDateEnd, DateUtils.yyyyMMdd.get()));
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected() {
                // 图表外部点击事件 （二次点击事件）
            }
        });
        return barChart;
    }



    /**
     * 设置饼图点击事件
     * @param pieChart
     * 饼图
     * @return pieChart
     */
    protected PieChart setPieChartClickListener(PieChart pieChart) {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e == null) {
                    return;
                }
                if(mTabLayout.getSelectedTabPosition() == 0) {
                    int selecedindex = (int) h.getX();
                    XToastUtils.toast(Integer.toString(selecedindex));
                    //跳转Acticity
                    Intent intent = new Intent(getContext(), ChartsActivity.class);
                    intent.putExtra("category", Integer.toString(selecedindex));
                    intent.putExtra("datestart", DateUtils.date2String(mDateStart, DateUtils.yyyyMMdd.get()));
                    intent.putExtra("dateend", DateUtils.date2String(mDateEnd, DateUtils.yyyyMMdd.get()));
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected() {
                // 图表外部点击事件 （二次点击事件）
            }
        });
        return pieChart;
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



    @Override
    protected void initArgs() {

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBackPressed();
        }
        return true;
    }


    private void handleBackPressed() {

    }



    /**
     * 初始化日期选择控件
     */
    protected void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        mDateEnd = calendar.getTime();
        Btn_date_end.setText(DateUtils.date2String(mDateEnd, DateUtils.yyyyMMdd.get()));

        calendar.roll(Calendar.MONTH, -1);
        mDateStart = calendar.getTime();
        Btn_date_start.setText(DateUtils.date2String(mDateStart, DateUtils.yyyyMMdd.get()));

    }



    /**
     * 日期选择控件
     * @param view
     * 当前视图
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.btn_date_start, R.id.btn_date_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_date_start:
                showDatePickerStart();
                break;
            case R.id.btn_date_end:
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
            calendar.roll(Calendar.MONTH, -1);
            mDatePickerStart = new TimePickerBuilder(Objects.requireNonNull(getContext()), (date, v) ->
            {
                //选择器监听
                mDateStart = date;
                Btn_date_start.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
                if(mDateStart.after(mDateEnd)) {
                    XToastUtils.error("开始日期不能晚于结束日期");
                }
                refreshCharts();
            })
                    .setDate(calendar)  //默认日期为当前的前一个月
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
            mDatePickerEnd = new TimePickerBuilder(Objects.requireNonNull(getContext()), (date, v) ->
            {
                //选择器监听
                mDateEnd = date;
                Btn_date_end.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
                if(mDateStart.after(mDateEnd)) {
                    XToastUtils.error("结束日期不能早于开始日期");
                }
                refreshCharts();
            })
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
     * tab栏设置
     */
    private void init_tab() {
        mTabLayout.addTab(mTabLayout.newTab().setText("类别"));
        mTabLayout.addTab(mTabLayout.newTab().setText("成员"));
        mTabLayout.addTab(mTabLayout.newTab().setText("账户"));
        mTabLayout.addOnTabSelectedListener(this);
    }


    /**
     * tab栏监听
     * @param tab
     * tab按钮
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        XToastUtils.toast("选中了:" + tab.getText());

        refreshCharts();
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        tabSelected = tab.getText();

        XToastUtils.toast("选中了:" + tabSelected);

        refreshCharts();
    }


    /**
     * 隐藏切换图表
     * @param choice
     * 悬浮按钮选项
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
     * 悬浮按钮监听
     * @param v
     * 当前视图
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
