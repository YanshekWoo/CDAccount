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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.ExpanableBill.BillDataItem;
import com.xuexiang.cdaccount.R;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.text.DecimalFormat;
import java.util.Collection;

public class ExpandableItemAdapter extends BaseRecyclerAdapter<BillDataItem> {

    private RecyclerView mRecyclerView;
    private Context context;

    public ExpandableItemAdapter(Context context, RecyclerView recyclerView,Collection<BillDataItem> data) {
        super(data);
        mRecyclerView = recyclerView;
        this.context = context;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.account_expendable_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, BillDataItem item) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.text(R.id.date, item.getTime());
        holder.text(R.id.money,decimalFormat.format(item.getBill_Money()));
        holder.text(R.id.category,item.getBill_SubCategory());
        holder.text(R.id.account,item.getBill_Account());
        holder.text(R.id.member,item.getBill_Mumber());
        holder.click(R.id.account_detail, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSimpleTipDialog();
            }
        });
    }

    /**
     * 简单的提示性对话框
     */
    private void showSimpleTipDialog() {
        new MaterialDialog.Builder(context)
                //.iconRes(R.drawable.icon_tip)
                .title(R.string.account_details_title)
                .content(R.string.account_details_content)
                .positiveText(R.string.button_confirm)
                .show();
    }
}
