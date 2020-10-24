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
import android.content.SharedPreferences;
import android.text.InputType;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.ChangePasswordActivity;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.somethingDao.Dao.BillDao;
import com.xuexiang.cdaccount.utils.SettingUtils;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.app.ActivityUtils;

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
    @BindView(R.id.menu_common)
    SuperTextView menuCommon;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.menu_privacy)
    SuperTextView menuPrivacy;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.menu_change_passwd)
    SuperTextView menuChangeAccount;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.menu_clear_data)
    SuperTextView menuLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initViews() {
        menuCommon.setOnSuperTextViewClickListener(this);
        menuPrivacy.setOnSuperTextViewClickListener(this);
        menuChangeAccount.setOnSuperTextViewClickListener(this);
        menuLogout.setOnSuperTextViewClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @Override
    public void onClick(SuperTextView superTextView) {
        switch (superTextView.getId()) {
            case R.id.menu_common:
            case R.id.menu_privacy:
            case R.id.menu_change_passwd:
                ActivityUtils.startActivity(ChangePasswordActivity.class);
            break;
            case R.id.menu_clear_data:
//                DialogLoader.getInstance().showConfirmDialog(
//                        getContext(),
//                        getString(R.string.lab_logout_confirm),
//                        getString(R.string.lab_yes),
//                        (dialog, which) -> {
//                            dialog.dismiss();
//                            XUtil.getActivityLifecycleHelper().exit();
//                            TokenUtils.handleLogoutSuccess();
//                        },
//                        getString(R.string.lab_no),
//                        (dialog, which) -> dialog.dismiss()
//                );
                showInputDialog();
                break;
            default:
                break;
        }
    }


    /**
     * 带输入框的对话框
     */
    private void showInputDialog() {
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
