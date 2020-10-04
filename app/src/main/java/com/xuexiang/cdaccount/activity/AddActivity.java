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

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.utils.XToastUtils;
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
public class AddActivity extends AppCompatActivity {

    private ShadowImageView mIvBack;
    private ShadowButton mBtnConfirm;

    private RadioGroup mRgRecordType;
    private int mIntRecordType;

    private EditText mEtAmount;
    private double mAmount;

    private TextView mTvDateTime;
    private TimePickerView mDatePicker;
    private TimePickerView mTimePicker;
    private Date mDate, mTime;

    private TextView mTvType;
    private List<String> options1Item = new ArrayList<>();
    private List<List<String>> options2Item = new ArrayList<>();
    private String mOption1,mOption2,mOption;
    private ShadowImageView mBtnNewType;

    private TextView mTvAccount;
    private List<String> Accounts1Item = new ArrayList<>();
    private List<List<String>> Accounts2Item = new ArrayList<>();
    private String mAccount1, mAccount2, mAccount;
    private ShadowImageView mBtnNewAccount;

    private TextView mTvMember;
    private List<String> MembersItem = new ArrayList<>();
    private String mMember;
    private ShadowImageView mBtnNewMember;

    private EditText mEtRemark;
    private String mRemark;


    private TextView mTvDialogItem1,mTvDialogItem2;
    private EditSpinner mEsDialog;
    private EditText mEtDialog;
    private String mStrNewItem1,mStrNewItem2;


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
                XToastUtils.toast("Insert");
                finish();
            }
        });





        //记账类别(收支）
        mRgRecordType = findViewById(R.id.rg_record_type);
        mRgRecordType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                mIntRecordType = checkedId;
            }
        });

        //记账金额

        mEtAmount = findViewById(R.id.et_amount);
        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_yuan);
        drawable1.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        mEtAmount.setCompoundDrawables(null, null, drawable1, null);//只放右边

        mEtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                    XToastUtils.error("小数点后不超过两位");
                }
                mAmount = Double.parseDouble(temp);
            }
        });


        //记账属性——时间

