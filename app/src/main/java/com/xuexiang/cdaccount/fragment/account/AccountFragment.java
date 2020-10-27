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

package com.xuexiang.cdaccount.fragment.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.AccountDetailsActivity;
import com.xuexiang.cdaccount.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.database.AccountDataEntry;
import com.xuexiang.cdaccount.database.ChartDataEntry;
import com.xuexiang.cdaccount.somethingDao.Dao.BillDao;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.LogRecord;


import butterknife.BindView;

/**
 * @author Tingwei Wen
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class AccountFragment extends BaseFragment{

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    @BindView(R.id.total1)
    TextView total;

    ImageView img;

    BillDao billDao;

    private SimpleDelegateAdapter<AccountDataEntry> adapter;
//    private SmartRecyclerAdapter<AccountDataEntry> mAdapter;
    private List<AccountDataEntry> accountDataEntries;

    int countClick = 0;
    android.os.Handler handler = new Handler();

//    private static final long clickTime = 300;
//    private static long lastClickTime = 0;
//    private static long currentClickTime;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    /**
     * 初始化控件
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initViews() {

        billDao = new BillDao(getContext());
        accountDataEntries = new ArrayList<>();

        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(Objects.requireNonNull(getContext()));
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);


        adapter = new SimpleDelegateAdapter<AccountDataEntry>(R.layout.adapter_account_list_item,new LinearLayoutHelper()) {
//            @SuppressLint("DefaultLocale")
//            @Override
//            protected void onBindViewHolder(SmartViewHolder holder, AccountDataEntry item, int position) {
//                holder.text(R.id.account_name, item.getName());
//                holder.text(R.id.account_income, String.format("%.2f", item.getInMoney()));
//                holder.text(R.id.account_outcome, String.format("%.2f", item.getOutMoney()));
//                holder.text(R.id.account_money, String.format("%.2f", item.getInMoney() - item.getOutMoney()));
////                holder.click(R.id.account_card, new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        viewHolderClick(item);
////                    }
////                });
//            }
            @SuppressLint("DefaultLocale")
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, AccountDataEntry item) {
                holder.text(R.id.account_name,item.getName());
                holder.text(R.id.account_income, String.format("%.2f", item.getInMoney()));
                holder.text(R.id.account_outcome,String.format("%.2f", item.getOutMoney()));
                holder.text(R.id.account_money, String.format("%.2f", item.getInMoney()-item.getOutMoney()));
                holder.click(R.id.account_card, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        countClick++;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(countClick == 1){
                                    viewHolderClick(item);
                                }else {
                                    changAccount(item);
                                }
                                handler.removeCallbacksAndMessages(null);
                                countClick = 0;
                            }
                        },400);
                    }
                });
                switch (position){
                    case 0:
                        holder.image(R.id.account_img,R.drawable.ic_cash);
                        break;
                    case 1:
                        holder.image(R.id.account_img,R.drawable.ic_creditcard);
                        break;
                    case 2:
                        holder.image(R.id.account_img,R.drawable.ic_debitcard);
                        break;
                    case 3:
                        holder.image(R.id.account_img,R.drawable.ic_alipay);
                        break;
                    case 4:
                        holder.image(R.id.account_img,R.drawable.ic_fund);
                        break;
                    default:
                        holder.image(R.id.account_img,R.drawable.ic_otheraccount);
                        break;
                }
//                holder.click(R.id.account_card,view -> onItemLongClick(view,item,position));
//                holder.getView(R.id.account_card).setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//
//                        showAccountChangeDialog(item);
//                        return true;
//                    }
//                });
                }
            };
        recyclerView.setAdapter(adapter);

//        mAdapter.setOnItemClickListener(new SmartViewHolder.OnItemClickListener() {
//            @Override
//            public void onItemClick(View itemView, int position) {
//                createAccount();
//            }
//        });
//
//        mAdapter.setOnItemLongClickListener(new SmartViewHolder.OnItemLongClickListener() {
//            @Override
//            public void onItemLongClick(View itemView, int position) {
//                createAccount();
//            }
//        });
//        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
//        delegateAdapter.addAdapter(adapter);



        img = findViewById(R.id.account_add);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createAccount();
            }
        });

        initRefreshLayoutListeners();
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void accountSum(){
        Double totalInMoney = accountDataEntries.stream().map(AccountDataEntry::getInMoney).reduce(0.00, Double::sum);
        Double totalOutMoney = accountDataEntries.stream().map(AccountDataEntry::getOutMoney).reduce(0.00, Double::sum);
        total.setText(String.format("%.2f", totalInMoney-totalOutMoney));
        if(totalInMoney<totalOutMoney){
            total.setTextColor(getContext().getResources().getColor(R.color.app_color_theme_1));
        }else{
            total.setTextColor(getContext().getResources().getColor(R.color.app_color_theme_5));
        }
    }

    private void createAccount(){
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("添加账户")
                .inputType(
                        InputType.TYPE_CLASS_TEXT)
                .input(
                        "请输入新账户",
                        "",
                        false,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            }
                        })
                .inputRange(1,5)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                mAccount = dialog.getInputEditText().getText().toString();
//                                mTvAccount.setText(mAccount);
                        //TODO:insert_new_account
                        assert dialog.getInputEditText() != null;
                        if(billDao.InsertAccount(dialog.getInputEditText().getText().toString())){
                            XToastUtils.success("添加账户成功");
                            refreshLayout.autoRefresh();
                        }else{
                            XToastUtils.error("添加账户失败，该账户已存在");
                        }
                    }
                })
                .show();
    }

    private void changAccount(AccountDataEntry item){
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title("修改账户")
                .inputType(
                        InputType.TYPE_CLASS_TEXT)
                .input(
                        "请输入新账户",
                        "",
                        false,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            }
                        })
                .inputRange(1,5)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        assert dialog.getInputEditText() != null;
                        if(billDao.ChangeAccountName(item.getName(),dialog.getInputEditText().getText().toString())){
                            XToastUtils.success("修改账户成功");
                            refreshLayout.autoRefresh();
                        }else{
                            XToastUtils.error("修改账户失败，该账户已存在");
                        }
                    }
                })
                .show();
    }

    private void viewHolderClick(AccountDataEntry item){
        int focusType = 0;
        String account = item.getName();
        String member = getResources().getString(R.string.unlimited);

        Intent intent = new Intent(getContext(), AccountDetailsActivity.class);
        intent.putExtra("focusType", focusType);
        intent.putExtra("member", member);
        intent.putExtra("account", account);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void initRefreshLayoutListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                accountDataEntries.clear();
                accountDataEntries = billDao.getBalanceByAccount();
                adapter.refresh(accountDataEntries);
                accountSum();

                refreshLayout.finishRefresh();
            }
        });

        //上拉加载
//        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
//            refreshLayout.getLayout().postDelayed(() -> {
//                if (adapter.getItemCount() > 8) {
//                    XToastUtils.toast("数据全部加载完毕");
//                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
//                } else {
//                    adapter.loadMore(datas);
//                    refreshLayout.finishLoadMore();
//                }
//            }, 1000);
//        });
        refreshLayout.autoRefresh();    //第一次进入触发自动刷新，演示效果
    }


//    private void showAccountChangeDialog(AccountDataEntry item) {
//        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
//                //.iconRes(R.drawable.icon_warning)
//                //.title(R.string.tip_warning)
//                .content(R.string.account_change_content)
//                .inputType(
//                        InputType.TYPE_CLASS_TEXT)
//                .input(
//                        getString(R.string.account_change_content),
//                        "",
//                        false,
//                        ((dialog, input) -> XToastUtils.toast(input.toString())))
//                .inputRange(1, 5)
//                .positiveText(R.string.button_confirm)
//                .negativeText(R.string.button_cancel)
//                .onPositive((dialog, which) -> {
//                    assert dialog.getInputEditText() != null;
//                    if(billDao.ChangeAccountName(item.getName(),dialog.getInputEditText().getText().toString())){
//                        XToastUtils.success("修改账户成功");
//                        refreshLayout.autoRefresh();
//                    }else{
//                        XToastUtils.error("修改账户失败，该账户已存在");
//                    }
//                })
//                .cancelable(false)
//                .show();
//    }


    @Override
    public void onResume() {
        super.onResume();

        refreshLayout.autoRefresh();
    }

}
