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
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.display.Colors;

/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:21
 */
public class RegiterNumberActivity extends BaseActivity implements ClickUtils.OnClick2ExitListener {

    private Button mBtSign;
    private EditText mEt_user;
    private EditText mEt_password;
    private EditText mEt_password_2;
    private Handler handler;
    private Runnable delayRun;
    private Handler handler_2;
    private Runnable delayRun_2;
    private Handler handler_3;
    private Runnable delayRun_3;
    private String editString;
    private String editString_2;
    private String editString_3;
    private String password_save;
    private String user_save;
    private String password_sure_save;
    private SharedPreferences mSharedPreferences_user;
    private SharedPreferences mSharedPreferences_password;
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
        initHandler();
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
        mSharedPreferences_user = getSharedPreferences("user",MODE_PRIVATE);
        mSharedPreferences_password = getSharedPreferences("password",MODE_PRIVATE);
        mEditor_user = mSharedPreferences_user.edit();
        mEditor_password = mSharedPreferences_password.edit();
    }


    private void initHandler() {
        handler = new Handler();
        delayRun = new Runnable() {
            @Override
            public void run() {
                Log.i("user",editString);
                user_save = editString;
            }
        };
        handler_2 = new Handler();
        delayRun_2 = new Runnable() {
            @Override
            public void run() {
                Log.i("password",editString_2);
                password_save = editString_2;
            }
        };
        handler_3 = new Handler();
        delayRun_3 = new Runnable() {
            @Override
            public void run() {
                Log.i("password_sure",editString_3);
                password_sure_save = editString_3;
            }
        };
    }


    private void initTextView() {
        mEt_user = (EditText)findViewById(R.id.register_user);
        mEt_password = (EditText)findViewById(R.id.register_passwd);
        mEt_password_2 = (EditText)findViewById(R.id.register_passwd_again);

        mEt_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                editString = s.toString();

                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 800);
            }
        });

        mEt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun_2!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler_2.removeCallbacks(delayRun_2);
                }
                editString_2 = s.toString();

                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler_2.postDelayed(delayRun_2, 800);
            }
        });

        mEt_password_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun_3!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler_3.removeCallbacks(delayRun_3);
                }
                editString_3 = s.toString();

                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler_3.postDelayed(delayRun_3, 800);
            }
        });
    }


    public void setButtomClickListener() {
        mBtSign = (Button) findViewById(R.id.register_commit);
        mBtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password_save.equals(password_sure_save))
                {
                    Toast.makeText(RegiterNumberActivity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mEditor_user.putString("user",user_save);
                    mEditor_user.apply();
                    mEditor_password.putString("password",password_save);
                    mEditor_password.apply();
                    Toast.makeText(RegiterNumberActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegiterNumberActivity.this, RegiterGestureActivity.class);
                    startActivity(intent);
                }
            }
        });
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
