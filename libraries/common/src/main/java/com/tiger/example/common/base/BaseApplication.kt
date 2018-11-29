package com.tiger.example.common.base

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.tiger.example.common.BuildConfig

open class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()
        }
        ARouter.init(this)

    }
}
