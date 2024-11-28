package com.example.tvapplicationpaging3

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Loads [MainFragment].
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private var currentFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            currentFragment = MainFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, currentFragment as Fragment)
                .commitNow()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (currentFragment is SubMainFragment) {
          return  (currentFragment as SubMainFragment).onKeyDown(keyCode)
        }
        return super.onKeyDown(keyCode, event)
    }
}