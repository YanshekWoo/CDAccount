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
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.fragment.docs.PrivacyFragment;
import com.xuexiang.cdaccount.utils.SettingUtils;
import com.xuexiang.cdaccount.utils.TokenUtils;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.display.Colors;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:21
 */
public class RegiterGestureActivity extends BaseActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_patter_lock_view)
    PatternLockView mPatternLockView;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_gesture_text)
    TextView tv_register_gesture;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.register_gesture_img)
    ImageView imageView;


    private String GestureSignUp;

    private SharedPreferences.Editor mEditor_gesture;

    MyPatternLockViewListener myPatternLockViewListener;




    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_gesture;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSP();
        initImageView();
        initLock();
        if(!SettingUtils.isFirstOpen()){
            tv_register_gesture.setText("请重置手势密码");
        }
    }


    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }


    @Override
    protected void initStatusBarStyle() {
        StatusBarUtils.initStatusBarStyle(this, false, Colors.WHITE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }


    @SuppressLint("CommitPrefEdits")
    private void initSP() {
        SharedPreferences mSharedPreferences_gesture = getSharedPreferences("gesture", MODE_PRIVATE);
        mEditor_gesture = mSharedPreferences_gesture.edit();
    }


    private void initImageView() {
        if(TokenUtils.hasToken()) {
            imageView.setImageResource(R.drawable.ic_findpasswd);
        }
    }

    @SuppressLint("CheckResult")
    private void initLock() {
        // n*n大小   3*3
        mPatternLockView.setDotCount(3);
        //没有点击时点的大小
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        //点击时点的大小
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        //更改路径距离
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);


        myPatternLockViewListener = new MyPatternLockViewListener();
        mPatternLockView.addPatternLockListener(myPatternLockViewListener);


//        RxPatternLockView.patternComplete(mPatternLockView)
//                .subscribe(new Consumer<PatternLockCompleteEvent>() {
//                    @Override
//                    public void accept(PatternLockCompleteEvent patternLockCompleteEvent) {
//                        Log.d(getClass().getName(), "Complete: " + Objects.requireNonNull(patternLockCompleteEvent.getPattern()).toString());
//                    }
//                });
//
//        RxPatternLockView.patternChanges(mPatternLockView)
//                .subscribe(new Consumer<PatternLockCompoundEvent>() {
//                    @Override
//                    public void accept(PatternLockCompoundEvent event) {
//                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
//                            Log.d(getClass().getName(), "Pattern drawing started");
//                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
//                            Log.d(getClass().getName(), "Pattern progress: " +
//                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
//                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
//                            Log.d(getClass().getName(), "Pattern complete: " +
//                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
//                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
//                            Log.d(getClass().getName(), "Pattern has been cleared");
//                        }
//                    }
//                });

    }



    private class MyPatternLockViewListener implements PatternLockViewListener {
        private int state = 1;

        @Override
        public void onStarted() {

        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {

        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            //手势密码注册
            String patternToString = PatternLockUtils.patternToString(mPatternLockView, pattern);
            if(!TextUtils.isEmpty(patternToString)){
                if(state == 1)//第一次输入密码
                {
                    GestureSignUp = patternToString;
                    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    tv_register_gesture.setText("请再次输入密码");
                    XToastUtils.info("请再次输入密码");
                    state = 2;
                    //1s后清除图案
                    new Handler().postDelayed(() -> mPatternLockView.clearPattern(),500);
                }
                else if(state == 2)//第二次输入确认密码
                {
                    if(GestureSignUp.equals(patternToString))//两次输入密码一致
                    {
                        mEditor_gesture.putString("gesture_sign", MD5Utils.encode(GestureSignUp));
                        mEditor_gesture.apply();
                        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
//                        XToastUtils.success("注册成功");

                        onLoginSuccess();
                    }
                    else//两次输入密码不一致
                    {
                        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                        if(!SettingUtils.isFirstOpen()){
                            tv_register_gesture.setText("请重置手势密码");
                        }else {
                            tv_register_gesture.setText("请输入手势密码");
                        }
                        XToastUtils.error("两次输入的密码不一致");
                        state = 1;
                        //1s后清除图案
                        new Handler().postDelayed(() -> mPatternLockView.clearPattern(),500);
                    }
                }
            }

        }
        @Override
        public void onCleared() {

        }
    }


    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @OnClick({R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user_protocol:
                XToastUtils.info("用户协议");
                break;
            case R.id.tv_privacy_protocol:
//                XToastUtils.info("隐私政策");
                openNewPage(PrivacyFragment.class);
                break;
            default:
                break;
        }
    }


    /**
     * 注册成功的处理
     */
    private void onLoginSuccess() {
//        if(SettingUtils.isFirstOpen()){                                     //若找回密码时调用此页面，则不跳转
//            ActivityUtils.startActivity(RegisterVerifyActivity.class);
//        }else{
//            XToastUtils.success("手势密码已重置");
//        }
//        finish();

        if(!TokenUtils.hasToken()) {
            ActivityUtils.startActivity(RegisterVerifyActivity.class);
        }
        else {
            TokenUtils.clearToken();
            SettingUtils.setIsFirstOpen(false);
            ActivityUtils.startActivity(MainActivity.class);
        }
        finish();
    }


    @Override
    public void onDestroy() {
        myPatternLockViewListener = null;
        super.onDestroy();
    }


}
