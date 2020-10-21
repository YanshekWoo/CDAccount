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

package com.xuexiang.cdaccount.adapter.record;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cxd.chatview.moudle.ChatView;
import com.xuexiang.cdaccount.R;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mRecentDate, mRecentInfo;
    private List<Integer> mRecentType;
    private int mLength;

    public enum ItemType {
        OUT, IN, END
    }

    public RecordAdapter(Context context, List<String> Date, List<String> Info, List<Integer> Type) {
        this.mContext = context;
        this.mRecentInfo = Info;
        this.mRecentDate = Date;
        this.mRecentType = Type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ItemType.OUT.ordinal()) {
            return new RecordViewHolder1(LayoutInflater.from(mContext).inflate(R.layout.layout_record_out, parent, false));
        } else if (viewType == ItemType.IN.ordinal()) {
            return new RecordViewHolder2(LayoutInflater.from(mContext).inflate(R.layout.layout_record_in, parent, false));
        } else if (viewType == ItemType.END.ordinal()) {
            return new RecordViewHolder3(LayoutInflater.from(mContext).inflate(R.layout.layout_record_end, parent, false));
        }
        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String Date = "", Message = "";

        if (position < mRecentDate.size()) {
            Date = mRecentDate.get(position);
            Log.d("--RecordAdapter--", String.valueOf(position) + Date);
            Message = mRecentInfo.get(position);
        }

        boolean IsTimeHide = false;
//        if (position < mRecentDate.size() - 1 && mRecentDate.get(position).equals(mRecentDate.get(position + 1))) {
//            IsTimeHide = true;
//        } else {
//            IsTimeHide = false;
//        }

        if (holder instanceof RecordViewHolder1) {
            if (mRecentDate.size() == 0) {
                ((RecordViewHolder1) holder).time.setHeight(0);
                ((RecordViewHolder1) holder).message.setText("欢迎来到《天龙记账》，点击上方的“+”即可开始记账，我会在这里告诉你最近的账目");
            } else {
                ((RecordViewHolder1) holder).time.setText(Date);
                if (IsTimeHide) {
                    ((RecordViewHolder1) holder).time.setHeight(0);
                }
                ((RecordViewHolder1) holder).message.setText(Message);
            }
        } else if (holder instanceof RecordViewHolder2) {
            ((RecordViewHolder2) holder).time.setText(Date);
            if (IsTimeHide) {
                ((RecordViewHolder2) holder).time.setHeight(0);
            }
            ((RecordViewHolder2) holder).message.setText(Message);
        } else if (holder instanceof RecordViewHolder3) ;

    }

    @Override
    public int getItemCount() {
        return mRecentDate.size() + 1;       //至多展示20条数据，加一条末尾提示

    }

    @Override
    public int getItemViewType(int position) {
        if (position < mRecentDate.size()) {
            return (mRecentType.get(position));
        } else if (mRecentDate.size() == 0) {
            return ItemType.OUT.ordinal();
        } else {
            return ItemType.END.ordinal();
        }
    }

    public static class RecordViewHolder1 extends RecyclerView.ViewHolder {
        private TextView time, message;
        private ChatView chatView;

        public RecordViewHolder1(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_time);
            message = itemView.findViewById(R.id.tv_record);
            chatView = itemView.findViewById(R.id.chatview);
        }
    }

    public static class RecordViewHolder2 extends RecyclerView.ViewHolder {
        private TextView time, message;
        private ChatView chatView;

        public RecordViewHolder2(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_time);
            message = itemView.findViewById(R.id.tv_record);
            chatView = itemView.findViewById(R.id.chatview);
        }
    }

    public static class RecordViewHolder3 extends RecyclerView.ViewHolder {

        public RecordViewHolder3(@NonNull View itemView) {
            super(itemView);

        }
    }
}
