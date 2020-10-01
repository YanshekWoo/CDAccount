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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
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
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.adapter.dropdownmenu.ListDropDownAdapter;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.spinner.DropDownMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * @author Xinshuo Hu
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class ChartsFragment extends BaseFragment {

    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;

    @BindView(R.id.lineChart)
    LineChart mLineChart;

    @BindView(R.id.barChart)
    BarChart mBarChart;

    @BindView(R.id.pieChart)
    PieChart mPieChart;


    private String[] mHeaders = {"类别", "成员", "账户"};
    private List<View> mPopupViews = new ArrayList<>();

    private ListDropDownAdapter mCategoryAdapter;
    private ListDropDownAdapter mMemberAdapter;
    private ListDropDownAdapter mAccountAdapter;

    private String[] mCategories;
    private String[] mMembers;
    private String[] mAccounts;
    private String[] mPieColors;




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
        PieData pieData = setPiedata();
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
        legend.setTextColor(Color.CYAN); //设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    }

    protected BarData setBardata() {
        List<BarEntry> entries = new ArrayList<>();
        for(int i = 0;i < 12;i++) {
            entries.add(new BarEntry(i, new Random().nextInt(200)));
        }
        BarDataSet barDataSet = new BarDataSet(entries, "柱状图数据");
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
        lineChart.setNoDataText(getResources().getString(R.string.no_data));
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        Legend legend = lineChart.getLegend();
        setBarChartAxis(xAxis, yAxisLeft, yAxisRight, legend);
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
        legend.setTextColor(Color.CYAN); //设置Legend 文本颜色
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
        LineDataSet lineDataSet = new LineDataSet(entries, "支出");
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
        pieChart.setDescription(null);
        pieChart.setUsePercentValues(true);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad); //设置动画
        pieChart.setNoDataText(getResources().getString(R.string.no_data));
        Legend legend = pieChart.getLegend();
        setPieChartAxis(legend);
        return pieChart;
    }

    protected void setPieChartAxis(Legend legend) {
        //设置Lengend位置
        legend.setTextColor(Color.CYAN); //设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setWordWrapEnabled(true);
    }

    protected PieData setPiedata() {
        Random myRandom = new Random();
        //设置数据
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> piecolors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new PieEntry((float) (Math.random()) * 80));

            piecolors.add(Color.parseColor(mPieColors[i%10]));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "支出");
        pieDataSet.setColors(piecolors);
        PieData piedata = new PieData(pieDataSet);
        piedata.setDrawValues(true);
        piedata.setValueTextSize(12f);
        piedata.setValueTextColor(getResources().getColor(R.color.black));
        return piedata;
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
        contentView.setText("图表");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(mHeaders, mPopupViews, contentView);
    }

    protected void setDropDownMenuClickLisener() {

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

}
