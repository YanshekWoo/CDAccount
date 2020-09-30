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
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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


    private String[] mHeaders = {"类别", "成员", "账户"};
    private List<View> mPopupViews = new ArrayList<>();

    private ListDropDownAdapter mCategoryAdapter;
    private ListDropDownAdapter mMemberAdapter;
    private ListDropDownAdapter mAccountAdapter;

    private String[] mCategories;
    private String[] mMembers;
    private String[] mAccounts;




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
        //显示边界
        mLineChart.setDrawBorders(true);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i, (float) (Math.random()) * 80));
        }
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "支出");
        LineData data = new LineData(lineDataSet);
        mLineChart.setData(data);


        //得到X轴
        XAxis xAxis = mLineChart.getXAxis();
        //设置X轴的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//值：BOTTOM,BOTH_SIDED,BOTTOM_INSIDE,TOP,TOP_INSIDE
        //设置X轴坐标之间的最小距离
        xAxis.setGranularity(1f);
        //设置X轴的值
        xAxis.setLabelCount(12, true);


        //得到Lengend
        Legend legend = mLineChart.getLegend();
        //设置Lengend位置
        legend.setTextColor(Color.CYAN); //设置Legend 文本颜色
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
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
