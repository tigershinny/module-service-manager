package com.tiger.example.a

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.tiger.example.common.service.IAModuleViewService

@Route(path = "/moduleA/AModuleViewService", name ="provide view")
class AModuleViewService : IAModuleViewService {
    override fun provideAModuleView(context: Context, attrs: AttributeSet?): View {
        return AModuleView(context, attrs)
    }


    override fun init(context: Context?) {
        //do nothing for now
    }
}