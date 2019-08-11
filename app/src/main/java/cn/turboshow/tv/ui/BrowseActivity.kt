package cn.turboshow.tv.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.turboshow.tv.R

class BrowseActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BrowseActivity::class.java)
        }
    }
}
