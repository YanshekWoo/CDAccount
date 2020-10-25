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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.FindpasswdActivity;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;
import com.xuexiang.xutil.app.ActivityUtils;

import java.util.Objects;

import butterknife.BindView;
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

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.login_commit)
    SuperButton BtLogin;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.login_passwd)
    MaterialEditText etlogin_passwd;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.login_user)
    MaterialEditText etlogin_user;


    private String password;
    private String user_name;


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
        initEditText();
        setButtomClickListener();
    }


    private void initSP() {
        SharedPreferences mSharedPreferences_user = Objects.requireNonNull(getActivity()).getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences mSharedPreferences_passwd = getActivity().getSharedPreferences("password", MODE_PRIVATE);
        user_name = mSharedPreferences_user.getString("user","");
        password = mSharedPreferences_passwd.getString("password","");
    }


    private void initEditText() {
        etlogin_user.setText(user_name);
        etlogin_user.setFocusable(false);

        etlogin_passwd.setFilters(new InputFilter[]{new LengthFilter(18)});
    }


    /**
     * 登录按钮监听
     */
    private void setButtomClickListener() {
        BtLogin.setOnClickListener(v -> {
            if(password.equals(MD5Utils.encode(Objects.requireNonNull(etlogin_passwd.getText()).toString())))
            {
//                    XToastUtils.success("密码正确");
                Objects.requireNonNull(getActivity()).finish();
            }
            else
            {
                XToastUtils.error("密码错误");
            }
        });
    }


    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @OnClick({R.id.tv_other_login2, R.id.tv_forget_password, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_other_login2:
//                openPage(LoginGestureFragment.class, getActivity().getIntent().getExtras());
                openPage(LoginGestureFragment.class, false);
                break;
            case R.id.tv_forget_password:
                ActivityUtils.startActivity(FindpasswdActivity.class);
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

    @Override
    public void onResume() {    //修改密码后重新加载
        super.onResume();
        initSP();
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

