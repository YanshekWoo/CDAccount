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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.display.Colors;

import java.util.Objects;

import butterknife.BindView;

/**
 * 重设密码页面
 *
 * @author Chenhao
 * @since 2020-10-22
 */
public class SetpasswdActivity extends BaseActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_commit)
    Button mBtSign;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_user)
    TextView mTvUsername;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_passwd)
    MaterialEditText mEt_password;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_passwd_again)
    MaterialEditText mEt_password_again;

    private SharedPreferences.Editor mEditor_password;
    private String user_name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setpasswd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSP();
        setButtomClickListener();
        initTextView();

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
        SharedPreferences mSharedPreferences_user = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_password = getSharedPreferences("password", MODE_PRIVATE);
        mEditor_password = mSharedPreferences_password.edit();
        user_name = mSharedPreferences_user.getString("user","");

    }

    public void initTextView() {
        mTvUsername.setText(user_name);
        mEt_password.setFilters(new InputFilter[]{new LengthFilter(18)});
        mEt_password_again.setFilters(new InputFilter[]{new LengthFilter(18)});
    }

    public void setButtomClickListener() {
        mBtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd1 = Objects.requireNonNull(mEt_password.getText()).toString();
                String passwd2 = Objects.requireNonNull(mEt_password_again.getText()).toString();

                if (passwd1.equals(passwd2)) {
                    if (passwd1.length() < 4 || passwd2.length() < 4) {
                        XToastUtils.error("密码长度不能小于4");
                    } else if (passwd1.length() > 18 || passwd2.length() > 18) {
                        XToastUtils.error("密码长度不能超过18");
                    } else {
                        mEditor_password.putString("password", MD5Utils.encode(passwd2));
                        mEditor_password.apply();
                        XToastUtils.success("数字密码已重置");

//                        Intent intent = new Intent(RegiterNumberActivity.this, RegiterGestureActivity.class);
//                        startActivity(intent);
                        ActivityUtils.startActivity(SetGestureActivity.class);
                        finish();
                    }

                } else {
                    XToastUtils.error("两次输入的密码不一致");
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
