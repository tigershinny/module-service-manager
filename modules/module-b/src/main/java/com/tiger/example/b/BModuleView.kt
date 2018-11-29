package com.tiger.example.b

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.module_b_view.view.*

class BModuleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs){

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        inflate(context, R.layout.module_b_view, this)
        btnB.setOnClickListener {
            Toast.makeText(context, "Toast From B View", Toast.LENGTH_SHORT).show()
        }
    }

}
