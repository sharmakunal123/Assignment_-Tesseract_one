package com.power.tesseractapplistsdk.data

import android.graphics.drawable.Drawable

data class InstalledAppData(
    var appName: String? = null,
    var packageName: String? = null,
    var icon: Drawable? = null,
    var mainActivity: String? = null,
    var clsName: String? = null,
    var versionCode: Int? = null,
    var versionName: String? = null,
)