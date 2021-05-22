package com.power.tesseract

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.power.tesseract.ui.main.MainFragment
import com.power.tesseractapplistsdk.AppListBaseLayer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initModule()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .addToBackStack(MainFragment.newInstance().tag)
                .commit()
        }
    }

    /**
     * Init Package Module
     */
    private fun initModule() {
        val packages = packageManager
        AppListBaseLayer.initModel(packages)
    }
    
    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments.count()
        if (fragmentList == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }

    }

}