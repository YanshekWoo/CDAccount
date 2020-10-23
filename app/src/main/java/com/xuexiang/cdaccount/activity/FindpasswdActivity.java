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
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.fragment.login.LoginGestureFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.display.Colors;

import java.util.Objects;

import butterknife.BindView;

/**
 * 找回密码页面
 *
 * @author Chenhao
 * @since 2020-10-22
 */
public class FindpasswdActivity extends BaseActivity{

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_verify_commit)
    SuperButton mBtSign;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.verify_question1)
    MaterialSpinner mMaterialSpinner1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.verify_answer1)
    MaterialEditText mEt_answer1;

    String[] Questions = new String[3];
    String[] Answers = new String[3];
    @Override
    protected int getLayoutId() {
        return R.layout.activity_findpasswd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSP();
        initTextView();
        setButtomClickListener();

    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    @Override
    protected void initStatusBarStyle() {
        StatusBarUtils.initStatusBarStyle(this, false, Colors.WHITE);
    }


    @SuppressLint("CommitPrefEdits")
    private void initSP() {
        SharedPreferences mSharedPreferences_question1 = getSharedPreferences("question1", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_question2 = getSharedPreferences("question2", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_question3 = getSharedPreferences("question3", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_answer1 = getSharedPreferences("answer1", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_answer2 = getSharedPreferences("answer2", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_answer3 = getSharedPreferences("answer3", MODE_PRIVATE);

        Questions[0] = ResUtils.getStringArray(R.array.verify_question1)[mSharedPreferences_question1.getInt("verify_qustion1",0)];
        Questions[1] = ResUtils.getStringArray(R.array.verify_question2)[mSharedPreferences_question2.getInt("verify_qustion2",0)];
        Questions[2] = ResUtils.getStringArray(R.array.verify_question3)[mSharedPreferences_question3.getInt("verify_qustion3",0)];
        Answers[0] = mSharedPreferences_answer1.getString("verify_answer1","");
        Answers[1] = mSharedPreferences_answer2.getString("verify_answer2","");
        Answers[2] = mSharedPreferences_answer3.getString("verify_answer3","");

        mMaterialSpinner1.setItems(Questions);
        mMaterialSpinner1.setSelectedIndex(0);

    }

    private void initTextView() {
        mEt_answer1.setFilters(new InputFilter[]{new LengthFilter(12)});
    }

    public void setButtomClickListener() {
        mBtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ans1 = Objects.requireNonNull(mEt_answer1.getText()).toString();
               if(MD5Utils.encode(ans1).equals(Answers[mMaterialSpinner1.getSelectedIndex()])){
                   XToastUtils.success("答案正确，请重置密码");
                   ActivityUtils.startActivity(SetpasswdActivity.class);
                   finish();
               }else {
                    XToastUtils.error("答案错误");
                }
            }
        });
    }

    /**
     * 限制最大长度
     */
    public static class LengthFilter implements InputFilter {
        private final int mMax;

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

    }
}
