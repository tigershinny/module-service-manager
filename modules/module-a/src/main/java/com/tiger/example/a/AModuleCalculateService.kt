package com.tiger.example.a

import android.content.Context
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.tiger.example.common.service.IAModuleCalculateService

@Route(path = "/moduleA/AModuleCalculateService", name = "Module A Calculate Service")
class AModuleCalculateService : IAModuleCalculateService {

    override fun showMsg(context: Context) {
        Toast.makeText(context, "Toast From A Module", Toast.LENGTH_SHORT).show()
    }

    override fun calculate(input: Int): Int {
        return input * 70 / 100
    }

    override fun init(context: Context?) {
        //do nothing
    }
}