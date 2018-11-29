package com.tiger.example.common.service

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.alibaba.android.arouter.facade.template.IProvider

interface IAModuleViewService: IProvider{
    fun provideAModuleView(context: Context, attrs: AttributeSet?): View
}