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

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.fragment.LoginFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.display.Colors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.xuexiang.xui.XUI.getContext;

/**
 * 记账页面
 *
 * @author Chenhao
 * @since 2020年10月2日22:02:18
 */
public class AddActivity extends AppCompatActivity {

    private RadioGroup mRgRecordType;
    private EditText mEtAmount;
    private TextView mTvDateTime;
    private TimePickerView mDatePicker;
    private TimePickerView mTimePicker;
    private Date mDate, mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //记账类别(收支）
        mRgRecordType = findViewById(R.id.rg_record_type);
        mRgRecordType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                Toast.makeText(AddActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
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

            }
        });


        //记账属性——时间

        Date cur_date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        mTvDateTime = findViewById(R.id.tv_datatime);
        mTvDateTime.setText(dateFormat.format(cur_date));
        mTvDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        //记账属性——分类




    }

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
}
