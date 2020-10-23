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
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.utils.TokenUtils;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xutil.app.ActivityUtils;

import butterknife.BindView;

/**
 * @author Chenhao
 * @since 2020-10-22
 */
public class ChangePasswordActivity extends BaseActivity {

    private Boolean HasLogin;

    Button mBtSign;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setpasswd;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HasLogin = false;
        super.onCreate(savedInstanceState);
        TokenUtils.clearToken();
        XToastUtils.info("请进行身份验证");
        ActivityUtils.startActivity(ReLoginActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("---changePasswd---", "onResume: ");
        if (TokenUtils.hasToken()) {
            TokenUtils.clearToken();
            XToastUtils.success("身份验证成功，请修改密码");
            ActivityUtils.startActivity(SetpasswdActivity.class);
        }
        if(!HasLogin){
            HasLogin = true;
        }else{
            finish();
        }

    }


    //
//    @SuppressLint("CommitPrefEdits")
//    private void initSP() {
//        SharedPreferences mSharedPreferences_user = getSharedPreferences("user", MODE_PRIVATE);
//        SharedPreferences mSharedPreferences_password = getSharedPreferences("password", MODE_PRIVATE);
//        mEditor_password = mSharedPreferences_password.edit();
//        user_name = mSharedPreferences_user.getString("user","");
//        old_passwd = mSharedPreferences_password.getString("password","");
//    }
//    public void initTextView() {
//        mTvUsername.setText(user_name);
//        mEt_password.setFilters(new InputFilter[]{new SetpasswdActivity.LengthFilter(18)});
//        mEt_password_again.setFilters(new InputFilter[]{new SetpasswdActivity.LengthFilter(18)});
//    }
//    public void setButtomClickListener() {
//        mBtSign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String passwd1 = Objects.requireNonNull(mEt_password.getText()).toString();
//                String passwd2 = Objects.requireNonNull(mEt_password_again.getText()).toString();
//                if(MD5Utils.encode(passwd0).equals(old_passwd)) {
//                    if (passwd1.equals(passwd2)) {
//                        if (passwd1.length() < 4 || passwd2.length() < 4) {
//                            XToastUtils.error("新密码长度不能小于4");
//                        } else if (passwd1.length() > 18 || passwd2.length() > 18) {
//                            XToastUtils.error("新密码长度不能超过18");
//                        } else {
//                            mEditor_password.putString("password", MD5Utils.encode(passwd2));
//                            mEditor_password.apply();
//                            XToastUtils.success("密码已修改");
//
////                        Intent intent = new Intent(RegiterNumberActivity.this, RegiterGestureActivity.class);
////                        startActivity(intent);
//                            finish();
//                        }
//
//                    } else {
//                        XToastUtils.error("两次输入的新密码不一致");
//                    }
//                }else{
//                    XToastUtils.error("原密码输入错误");
//                }
//            }
//        });
//    }
//
//
//
//
//    /**
//     * 限制最大长度
//     */
//    public static class LengthFilter implements InputFilter {
//        private final int mMax;
//
//        public LengthFilter(int max) {
//            mMax = max;
//        }
//
//        @Override
//        public CharSequence filter(CharSequence source, int start, int end,
//                                   Spanned dest, int dstart, int dend) {
//            int keep = mMax - (dest.length() - (dend - dstart));
//
//            if (keep <= 0) {
//                XToastUtils.error("最多仅可输入" + mMax + "个字符");
//                return "";
//            } else if (keep >= end - start) {
//                return null; // keep original
//            } else {
//                keep += start;
//                XToastUtils.error("最多仅可输入" + mMax + "个字符");
//                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
//                    --keep;
//                    if (keep == start) {
//                        return "";
//                    }
//                }
//                return source.subSequence(start, keep);
//            }
//        }
//
//    }
}
