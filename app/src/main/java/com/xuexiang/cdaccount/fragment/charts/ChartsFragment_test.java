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

import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.adapter.dropdownmenu.ListDropDownAdapter;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.spinner.DropDownMenu;
import com.xuexiang.xutil.data.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Xinshuo Hu
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class ChartsFragment_test extends BaseFragment {

    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;

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

    // 下拉菜单
    private String[] mHeaders = {"类别", "成员", "账户"};
    private List<View> mPopupViews = new ArrayList<>();

    private ListDropDownAdapter mCategoryAdapter;
    private ListDropDownAdapter mMemberAdapter;
    private ListDropDownAdapter mAccountAdapter;

    // 图表数据
    private String[] mCategories;
    private String[] mMembers;
    private String[] mAccounts;
    private String[] mPieColors;

    // 日期选择器
    private TimePickerView mDatePickerStart;
    private TimePickerView mDatePickerEnd;
    private Date mDateStart;
    private Date mDateEnd;


    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_charts;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        initChart();
        initSpinner();
        initTimePicker();
    }


    /**
     *  初始化图表
     */
    protected void initChart() {
        //柱状图
        mBarChart = initBarChart(mBarChart);
        BarData barData = setBardata();
        mBarChart.setData(barData);
        mBarChart.invalidate();

        //折线图
        mLineChart = initLineChart(mLineChart);
        LineData lineData = setLinedata();
        mLineChart.setData(lineData);
        mLineChart.invalidate();

        //饼图
        mPieChart = initPieChart(mPieChart);
        PieData pieData = setPiedata(mPieChart);
        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }

    /**
     * 初始化柱状图
     * @param barChart
     * @return
     */
    protected BarChart initBarChart(BarChart barChart) {
        barChart.setDescription(null);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setNoDataText(getResources().getString(R.string.no_data));
        XAxis xAxis = barChart.getXAxis();
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        Legend legend = barChart.getLegend();
        setBarChartAxis(xAxis, yAxisLeft, yAxisRight, legend);

        barChart = setBarChartClickListener(barChart);
        return barChart;
    }

    protected BarChart setBarChartClickListener(BarChart barChart) {
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e == null) {
                    return;
                }
                int selecedindex = (int) h.getX();
                XToastUtils.toast(Integer.toString(selecedindex));
            }

            @Override
            public void onNothingSelected() {
                // 图表外部点击事件 （二次点击事件）
                XToastUtils.toast("click edge");
            }
        });
        return barChart;
    }

    protected void setBarChartAxis(XAxis xAxis, YAxis yAxisLeft, YAxis yAxisRight, Legend legend) {
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
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    protected BarData setBardata() {
        List<BarEntry> entries = new ArrayList<>();
        for(int i = 0;i < 12;i++) {
            entries.add(new BarEntry(i, new Random().nextInt(2000)));
        }
        BarDataSet barDataSet = new BarDataSet(entries, "柱状图数据");


        barDataSet.setDrawIcons(false);
        //双色柱状图
        int startColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
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


        BarData bardata = new BarData(barDataSet);
        bardata.setValueTextSize(12f);


        return bardata;
    }



    /**
     * 初始化折线图
     * @param lineChart
     * @return
     */
    protected LineChart initLineChart(LineChart lineChart) {
        lineChart.setDescription(null);
        lineChart.setDrawGridBackground(false);
        // 开启手势触摸
        lineChart.setTouchEnabled(true);
        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        //无数据时显示
        lineChart.setNoDataText(getResources().getString(R.string.no_data));

        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        Legend legend = lineChart.getLegend();
        setLineChartAxis(xAxis, yAxisLeft, yAxisRight, legend);
        return lineChart;
    }

    protected void setLineChartAxis(XAxis xAxis, YAxis yAxisLeft, YAxis yAxisRight, Legend legend) {
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
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    protected LineData setLinedata() {
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i, (float) (Math.random()) * 80));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "折线图数据");
        lineDataSet.setColor(getResources().getColor(R.color.app_color_theme_5));
        lineDataSet.setCircleColor(getResources().getColor(R.color.app_color_theme_7));
        LineData linedata = new LineData(lineDataSet);
        linedata.setValueTextSize(12f);
        return linedata;
    }



    /**
     * 初始化折线图
     * @param pieChart
     * @return
     */
    protected PieChart initPieChart(PieChart pieChart) {
        //设置百分比显示
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        //设置图标中心空白，空心
        pieChart.setDrawHoleEnabled(true);
        //设置空心圆的弧度百分比，最大100
        pieChart.setHoleRadius(50f);
        pieChart.setHoleColor(Color.WHITE);
        //设置透明弧的样式
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setTransparentCircleRadius(55f);
        //无数据显示
        pieChart.setNoDataText(getResources().getString(R.string.no_data));
        //标签颜色
        pieChart.setEntryLabelColor(Color.BLACK);

        Legend legend = pieChart.getLegend();
        setPieChartAxis(legend);

        //设置点击监听
        pieChart = setPieChartClickListener(pieChart);
        return pieChart;
    }

    protected PieChart setPieChartClickListener(PieChart pieChart) {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e == null) {
                    return;
                }
                int selecedindex = (int) h.getX();
                XToastUtils.toast(Integer.toString(selecedindex));
            }

            @Override
            public void onNothingSelected() {
                // 图表外部点击事件 （二次点击事件）
                XToastUtils.toast("click edge");
            }
        });
        return pieChart;
    }

    protected void setPieChartAxis(Legend legend) {
        //设置Lengend位置
        legend.setTextColor(getResources().getColor(R.color.app_color_theme_5)); //设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setWordWrapEnabled(false);
    }

    protected PieData setPiedata(PieChart pieChart) {
        Random myRandom = new Random();
        //设置数据
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new PieEntry((float) (Math.random()) * 80, Integer.toString(i)));
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


        PieDataSet pieDataSet = new PieDataSet(entries, "饼图数据");
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setIconsOffset(new MPPointF(0, 40));
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(colors);

        PieData piedata = new PieData(pieDataSet);
        piedata.setValueFormatter(new PercentFormatter(pieChart));
        piedata.setDrawValues(true);
        piedata.setValueTextSize(12f);
        piedata.setValueTextColor(getResources().getColor(R.color.black));
        return piedata;
    }


    /**
     * 用数组初始化菜单选项
     */
    @Override
    protected void initArgs() {
        mCategories = ResUtils.getStringArray(R.array.category_entry);
        mMembers = ResUtils.getStringArray(R.array.member_entry);
        mAccounts = ResUtils.getStringArray(R.array.account_entry);
        mPieColors = ResUtils.getStringArray(R.array.pieColor);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBackPressed();
        }
        return true;
    }

    private void handleBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            popToBack();
        }
    }


    /**
     * 下拉菜单设置
     */
    protected void initSpinner() {
        //init category menu
        final ListView categoryView = new ListView(getContext());
        mCategoryAdapter = new ListDropDownAdapter(getContext(), mCategories);
        categoryView.setDividerHeight(0);
        categoryView.setAdapter(mCategoryAdapter);

        //init member menu
        final ListView memberView = new ListView(getContext());
        memberView.setDividerHeight(0);
        mMemberAdapter = new ListDropDownAdapter(getContext(), mMembers);
        memberView.setAdapter(mMemberAdapter);

        //init accout book menu
        final ListView accoutView = new ListView(getContext());
        accoutView.setDividerHeight(0);
        mAccountAdapter = new ListDropDownAdapter(getContext(), mAccounts);
        accoutView.setAdapter(mAccountAdapter);

        //init mPopupViews
        mPopupViews.add(categoryView);
        mPopupViews.add(memberView);
        mPopupViews.add(accoutView);


        //add item click event
        categoryView.setOnItemClickListener((parent, view, position, id) -> {
            mCategoryAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[0] : mCategories[position]);
            XToastUtils.toast("点击了:" + mCategories[position]);
            mDropDownMenu.closeMenu();
        });

        memberView.setOnItemClickListener((parent, view, position, id) -> {
            mMemberAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[1] : mMembers[position]);
            XToastUtils.toast("点击了:" + mMembers[position]);
            mDropDownMenu.closeMenu();
        });

        accoutView.setOnItemClickListener((parent, view, position, id) -> {
            mAccountAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[2] : mAccounts[position]);
            XToastUtils.toast("点击了:" + mAccounts[position]);
            mDropDownMenu.closeMenu();
        });

        //init context view
        TextView contentView = new TextView(getContext());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 3);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(mHeaders, mPopupViews, contentView);
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
     */
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
            })
                    .setDate(calendar)  //默认日期为当前的前一个月
                    .setTimeSelectChangeListener(date -> Log.i("pvTime", Calendar.getInstance().toString()))
                    .setTitleText("开始日期")
                    .build();
        }
        mDatePickerStart.show();
    }

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
            })
                    .setTimeSelectChangeListener(date -> Log.i("pvTime", Calendar.getInstance().toString()))
                    .setTitleText("结束日期")
                    .build();
        }
        mDatePickerEnd.show();
    }



}