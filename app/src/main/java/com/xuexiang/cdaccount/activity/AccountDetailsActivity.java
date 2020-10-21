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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.cdaccount.ExpanableBill.BillDataDay;
import com.xuexiang.cdaccount.ExpanableBill.BillDataItem;
import com.xuexiang.cdaccount.ExpanableBill.BillDataMonth;
import com.xuexiang.cdaccount.ExpanableBill.BillDataYear;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.adapter.ExpandableYearAdapter;
import com.xuexiang.cdaccount.adapter.dropdownmenu.ListDropDownAdapter;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.somethingDao.Dao.BillDao;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.spinner.DropDownMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

import static com.xuexiang.xutil.XUtil.getResources;


public class AccountDetailsActivity extends BaseActivity {


    private BillDao mBillDao;
    private String[] mHeaders = {"年",  "成员", "账户"};
    private List<View> mPopupViews = new ArrayList<>();

    private ListDropDownAdapter mMemberAdapter;
    private ListDropDownAdapter mAccountAdapter;
    private ListDropDownAdapter mTimeAdapter;

    private List<String> mMembers;
    private List<String> mAccounts;
    private List<String> mTimes;


//    private TimePickerView mDatePickerStart;
//    private TimePickerView mDatePickerEnd;
//    private Date mDateStart;
//    private Date mDateEnd;

    private boolean yearFocusable = true;
    private boolean monthFocusable =false;
    private boolean dayFocusable = false;
    List<BillDataYear> billDataYearList = new ArrayList<>();
    private int selectedYear;
    private String selectedMember;
    private String selectedAccount;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private ExpandableYearAdapter adapter;

    @BindView(R.id.account_title)
    TitleBar mTitleBar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.ddm_content)
    DropDownMenu mDropDownMenu;

//    @BindView(R.id.btn_date_start)
//    XUIAlphaButton Btn_date_start;
//
//    @BindView(R.id.btn_date_end)
//    XUIAlphaButton Btn_date_end;

    @BindView(R.id.account_header)
    ClassicsHeader mClassicsHeader;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTitleBar();
        initArgs();
        initSpinner();
