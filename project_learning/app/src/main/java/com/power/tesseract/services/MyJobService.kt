package com.power.tesseract.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.power.tesseract.R
import com.power.tesseract.utils.Preferences
import com.power.tesseractapplistsdk.AppListBaseLayer
import com.power.tesseractapplistsdk.data.InstalledAppData


class MyJobService(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        const val NOTIFICATION_KEY = 1
    }

    override fun doWork(): Result {
        appContext.let {
            val data = Preferences.getListOfPackages(it)
            val gson = Gson()
            val userListType = object :
                TypeToken<ArrayList<String>?>() {}.type
            Log.d("", userListType.toString())
            val userArray: ArrayList<String> =
                gson.fromJson(data, userListType)
            val list = AppListBaseLayer.getAppData()
            val prefSize = userArray.size
            val currentSize = list.size
            if (currentSize > prefSize) {
                handleNotificaiton(
                    appContext.getString(R.string.app_name),
                    "${currentSize - prefSize} Application Installed"
                )
            } else if (currentSize < prefSize) {
                handleNotificaiton(
                    appContext.getString(R.string.app_name),
                    "${prefSize - currentSize} Application Deleted"
                )
            }
            updatePreferencesData(gson, list)
        }
        return Result.success()
    }

    /**
     * Update New Package detail to Preferences.
     */
    private fun updatePreferencesData(gson: Gson, listOfPackages: List<InstalledAppData>) {
        val mList = arrayListOf<String>()
        for (values in listOfPackages) {
            values.packageName?.let {
                mList.add(it)
            }
        }
        val packJsonList = gson.toJson(mList)
        appContext.let {
            Preferences.setListOfPackages(it, packJsonList)
        }
    }

    /**
     * Display Notification
     */
    private fun handleNotificaiton(title: String, textContent: String) {
        val CHANNEL_ID = "0"
        val CHANNEL_NAME = "0"
        val manager = NotificationManagerCompat.from(appContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(textContent)
            .build()
        manager.notify(
            NOTIFICATION_KEY,
            notification
        )
    }

}