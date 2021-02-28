package com.hw.hwauthenticate

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hw.hwauthenticate.remind.RemindSQLiteHelper.searchRemindDataByID
import com.hw.hwauthenticate.remind.RemindSQLiteHelper.updateRemindData
import java.util.*

var remindID: Long = 0

class MyWorker(c: Context, workerParams: WorkerParameters)
    : Worker(c, workerParams) {

    override fun doWork(): Result {

        val result = searchRemindDataByID(remindID)
        val notNotification = result.isRemind
        val date: Date = Date()
        date.time = result.time
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date

        if (compareTime(calendar[Calendar.HOUR_OF_DAY])) {
            if (notNotification == 0 || notNotification == 1) {
                updateRemindData(remindID,result)
            } else {
                return Result.retry()
            }
        } else {
            return Result.retry()
        }

        remindID = 0
        val resultIntent = Intent(applicationContext, EditActivity::class.java)
        val intent = PendingIntent.getActivity(applicationContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return Result.success()
    }

    private fun compareTime(targetHour: Int): Boolean {
        val current = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return current == targetHour
    }
}