package com.power.tesseract

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.power.tesseract.ui.main.MainFragment
import com.power.tesseractapplistsdk.AppListBaseLayer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val packages = packageManager
        AppListBaseLayer.initModel(packages)
        val list = AppListBaseLayer.getAppData()
        for (values in list) {
            Log.e("App Name Display", "" + values.appName)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

//    private fun getInstalledApps(getSysPackages: Boolean) {
//        val res: ArrayList<InstalledAppData> = ArrayList<InstalledAppData>()
//        val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)
//
//        for (i in packs.indices) {
//            val p = packs[i]
//            if (p.versionName == null) {
//                continue
//            }
//            Log.e("appInfo:", p.applicationInfo.loadLabel(packageManager).toString())
//            Log.e("appInfo:", p.packageName)
//            Log.e("appInfo:", p.versionName)
//            Log.e("appInfo:", "" + p.versionCode)
////            newInfo.appName =
////            newInfo.pname = p.packageName
////            newInfo.versionName = p.versionName
////            newInfo.versionCode = p.versionCode
////            newInfo.icon = p.applicationInfo.loadIcon(packageManager)
////            res.add(newInfo)
//        }
//        return res
//    }
}