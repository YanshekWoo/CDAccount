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

import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.ExpanableBill.BillDataDay;
import com.xuexiang.cdaccount.R;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.layout.ExpandableLayout;

import java.text.DecimalFormat;
import java.util.Collection;

/**
 * 可伸缩布局适配器
 *
 * @author xuexiang
 * @since 2019-11-22 15:38
 */
public class ExpandableDayAdapter extends BaseRecyclerAdapter<BillDataDay> {

    private RecyclerView mRecyclerView;
    private Context context;
    boolean isSelected;
//    TestItem t;

    public ExpandableDayAdapter(Context context, RecyclerView recyclerView, Collection<BillDataDay> data) {
        super(data);
        mRecyclerView = recyclerView;
        this.context = context;
//        t = it;
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    protected int getItemLayoutId(int viewType) { return R.layout.account_expendable_days; }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position 索引
     * @param item     列表项
     */
    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, BillDataDay item) {
        ExpandableLayout expandableLayout = holder.findViewById(R.id.expandable_day_layout);
        AppCompatImageView ivIndicator = holder.findViewById(R.id.day_indicator);
        expandableLayout.setInterpolator(new OvershootInterpolator());
        expandableLayout.setOnExpansionChangedListener((expansion, state) -> {
            if (mRecyclerView != null && state == ExpandableLayout.State.EXPANDING) {
                mRecyclerView.smoothScrollToPosition(position);
            }
            if (ivIndicator != null) {
                ivIndicator.setRotation(expansion * 90);
            }
        });

//        if(item.ismExpanded()){
////            if(t.getDay()){
////                XToastUtils.toast("点击了:" + mSelectPosition +"he" + position);
//                expandableLayout.setExpanded(true, true);       //expend为true时，初始状态展开
//                mSelectPosition = position;
//                item.setmExpanded(false);
////            }else {
//////            isSelected = position == mSelectPosition;
//////                XToastUtils.toast("点击了:");
////                expandableLayout.setExpanded(false, true);
////                mSelectPosition = -1;
////            }
//        }
//        else {
////            XToastUtils.toast("点击了:" + mSelectPosition +"he" + position);
//            isSelected = position == mSelectPosition;         //false
//            expandableLayout.setExpanded(isSelected, true);
//        }
        if(position == 0)
        {
            expandableLayout.setExpanded(true,true);
            mSelectPosition = position;
        }

//        boolean isSelected = position == mSelectPosition;
//        expandableLayout.setExpanded(isSelected, false);
        
        RecyclerView recyclerView = holder.findViewById(R.id.day_expand_recycler_view);
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(new ExpandableItemAdapter(context, recyclerView, item.getmBillDataItemList()));

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.select(R.id.account_expendable_day, isSelected);
        holder.text(R.id.account_expendable_day_maintime, item.getmDay()+"日");
        holder.text(R.id.account_expendable_day_subtime,item.getmYear()+"."+item.getmMonth());
        holder.text(R.id.account_expendable_day_totalmoney,decimalFormat.format(item.getmDayIncome()-item.getmDayOutcome()));
        holder.text(R.id.account_expendable_day_income, decimalFormat.format(item.getmDayIncome()));
        holder.text(R.id.account_expendable_day_outcome,decimalFormat.format(item.getmDayOutcome()));
        //holder.text(R.id.tv_content, ResUtils.getResources().getString(R.string.item_example_number_abstract, position + 1));
        holder.click(R.id.account_expendable_day, new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                onClickItem(v, expandableLayout, position);
            }
        });


        //设置选择天的卡片样式
        if(item.ismDaySelected()){
            holder.getTextView(R.id.account_expendable_day_maintime).setTextSize(20);
            holder.getTextView(R.id.account_expendable_day_totalmoney).setTextSize(20);
//            holder.getImageView(R.id.month_indicator).setMaxHeight(10);
            holder.getTextView(R.id.account_expendable_day_subtime).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.account_expendable_day_income).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.account_expendable_day_outcome).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.account_expendable_day_text_income).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.account_expendable_day_text_outcome).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.account_expendable_day_maintime).setTextColor(context.getResources().getColor(R.color.black));
            holder.getTextView(R.id.account_expendable_day_totalmoney).setTextColor(context.getResources().getColor(R.color.black));
        }
        else {
            holder.getTextView(R.id.account_expendable_day_maintime).setTextSize(12);
            holder.getTextView(R.id.account_expendable_day_totalmoney).setTextSize(12);
//            holder.getImageView(R.id.month_indicator).setMaxHeight(10);
            holder.getTextView(R.id.account_expendable_day_subtime).setVisibility(View.GONE);
            holder.getTextView(R.id.account_expendable_day_income).setVisibility(View.GONE);
            holder.getTextView(R.id.account_expendable_day_outcome).setVisibility(View.GONE);
            holder.getTextView(R.id.account_expendable_day_text_income).setVisibility(View.GONE);
            holder.getTextView(R.id.account_expendable_day_text_outcome).setVisibility(View.GONE);
            holder.getTextView(R.id.account_expendable_day_maintime).setTextColor(context.getResources().getColor(R.color.grey));
            holder.getTextView(R.id.account_expendable_day_totalmoney).setTextColor(context.getResources().getColor(R.color.grey));
        }


    }

    private void onClickItem(View view, final ExpandableLayout expandableLayout, final int position) {
        RecyclerViewHolder holder = (RecyclerViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mSelectPosition);
        if (holder != null) {
            holder.select(R.id.account_expendable_day, false);
            ((ExpandableLayout) holder.findViewById(R.id.expandable_day_layout)).collapse();
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
