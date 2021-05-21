package com.power.tesseractapplistsdk

import android.content.pm.PackageManager
import com.power.tesseractapplistsdk.impls.InstalledAppsImpl
import com.power.tesseractapplistsdk.services.InstalledAppsService

/**
 * Entry Base class for module
 */
object AppListBaseLayer {

    lateinit var mInstallAppService: InstalledAppsService
    // lateinit var context: Context
    //context
    /**
     * Initialize Package
     */
    fun initModel(packageList: PackageManager) {
        mInstallAppService = InstalledAppsImpl()
        mInstallAppService.setPackageData(packageList)
    }

    fun getAppData() = mInstallAppService.getAppData()
}