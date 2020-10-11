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
import com.xuexiang.cdaccount.utils.SettingUtils;
import com.xuexiang.cdaccount.utils.Utils;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.StatusBarUtils;
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
public class RegiterNumberActivity extends BaseActivity implements ClickUtils.OnClick2ExitListener {

    @BindView(R.id.register_commit)
    Button mBtSign;

    @BindView(R.id.register_user)
    EditText mEt_user;
    @BindView(R.id.register_passwd)
    EditText mEt_password;
    @BindView(R.id.register_passwd_again)
    EditText mEt_password_again;


    private SharedPreferences.Editor mEditor_user;
    private SharedPreferences.Editor mEditor_password;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSP();
        setButtomClickListener();
        showPrivacy();
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
        mEditor_user = mSharedPreferences_user.edit();
        mEditor_password = mSharedPreferences_password.edit();
    }




    public void setButtomClickListener() {
        mBtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEt_password.getText().toString().equals(mEt_password_again.getText().toString())) {
                    mEditor_user.putString("user", mEt_user.getText().toString());
                    mEditor_user.apply();
                    mEditor_password.putString("password", mEt_password.getText().toString());
                    mEditor_password.apply();

                    XToastUtils.success("注册成功");

                    Intent intent = new Intent(RegiterNumberActivity.this, RegiterGestureActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    XToastUtils.error("两次输入的密码不一致");
                }
            }
        });
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


    private void showPrivacy() {
        //隐私政策弹窗
        if (!SettingUtils.isAgreePrivacy()) {
            Utils.showPrivacyDialog(RegiterNumberActivity.this, (dialog, which) -> {
                dialog.dismiss();
                SettingUtils.setIsAgreePrivacy(true);
            });
        }
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this);
        }
        return true;
    }

    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    @Override
    public void onExit() {
        XUtil.exitApp();
    }
}
