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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.cdaccount.MyApp;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.AccountDetailsActivity;
import com.xuexiang.cdaccount.adapter.charts.ChartListAdapter;
import com.xuexiang.cdaccount.arima.RunARIMA;
import com.xuexiang.cdaccount.chartsclass.MyBarChart;
import com.xuexiang.cdaccount.chartsclass.MyLineChart;
import com.xuexiang.cdaccount.chartsclass.MyPieChart;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.dbclass.ChartDataEntry;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xutil.data.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author Xinshuo Hu
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class ChartsFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.chart_recyclerView)
    RecyclerView chart_recyclerView;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.lineChart)
    LineChart mLineChart;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.barChart)
    BarChart mBarChart;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.pieChart)
    PieChart mPieChart;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_date_start)
    XUIAlphaButton Btn_date_start;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_date_end)
    XUIAlphaButton Btn_date_end;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fab_menu)
    FloatingActionMenu mFloatingActionMenu;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.chart_tab_inout)
    TabLayout mTabLayoutInout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_navigation_view)
    LinearLayout llNavigationView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.chart_tab_selector)
    TabLayout mTabLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.chart_tab_title)
    TextView chartTabTitle;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_switch)
    AppCompatImageView ivSwitch;

    private boolean mIsShowNavigationView;

    // 日期选择器
    private TimePickerView mDatePickerStart;
    private TimePickerView mDatePickerEnd;
    private Date mDateStart;
    private Date mDateEnd;

    private List<ChartDataEntry> chartDataEntries = new ArrayList<>();
    private List<ChartDataEntry> lineEntries = new ArrayList<>();
