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

package com.xuexiang.cdaccount.adapter;

import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.utils.DemoDataProvider;
import com.xuexiang.cdaccount.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.layout.ExpandableLayout;

import java.util.Collection;

/**
 * 可伸缩布局适配器
 *
 * @author xuexiang
 * @since 2019-11-22 15:38
 */
public class ExpandableYearAdapter extends BaseRecyclerAdapter<TestItem> {

    private RecyclerView mRecyclerView;
    private Context context;
    private ExpandableMonthAdapter mAdapter;
//    private boolean year_expendable,  month_expendable,  day_expendable;
    boolean isSelected;

    public ExpandableYearAdapter(Context context, RecyclerView recyclerView) {
//        super(data);
        mRecyclerView = recyclerView;
        this.context = context;
//        year_expendable = temp_year_expendable;
//        month_expendable = temp_month_expendable;
//        day_expendable = temp_day_expendable;
    }

    /**
     * 适配的布局
     *
     * @param viewType
     * @return
     */
    @Override
    protected int getItemLayoutId(int viewType) { return R.layout.account_expendable_years; }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position 索引
     * @param item     列表项
     */
    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, TestItem item) {
        ExpandableLayout expandableLayout = holder.findViewById(R.id.expandable_year_layout);
        AppCompatImageView ivIndicator = holder.findViewById(R.id.year_indicator);
        expandableLayout.setInterpolator(new OvershootInterpolator());
        expandableLayout.setOnExpansionChangedListener((expansion, state) -> {
            if (mRecyclerView != null && state == ExpandableLayout.State.EXPANDING) {
                mRecyclerView.smoothScrollToPosition(position);
            }
            if (ivIndicator != null) {
                ivIndicator.setRotation(expansion * 90);
            }
        });

        if(item.getRefresh()){
            if(item.getYear()){
                expandableLayout.setExpanded(true, true);       //expend为true时，初始状态展开
                mSelectPosition = position;
            }else {
//            isSelected = position == mSelectPosition;
                expandableLayout.setExpanded(false, true);
                mSelectPosition = -1;
            }
        }
        else {
//            XToastUtils.toast("点击了:" + mSelectPosition +"he" + position);
            isSelected = position == mSelectPosition;         //false
            expandableLayout.setExpanded(isSelected, true);
        }


        RecyclerView recyclerView = holder.findViewById(R.id.year_expand_recycler_view);
        WidgetUtils.initRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter = new ExpandableMonthAdapter(context, recyclerView, DemoDataProvider.getDemoData1(),item));

//        final RefreshLayout refreshLayout = holder.findViewById(R.id.refreshLayout_year);
//        refreshLayout.setEnableAutoLoadMore(true);
//        //下拉刷新
//        refreshLayout.setOnRefreshListener(refreshLayout12 -> refreshLayout12.getLayout().postDelayed(() -> {
//            mAdapter.refresh(DemoDataProvider.getDemoData());
//            refreshLayout12.finishRefresh();
//            refreshLayout12.resetNoMoreData();//setNoMoreData(false);
//        }, 2000));
//        //上拉加载
//        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> refreshLayout1.getLayout().postDelayed(() -> {
//            if (mAdapter.getItemCount() > 30) {
//                XToastUtils.toast("数据全部加载完毕");
//                refreshLayout1.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
//            } else {
//                mAdapter.loadMore(DemoDataProvider.getDemoData());
//                refreshLayout1.finishLoadMore();
//            }
//        }, 2000));

        holder.select(R.id.account_expendable_year, isSelected);
        holder.text(R.id.account_expendable_year_maintime,ResUtils.getResources().getString(R.string.item_example_number_year, position + 1));
        holder.text(R.id.account_expendable_year_subtime,"0000");
        holder.text(R.id.account_expendable_year_totalmoney,"000");
        holder.text(R.id.account_expendable_year_income,"00");
        holder.text(R.id.account_expendable_year_outcome,"0");
        //holder.text(R.id.tv_content, ResUtils.getResources().getString(R.string.item_example_number_abstract, position + 1));
        holder.click(R.id.account_expendable_year, new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                onClickItem(v, expandableLayout, position);
                XToastUtils.toast("点击了:" + mSelectPosition +"he" + position);
            }
        });



//        if(item.getYear() && item.getMonth() && !item.getDay()){
//            holder.getTextView(R.id.account_expendable_year_maintime).setTextSize(5);
//            holder.getTextView(R.id.account_expendable_year_maintime).setTextColor(0xD2CACA);
//        }

    }

    private void onClickItem(View view, final ExpandableLayout expandableLayout, final int position) {
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
