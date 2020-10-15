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

package com.xuexiang.cdaccount.activity;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.adapter.ExpandableListAdapter;
import com.xuexiang.cdaccount.adapter.ExpandableYearAdapter;
import com.xuexiang.cdaccount.adapter.ExpendableAdapter;
import com.xuexiang.cdaccount.adapter.TestItem;
import com.xuexiang.cdaccount.adapter.dropdownmenu.ConstellationAdapter;
import com.xuexiang.cdaccount.adapter.dropdownmenu.ListDropDownAdapter;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.utils.DemoDataProvider;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.spinner.DropDownMenu;
import com.xuexiang.xutil.data.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import butterknife.BindView;


public class AccountDetailsActivity extends BaseActivity {


    private String[] mHeaders = {"年", "类别", "成员", "账户","测试"};
    private List<View> mPopupViews = new ArrayList<>();

//    private ListDropDownAdapter mCategoryAdapter;
    private ListDropDownAdapter mTopCategoryAdapter;
    private ListDropDownAdapter mSubCategoryAdapter;
    private ListDropDownAdapter mMemberAdapter;
    private ListDropDownAdapter mAccountAdapter;
    private ListDropDownAdapter mTimeAdapter;
    private ListDropDownAdapter mTopCategoryAdapter;
    private ListDropDownAdapter mSubCategoryAdapter;

//    private String[] mCategories;
    private String[] mTopCategory;
    private String[] mSubCategory;
    private String[] mMembers;
    private String[] mAccounts;
    private String[] mTimes;
    private String[] mTopCategory;
    private String[] mSubCategory;


    private TimePickerView mDatePickerStart;
    private TimePickerView mDatePickerEnd;
    private Date mDateStart;
    private Date mDateEnd;

    private Collection<String> datas;
    private TestItem t;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private ExpandableYearAdapter adapter;

//    private boolean year_expendable = true, month_expendable = false, day_expendable = false;

    @BindView(R.id.account_title)
    TitleBar mTitleBar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;

    @BindView(R.id.btn_date_start)
    XUIAlphaButton Btn_date_start;

    @BindView(R.id.btn_date_end)
    XUIAlphaButton Btn_date_end;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_account_details);

//        recyclerView = findViewById(R.id.recycler_view);
//        DropDownMenu mDropDownMenu = findViewById(R.id.ddm_content);
//        XUIAlphaButton Btn_date_start = findViewById(R.id.btn_date_start);
//        XUIAlphaButton Btn_date_end = findViewById(R.id.btn_date_end);

        Btn_date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerStart(Btn_date_start);
            }
        });
        Btn_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerEnd(Btn_date_end);
            }
        });

        t = new TestItem();

//        initData(datas);
        initTitleBar();
        initArgs();
        initSpinner();
        initTimePicker();
        initRecyclerViews();

    }

//    protected void initData(Collection<String> datas){
//        datas = new ArrayList<String>();
//        for(int i = 0; i < 9 ;i++){
//            datas.add("" + i);
//        }
//    }

    protected void initTitleBar() {
        String account = getIntent().getStringExtra("account");
//        mTitleBar.setTitle(account);
        mTitleBar.setLeftClickListener(view -> {
            finish();
        });
    }

    protected boolean isSupportSlideBack() {
        return false;
    }

    /**
     *初始化Recycle布局
     */
    protected void initRecyclerViews() {
        adapter = new ExpandableYearAdapter(AccountDetailsActivity.this, recyclerView);
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);



//        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout12 -> refreshLayout12.getLayout().postDelayed(() -> {
//            datas = DemoDataProvider.getDemoData();
//            TestItem t = new TestItem(0,true);
            t.addYear(true);
            t.addMonth(false);
            t.addDay(false);
            t.addRefresh(true);
            List<TestItem> datas = new ArrayList<>();
            datas.add(t);

            adapter.refresh(datas);
            refreshLayout12.finishRefresh();
            refreshLayout12.resetNoMoreData();//setNoMoreData(false);
        }, 2000));
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> refreshLayout1.getLayout().postDelayed(() -> {
            if (adapter.getItemCount() > 30) {
                XToastUtils.toast("数据全部加载完毕");
                refreshLayout1.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            } else {
//                adapter.loadMore(DemoDataProvider.getDemoData());
//                datas = DemoDataProvider.getDemoData();
//                TestItem t2 = new TestItem(1,false);
                t.addRefresh(true);
                List<TestItem> datas2 = new ArrayList<>();
                datas2.add(t);
                adapter.loadMore(datas2);
                refreshLayout1.finishLoadMore();
            }
        }, 2000));

        //触发自动刷新
        refreshLayout.autoRefresh();