//    private BillDao billDao;

    //图表定义类
    MyBarChart myBarChart;
    MyPieChart myPieChart;
    MyLineChart myLineChart;


    //tab选择项
    private int tabSelected;
    private int tabInout;
    private int tabChart;

    private ChartListAdapter madapter;

    private final List<String> legendSelectArray = Arrays.asList("主类", "次类", "成员", "账户");


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
//        billDao  = new BillDao(getContext());
        initTab();
        initRecycleView();
        initTimePicker();
        initChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        refreshCharts();
    }

    /**
     *  初始化图表
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void initChart() {
        // 初始显示饼图
        tabChart = 0;
        selectChart(tabChart);

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

        getDataFromDB();

//        new Thread(new ChartDataRunnable()).start();

        String legendInOut = tabInout==0? "支出" : "收入";
        String legendSelect = legendSelectArray.get(tabSelected);

        switch (tabChart) {
            case 0:
                // 饼图刷新
                PieData pieData = myPieChart.setPiedata(mPieChart, chartDataEntries, legendSelect+"-"+legendInOut);
                mPieChart.setData(pieData);
                mPieChart.animateXY(1500, 1500);
                mPieChart.invalidate();

                break;
            case 1:
                // 柱状图刷新
                BarData barData = myBarChart.setBardata(mBarChart, chartDataEntries, legendSelect+"-"+legendInOut);
                mBarChart.setData(barData);
                mBarChart.animateXY(1500, 1500);
                mBarChart.invalidate();
                break;
            case 2:
                // 折线图刷新
                LineData lineData = myLineChart.setLinedata(mLineChart, lineEntries, "总"+legendInOut+"趋势");
                mLineChart.setData(lineData);
                mLineChart.animateXY(1500, 1500);
                mLineChart.invalidate();
                break;
            default:
                break;
        }


        // 列表刷新
        List<ChartDataEntry> sortedCharData = new ArrayList<>(chartDataEntries);
        Collections.sort(sortedCharData, (ChartDataEntry a, ChartDataEntry b)-> b.compareTo(a));
        madapter.refresh(sortedCharData);
    }

    /**
     * 从数据库取出图表数据并进行初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getDataFromDB() {
        // parse date to string
        String[] strDateStart = DateUtils.date2String(mDateStart, DateUtils.yyyyMMdd.get()).split("-");
        String[] strDateEnd = DateUtils.date2String(mDateEnd, DateUtils.yyyyMMdd.get()).split("-");
        String start_year = strDateStart[0];
        String start_month = strDateStart[1];
        String start_day = strDateStart[2];
        String end_year = strDateEnd[0];
        String end_month = strDateEnd[1];
        String end_day = strDateEnd[2];


        // 饼图和柱状图数据
        chartDataEntries.clear();
        lineEntries.clear();
        switch (tabSelected) {
            case 0:
                if(tabInout==0) {
                    chartDataEntries = MyApp.billDao.getDataByOutTopCategory(start_year, start_month, start_day, end_year, end_month, end_day);
                }
                else if(tabInout==1) {
                    chartDataEntries = MyApp.billDao.getDataByInTopCategory(start_year, start_month, start_day, end_year, end_month, end_day);
                }
                break;
            case 1:
                if(tabInout==0) {
                    chartDataEntries = MyApp.billDao.getDataByOutSubCategory(start_year, start_month, start_day, end_year, end_month, end_day);
                }
                else if(tabInout==1) {
                    chartDataEntries = MyApp.billDao.getDataByInSubCategory(start_year, start_month, start_day, end_year, end_month, end_day);
                }
                break;
            case 2:
                chartDataEntries = MyApp.billDao.getDataByMember(start_year, start_month, start_day, end_year, end_month, end_day, tabInout);
                break;
            case 3:
                chartDataEntries = MyApp.billDao.getDataByAccount(start_year, start_month, start_day, end_year, end_month, end_day, tabInout);
                break;
        }

        // 获取总金额
        Double sumMoney = chartDataEntries.stream().map(ChartDataEntry::getDataMoney).reduce(0.00, Double::sum);
        for(ChartDataEntry e: chartDataEntries) {
            e.setSumMoney(sumMoney);
        }


        // 折线图数据
        lineEntries = MyApp.billDao.getSumByDate(start_year, start_month, start_day, end_year, end_month, end_day, tabInout);


        // ARIMA预测 predict
        if(lineEntries.size() >= 14) {
            int length = lineEntries.size();
            for(int i = length; i < (length + 3); i++) {
                double predictData = RunARIMA.runPrediction(lineEntries);
                lineEntries.add(new ChartDataEntry("9999-99-99", predictData));
            }
        }
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
        Btn_date_end.setText(DateUtils.date2String(calendar.getTime(), DateUtils.yyyyMMdd.get()));

        calendar.roll(Calendar.MONTH, -1);
        mDateStart = calendar.getTime();
        Btn_date_start.setText(DateUtils.date2String(calendar.getTime(), DateUtils.yyyyMMdd.get()));

    }



    /**
     * 日期选择控件
     * @param view
     * 当前视图
     */
    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.btn_date_start, R.id.btn_date_end})
    protected void onTimePickerClicked(View view) {
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
                        if(mDateEnd.before(date)) {
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
                if(mDateEnd.before(mDateStart)) {
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
    private void initTab() {
        mTabLayoutInout.addTab(mTabLayoutInout.newTab().setText("支出"));
        mTabLayoutInout.addTab(mTabLayoutInout.newTab().setText("收入"));
        mTabLayoutInout.addOnTabSelectedListener(this);

        for(int i=0;i<4;i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(legendSelectArray.get(i)));
        }
        mTabLayout.addOnTabSelectedListener(this);
        // 初始不可见，避免隐藏状态下被点击
        mTabLayout.setVisibility(View.INVISIBLE);
    }


    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        List<ChartDataEntry> chartDataEntries = new ArrayList<>();
        madapter = new ChartListAdapter(getContext(), chart_recyclerView, chartDataEntries) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, ChartDataEntry item) {
                super.bindData(holder, position, item);

                holder.click(R.id.adapter_chart_list_card, new View.OnClickListener() {
                    @SingleClick
                    @Override
                    public void onClick(View v) {
                        int focusType = 1;
                        String account = getResources().getString(R.string.unlimited);
                        String member = getResources().getString(R.string.unlimited);
                        if(tabSelected==2) {
                            member = item.getDataName();
                        }
                        else if(tabSelected==3) {
                            account = item.getDataName();
                        }

                        Intent intent = new Intent(getContext(), AccountDetailsActivity.class);
                        intent.putExtra("focusType", focusType);
                        intent.putExtra("member", member);
                        intent.putExtra("account", account);
//                        openNewPage(SettingsFragment.class, getActivity().getIntent().getExtras());
//                        openNewPage(AccountDetailFragment.class);
                        Objects.requireNonNull(getContext()).startActivity(intent);
                    }
                });
            }
        };


        WidgetUtils.initRecyclerView(chart_recyclerView);
        chart_recyclerView.setAdapter(madapter);
    }



    /**
     * tab栏监听
     * @param tab
     * tab按钮
     */
    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//        XToastUtils.toast("选中了:" + tab.getText());
        assert tab.parent != null;
        switch (tab.parent.getId()) {
            case R.id.chart_tab_inout:
                tabInout = tab.getPosition();
                refreshCharts();
                break;
            case R.id.chart_tab_selector:
                tabSelected = tab.getPosition();
                refreshCharts();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        refreshCharts();
    }


    /**
     * Tab栏点击刷新状态
     * @param isShow
     * 是否打开
     */
    private void refreshStatus(final boolean isShow) {
        ObjectAnimator rotation;
        ObjectAnimator tabAlpha;
        ObjectAnimator textAlpha;
        if (isShow) {
            rotation = ObjectAnimator.ofFloat(ivSwitch, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayout, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(chartTabTitle, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(ivSwitch, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayout, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(chartTabTitle, "alpha", 0, 1);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotation).with(textAlpha).with(tabAlpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                chartTabTitle.setVisibility(isShow ? View.GONE : View.VISIBLE);
                mTabLayout.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
            }
        });
        animatorSet.setDuration(400).start();
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
    }



    /**
     * 悬浮按钮监听
     * @param v
     * 当前视图
     */
    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SingleClick
    @OnClick({R.id.fab_piechart, R.id.fab_barchart, R.id.fab_linechart, R.id.iv_switch})
    protected void onBottumClick(@NotNull View v) {
        switch (v.getId()) {
            case R.id.fab_piechart:
                tabChart = 0;
                selectChart(tabChart);
                mFloatingActionMenu.toggle(false);
                refreshCharts();
                break;
            case R.id.fab_barchart:
                tabChart = 1;
                selectChart(tabChart);
                mFloatingActionMenu.toggle(false);
                refreshCharts();
                break;
            case R.id.fab_linechart:
                tabChart = 2;
                selectChart(tabChart);
                mFloatingActionMenu.toggle(false);
                refreshCharts();
                break;
            case R.id.iv_switch:
                refreshStatus(mIsShowNavigationView = !mIsShowNavigationView);
                break;
            default:
                selectChart(0);
                break;
        }
    }

}
