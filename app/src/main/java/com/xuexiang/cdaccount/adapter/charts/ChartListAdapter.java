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

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.database.ChartDataEntry;
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

    public ChartListAdapter(Context context, RecyclerView recyclerView, Collection<ChartDataEntry> data) {
        super(data);
    }


    /**
     * 适配的布局
     *
     * @param viewType 控件类型
     * @return  控件ID
     */
    @Override
    protected int getItemLayoutId(int viewType) { return R.layout.adapter_chart_list; }


    /**
     * 绑定数据
     *
     * @param holder  ViewHolder
     * @param position 索引
     * @param item     列表项
     */
    @SuppressLint("DefaultLocale")
    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, ChartDataEntry item) {
        ProgressBar progressBar = holder.findViewById(R.id.adapter_chart_list_progressbar);
        int progress = 0;
        if(item.getSumMoney() > 0) {
            progress = (int) (100 * item.getDataMoney() / item.getSumMoney());
        }
        progressBar.setProgress(progress);

        holder.text(R.id.adapter_chart_list_position, Integer.toString(position));
        holder.text(R.id.adapter_chart_list_title, item.getDataName());
        holder.text(R.id.adapter_chart_list_money, String.format("%.2f", item.dataMoney));

    }


}
