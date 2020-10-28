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

import android.content.Intent;
import android.view.KeyEvent;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.biometriclib.BiometricPromptManager;
import com.xuexiang.cdaccount.utils.SettingUtils;
import com.xuexiang.cdaccount.utils.Utils;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;
import com.xuexiang.xutil.app.ActivityUtils;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 启动页【无需适配屏幕大小】
 *
 * @author xuexiang
 * @since 2019-06-30 17:32
 */
public class SplashActivity extends BaseSplashActivity implements CancelAdapt {

    @Override
    protected long getSplashDurationMillis() {
        return 400;
    }

    /**
     * activity启动后的初始化
     */
    @Override
    protected void onCreateActivity() {
        initSplashView(R.drawable.xui_config_bg_splash);
        startSplash(false);
    }


    /**
     * 启动页结束后的动作
     */
    @Override
    protected void onSplashFinished() {
        if (SettingUtils.isAgreePrivacy()) {
            loginOrRegister();
        } else {
            Utils.showPrivacyDialog(this, (dialog, which) -> {
                dialog.dismiss();
                SettingUtils.setIsAgreePrivacy(true);
                loginOrRegister();
            });
        }
    }

    private void loginOrRegister() {
        if (SettingUtils.isFirstOpen()) {
            BiometricPromptManager.setBiometricSettingEnable(false);
            ActivityUtils.startActivity(RegiterNumberActivity.class);
        } else {
//            Intent[] intents = new Intent[2];
//            intents[0] = new Intent(this,MainActivity.class);
//            intents[1] = new Intent(this,LoginActivity.class);
//            startActivities(intents);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("LogInTYPE", 0);
            startActivity(intent);
//            ActivityUtils.startActivity(LoginActivity.class);
        }
        finish();
    }




    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }
}
