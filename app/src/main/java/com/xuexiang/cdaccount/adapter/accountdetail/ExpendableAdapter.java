/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.layout.ExpandableLayout;

public class ExpendableAdapter extends RecyclerView.Adapter<ExpendableAdapter.LinearViewHolder> {

    private Context mContext;
    private RecyclerView mRecyclerView;

    public ExpendableAdapter(Context context, RecyclerView recyclerView){
        this.mContext = context;
        this.mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ExpendableAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.account_expendable_years,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpendableAdapter.LinearViewHolder holder, int position) {
        holder.maintime.setText(ResUtils.getResources().getString(R.string.item_example_number_year, position + 1));
        holder.subtime.setText("0000");
        holder.total.setText("000");
        holder.income.setText("00");
        holder.outcome.setText("0");
        holder.expandableLayout.setInterpolator(new OvershootInterpolator());
        holder.expandableLayout.setOnExpansionChangedListener((expansion, state) -> {
            if (mRecyclerView != null && state == ExpandableLayout.State.EXPANDING) {
                mRecyclerView.smoothScrollToPosition(position);
            }
            if (holder.ivIndicator != null) {
                holder.ivIndicator.setRotation(expansion * 90);
            }
        });

        int mSelectPosition = -1;
        boolean isSelected = position == mSelectPosition;
        holder.expandableLayout.setExpanded(isSelected, false);

//        WidgetUtils.initRecyclerView(holder.recyclerView);
//        holder.recyclerView.setAdapter(new ExpandableItemAdapter(mContext, holder.recyclerView, DemoDataProvider.getDemoData1()));

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //holder.recyclerView.setAdapter(new ExpendableMonthAdapter(mContext, holder.recyclerView));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem(view, holder.expandableLayout, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView maintime,subtime,income,outcome,total;
        ExpandableLayout expandableLayout ;
        AppCompatImageView ivIndicator ;
        RecyclerView recyclerView;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            maintime = itemView.findViewById(R.id.account_expendable_year_maintime);
            subtime = itemView.findViewById(R.id.account_expendable_year_subtime);
            income = itemView.findViewById(R.id.account_expendable_year_income);
            outcome = itemView.findViewById(R.id.account_expendable_year_outcome);
            total = itemView.findViewById(R.id.account_expendable_year_totalmoney);
            expandableLayout = itemView.findViewById(R.id.expandable_year_layout);
            ivIndicator = itemView.findViewById(R.id.year_indicator);
            recyclerView = itemView.findViewById(R.id.year_expand_recycler_view);
        }
    }

    private void onClickItem(View view, final ExpandableLayout expandableLayout, final int position) {
        int mSelectPosition = -1;
        RecyclerViewHolder holder = (RecyclerViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mSelectPosition);
        if (holder != null) {
            holder.select(R.id.account_expendable_year, false);
            ((ExpandableLayout) holder.findViewById(R.id.expandable_year_layout)).collapse();
        }

        if (position == mSelectPosition) {
            mSelectPosition = -1;
        } else {
            view.setSelected(true);
            expandableLayout.expand();
            mSelectPosition = position;
        }
    }
}
