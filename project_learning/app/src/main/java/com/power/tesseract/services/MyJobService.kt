package com.power.tesseract.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.power.tesseract.utils.Preferences

class MyJobService(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {


    override fun doWork(): Result {

//       val packJsonList = gson.toJson(listOfPackages)
        appContext.let {
            val data = Preferences.getListOfPackages(it)
            val gson = Gson()
            val jsonArray = gson.toJson(data)
        }


//        val intent = Intent()
//        val pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0)
//        val notification: NotificationCompat.Builder =
//            NotificationCompat.Builder(appContext, "channelId")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentText("MyMessage")
//                .setContentTitle("new")
//                .setContentIntent(pendingIntent)
//
//        appContext.getSystemService(Context.NOTIFICATION_SERVICE).notify(0, notification.build())
        return Result.success()
    }


}