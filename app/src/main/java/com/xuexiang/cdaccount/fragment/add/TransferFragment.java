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

package com.xuexiang.cdaccount.fragment.add;

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

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Page(anim = CoreAnim.none)
public class TransferFragment extends BaseFragment {

    private Context mContext;

    private EditText mEtAmount;
    private double mAmount = -1;    //负数作空标志

    private TextView mTvDateTime;
    private TimePickerView mDatePicker;
    private TimePickerView mTimePicker;
    private Date mDate, mTime;

    private TextView mTvType;
    private List<String> options1Item = new ArrayList<>();
    private List<List<String>> options2Item = new ArrayList<>();
    private String mOption1, mOption2, mOption;
    private ShadowImageView mBtnNewType;

    private TextView mTvAccount1,mTvAccount2;
    private List<String> Accounts1Item = new ArrayList<>();
    private String mAccount1,mAccount2;
    private ShadowImageView mBtnNewAccount1,mBtnNewAccount2;

    private TextView mTvMember;
    private List<String> MembersItem = new ArrayList<>();
    private String mMember;
    private ShadowImageView mBtnNewMember;

    private EditText mEtRemark;
    private String mRemark;


    private TextView mTvDialogItem1, mTvDialogItem2;
    private EditSpinner mEsDialog;
    private EditText mEtDialog;
    private String mStrNewItem1, mStrNewItem2;

    private TransferMessage Transfer;


    public interface TransferMessage{
        void InsertTransfer(double Amount, Date Date, Date Time, String FirstCategory, String SecondCategory, String AccountOut, String AccountIn, String Member, String Remark);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Transfer.InsertTransfer(0, mDate, mTime, "测试", null, null, null, null, null);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Transfer = (TransferMessage) context;
        }catch (ClassCastException e){
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

    @Override
    protected void initViews() {

        //记账金额

        mEtAmount = findViewById(R.id.et_amount);
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
                    mAmount = Double.parseDouble(temp);
                } else {
                    mAmount = -1;
                }
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

//        //记账属性——分类
//
//        loadOptionData();
//
//        mTvType = findViewById(R.id.tv_type);
//        mTvType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showOptionPickerView(false);
//            }
//        });
//
//        mBtnNewType = findViewById(R.id.btn_new_type);
//        mBtnNewType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LayoutInflater inflater = getLayoutInflater();
//                View dialog = inflater.inflate(R.layout.dialog_new, null);
//
//                MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(getContext())
//                        .customView(dialog, true)
//                        .title("添加分类")
//                        .positiveText("确定")
//                        .negativeText("取消");
//
//                mTvDialogItem1 = dialog.findViewById(R.id.item_title1);
//                mTvDialogItem2 = dialog.findViewById(R.id.item_title2);
//                mEsDialog = dialog.findViewById(R.id.es_item1);
//                mEtDialog = dialog.findViewById(R.id.et_item2);
//                mTvDialogItem1.setText("一级分类");
//                mTvDialogItem2.setText("二级分类");
//                mEsDialog.setHint("选择已有分类或新建");
//                mEsDialog.setItems(options1Item);
//                materialDialog.onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        mStrNewItem1 = mEsDialog.getText();
//                        mStrNewItem2 = mEtDialog.getText().toString();
//                        mOption = mStrNewItem1 + "-" + mStrNewItem2;
//                        mTvType.setText(mOption);
//                    }
//                });
//                materialDialog.show();
//            }
//        });


        //记账属性——账户1
        loadAccountData();
        mTvAccount1 = findViewById(R.id.tv_account1);
        mTvAccount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountPickerView(false);

            }
        });
        mBtnNewAccount1 = findViewById(R.id.btn_new_account1);
        mBtnNewAccount1.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  new MaterialDialog.Builder(getContext())
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
                                                          .inputRange(1,10)
                                                          .positiveText("确定")
                                                          .negativeText("取消")
                                                          .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                              @Override
                                                              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                  mAccount1 = dialog.getInputEditText().getText().toString();
                                                                  mTvAccount1.setText(mAccount1);
                                                              }
                                                          })
                                                          .show();
                                              }
                                          }
        );

        //记账属性——账户2
        loadAccountData();
        mTvAccount2 = findViewById(R.id.tv_account2);
        mTvAccount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountPickerView(false);

            }
        });
        mBtnNewAccount2 = findViewById(R.id.btn_new_account2);
        mBtnNewAccount2.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  new MaterialDialog.Builder(getContext())
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
                                                          .inputRange(1,10)
                                                          .positiveText("确定")
                                                          .negativeText("取消")
                                                          .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                              @Override
                                                              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                  mAccount2 = dialog.getInputEditText().getText().toString();
                                                                  mTvAccount2.setText(mAccount2);
                                                              }
                                                          })
                                                          .show();
                                              }
                                          }
        );


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
                new MaterialDialog.Builder(getContext())
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
                        .inputRange(1,10)
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
     * 函数
     */
    //记账属性——时间
    private void showDatePicker() {
        if (mDatePicker == null) {
            mDatePicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
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
            mTimePicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
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
    private void loadOptionData() {
        String[] str1 = {"餐饮", "交通", "购物"};
        String[] str2_1 = {"早餐", "午餐", "晚餐"};
        String[] str2_2 = {"公交", "火车", "飞机"};
        String[] str2_3 = {"服饰", "生活", "数码"};
        options1Item = Arrays.asList(str1);
        options2Item.add(Arrays.asList(str2_1));
        options2Item.add(Arrays.asList(str2_2));
        options2Item.add(Arrays.asList(str2_3));
    }

    private void showOptionPickerView(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {0, 0};

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            //返回的分别是三个级别的选中位置
            mOption1 = options1Item.get(options1);
            mOption2 = options2Item.get(options1).get(options2);
            mOption = mOption1 + "-" + mOption2;
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


    //记账属性——账户1
    private void loadAccountData() {                        //仅重复一次
        String[] str1 = {"现金账户", "银行卡账户", "信用卡账户"};
        Accounts1Item = Arrays.asList(str1);
    }

    private void showAccountPickerView1(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {0};

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, accounts1, accounts2, accounts3) -> {
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
                .setSelectOptions(defaultSelectOptions[0])
                .build();

        pvOptions.setPicker(Accounts1Item);//一级选择器
//        pvOptions.setPicker(Accounts1Item, Accounts2Item);//二级选择器
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    //记账属性——账户2

    private void showAccountPickerView(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {0};

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, accounts1, accounts2, accounts3) -> {
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
                .setSelectOptions(defaultSelectOptions[0])
                .build();

        pvOptions.setPicker(Accounts1Item);//一级选择器
//        pvOptions.setPicker(Accounts1Item, Accounts2Item);//二级选择器
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    //记账属性——成员
    private void loadMemberData() {
        String[] str1 = {"本人", "配偶", "子女"};
        MembersItem = Arrays.asList(str1);
    }

    private void showMemberPickerView(boolean isDialog) {// 弹出选择器
        int[] defaultSelectOptions = {0};

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, member1, member2, member3) -> {
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



    /**
     * 限制最大长度
     */
    public class LengthFilter implements InputFilter {
        public LengthFilter(int max) {
            mMax = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int keep = mMax - (dest.length() - (dend - dstart));

            if (keep <= 0) {
                XToastUtils.error("最多仅可输入10位（含小数点）");
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                keep += start;
                XToastUtils.error("最多仅可输入10位（含小数点）");
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        private int mMax;

    }
}
