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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.ExpanableBill.BillDataItem;
import com.xuexiang.cdaccount.R;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.text.DecimalFormat;
import java.util.Collection;

public class ExpandableItemAdapter extends BaseRecyclerAdapter<BillDataItem> {

    private RecyclerView mRecyclerView;
    private Context context;
    private Dialog dialog;

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
        holder.text(R.id.member,item.getBill_Mumber());
        holder.click(R.id.account_detail, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSimpleTipDialog(item, decimalFormat);
            }
        });

        if (item.getBill_TYPE() == 2) {
            holder.text(R.id.category,item.getBill_Account()+"   >   "+item.getBill_toAccount());
            holder.getTextView(R.id.account).setVisibility(View.GONE);
        } else {
            holder.text(R.id.category, item.getBill_SubCategory());
            holder.text(R.id.account, item.getBill_Account());
        }

        switch (item.getBill_TYPE()){
            case 0:
                holder.getTextView(R.id.money).setTextColor(mRecyclerView.getContext().getResources().getColor(R.color.app_color_theme_1));
                break;
            case 1:
                holder.getTextView(R.id.money).setTextColor(mRecyclerView.getContext().getResources().getColor(R.color.app_color_theme_5));
                break;
            default:
                holder.getTextView(R.id.money).setTextColor(mRecyclerView.getContext().getResources().getColor(R.color.black));
                break;
        }
    }

    /**
     * 对话框
     */
    @SuppressLint("SetTextI18n")
    private void showSimpleTipDialog(BillDataItem item, DecimalFormat decimalFormat) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.account_dialog,null);

        TextView money = view.findViewById(R.id.account_dialog_money);
        TextView time = view.findViewById(R.id.account_dialog_time);
        TextView category = view.findViewById(R.id.account_dialog_category);
        TextView account = view.findViewById(R.id.account_dialog_account);
        TextView member = view.findViewById(R.id.account_dialog_member);
        TextView remark = view.findViewById(R.id.account_dialog_remark);

        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                .customView(view, true)
                .positiveText("确定")
                .autoDismiss(false);
        account.setText(item.getBill_Account());
        money.setText(decimalFormat.format(item.getBill_Money()));
        time.setText(item.getYear()+"-"+item.getMonth()+"-"+item.getDay()+"    "+item.getTime());
        category.setText(item.getBill_SubCategory());
        member.setText(item.getBill_Mumber());
        if(item.getBill_Remark().equals("")){
            remark.setText("无");
        }else{
            remark.setText(item.getBill_Remark());
        }

        materialDialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        switch (item.getBill_TYPE()){
            case 0:
                materialDialog.title("支出");
                break;
            case 1:
                materialDialog.title("收入");
                break;
            default:
                materialDialog.title("转账");
                category.setText("无");
                account.setText(item.getBill_Account()+"   >   "+item.getBill_toAccount());
                break;
        }
    materialDialog.show();
    }

}
