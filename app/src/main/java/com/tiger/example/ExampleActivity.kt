package com.tiger.example

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.tiger.example.common.base.BaseActivity
import kotlinx.android.synthetic.main.app_activity_example.*

class ExampleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_example)
        toAModule.setOnClickListener {
            ARouter.getInstance().build("/moduleA/main").navigation()
        }
        toBModule.setOnClickListener{
            //Test code. It would be better to use router.
            startActivity(Intent().setClassName(this@ExampleActivity, "com.tiger.example.b.BModuleActivity"))
        }
    }

}
