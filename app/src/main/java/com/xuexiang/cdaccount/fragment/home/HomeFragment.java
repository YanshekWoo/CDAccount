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

package com.xuexiang.cdaccount.fragment.home;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.AddActivity;
import com.xuexiang.cdaccount.adapter.record.RecordAdapter;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowImageView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author Hao Chen
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class HomeFragment extends BaseFragment {

    private RecyclerView mRvRecord;
    private ShadowImageView mIbAdd;
    private String[] mDate,mMessage;

    private TextView mTvIn,mTvOut;
    private double mAmountIn;
    private double mAmountOut;


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
        return R.layout.fragment_home;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        //加载数据
        loadData();
        //小计
        mTvIn = findViewById(R.id.tv_in);
        mTvOut = findViewById(R.id.tv_out);
        mTvIn.setText(String.valueOf(mAmountIn));
        mTvOut.setText(String.valueOf(mAmountOut));
        //流水显示
        mRvRecord = findViewById(R.id.record);
        //倒序
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        layout.setReverseLayout(true);//列表翻转
        mRvRecord.setLayoutManager(layout);
        RecordAdapter adapter = new RecordAdapter(getContext(), mDate, mMessage);
        mRvRecord.setAdapter(adapter);
        mRvRecord.scrollToPosition(0);


        //记账
        mIbAdd = findViewById(R.id.ib_add);
        mIbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        RecordAdapter adapter = new RecordAdapter(getContext(), mDate, mMessage);
        mRvRecord.setAdapter(adapter);
        mRvRecord.scrollToPosition(0);
    }


    private void loadData(){
        mDate = new String[]{"2020/10/1","2020/10/2","2020/10/3","2020/10/4","2020/10/5","2020/10/6","2020/10/7","2020/10/8","2020/10/9","2020/10/10",};
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String str = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);
        mMessage = new String[]{str,str,str,str,str,str,str,str,str,str,};
        mAmountIn = 100.00;
        mAmountOut = 100.00;
    }
}
