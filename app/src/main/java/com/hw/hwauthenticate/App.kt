package com.hw.hwauthenticate

import android.app.Application
import com.hw.hwauthenticate.remind.RemindSQLiteHelper
import com.hw.hwauthenticate.utils.SpUtils

/**
 * APP
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SpUtils.init(this)
    }

}