//        initTimePicker();
        initRecyclerViews();

    }


    protected void initTitleBar() {
        mTitleBar.setLeftClickListener(view -> {
            finish();
        });
    }

    protected boolean isSupportSlideBack() {
        return true;
    }

    /**
     *初始化Recycle布局
     */
    protected void initRecyclerViews() {
        List<BillDataYear> startData = new ArrayList<>();
        adapter = new ExpandableYearAdapter(AccountDetailsActivity.this, recyclerView, startData);
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        getBillData();
        printHeaderAndFooter();

        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        refreshLayout.setFooterHeight(80);
        refreshLayout.setFooterMaxDragRate(2);
        refreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多
        refreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        refreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        mClassicsHeader.setEnableLastTime(false);

        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener(){
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                selectedYear++;
                printHeaderAndFooter();
                getBillData();
                refreshLayout.finishRefresh();
            }
        });

        //上拉加载
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                selectedYear--;
                printHeaderAndFooter();
                getBillData();
                refreshLayout.finishLoadMore();
            }

        });

    }

    void printHeaderAndFooter() {
        ClassicsFooter.REFRESH_FOOTER_PULLING = getString(R.string.footer_pulling,selectedYear-1);
        ClassicsFooter.REFRESH_FOOTER_RELEASE = getString(R.string.footer_release, selectedYear-1);
        ClassicsHeader.REFRESH_HEADER_PULLING = getString(R.string.header_pulling,selectedYear+1);
        ClassicsHeader.REFRESH_HEADER_RELEASE = getString(R.string.header_release, selectedYear+1);
    }


    protected List<BillDataYear> getTestData(int year) {
        List<BillDataYear> billDataYearList = new ArrayList<>();
            List<BillDataMonth> billDataMonthList = new ArrayList<>();

            for(int m=1;m < 3;m++) {
                List<BillDataDay> billDataDayList = new ArrayList<>();

                for(int d=1;d < 5; d++) {
                    List<BillDataItem> billDataItemList = new ArrayList<>();
                    for(int i=1;i< 4;i++) {
                        billDataItemList.add(new BillDataItem(new Random().nextInt(2), "早午晚餐", "信用卡", "to账户", "本人", Integer.toString(year), Integer.toString(m), Integer.toString(d), "20:34", (double) new Random().nextInt(2000), "这是备注"));
                    }
                    billDataDayList.add(new BillDataDay(Integer.toString(d), (double) new Random().nextInt(2000), (double) new Random().nextInt(2000), billDataItemList));
                }

                billDataMonthList.add(new BillDataMonth(Integer.toString(m), (double) new Random().nextInt(2000), (double) new Random().nextInt(2000), billDataDayList));
            }

            billDataYearList.add(new BillDataYear(Integer.toString(year), (double) new Random().nextInt(2000), (double) new Random().nextInt(2000), billDataMonthList));
        return billDataYearList;
    }



    protected void getBillData() {
        BillDataYear billDataYear = mBillDao.getJournalAccount(Integer.toString(selectedYear), selectedMember, selectedAccount);
        billDataYearList.clear();
        billDataYearList.add(billDataYear);
        setFocusedExpandable(yearFocusable, monthFocusable, dayFocusable);
        adapter.refresh(billDataYearList);
    }

    /**
     * 用数组初始化菜单选项
     */
    protected void initArgs() {
        mBillDao = new BillDao(AccountDetailsActivity.this);
        mTimes = new ArrayList<>();
        mTimes.add("年");
        mTimes.add("月");
        mTimes.add("日");
        mMembers = mBillDao.QueryMember();
        mMembers.add(0,getResources().getString(R.string.unlimited));
        mAccounts = mBillDao.QueryAccount();
        mAccounts.add(0,getResources().getString(R.string.unlimited));

        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedAccount = getResources().getString(R.string.unlimited);
        selectedMember = getResources().getString(R.string.unlimited);
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
        int initialPosition = 0;

        //init time menu
        final ListView timeView = new ListView(AccountDetailsActivity.this);
        mTimeAdapter = new ListDropDownAdapter(AccountDetailsActivity.this, mTimes);
        mTimeAdapter.setSelectPosition(initialPosition);
        timeView.setDividerHeight(0);
        timeView.setAdapter(mTimeAdapter);


        //init member menu
        final ListView memberView = new ListView(AccountDetailsActivity.this);
        memberView.setDividerHeight(0);
        mMemberAdapter = new ListDropDownAdapter(AccountDetailsActivity.this, mMembers);
        initialPosition = Math.max(mMembers.indexOf(selectedMember), 0);
        mMemberAdapter.setSelectPosition(initialPosition);
        memberView.setAdapter(mMemberAdapter);

        //init accout book menu
        final ListView accoutView = new ListView(AccountDetailsActivity.this);
        accoutView.setDividerHeight(0);
        mAccountAdapter = new ListDropDownAdapter(AccountDetailsActivity.this, mAccounts);
        initialPosition = Math.max(mAccounts.indexOf(selectedAccount), 0);
        mAccountAdapter.setSelectPosition(initialPosition);
        accoutView.setAdapter(mAccountAdapter);



        //init mPopupViews
        mPopupViews.add(timeView);
        mPopupViews.add(memberView);
        mPopupViews.add(accoutView);

        //add item click event
        timeView.setOnItemClickListener((parent, view, position, id) -> {
            mTimeAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[0] : mTimes.get(position));
            changeFocusable(position);
            getBillData();
            mDropDownMenu.closeMenu();
        });

        memberView.setOnItemClickListener((parent, view, position, id) -> {
            mMemberAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[1] : mMembers.get(position));
            XToastUtils.toast("点击了:" + mMembers.get(position));
            selectedMember = mMembers.get(position);
            getBillData();
            mDropDownMenu.closeMenu();
        });

        accoutView.setOnItemClickListener((parent, view, position, id) -> {
            mAccountAdapter.setSelectPosition(position);
            mDropDownMenu.setTabMenuText(position == 0 ? mHeaders[2] : mAccounts.get(position));
            XToastUtils.toast("点击了:" + mAccounts.get(position));
            selectedAccount = mAccounts.get(position);
            getBillData();
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


    private void changeFocusable(int position) {
        switch (position){
            case 0:
                yearFocusable = true;
                monthFocusable = false;
                dayFocusable = false;
                break;
            case 1:
                yearFocusable = false;
                monthFocusable = true;
                dayFocusable = false;
                break;
            case 2:
                yearFocusable = false;
                monthFocusable = false;
                dayFocusable = true;
                break;
        }
    }


    private void setFocusedExpandable(boolean yearSelect, boolean monthSelect, boolean daySelect) {
        for(BillDataYear billDataYear : billDataYearList) {
            billDataYear.setmYearSelected(yearSelect);
            for(BillDataMonth billDataMonth : billDataYear.getmBillDataMonthList()){
                billDataMonth.setmMonthSelected(monthSelect);
                for(BillDataDay billDataDay : billDataMonth.getmBillDataDayList()) {
                    billDataDay.setmDaySelected(daySelect);
                }
            }
        }
    }


//    protected void initTimePicker() {
//        Calendar calendar = Calendar.getInstance();
//        mDateEnd = calendar.getTime();
//        Btn_date_end.setText(DateUtils.date2String(mDateEnd, DateUtils.yyyyMMdd.get()));
//
//        calendar.roll(Calendar.MONTH, -1);
//        mDateStart = calendar.getTime();
//        Btn_date_start.setText(DateUtils.date2String(mDateStart, DateUtils.yyyyMMdd.get()));
//
//    }
//
//
//    /**
//     * 日期选择控件
//     *
//     **/
//    @SuppressLint("NonConstantResourceId")
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @OnClick({R.id.btn_date_start, R.id.btn_date_end})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_date_start:
//                showDatePickerStart();
//                break;
//            case R.id.btn_date_end:
//                showDatePickerEnd();
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    private void showDatePickerStart() {
//        if (mDatePickerStart == null) {
//            //选择器初始化
//            Calendar calendar = Calendar.getInstance();
//            calendar.roll(Calendar.MONTH, -1);
//            mDatePickerStart = new TimePickerBuilder(this, (date, v) ->
//            {
//                //选择器监听
//                mDateStart = date;
//                Btn_date_start.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
//                if(mDateStart.after(mDateEnd)) {
//                    XToastUtils.error("开始日期不能晚于结束日期");
//                }
//            })
//                    .setDate(calendar)  //默认日期为当前的前一个月
//                    .setTimeSelectChangeListener(date -> {
//                        if(date.after(mDateEnd)) {
//                            XToastUtils.error("开始日期不能晚于结束日期");
//                        }
//                    })
//                    .setTitleText("开始日期")
//                    .build();
//        }
//        mDatePickerStart.show();
//    }
//
//
//    private void showDatePickerEnd() {
//        if (mDatePickerEnd == null) {
//            //选择器初始化
//            mDatePickerEnd = new TimePickerBuilder(this, (date, v) ->
//            {
//                //选择器监听
//                mDateEnd = date;
//                Btn_date_end.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
//                if(mDateStart.after(mDateEnd)) {
//                    XToastUtils.error("结束日期不能早于开始日期");
//                }
//            })
//                    .setTimeSelectChangeListener(date -> {
//                        if(date.before(mDateStart)) {
//                            XToastUtils.error("结束日期不能早于开始日期");
//                        }
//                    })
//                    .setTitleText("结束日期")
//                    .build();
//        }
//        mDatePickerEnd.show();
//    }

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
