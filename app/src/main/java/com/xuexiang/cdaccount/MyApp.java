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

package com.xuexiang.cdaccount;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.xuexiang.cdaccount.activity.LoginActivity;
import com.xuexiang.cdaccount.utils.SettingUtils;
import com.xuexiang.cdaccount.utils.sdkinit.ANRWatchDogInit;
import com.xuexiang.cdaccount.utils.sdkinit.UMengInit;
import com.xuexiang.cdaccount.utils.sdkinit.XBasicLibInit;
import com.xuexiang.cdaccount.utils.sdkinit.XUpdateInit;

import net.sqlcipher.database.SQLiteDatabase;

import static com.xuexiang.xui.XUI.getContext;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class MyApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLibs();
        AppFrontBackHelper helper = new AppFrontBackHelper();
        helper.register(MyApp.this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //应用切到前台处理
                Log.d("---MyApp", "onFront: ");
                if (!SettingUtils.isFirstOpen()) {          //若已完成注册，则进入登录界面
//                    Intent intent = new Intent(getContext(), LoginActivity.class);
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                    intent.setAction(Intent.ACTION_MAIN);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    Log.d("---MyApp", "test");

                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("LogInTYPE", 2);
                    startActivity(intent);

                }

            }

            @Override
            public void onBack() {
                //应用切到后台处理
                Log.d("---MyApp", "onBack: ");


            }
        });

    }

    /**
     * 初始化基础库
     */
    private void initLibs() {
        // SQLcipher 加密库
        SQLiteDatabase.loadLibs(this);

        XBasicLibInit.init(this);
        XUpdateInit.init(this);

        //运营统计数据运行时不初始化
        if (!MyApp.isDebug()) {
            UMengInit.init(this);
        }

        //ANR监控
        ANRWatchDogInit.init();
    }


    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }


}
