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

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.fragment.account.AccountFragment;
import com.xuexiang.cdaccount.fragment.add.IncomeFragment;
import com.xuexiang.cdaccount.fragment.add.OutcomeFragment;
import com.xuexiang.cdaccount.fragment.add.TransferFragment;
import com.xuexiang.cdaccount.fragment.charts.ChartsFragment;
import com.xuexiang.cdaccount.fragment.home.HomeFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowImageView;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.spinner.editspinner.EditSpinner;
import com.xuexiang.xutil.data.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_TEXT;

/**
 * 记账页面
 *
 * @author Chenhao
 * @since 2020年10月2日22:02:18
 */
public class AddActivity extends AppCompatActivity implements OutcomeFragment.OutcomeMessage, IncomeFragment.IncomeMessage, TransferFragment.TransferMessage {

    private ShadowImageView mIvBack;
    private ShadowButton mBtnConfirm;
    private TabLayout mTlAdd;
    private ViewPager mVpAdd;
    private Boolean mBlConfirm = false;     //标识变量



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnConfirm = findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlConfirm = true;      //置为true，表示需要插入新数据
                finish();
            }
        });

        /**
         *设置viewpager
         * */

        mVpAdd = findViewById(R.id.vp_add);
        mVpAdd.setOffscreenPageLimit(5);

        /**
         * 设置tab
         */
        List<String> titles = new ArrayList<>();
        titles.add("支出");
        titles.add("收入");
        titles.add("转账");
        mTlAdd = findViewById(R.id.tl_add);
        BaseFragment[] fragments = new BaseFragment[]{
                new OutcomeFragment(),
                new IncomeFragment(),
                new TransferFragment()
        };
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getSupportFragmentManager(), fragments);
        adapter.setTitles(titles);
        mVpAdd.setAdapter(adapter);
        mTlAdd.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTlAdd.setupWithViewPager(mVpAdd);


        WidgetUtils.setTabLayoutTextFont(mTlAdd);

    }


    /**
     * 函数
     */

    //隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void InsertOutcome(double Amount, Date Date, Date Time, String FirstCategory, String SecondCategory, String AccountOut, String AccountIn, String Member, String Remark) {
        if(mBlConfirm && mVpAdd.getCurrentItem()==0)XToastUtils.toast("Outcome");
    }

    @Override
    public void InsertIncome(double Amount, Date Date, Date Time, String FirstCategory, String SecondCategory, String AccountOut, String AccountIn, String Member, String Remark) {
        if(mBlConfirm && mVpAdd.getCurrentItem()==1)XToastUtils.toast("Income");

    }

    @Override
    public void InsertTransfer(double Amount, Date Date, Date Time, String FirstCategory, String SecondCategory, String AccountOut, String AccountIn, String Member, String Remark) {
        if(mBlConfirm && mVpAdd.getCurrentItem()==2)XToastUtils.toast("Transfer");

    }
}



