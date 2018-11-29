package com.tiger.example.common.base

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter

open class BaseActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
    }
}