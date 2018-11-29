package com.tiger.example.wrapper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView

class WrapperActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            text = "Click to module"
            setOnClickListener {
                startActivity(Intent().setClassName(packageName, "com.tiger.example.b.BModuleActivity"))
            }
        })

    }
}
