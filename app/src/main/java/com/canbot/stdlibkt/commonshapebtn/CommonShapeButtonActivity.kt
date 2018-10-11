package com.canbot.stdlibkt.commonshapebtn

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.canbot.stdlibkt.databinding.ActivityCommonShapeButtonBinding

class CommonShapeButtonActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                ActivityCommonShapeButtonBinding.inflate(layoutInflater).apply { setContentView(root) }
        }
}
