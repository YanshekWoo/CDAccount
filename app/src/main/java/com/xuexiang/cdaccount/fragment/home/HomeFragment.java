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

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.activity.AddBillActivity;
import com.xuexiang.cdaccount.adapter.record.RecordAdapter;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.cdaccount.somethingDao.Dao.BillDao;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowImageView;
import com.xuexiang.xui.widget.textview.autofit.AutoFitTextView;
import com.xuexiang.xutil.app.ActivityUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Hao Chen
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class HomeFragment extends BaseFragment {

    private RecyclerView mRvRecord;
    private ShadowImageView mIbAdd;
    private List<String> mRecentDate, mRecentInfo;
    private List<Integer>mRecentType;

    private AutoFitTextView  mTvIn,mTvOut;
    private String mAmountIn, mAmountOut;
    private BillDao mDataBaseHelper;


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
        mDataBaseHelper = new BillDao(getContext());

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
        RecordAdapter adapter = new RecordAdapter(getContext(), mRecentDate, mRecentInfo, mRecentType);
        mRvRecord.setAdapter(adapter);
        mRvRecord.scrollToPosition(0);


        //记账
        mIbAdd = findViewById(R.id.ib_add);
        mIbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), AddBillActivity.class);
//                startActivity(intent);
                ActivityUtils.startActivity(AddBillActivity.class);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("---Home---","Resume");
        loadData();
        RecordAdapter adapter = new RecordAdapter(getContext(), mRecentDate, mRecentInfo, mRecentType);
        mRvRecord.setAdapter(adapter);
        mRvRecord.scrollToPosition(0);
        mTvIn.setText((String.valueOf(mAmountIn)));
        mTvOut.setText((String.valueOf(mAmountOut)));
    }


    private void loadData(){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        mAmountIn = decimalFormat.format(mDataBaseHelper.QueryMonthIncome());
        mAmountOut = decimalFormat.format(mDataBaseHelper.QueryMonthpay());
        mRecentInfo = mDataBaseHelper.GetRecentInformation();
        mRecentDate = mDataBaseHelper.GetRecentDate();
        mRecentType = mDataBaseHelper.GetRecentIO();

    }
}
