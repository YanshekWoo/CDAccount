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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseActivity;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xutil.display.Colors;

import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:21
 */
public class RegiterGestureActivity extends BaseActivity {

    @BindView(R.id.register_patter_lock_view)
    PatternLockView mPatternLockView;

    @BindView(R.id.register_gesture_text)
    TextView tv_register_gesture;

    private String GestureSignUp;
    private String GestureSignUp_sure;
    private int state = 1;
    private SharedPreferences mSharedPreferences_gesture;
    private SharedPreferences.Editor mEditor_gesture;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_gesture;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSP();
        initLock();
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
        mSharedPreferences_gesture = getSharedPreferences("gesture",MODE_PRIVATE);
        mEditor_gesture = mSharedPreferences_gesture.edit();
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
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);



        RxPatternLockView.patternComplete(mPatternLockView)
                .subscribe(new Consumer<PatternLockCompleteEvent>() {
                    @Override
                    public void accept(PatternLockCompleteEvent patternLockCompleteEvent) throws Exception {
                        Log.d(getClass().getName(), "Complete: " + patternLockCompleteEvent.getPattern().toString());
                    }
                });

        RxPatternLockView.patternChanges(mPatternLockView)
                .subscribe(new Consumer<PatternLockCompoundEvent>() {
                    @Override
                    public void accept(PatternLockCompoundEvent event) throws Exception {
                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
                            Log.d(getClass().getName(), "Pattern drawing started");
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
                            Log.d(getClass().getName(), "Pattern progress: " +
                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
                            Log.d(getClass().getName(), "Pattern complete: " +
                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
                            Log.d(getClass().getName(), "Pattern has been cleared");
                        }
                    }
                });
    }


    //设置监听器
    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
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
                }
                else if(state == 2)//第二次输入确认密码
                {
                    GestureSignUp_sure = patternToString;
                    if(GestureSignUp.equals(GestureSignUp_sure))//两次输入密码一致
                    {
                        mEditor_gesture.putString("gesture_sign",GestureSignUp);
                        mEditor_gesture.apply();
                        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
//                        XToastUtils.success("注册成功");

                        onLoginSuccess();
                    }
                    else//两次输入密码不一致
                    {
                        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                        tv_register_gesture.setText("请输入手势密码");
                        XToastUtils.error("两次输入的密码不一致");
                        state = 1;
                    }
                }
            }
            //1s后清除图案
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPatternLockView.clearPattern();
                }
            },200);
        }


        @Override
        public void onCleared() {
//            Log.d(getClass().getName(), "Pattern has been cleared");
        }

    };

    /**
     * 登录成功的处理
     */
    private void onLoginSuccess() {
        Intent intent = new Intent(RegiterGestureActivity.this, RegisterVerifyActivity.class);
        startActivity(intent);
        finish();
    }



}
