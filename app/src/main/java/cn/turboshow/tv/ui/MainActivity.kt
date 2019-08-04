package cn.turboshow.tv.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.turboshow.tv.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
