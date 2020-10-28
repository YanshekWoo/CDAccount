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
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.fragment.addbill.IncomeFragment;
import com.xuexiang.cdaccount.fragment.addbill.OutcomeFragment;
import com.xuexiang.cdaccount.fragment.addbill.TransferFragment;
import com.xuexiang.cdaccount.somethingDao.Dao.BillDao;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 记账页面
 *
 * @author Chenhao
 * @since 2020年10月2日22:02:18
 */
public class AddBillActivity extends AppCompatActivity implements OutcomeFragment.OutcomeMessage, IncomeFragment.IncomeMessage, TransferFragment.TransferMessage {

    private ViewPager mVpAdd;
    private Boolean mBlConfirm = false;     //标识变量

    private double mIncomeAmount;
    private double mOutcomeAmount;
    private double mTransferAmount;

    boolean IsAmountFill = false;

    private BillDao mDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbill);

        mDataBaseHelper = new BillDao(this);
//        initDate();

        // 金额初始值
        mIncomeAmount = -1;
        mOutcomeAmount = -1;
        mTransferAmount = -1;

        // 设置viewpager
        mVpAdd = findViewById(R.id.vp_add);
        mVpAdd.setOffscreenPageLimit(5);

        // 设置tab
        List<String> titles = new ArrayList<>();
        titles.add("支出");
        titles.add("收入");
        titles.add("转账");
        TabLayout mTlAdd = findViewById(R.id.tl_add);

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

//        /**
//         * 返回按钮
//         */
//
//        mIvBack = findViewById(R.id.iv_back);
//        mIvBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        /**
//         * 确认按钮
//         */
//        mBtnConfirm = findViewById(R.id.btn_confirm);
//        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (mVpAdd.getCurrentItem()) {               //判断金额是否已填入
//                    case 0:
//                        IsAmountFill = mOutcomeAmount == -1;
//                        break;
//                    case 1:
//                        IsAmountFill = mIncomeAmount == -1;
//                        break;
//                    case 2:
//                        IsAmountFill = mTransferAmount == -1;
//                        break;
//                }
//                if (IsAmountFill) {
//                    XToastUtils.error("请填写金额");
//                } else {
//                    mBlConfirm = true;      //置为true，表示需要插入新数据
//                    finish();               //利用生命周期的回调函数完成写入数据库
//                }
//
//            }
//        });
//

        //    private ShadowImageView mIvBack;
        //    private ShadowButton mBtnConfirm;
        TitleBar mTbTitle = findViewById(R.id.add_title);
        mTbTitle.setLeftClickListener(v -> finish())
                .addAction(new TitleBar.TextAction("确认") {
                    @Override
                    public void performAction(View view) {
                        switch (mVpAdd.getCurrentItem()) {               //判断金额是否已填入
                            case 0:
                                IsAmountFill = mOutcomeAmount == -1;
                                break;
                            case 1:
                                IsAmountFill = mIncomeAmount == -1;
                                break;
                            case 2:
                                IsAmountFill = mTransferAmount == -1;
                                break;
                        }
                        if (IsAmountFill) {
                            XToastUtils.error("请填写金额");
                        } else {
                            mBlConfirm = true;      //置为true，表示需要插入新数据
//                            if(mDataBaseHelper.QueryMonthpay()-mDataBaseHelper.QueryMonthIncome()>5000){
//                                showSimpleTipDialog();
//                            }
                            finish();               //利用生命周期的回调函数完成写入数据库
                        }
                    }
                });

    }

//    private void showSimpleTipDialog() {
//        new MaterialDialog.Builder(AddActivity.this)
//                .iconRes(R.drawable.icon_tip)
//                .title(R.string.tip_infos)
//                .content(R.string.content_simple_confirm_dialog)
//                .positiveText("确认")
//                .show();
//    }

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
     * @param v 视图
     * @param event  点击事件
     * @return  布尔值
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }


    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token  token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void InsertOutcome(double Amount, String Year, String Month, String Day, String Time, String Subcategory, String Account, String toAccount, String Member, String Remark) {
        if (mBlConfirm && mVpAdd.getCurrentItem() == 0) {
//            Log.d("---InsertIncome---", String.valueOf(Amount) + " " + Year + " " + Month + " " + Day + " " + Time + " " + Subcategory + " " + Account + " " + toAccount + " " + Member + " " + Remark);
            mDataBaseHelper.insertBill(0, Subcategory, Account, toAccount, Member, Year, Month, Day, Time, Remark, Amount);
            XToastUtils.success("记账成功");

        }
    }

    @Override
    public void getOutcomeAmount(double Amount) {
        mOutcomeAmount = Amount;
    }


    @Override
    public void InsertIncome(double Amount, String Year, String Month, String Day, String Time, String Subcategory, String Account, String toAccount, String Member, String Remark) {
        if (mBlConfirm && mVpAdd.getCurrentItem() == 1) {
//            Log.d("---InsertIncome---", String.valueOf(Amount) + " " + Year + " " + Month + " " + Day + " " + Time + " " + Subcategory + " " + Account + " " + toAccount + " " + Member + " " + Remark);
            mDataBaseHelper.insertBill(1, Subcategory, Account, toAccount, Member, Year, Month, Day, Time, Remark, Amount);
            XToastUtils.success("记账成功");
        }
    }

    @Override
    public void getIncomeAmount(double Amount) {
        mIncomeAmount = Amount;
    }


    @Override
    public void InsertTransfer(double Amount, String Year, String Month, String Day, String Time, String Subcategory, String Account, String toAccount, String Member, String Remark) {
        if (mBlConfirm && mVpAdd.getCurrentItem() == 2) {
//            Log.d("---InsertTransfer---", String.valueOf(Amount) + " " + Year + " " + Month + " " + Day + " " + Time + " " + Subcategory + " " + Account + " " + toAccount + " " + Member + " " + Remark);
            mDataBaseHelper.insertBill(2, Subcategory, Account, toAccount, Member, Year, Month, Day, Time, Remark, Amount);
            XToastUtils.success("记账成功");

        }
    }

    @Override
    public void getTransferAmount(double Amount) {
        mTransferAmount = Amount;

    }


}