//        recyclerView.setLayoutManager(new LinearLayoutManager(AccountDetailsActivity.this));
//        recyclerView.setAdapter(new ExpandableYearAdapter(AccountDetailsActivity.this, recyclerView, DemoDataProvider.getDemoData1()));
    }

    /**
     * 用数组初始化菜单选项
     */
    protected void initArgs() {
        mTimes = ResUtils.getStringArray(R.array.time_entry);
//        mCategories = ResUtils.getStringArray(R.array.category_entry);
        mTopCategory = ResUtils.getStringArray(R.array.category_entry);
        mSubCategory = ResUtils.getStringArray(R.array.member_entry);
        mMembers = ResUtils.getStringArray(R.array.member_entry);
        mAccounts = ResUtils.getStringArray(R.array.account_entry);
        mTopCategory = ResUtils.getStringArray(R.array.category_entry);
        mSubCategory = ResUtils.getStringArray(R.array.account_entry);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event, DropDownMenu mDropDownMenu) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBackPressed(mDropDownMenu);
        }
        return true;
    }

    private void handleBackPressed(DropDownMenu mDropDownMenu) {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        }
    }


    /**
     * 下拉菜单设置
     */
    protected void initSpinner() {
        //init time menu
        final ListView timeView = new ListView(AccountDetailsActivity.this);
        mTimeAdapter = new ListDropDownAdapter(AccountDetailsActivity.this, mTimes);
        timeView.setDividerHeight(0);
        timeView.setAdapter(mTimeAdapter);

//        //init category menu
//        final ListView categoryView = new ListView(AccountDetailsActivity.this);
//        mCategoryAdapter = new ListDropDownAdapter(AccountDetailsActivity.this, mCategories);
//        categoryView.setDividerHeight(0);
//        categoryView.setAdapter(mCategoryAdapter);
        //init category
        final View categoryListView = getLayoutInflater().inflate(R.layout.layout_drop_down_category, null);
        ListView topListView = categoryListView.findViewById(R.id.dorp_down_topcategory);
        ListView subListView = categoryListView.findViewById(R.id.dorp_down_subcategory);
        mTopCategoryAdapter = new ListDropDownAdapter(this, mTopCategory);
        mSubCategoryAdapter = new ListDropDownAdapter(this, mSubCategory);
        topListView.setAdapter(mTopCategoryAdapter);
        subListView.setAdapter(mSubCategoryAdapter);
        categoryListView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            mDropDownMenu.setTabMenuText(mTopCategoryAdapter.getSelectPosition() <= 0 ? mHeaders[3] : mTopCategoryAdapter.getSelectItem());
            mDropDownMenu.closeMenu();
        });
        topListView.setOnItemClickListener((parent, view, position, id) -> {
            mTopCategoryAdapter.setSelectPosition(position);
            if(position==0) {
                mDropDownMenu.closeMenu();
            }
        });
        subListView.setOnItemClickListener((parent, view, position, id) -> {
            mSubCategoryAdapter.setSelectPosition(position);
            mDropDownMenu.closeMenu();
        });


        //init member menu
        final ListView memberView = new ListView(AccountDetailsActivity.this);
        memberView.setDividerHeight(0);
        mMemberAdapter = new ListDropDownAdapter(AccountDetailsActivity.this, mMembers);
        memberView.setAdapter(mMemberAdapter);

        //init accout book menu
        final ListView accoutView = new ListView(AccountDetailsActivity.this);
        accoutView.setDividerHeight(0);
        mAccountAdapter = new ListDropDownAdapter(AccountDetailsActivity.this, mAccounts);
        accoutView.setAdapter(mAccountAdapter);

        //init category
        final View categoryListView = getLayoutInflater().inflate(R.layout.layout_drop_down_category, null);
        ListView topListView = categoryListView.findViewById(R.id.dorp_down_topcategory);
        ListView subListView = categoryListView.findViewById(R.id.dorp_down_subcategory);
        mTopCategoryAdapter = new ListDropDownAdapter(this, mTopCategory);
        mSubCategoryAdapter = new ListDropDownAdapter(this, mSubCategory);
        topListView.setAdapter(mTopCategoryAdapter);
        subListView.setAdapter(mSubCategoryAdapter);
        categoryListView.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            mDropDownMenu.setTabMenuText(mTopCategoryAdapter.getSelectPosition() <= 0 ? mHeaders[3] : mTopCategoryAdapter.getSelectItem());
            mDropDownMenu.closeMenu();
        });
        topListView.setOnItemClickListener((parent, view, position, id) -> {
            mTopCategoryAdapter.setSelectPosition(position);
        });
        subListView.setOnItemClickListener((parent, view, position, id) -> {
            mSubCategoryAdapter.setSelectPosition(position);
            mDropDownMenu.closeMenu();
        });



        //init mPopupViews
        mPopupViews.add(timeView);
        mPopupViews.add(categoryListView);
        mPopupViews.add(memberView);
        mPopupViews.add(accoutView);
        mPopupViews.add(categoryListView);

        //add item click event
        timeView.setOnItemClickListener((parent, view, position, id) -> {
            mTimeAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[0] : mTimes[position]);
            switch (position){
                case 0:
//                    year_expendable = true;
//                    month_expendable = false;
//                    day_expendable = false;
                    t.addYear(true);
                    t.addMonth(false);
                    t.addDay(false);
                    break;
                case 1:
//                    month_expendable = true;
//                    year_expendable = false;
//                    day_expendable = false;
                    t.addYear(true);
                    t.addMonth(true);
                    t.addDay(false);
                    break;
                case 2:
//                    day_expendable = true;
//                    year_expendable = false;
//                    month_expendable = false;
                    t.addYear(true);
                    t.addMonth(true);
                    t.addDay(true);
                    break;
            }

//            datas = DemoDataProvider.getDemoData();
//            adapter.refresh(datas);
            refreshLayout.autoRefresh();

//            refreshLayout.autoRefresh();
//            refreshLayout12.finishRefresh();
//            refreshLayout12.resetNoMoreData();//setNoMoreData(false);

//            XToastUtils.toast("点击了:" + mTimes[position]);

            mDropDownMenu.closeMenu();
        });

