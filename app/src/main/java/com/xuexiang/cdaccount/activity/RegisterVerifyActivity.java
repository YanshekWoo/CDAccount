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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.utils.RandomUtils;
import com.xuexiang.cdaccount.utils.SettingUtils;
import com.xuexiang.cdaccount.utils.TokenUtils;
import com.xuexiang.cdaccount.utils.Utils;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.display.Colors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 *
 * @author LaMeloBall
 * @since 2020-10-09 20:22
 */
public class RegisterVerifyActivity extends BaseActivity {

    @BindView(R.id.register_verify_commit)
    Button mBtSign;

    @BindView(R.id.verify_question1)
    MaterialSpinner mMaterialSpinner1;
    @BindView(R.id.verify_answer1)
    EditText mEt_answer1;

    @BindView(R.id.verify_question2)
    MaterialSpinner mMaterialSpinner2;
    @BindView(R.id.verify_answer2)
    EditText mEt_answer2;

    @BindView(R.id.verify_question3)
    MaterialSpinner mMaterialSpinner3;
    @BindView(R.id.verify_answer3)
    EditText mEt_answer3;

    private SharedPreferences.Editor mEditor_question1;
    private SharedPreferences.Editor mEditor_question2;
    private SharedPreferences.Editor mEditor_question3;
    private SharedPreferences.Editor mEditor_answer1;
    private SharedPreferences.Editor mEditor_answer2;
    private SharedPreferences.Editor mEditor_answer3;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_verify;
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initStatusBarStyle() {
        StatusBarUtils.initStatusBarStyle(this, false, Colors.WHITE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSP();
        initSpinner();
        setButtomClickListener();
    }


    private void initSpinner() {
        mMaterialSpinner1.setItems(ResUtils.getStringArray(R.array.verify_question1));
//        mMaterialSpinner1.setOnNothingSelectedListener(spinner -> SnackbarUtils.Long(spinner, "Nothing selected").show());
        mMaterialSpinner1.setSelectedItem("你的父亲的名字是什么");

        mMaterialSpinner2.setItems(ResUtils.getStringArray(R.array.verify_question2));
//        mMaterialSpinner2.setOnNothingSelectedListener(spinner -> SnackbarUtils.Long(spinner, "Nothing selected").show());
        mMaterialSpinner2.setSelectedItem("你最喜欢的运动是什么");

        mMaterialSpinner3.setItems(ResUtils.getStringArray(R.array.verify_question3));
//        mMaterialSpinner3.setOnNothingSelectedListener(spinner -> SnackbarUtils.Long(spinner, "Nothing selected").show());
        mMaterialSpinner3.setSelectedItem("毕业于哪个初中");
    }


    @SuppressLint("CommitPrefEdits")
    private void initSP() {
        SharedPreferences mSharedPreferences_question1 = getSharedPreferences("question1", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_question2 = getSharedPreferences("question2", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_question3 = getSharedPreferences("question3", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_answer1 = getSharedPreferences("answer1", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_answer2 = getSharedPreferences("answer2", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_answer3 = getSharedPreferences("answer3", MODE_PRIVATE);
        mEditor_question1 = mSharedPreferences_question1.edit();
        mEditor_question2 = mSharedPreferences_question2.edit();
        mEditor_question3 = mSharedPreferences_question3.edit();
        mEditor_answer1 = mSharedPreferences_answer1.edit();
        mEditor_answer2 = mSharedPreferences_answer2.edit();
        mEditor_answer3 = mSharedPreferences_answer3.edit();
    }




    public void setButtomClickListener() {
        mBtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor_question1.putInt("verify_qustion1", mMaterialSpinner1.getSelectedIndex());
                mEditor_question1.apply();
                mEditor_question2.putInt("verify_qustion2", mMaterialSpinner2.getSelectedIndex());
                mEditor_question2.apply();
                mEditor_question3.putInt("verify_qustion3", mMaterialSpinner3.getSelectedIndex());
                mEditor_question3.apply();

                mEditor_answer1.putString("verify_answer1", mEt_answer1.getText().toString());
                mEditor_answer1.apply();
                mEditor_answer2.putString("verify_answer2", mEt_answer2.getText().toString());
                mEditor_answer2.apply();
                mEditor_answer3.putString("verify_answer3", mEt_answer1.getText().toString());
                mEditor_answer3.apply();

                XToastUtils.success("注册成功");
                onLoginSuccess();
            }
        });
    }


    /**
     * 登录成功的处理
     */
    private void onLoginSuccess() {
        String token = RandomUtils.getRandomNumbersAndLetters(16);
        if (TokenUtils.handleLoginSuccess(token)) {
            Intent intent = new Intent(RegisterVerifyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @SingleClick
    @OnClick({R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user_protocol:
                XToastUtils.info("用户协议");
                break;
            case R.id.tv_privacy_protocol:
                XToastUtils.info("隐私政策");
                break;
            default:
                break;
        }
    }







}
