package com.power.tesseract.utils

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val APP_SETTINGS = "list_packages_pref"

    // properties
    private const val KEY_LIST_PACKAGES = "key_packages"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getListOfPackages(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_LIST_PACKAGES, null)
    }

    fun setListOfPackages(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_LIST_PACKAGES, newValue)
        editor.apply()
    }
}
