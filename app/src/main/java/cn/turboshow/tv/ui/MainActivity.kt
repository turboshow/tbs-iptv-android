package cn.turboshow.tv.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.turboshow.tv.R
import cn.turboshow.tv.service.TBSService
import org.fourthline.cling.android.AndroidUpnpService

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        startService(TBSService.newIntent(this))
        startService(Intent(this, AndroidUpnpService::class.java))
    }
}
