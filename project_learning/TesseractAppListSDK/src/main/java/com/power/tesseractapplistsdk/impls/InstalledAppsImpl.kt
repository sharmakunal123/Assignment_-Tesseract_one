package com.power.tesseractapplistsdk.impls

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.power.tesseractapplistsdk.data.InstalledAppData
import com.power.tesseractapplistsdk.services.InstalledAppsService


class InstalledAppsImpl : InstalledAppsService {

    private var mPackageList: PackageManager? = null

    override fun setPackageData(packageList: PackageManager) {
        mPackageList = packageList
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun getAppData(): List<InstalledAppData> {
        var responseList = mutableListOf<InstalledAppData>()

        // TODO Handle Below code with Scope Function..
        val packs: List<PackageInfo> = mPackageList!!.getInstalledPackages(0)

        val localListData = mutableListOf<InstalledAppData>()
        for (packageInfo in packs) {
                if (packageInfo.versionName == null) {
                    continue
                }
            val packageName = packageInfo.packageName
            val installedApps = InstalledAppData(
                appName = packageInfo.applicationInfo.loadLabel(mPackageList!!).toString(),
                packageName = packageInfo.packageName,
                versionCode = packageInfo.versionCode,
                versionName = packageInfo.versionName,
                clsName = packageInfo.applicationInfo.className
            )
            localListData.add(installedApps)
        }
        responseList =
            localListData.sortedWith(compareBy { it.appName }) as MutableList<InstalledAppData>

        return responseList
    }


    // App name
    // String appname = packageManager.getApplicationLabel(packageName).toString()
//                val appName =
//                    requireActivity().packageManager.getApplicationLabel(packageInfo).toString()
//                Log.d("Resule::", "AppNames:$appName")
//                requireActivity().packageManager.getPackageInfo(packageName, 0).apply {
//                    Log.d("Resule::", "versionCode:${versionCode})")
//                    Log.d("Resule::", "versionNames:${versionName}")
//                    Log.d("Resule::", "App Names Names:${this.applicationInfo.className}")
//                }
//                val icon: Drawable = requireContext().packageManager.getApplicationIcon(packageName)

    // List<ApplicationInfo> apps = getPackageManager().getInstalledPackages(0);
    //  val apps: List<ApplicationInfo> = getPackageManager().getInstalledApplications(0)
    // for (app in apps) {
    // if (app.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) > 0) {
    //      // It is a system app
    //  } else {
    //  // It is installed by the user
    // }
    //  }
}