//        Date cur_date = new Date();
//        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        mTvDateTime.setText(dateFormat.format(cur_date));
        mTvDateTime = findViewById(R.id.tv_datatime);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        mTvDateTime.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
        mTvDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        //记账属性——分类

        loadOptionData();

        mTvType = findViewById(R.id.tv_type);
        mTvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionPickerView(false);
            }
        });

        mBtnNewType = findViewById(R.id.btn_new_type);
        mBtnNewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = AddActivity.this.getLayoutInflater();
                View dialog=inflater.inflate(R.layout.dialog_new,null);

                MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(AddActivity.this)
                        .customView(dialog, true)
                        .title("添加分类")
                        .positiveText("确定")
                        .negativeText("取消");

                mTvDialogItem1 = dialog.findViewById(R.id.item_title1);
                mTvDialogItem2 = dialog.findViewById(R.id.item_title2);
                mEsDialog = dialog.findViewById(R.id.es_item1);
                mEtDialog = dialog.findViewById(R.id.et_item2);
                mTvDialogItem1.setText("一级分类");
                mTvDialogItem2.setText("二级分类");
                mEsDialog.setHint("选择已有分类或新建");
                mEsDialog.setItems(options1Item);
                materialDialog.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mStrNewItem1 = mEsDialog.getText();
                        mStrNewItem2 = mEtDialog.getText().toString();
                        mOption = mStrNewItem1+"-"+mStrNewItem2;
                        mTvType.setText(mOption);
                    }
                });
                materialDialog.show();
            }
        });


        //记账属性——账户
        loadAccountData();
        mTvAccount = findViewById(R.id.tv_account);
        mTvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountPickerView(false);

            }
        });
        mBtnNewAccount = findViewById(R.id.btn_new_account);
        mBtnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = AddActivity.this.getLayoutInflater();
                View dialog=inflater.inflate(R.layout.dialog_new,null);

                MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(AddActivity.this)
                        .customView(dialog, true)
                        .title("添加账户")
                        .positiveText("确定")
                        .negativeText("取消");

                mTvDialogItem1 = dialog.findViewById(R.id.item_title1);
                mTvDialogItem2 = dialog.findViewById(R.id.item_title2);
                mEsDialog = dialog.findViewById(R.id.es_item1);
                mEtDialog = dialog.findViewById(R.id.et_item2);
                mTvDialogItem1.setText("一级账户");
                mTvDialogItem2.setText("二级账户");
                mEsDialog.setHint("选择已有账户或新建");
                mEsDialog.setItems(Accounts1Item);
                materialDialog.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mStrNewItem1 = mEsDialog.getText();
                        mStrNewItem2 = mEtDialog.getText().toString();
                        mAccount = mStrNewItem1+"-"+mStrNewItem2;
                        mTvAccount.setText(mAccount);
                    }
                });
                materialDialog.show();
            }
        });


        //记账属性——成员
        loadMemberData();
        mTvMember = findViewById(R.id.tv_member);
        mTvMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMemberPickerView(false);
            }
        });
        mBtnNewMember = findViewById(R.id.btn_new_member);
        mBtnNewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(AddActivity.this)
                        .title("添加成员")
                        .inputType(
                                InputType.TYPE_CLASS_TEXT)
                        .input(
                                "",
                                "",
                                false,
                                new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    }
                                })
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mMember = dialog.getInputEditText().getText().toString();
                                mTvMember.setText(mMember);
                            }
                        })
                        .show();
            }
        });

        //记账属性——备注
        mEtRemark = findViewById(R.id.et_remark);
        mEtRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mRemark = s.toString();
            }
        });



    }

    /**
    *函数
     */
    //记账属性——时间
    private void showDatePicker() {
        if (mDatePicker == null) {
            mDatePicker = new TimePickerBuilder(AddActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    mDate = date;
                    showTimePicker();
                }
            }).setTitleText("日期选择")
                    .build();
        }
        mDatePicker.show();
    }

    private void showTimePicker() {
        if (mTimePicker == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.getNowDate());
            mTimePicker = new TimePickerBuilder(AddActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    mTime = date;
                    mTvDateTime.setText(DateUtils.date2String(mDate, DateUtils.yyyyMMdd.get()) + " " + DateUtils.date2String(mTime, DateUtils.HHmmss.get()));
                }
            })
                    .setType(TimePickerType.TIME)
                    .setTitleText("时间选择")
                    .setDate(calendar)
                    .build();
        }
        mTimePicker.show();
    }

    //记账属性——分类
    private void loadOptionData(){
        String[] str1 = {"餐饮","交通","购物"};
        String[] str2_1 = {"早餐","午餐","晚餐"};
        String[] str2_2 = {"公交","火车","飞机"};
        String[] str2_3 = {"服饰","生活","数码"};
        options1Item = Arrays.asList(str1);
        options2Item.add(Arrays.asList(str2_1));
        options2Item.add(Arrays.asList(str2_2));
        options2Item.add(Arrays.asList(str2_3));
    }

    private void showOptionPickerView(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {0,0};

        OptionsPickerView pvOptions = new OptionsPickerBuilder(AddActivity.this, (v, options1, options2, options3) -> {
            //返回的分别是三个级别的选中位置
            mOption1 = options1Item.get(options1);
            mOption2 = options2Item.get(options1).get(options2);
            mOption = mOption1+"-"+mOption2;
            mTvType.setText(mOption);
            return false;
        })

                .setTitleText("选择类别")
                .setDividerColor(Color.BLACK)
                //切换选项时，还原到第一项
                .isRestoreItem(true)
                //设置选中项文字颜色
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .isDialog(isDialog)
                .setSelectOptions(defaultSelectOptions[0], defaultSelectOptions[1])
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Item, options2Item);//二级选择器
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void showCustomDialog() {


    }




    //记账属性——账户
    private void loadAccountData(){
        String[] str1 = {"现金账户","银行卡账户","信用卡账户"};
        String[] str2_1 = {"现金","微信","支付宝"};
        String[] str2_2 = {"工商银行","农业银行",};
        String[] str2_3 = {"平安银行","交通银行",};
        Accounts1Item = Arrays.asList(str1);
        Accounts2Item.add(Arrays.asList(str2_1));
        Accounts2Item.add(Arrays.asList(str2_2));
        Accounts2Item.add(Arrays.asList(str2_3));
    }

    private void showAccountPickerView(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {0,0};

        OptionsPickerView pvOptions = new OptionsPickerBuilder(AddActivity.this, (v, accounts1, accounts2, accounts3) -> {
            //返回的分别是三个级别的选中位置
            mAccount1 = Accounts1Item.get(accounts1);
            mAccount2 = Accounts2Item.get(accounts1).get(accounts2);
            mAccount = mAccount1+"-"+mAccount2;
            mTvAccount.setText(mAccount);
            return false;
        })

                .setTitleText("选择账户")
                .setDividerColor(Color.BLACK)
                //切换选项时，还原到第一项
                .isRestoreItem(true)
                //设置选中项文字颜色
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .isDialog(isDialog)
                .setSelectOptions(defaultSelectOptions[0], defaultSelectOptions[1])
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(Accounts1Item, Accounts2Item);//二级选择器
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    //记账属性——成员
    private void loadMemberData(){
        String[] str1 = {"本人","配偶","子女"};
        MembersItem = Arrays.asList(str1);
    }

    private void showMemberPickerView(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {0};

        OptionsPickerView pvOptions = new OptionsPickerBuilder(AddActivity.this, (v, member1, member2, member3) -> {
            //返回的分别是三个级别的选中位置
            mMember = MembersItem.get(member1);
            mTvMember.setText(mMember);
            return false;
        })

                .setTitleText("选择成员")
                .setDividerColor(Color.BLACK)
                //切换选项时，还原到第一项
                .isRestoreItem(true)
                //设置选中项文字颜色
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .isDialog(isDialog)
                .setSelectOptions(defaultSelectOptions[0])
                .build();

        pvOptions.setPicker(MembersItem);//一级选择器
        //pvOptions.setPicker(Accounts1Item, Accounts2Item);//二级选择器
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }




}
