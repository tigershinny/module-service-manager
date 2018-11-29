package com.tiger.example.b

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.tiger.example.common.base.BaseActivity
import com.tiger.example.common.service.IAModuleCalculateService
import com.tiger.example.common.service.IAModuleViewService
import kotlinx.android.synthetic.main.module_b_activity.*


@Route(path = "/moduleB/main")
class BModuleActivity : BaseActivity() {

    @Autowired
    @JvmField var aModuleViewService: IAModuleViewService? = null

    @Autowired
    @JvmField var aModuleCalculateService: IAModuleCalculateService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.module_b_activity)

       aModuleViewService?.provideAModuleView(this, null ).let {
            viewContainer.addView(it)
        }

        val fragment = ARouter.getInstance().build("/moduleA/AModuleFragment").navigation() as Fragment
        fragment.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, it)
            transaction.commit()
        }

        callAMsg.setOnClickListener {
            aModuleCalculateService?.showMsg(this@BModuleActivity)
        }
        callACal.setOnClickListener {
            Toast.makeText(this, "" + aModuleCalculateService?.calculate(23), Toast.LENGTH_SHORT).show()
        }
    }
}
