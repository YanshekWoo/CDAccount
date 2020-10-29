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

package com.xuexiang.cdaccount.fragment.addbill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xuexiang.cdaccount.MyApp;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.somethingDao.Dao.BillDao;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowImageView;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.spinner.editspinner.EditSpinner;
import com.xuexiang.xutil.data.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Page(anim = CoreAnim.none)
public class TransferFragment extends BaseFragment {


    private double mAmount = -1;    //负数作空标志

    private TextView mTvDate, mTvTime;
    private TimePickerView mDatePicker;
    private TimePickerView mTimePicker;
    private Date mDate, mTime;
    String mStrYear, mStrMonth, mStrDay, mStrTime;

    private TextView mTvType;
    private List<String> options1Item = new ArrayList<>();
    private final List<List<String>> options2Item = new ArrayList<>();
    private String mOption1, mOption2, mOption;


    private TextView mTvAccount1, mTvAccount2;
    private List<String> Accounts1Item = new ArrayList<>();
    private String mAccount1,mAccount2;

    private TextView mTvMember;
    private List<String> MembersItem = new ArrayList<>();
    private String mMember;

    private String mRemark = "";


    private TransferMessage Transfer;

    public TransferFragment() {
    }

//    private BillDao mDatabaseHelper;

    private MaterialDialog.Builder mAcountDialog = null;
    private MaterialDialog.Builder mMemberDialog = null;
    private LayoutInflater mInflater;
    private View mdialog;

    private OptionsPickerView mPvCategory = null;
    private OptionsPickerView mPvAccount1 = null;
    private OptionsPickerView mPvAccount2= null;
    private OptionsPickerView mPvMember = null;


    public interface TransferMessage{
        void InsertTransfer(double Amount, String Year, String Month, String Day, String Time, String Subcategory, String Account, String toAccount, String Member, String Remark);
        void getTransferAmount(double Amount);

    }


    @Override
    public void onPause() {
        super.onPause();
        Transfer.InsertTransfer(mAmount, mStrYear, mStrMonth, mStrDay, mStrTime, null, mAccount1, mAccount2, mMember, mRemark);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Transfer = (TransferMessage) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity必须实现OutMessage");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transfer;
    }

    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void initViews() {

//        MyApp.billDao = new BillDao(getContext());


        //记账金额

        EditText mEtAmount = findViewById(R.id.et_amount);
        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_yuan);
        drawable1.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        mEtAmount.setCompoundDrawables(null, null, drawable1, null);//只放右边
        mEtAmount.setFilters(new InputFilter[]{new LengthFilter(10)});
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
                int posDot = -1;
                posDot = temp.indexOf(".");

