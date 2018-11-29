package com.tiger.example.a

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.tiger.example.common.base.BaseActivity
import kotlinx.android.synthetic.main.module_a_activity.*

@Route(path = "/moduleA/main")
class AModuleActivity : BaseActivity() {

    @Autowired
    @JvmField var aModuleViewService: AModuleViewService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.module_a_activity)

        aModuleViewService?.provideAModuleView(this, null)?.let {
            viewContainer.addView(it)
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, AModuleFragment())
        transaction.commit()

    }
}
