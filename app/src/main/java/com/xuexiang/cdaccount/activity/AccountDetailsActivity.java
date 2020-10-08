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
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.adapter.ExpandableListAdapter;
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


    private String[] mHeaders = {"类别", "成员", "账户"};
    private List<View> mPopupViews = new ArrayList<>();

    private ListDropDownAdapter mCategoryAdapter;
    private ListDropDownAdapter mMemberAdapter;
    private ListDropDownAdapter mAccountAdapter;

    private String[] mCategories;
    private String[] mMembers;
    private String[] mAccounts;


    private TimePickerView mDatePickerStart;
    private TimePickerView mDatePickerEnd;
    private Date mDateStart;
    private Date mDateEnd;

    private Collection<String> datas;

    @BindView(R.id.account_title)
    TitleBar mTitleBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_account_details);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        DropDownMenu mDropDownMenu = findViewById(R.id.ddm_content);
        XUIAlphaButton Btn_date_start = findViewById(R.id.btn_date_start);
        XUIAlphaButton Btn_date_end = findViewById(R.id.btn_date_end);

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

        initData(datas);
        initTitleBar();
        initArgs();
        initSpinner(mDropDownMenu);
        initTimePicker(Btn_date_start,Btn_date_end);
        initRecyclerViews(recyclerView);

    }

    protected void initData(Collection<String> datas){
        datas = new ArrayList<String>();
        for(int i = 0; i < 9 ;i++){
            datas.add("" + i);
        }
    }

    protected void initTitleBar() {
        String account = getIntent().getStringExtra("account");
        mTitleBar.setTitle(account);
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
    protected void initRecyclerViews(RecyclerView recyclerView) {
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(new ExpandableListAdapter(AccountDetailsActivity.this,recyclerView, DemoDataProvider.getDemoData1()));
    }

    /**
     * 用数组初始化菜单选项
     */
    protected void initArgs() {
        mCategories = ResUtils.getStringArray(R.array.category_entry);
        mMembers = ResUtils.getStringArray(R.array.member_entry);
        mAccounts = ResUtils.getStringArray(R.array.account_entry);
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
    protected void initSpinner(DropDownMenu mDropDownMenu) {
        //init category menu
        final ListView categoryView = new ListView(AccountDetailsActivity.this);
        mCategoryAdapter = new ListDropDownAdapter(AccountDetailsActivity.this, mCategories);
        categoryView.setDividerHeight(0);
        categoryView.setAdapter(mCategoryAdapter);

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
    protected void initTimePicker(XUIAlphaButton Btn_date_start, XUIAlphaButton Btn_date_end) {
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
}