                if (posDot > 0 && temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                    XToastUtils.error("小数点后不超过两位");
                }
                if (!temp.equals("")) {
                    mAmount = Double.parseDouble(s.toString());
                } else {
                    mAmount = -1;
                }
                Transfer.getTransferAmount(mAmount);

            }
        });


        //记账属性——时间

        mTvDate = findViewById(R.id.tv_date);
        mTvTime = findViewById(R.id.tv_time);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        mStrYear = String.valueOf(year);
        mStrMonth = String.valueOf(month);
        mStrDay = String.valueOf(day);
        mStrTime = String.format("%02d", hour) + ":" + String.format("%02d", minute);
        mTvDate.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
        mTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        mTvTime.setText(mStrTime);
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });


        //记账属性——账户1
        loadAccountData();
        mTvAccount1 = findViewById(R.id.tv_account1);
        mAccount1 = Accounts1Item.get(0);
        mTvAccount1.setText(mAccount1);               //初始化
        mTvAccount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAccountData();
                showAccountPickerView1(false);

            }
        });
        ShadowImageView mBtnNewAccount1 = findViewById(R.id.btn_new_account1);
        mBtnNewAccount1.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (mAcountDialog == null) {
                                                       initAccountDialog();
                                                   }
                                                   mAcountDialog.show();

                                               }
                                           }
        );

        //记账属性——账户2
        mTvAccount2 = findViewById(R.id.tv_account2);
        mAccount2 = Accounts1Item.get(0);
        mTvAccount2.setText(mAccount2);               //初始化
        mTvAccount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAccountData();
                showAccountPickerView2(false);

            }
        });
        ShadowImageView mBtnNewAccount2 = findViewById(R.id.btn_new_account2);
        mBtnNewAccount2.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (mAcountDialog == null) {
                                                       initAccountDialog();
                                                   }
                                                   mAcountDialog.show();
                                               }
                                           }
        );


        //记账属性——成员
        loadMemberData();
        mTvMember = findViewById(R.id.tv_member);
        mMember = MembersItem.get(0);
        mTvMember.setText(mMember);
        mTvMember.setTextColor(this.getResources().getColor(R.color.app_color_theme_10));
        mTvMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMemberData();
                showMemberPickerView(false);
            }
        });
        ShadowImageView mBtnNewMember = findViewById(R.id.btn_new_member);
        mBtnNewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMemberDialog == null) {
                    mMemberDialog = new MaterialDialog.Builder(getContext())
                            .title("添加成员")
                            .inputType(
                                    InputType.TYPE_CLASS_TEXT)
                            .input(
                                    "请输入新成员",
                                    "",
                                    false,
                                    new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                        }
                                    })
                            .positiveText("确定")
                            .negativeText("取消")
                            .inputRange(1, 5)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    assert dialog.getInputEditText() != null;
                                    mMember = dialog.getInputEditText().getText().toString();
                                    mTvMember.setText(mMember);

                                    if (mMember.equals(MembersItem.get(0))) {
                                        mTvMember.setTextColor(0xFF6E6E6E);
                                    } else {
                                        mTvMember.setTextColor(0xFF000000);
                                    }
                                    //TODO:insert new member
                                    if (MyApp.billDao.insertMember(mMember)) {
//                                        XToastUtils.success("添加成员成功");
                                        loadMemberData();
                                    } else {
                                        XToastUtils.error("添加成员失败，该成员已存在");
                                    }

                                }
                            });
                }
                mMemberDialog.show();
            }
        });

        //记账属性——备注
        MaterialEditText mEtRemark = findViewById(R.id.et_remark);
        mEtRemark.setFilters(new InputFilter[]{new LengthFilter(12)});
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
     * 函数
     */
    //记账属性——时间
    private void showDatePicker() {
        if (mDatePicker == null) {
            mDatePicker = new TimePickerBuilder(Objects.requireNonNull(getContext()), new OnTimeSelectListener() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onTimeSelected(Date date, View v) {
                    mDate = date;
                    mStrYear = DateUtils.date2String(mDate, new SimpleDateFormat("yyyy"));
                    mStrMonth = DateUtils.date2String(mDate, new SimpleDateFormat("MM"));
                    mStrDay = DateUtils.date2String(mDate, new SimpleDateFormat("dd"));
                    mTvDate.setText(DateUtils.date2String(mDate, DateUtils.yyyyMMdd.get()));
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
            mTimePicker = new TimePickerBuilder(Objects.requireNonNull(getContext()), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    mTime = date;
                    mStrTime = DateUtils.date2String(mTime, DateUtils.HHmm.get());
                    mTvTime.setText(mStrTime);
                }
            })
                    .setType(false, false, false, true, true, false)     //只显示时分
                    .setTitleText("时间选择")
                    .setDate(calendar)
                    .build();
        }
        mTimePicker.show();
    }

    //记账属性——账户1
    private void loadAccountData() {                        //仅重复一次
        Accounts1Item = MyApp.billDao.queryAccountList();

    }

    private void initAccountDialog() {
        mAcountDialog = new MaterialDialog.Builder(getContext())
                .title("添加账户")
                .inputType(
                        InputType.TYPE_CLASS_TEXT)
                .input(
                        "请输入新账户",
                        "",
                        false,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            }
                        })
                .inputRange(1, 5)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mAccount2 = dialog.getInputEditText().getText().toString();
                        mTvAccount2.setText(mAccount2);
                        if (MyApp.billDao.insertAccount(mAccount2)) {
//                            XToastUtils.success("添加账户成功");
                            loadAccountData();
                        } else {
                            XToastUtils.error("添加账户失败，该账户已存在");
                        }

                    }
                });
    }

    private void showAccountPickerView1(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {Accounts1Item.indexOf(mAccount1)};
        if(mPvAccount1 == null) {
            mPvAccount1 = new OptionsPickerBuilder(getContext(), (v, accounts1, accounts2, accounts3) -> {
                //返回的分别是三个级别的选中位置
                mAccount1 = Accounts1Item.get(accounts1);
                mTvAccount1.setText(mAccount1);
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
                    .build();
        }
        mPvAccount1.setSelectOptions(defaultSelectOptions[0]);
        mPvAccount1.setPicker(Accounts1Item);//一级选择器
        mPvAccount1.show();
    }

    //记账属性——账户2

    private void showAccountPickerView2(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {Accounts1Item.indexOf(mAccount2)};
        if(mPvAccount1 == null) {
            mPvAccount2 = new OptionsPickerBuilder(getContext(), (v, accounts1, accounts2, accounts3) -> {
                //返回的分别是三个级别的选中位置
                mAccount2 = Accounts1Item.get(accounts1);
                mTvAccount2.setText(mAccount2);
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
                    .build();
        }
        mPvAccount2.setSelectOptions(defaultSelectOptions[0]);
        mPvAccount2.setPicker(Accounts1Item);//一级选择器
        mPvAccount2.show();
    }

    //记账属性——成员
    private void loadMemberData() {
        MembersItem = MyApp.billDao.queryMemberList();
    }

    private void showMemberPickerView(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {MembersItem.indexOf(mMember)};

        if(mPvMember == null) {
            mPvMember = new OptionsPickerBuilder(getContext(), (v, member1, member2, member3) -> {
                //返回的分别是三个级别的选中位置
                mMember = MembersItem.get(member1);
                mTvMember.setText(mMember);
                if (member1 == 0) {
                    mTvMember.setTextColor(this.getResources().getColor(R.color.app_color_theme_10));
                } else {
                    mTvMember.setTextColor(this.getResources().getColor(R.color.black));
                }
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
                    .build();
        }
        mPvMember.setSelectOptions(defaultSelectOptions[0]);
        mPvMember.setPicker(MembersItem);//一级选择器
        //pvOptions.setPicker(Accounts1Item, Accounts2Item);//二级选择器
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        mPvMember.show();
    }


    /**
     * 限制最大长度
     */
    public static class LengthFilter implements InputFilter {
        public LengthFilter(int max) {
            mMax = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int keep = mMax - (dest.length() - (dend - dstart));

            if (keep <= 0) {
                XToastUtils.error("最多仅可输入" + mMax + "个字符");
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                keep += start;
                XToastUtils.error("最多仅可输入" + mMax + "个字符");
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        private final int mMax;

    }
}
