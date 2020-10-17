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

package com.xuexiang.cdaccount.adapter.charts;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.AccountDetailsActivity;
import com.xuexiang.cdaccount.database.ChartDataEntry;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

import java.util.Collection;

/**
 * 可伸缩布局适配器
 *
 * @author xuexiang
 * @since 2019-11-22 15:38
 */
public class ChartListAdapter extends BaseRecyclerAdapter<ChartDataEntry> {

    private RecyclerView mRecyclerView;
    private Context context;

    public ChartListAdapter(Context context, RecyclerView recyclerView, Collection<ChartDataEntry> data) {
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
    protected int getItemLayoutId(int viewType) { return R.layout.adapter_chart_list; }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position 索引
     * @param item     列表项
     */
    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, ChartDataEntry item) {
        ProgressBar progressBar = holder.findViewById(R.id.adapter_chart_list_progressbar);

        progressBar.setProgress(58);
        holder.text(R.id.adapter_chart_list_position,Integer.toString(position));
        holder.text(R.id.adapter_chart_list_title,item.getDataName());
        holder.text(R.id.adapter_chart_list_money,Double.toString(item.dataMoney));


        holder.click(R.id.adapter_chart_list_card, new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                XToastUtils.toast("点击了:" + mSelectPosition);
                Intent intent = new Intent(context, AccountDetailsActivity.class);
                context.startActivity(intent);
            }
        });
    }

}
