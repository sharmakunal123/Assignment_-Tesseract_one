package com.power.tesseract.viewmodels

import com.power.tesseractapplistsdk.data.InstalledAppData

class AdapterListData(val installedAppData: InstalledAppData) {

    val appName
        get() = installedAppData.appName

    val clsName
        get() = installedAppData.clsName

    val mainActivity
        get() = installedAppData.mainActivity

    val packageName
        get() = installedAppData.packageName

    val vCode
        get() = installedAppData.versionCode.toString()

    val vName
        get() = installedAppData.versionName

    val iconDrawable
        get() = installedAppData.icon

}