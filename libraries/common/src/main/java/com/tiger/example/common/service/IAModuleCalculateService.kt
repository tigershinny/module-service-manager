package com.tiger.example.common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IAModuleCalculateService: IProvider {

    fun showMsg(context: Context)

    fun calculate(input: Int): Int

}