//        原一级categoryview
//        categoryView.setOnItemClickListener((parent, view, position, id) -> {
//            mCategoryAdapter.setSelectPosition(position);
//            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[1] : mCategories[position]);
//            XToastUtils.toast("点击了:" + mCategories[position]);
//            mDropDownMenu.closeMenu();
//        });

        memberView.setOnItemClickListener((parent, view, position, id) -> {
            mMemberAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[2] : mMembers[position]);
            XToastUtils.toast("点击了:" + mMembers[position]);
            mDropDownMenu.closeMenu();
        });

        accoutView.setOnItemClickListener((parent, view, position, id) -> {
            mAccountAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[3] : mAccounts[position]);
            XToastUtils.toast("点击了:" + mAccounts[position]);
            mDropDownMenu.closeMenu();
        });


        //init context view
        TextView contentView = new TextView(AccountDetailsActivity.this);
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
     *

    OnClick({R.id.btn_date_start, R.id.btn_date_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_date_start:
                showDatePickerStart(Btn_date_start);
                break;
            case R.id.btn_date_end:
                showDatePickerEnd(Btn_date_end);
                break;
            default:
                break;
        }
    }*/

    private void showDatePickerStart(XUIAlphaButton Btn_date_start) {
        if (mDatePickerStart == null) {
            //选择器初始化
            Calendar calendar = Calendar.getInstance();
            calendar.roll(Calendar.MONTH, -1);
            mDatePickerStart = new TimePickerBuilder(AccountDetailsActivity.this, (date, v) ->
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

    private void showDatePickerEnd(XUIAlphaButton Btn_date_end) {
        if (mDatePickerEnd == null) {
            //选择器初始化
            mDatePickerEnd = new TimePickerBuilder(AccountDetailsActivity.this, (date, v) ->
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

//    protected void initListeners() {
//        //下拉刷新
//        refreshLayout.setOnRefreshListener(refreshLayout -> {
//            refreshLayout.getLayout().postDelayed(() -> {
//                adapter.refresh(datas);
//                refreshLayout.finishRefresh();
//            }, 500);
//        });
//        //上拉加载
//        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
//            refreshLayout.getLayout().postDelayed(() -> {
//                adapter.loadMore(datas);
//                refreshLayout.finishLoadMore();
//            }, 1000);
//        });
//        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
//    }
}
