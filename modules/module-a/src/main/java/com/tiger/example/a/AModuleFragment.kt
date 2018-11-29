package com.tiger.example.a

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/moduleA/AModuleFragment")
class AModuleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            text = "A Fragment"
            setTextColor(Color.parseColor("#FF0000FF"))
        }
    }

}