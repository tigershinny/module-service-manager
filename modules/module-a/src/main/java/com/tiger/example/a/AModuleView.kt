package com.tiger.example.a

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.module_a_view.view.*

class AModuleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        inflate(context, R.layout.module_a_view, this)
        btnA.setOnClickListener {
            Toast.makeText(context, "Toast From A View", Toast.LENGTH_SHORT).show()
        }
    }


}
