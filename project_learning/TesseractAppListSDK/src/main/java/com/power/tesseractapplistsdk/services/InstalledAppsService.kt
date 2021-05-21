package com.power.tesseractapplistsdk.services

import android.content.pm.PackageManager
import com.power.tesseractapplistsdk.data.InstalledAppData

interface InstalledAppsService {


    fun setPackageData(packageList: PackageManager)

    fun getAppData(): List<InstalledAppData>

}