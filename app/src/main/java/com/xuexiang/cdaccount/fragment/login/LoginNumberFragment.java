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

package com.xuexiang.cdaccount.fragment.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.MainActivity;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(anim = CoreAnim.none)
public class LoginNumberFragment extends BaseFragment {


    private Button BtLogin;
    private SharedPreferences mSharedPreferences_login;
    private EditText etlogin;
    private String login;
    private Handler handler;
    private Runnable delayRun;
    private String password;
    private String editString;


    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_number;
    }



    @Override
    protected void initViews() {
        initSP();
        initHandler();
        initTextView();
        setButtomClickListener();
    }

    @SingleClick
    @OnClick({R.id.tv_other_login2, R.id.tv_forget_password, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_other_login2:
                XToastUtils.info("其他登录方式");
                openPage(LoginGestureFragment.class, getActivity().getIntent().getExtras());
                break;
            case R.id.tv_forget_password:
                XToastUtils.info("忘记密码");
                break;
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


    private void initSP() {
        mSharedPreferences_login = getActivity().getSharedPreferences("password",MODE_PRIVATE);
        password = mSharedPreferences_login.getString("password","");
    }


    private void initHandler() {
        handler = new Handler();
        delayRun = new Runnable() {
            @Override
            public void run() {
                Log.d("password_login",editString);
                login = editString;
            }
        };


    }


    private void initTextView() {


        etlogin = (EditText)findViewById(R.id.login_passwd);
        etlogin.addTextChangedListener(new TextWatcher() {
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
    }


    private void setButtomClickListener() {
        BtLogin = (Button)findViewById(R.id.login_commit);
        BtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.equals(login))
                {
                    XToastUtils.success("密码正确，开锁成功");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else
                {
                    XToastUtils.success("密码错误，请重新绘制");
                }
            }
        });
    }

}

