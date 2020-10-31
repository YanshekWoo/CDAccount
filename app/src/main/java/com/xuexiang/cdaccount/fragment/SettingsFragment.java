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

package com.xuexiang.cdaccount.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.InputType;

import androidx.annotation.RequiresApi;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.ReLoginActivity;
import com.xuexiang.cdaccount.biometriclib.BiometricPromptManager;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.somethingDao.BackupTask;
import com.xuexiang.cdaccount.somethingDao.Dao.BillDao;
import com.xuexiang.cdaccount.utils.MMKVUtils;
import com.xuexiang.cdaccount.utils.SettingUtils;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.data.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author xuexiang
 * @since 2019-10-15 22:38
 */
@Page(name = "设置")
public class SettingsFragment extends BaseFragment implements SuperTextView.OnSuperTextViewClickListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.stv_switch_custom_theme)
    SuperTextView stvSwitchBiometric;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.stv_switch_budget)
    SuperTextView switch_budget;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.db_backup)
    SuperTextView button_backup;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.db_restore)
    SuperTextView button_restore;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.menu_change_passwd)
    SuperTextView menuChangeAccount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.menu_clear_data)
    SuperTextView menuLogout;

    Boolean budgetIsOpen = false;
    int budget;

//    MaterialDialog changeBudgetDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initViews() {
        initBiometricSwitch();
        initBudgetSwitch();

        button_backup.setOnSuperTextViewClickListener(this);
        button_restore.setOnSuperTextViewClickListener(this);

        menuChangeAccount.setOnSuperTextViewClickListener(this);
        menuLogout.setOnSuperTextViewClickListener(this);
    }

    /**
     * 指纹密码开关
     */
    private void initBiometricSwitch() {
        stvSwitchBiometric.setSwitchIsChecked(BiometricPromptManager.isBiometricSettingEnable());
        stvSwitchBiometric.setOnSuperTextViewClickListener(superTextView -> stvSwitchBiometric.setSwitchIsChecked(!stvSwitchBiometric.getSwitchIsChecked(), false));
        stvSwitchBiometric.setSwitchCheckedChangeListener((buttonView, isChecked) -> {
            BiometricPromptManager.setBiometricSettingEnable(stvSwitchBiometric.getSwitchIsChecked());
            XToastUtils.info(stvSwitchBiometric.getSwitchIsChecked()?"已开启指纹解锁":"已关闭指纹解锁");
        });
    }

    /**
     * 预算开关
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initBudgetSwitch() {
        budget = (int)MMKVUtils.get("Budget", -1);
        budgetIsOpen = budget > 0;
        switch_budget.setSwitchIsChecked(budgetIsOpen);
        switch_budget.setOnSuperTextViewClickListener(superTextView -> switch_budget.setSwitchIsChecked(!switch_budget.getSwitchIsChecked(),false));
        switch_budget.setSwitchCheckedChangeListener((compoundButton, b) -> {
            // 从关闭状态开启
            if(b) {
                changeBudget();
            }
            else{
                MMKVUtils.put("Budget", -1);
                budgetIsOpen = false;
            }
        });
    }


    
    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @Override
    public void onClick(@NotNull SuperTextView superTextView) {
        switch (superTextView.getId()) {
            case R.id.db_backup:
                databaseBackup();

                break;
            case R.id.db_restore:
                databaseRestoe();

                break;
            case R.id.menu_change_passwd:
                Intent intent = new Intent(getContext(), ReLoginActivity.class);
                intent.putExtra("LogInTYPE", 1);
                startActivity(intent);
//                ActivityUtils.startActivity(ReLoginActivity.class);
            break;
            case R.id.menu_clear_data:
                clearDataDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 设置预算
     */
    private void changeBudget(){
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("修改预算")
                .inputType(
                        InputType.TYPE_CLASS_NUMBER)
                .input(
                        "请输入预算",
                        "",
                        false,
                        (dialog, input) -> {
                        })
                .inputRange(1,10)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive((dialog, which) -> {
                    // 点击确认后存入budget值
                    assert dialog.getInputEditText() != null;
                    MMKVUtils.put("Budget", Integer.parseInt(dialog.getInputEditText().getText().toString()));
                    budgetIsOpen = true;
                })
                .onNegative((dialog, which) -> {
                    //  点击取消按钮，仍然关闭预算提醒，开关关闭。budget置-1
                    MMKVUtils.put("Budget", -1);
                    budgetIsOpen = false;
                    switch_budget.setSwitchIsChecked(false);
                })
                .show();
    }


    private void databaseBackup(){
        String backupTime = (String)MMKVUtils.get("BackpTime", "");
        String dialogContext;
        if(backupTime.equals("")) {
            dialogContext = "当前无备份";
        }
        else {
            dialogContext = "上一次备份时间"+"\n"+backupTime;
        }
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("数据备份")
                .content(dialogContext)
                .positiveText("更新")
                .negativeText("取消")
                .onPositive((dialog, which) -> {
                    new BackupTask(getContext()).execute(BackupTask.COMMAND_BACKUP);

                    Calendar calendar = Calendar.getInstance();
                    String currentTime = DateUtils.date2String(calendar.getTime(), DateUtils.yyyyMMddHHmmss.get());
                    MMKVUtils.put("BackpTime", currentTime);

                })
                .onNegative((dialog, which) -> {
                })
                .show();
    }


    private void databaseRestoe(){
        String backupTime = (String)MMKVUtils.get("BackpTime", "");
        String dialogContext;
        if(backupTime.equals("")) {
            dialogContext = "当前无备份";
        }
        else {
            dialogContext = "上一次备份时间"+"\n"+backupTime;
        }
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("数据恢复")
                .content(dialogContext)
                .positiveText("恢复")
                .negativeText("取消")
                .onPositive((dialog, which) -> {
                    if(!backupTime.equals("")) {
                        new BackupTask(getContext()).execute(BackupTask.COMMAND_RESTORE);
                    }
                })
                .onNegative((dialog, which) -> {
                })
                .show();
    }


    /**
     * 带输入框的对话框
     */
    private void clearDataDialog() {
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_warning)
                .title(R.string.tip_warning)
                .content(R.string.content_warning)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(
                        getString(R.string.hint_please_input_password),
                        "",
                        false,
                        ((dialog, input) ->{ }))
                .inputRange(4, 18)
                .positiveText(R.string.lab_confirem)
                .negativeText(R.string.lab_cancel)
                .onPositive((dialog, which) -> {
                    SharedPreferences mSharedPreferences_passwd = Objects.requireNonNull(getActivity()).getSharedPreferences("password", MODE_PRIVATE);
                    String password = mSharedPreferences_passwd.getString("password","");
                    assert dialog.getInputEditText() != null;
                    String inputText = MD5Utils.encode(dialog.getInputEditText().getText().toString());
                    if(inputText.equals(password)) {
                        BillDao billDao = new BillDao(getContext());
                        billDao.Destory();
                        dialog.dismiss();
                        SettingUtils.setIsAgreePrivacy(false);
                        SettingUtils.setIsFirstOpen(true);
                        XUtil.getActivityLifecycleHelper().exit();
                    }
                    else {
                        XToastUtils.error("密码错误");
                    }

                })
                .cancelable(false)
                .show();
    }


}
