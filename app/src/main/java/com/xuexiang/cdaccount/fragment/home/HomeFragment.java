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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.adapter.record.RecordAdapter;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hao Chen
 * @since 2020-09-26 20:46
 */
@Page(anim = CoreAnim.none)
public class HomeFragment extends BaseFragment {

    private RecyclerView mRvRecord;

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


        String[] data = new String[]{"2020/10/2", "2020/10/3"};

        //流水
        mRvRecord = findViewById(R.id.record);
        mRvRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvRecord.setAdapter(new RecordAdapter(getContext(), data));


    }
}
