package com.power.tesseractapplistsdk.impls

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.power.tesseractapplistsdk.data.InstalledAppData
import com.power.tesseractapplistsdk.services.InstalledAppsService


class InstalledAppsImpl : InstalledAppsService {

    private var mPackageList: PackageManager? = null

    /**
     * Update Package Manager Object
     */
    override fun setPackageData(packageList: PackageManager) {
        mPackageList = packageList
    }

    /**
     * Returns List of Packages Detail
     */
    @SuppressLint("QueryPermissionsNeeded")
    override fun getAppData(): List<InstalledAppData> {
        var responseList = mutableListOf<InstalledAppData>()
        mPackageList?.apply {
            val packs: List<PackageInfo> = getInstalledPackages(0)
            val localListData = mutableListOf<InstalledAppData>()
            for (packageInfo in packs) {
                if (!isSystemPackage(packageInfo)) {
                    val installedApps = InstalledAppData(
                        appName = packageInfo.applicationInfo.loadLabel(mPackageList!!).toString(),
                        packageName = packageInfo.packageName,
                        versionCode = packageInfo.versionCode,
                        versionName = packageInfo.versionName,
                        clsName = packageInfo.applicationInfo.className
                    )
                    localListData.add(installedApps)
                }
            }
            responseList =
                localListData.sortedWith(compareBy { it.appName }) as MutableList<InstalledAppData>
        }
        return responseList
    }

    /**
     * Return false is application is installed by user
     */
    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

}