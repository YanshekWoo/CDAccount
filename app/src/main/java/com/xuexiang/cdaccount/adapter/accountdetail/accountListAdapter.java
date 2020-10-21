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

package com.xuexiang.cdaccount.adapter.accountdetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.AccountDetailsActivity;
import com.xuexiang.cdaccount.database.AccountDataEntry;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.Collection;

import static com.xuexiang.xutil.XUtil.getResources;

/**
 * 可伸缩布局适配器
 *
 * @author xuexiang
 * @since 2019-11-22 15:38
 */
public class accountListAdapter extends BaseRecyclerAdapter<AccountDataEntry> {

    private RecyclerView mRecyclerView;
    private final Context context;


    public accountListAdapter(Context context, RecyclerView recyclerView, Collection<AccountDataEntry> data) {
        super(data);
        this.mRecyclerView = recyclerView;
        this.context = context;
    }


    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    protected int getItemLayoutId(int viewType) { return R.layout.adapter_account_list_item; }


    /**
     * 绑定数据
     *
     * @param holder
     * @param position 索引
     * @param item     列表项
     */
    @SuppressLint("DefaultLocale")
    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, AccountDataEntry item) {

        holder.text(R.id.account_name, item.getName());
        holder.text(R.id.account_income, String.format("%.2f", item.getInMoney()));
        holder.text(R.id.account_outcome, String.format("%.2f", item.getOutMoney()));
        holder.text(R.id.account_money, String.format("%.2f", item.getInMoney()-item.getOutMoney()));

        holder.click(R.id.adapter_chart_list_card, new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                int focusType = 1;
                String account = item.getName();
                String member = getResources().getString(R.string.unlimited);

                Intent intent = new Intent(context, AccountDetailsActivity.class);
                intent.putExtra("focusType", focusType);
                intent.putExtra("member", member);
                intent.putExtra("account", account);
                context.startActivity(intent);
            }
        });
    }

}
