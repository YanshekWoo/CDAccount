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

package com.xuexiang.cdaccount.fragment.docs;

import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.core.BaseFragment;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;

/**
 * @author xuexiang
 * @since 2019-10-30 00:02
 */
@Page(anim = CoreAnim.none)
public class PrivacyFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_privacy;
    }

    @Override
    protected void initViews() {

    }
}
