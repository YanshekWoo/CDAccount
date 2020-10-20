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

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.AccountDetailsActivity;
import com.xuexiang.cdaccount.adapter.base.delegate.SimpleDelegateAdapter;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;

/**
 * @author Tingwei Wen
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class AccountFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    ImageView img;

    private SimpleDelegateAdapter<String> adapter;
    private ArrayList<String> datas;
    private Context TrendingFragment;

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
    @Override
    protected void initViews() {
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(Objects.requireNonNull(getContext()));
        recyclerView.setLayoutManager(virtualLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);
        datas = new ArrayList<String>();
        for(int i = 0; i <15 ;i++){
            datas.add("" + i);
        }


        adapter = new SimpleDelegateAdapter<String>(R.layout.adapter_account_list_item,new LinearLayoutHelper()) {
            @Override
            protected void bindData(@NonNull RecyclerViewHolder holder, int position, String item) {
                holder.text(R.id.account_name,"银行卡");
                holder.text(R.id.account_money,datas.get(position));
                holder.text(R.id.account_income,"100.00");
                holder.text(R.id.account_outcome,"000.00");
                holder.click(R.id.account_card,view -> click(item));
            }
        };

        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.addAdapter(adapter);

        recyclerView.setAdapter(delegateAdapter);

        img = findViewById(R.id.account_add);
        img.setOnClickListener(view -> {
            showInputDialog();
            //Toast.makeText(getContext(),"温腿 ",Toast.LENGTH_SHORT).show();
        });

        accountInitListeners();
    }

    protected void accountInitListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.getLayout().postDelayed(() -> {
                adapter.refresh(datas);
                refreshLayout.finishRefresh();
            }, 1000);
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
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    public void click(String item){
        String expandType = "年";
        String account = item;
        String member = getResources().getString(R.string.unlimited);

        Intent intent = new Intent(getContext(), AccountDetailsActivity.class);
        intent.putExtra("expandType", expandType);
        intent.putExtra("member", member);
        intent.putExtra("account", account);
        startActivity(intent);
    }

    private void showInputDialog() {
        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                //.iconRes(R.drawable.icon_warning)
                //.title(R.string.tip_warning)
                .content(R.string.account_content)
                .inputType(
                        InputType.TYPE_CLASS_TEXT)
                .input(
                        getString(R.string.account_content),
                        "",
                        false,
                        ((dialog, input) -> XToastUtils.toast(input.toString())))
                .inputRange(1, 10)
                .positiveText(R.string.button_confirm)
                .negativeText(R.string.button_cancel)
                .onPositive((dialog, which) -> {
                    assert dialog.getInputEditText() != null;
                    XToastUtils.toast("你输入了:" + dialog.getInputEditText().getText().toString());
                })
                .cancelable(false)
                .show();
    }


//    private void showCustomDialog() {
//        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
//                .customView(R.layout.add_account, true)
//                .title("新建账户")
//                .positiveText(R.string.button_confirm)
//                .negativeText(R.string.button_cancel)
//                .cancelable(false)
//                .show();
//
//    }
}
