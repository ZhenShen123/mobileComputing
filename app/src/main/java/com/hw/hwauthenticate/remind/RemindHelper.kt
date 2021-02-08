package com.hw.hwauthenticate.remind

import android.os.Build
import android.os.Handler
import android.os.HandlerThread

/**
 * RemindHelper
 */
class RemindHelper {

    private val mHandlerThread = HandlerThread("remindHelper")
    private val mHandler: Handler
    private var mRun: Boolean = false
    private var mFinish: Boolean = false
    private var mListener: ((remindData: RemindSQLiteHelper.RemindData) -> Unit)? = null

    init {
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper)
    }

    private val mRunnable = object : Runnable {
        override fun run() {
            if (mRun && !mFinish) {
                loopToFindRemind()
                mHandler.postDelayed({
                    this.run()
                }, 500)
            }
        }
    }

    fun setOnThreadRemindListener(listener: ((remindData: RemindSQLiteHelper.RemindData) -> Unit)?) {
        mListener = listener
    }

    private fun loopToFindRemind() {
        val list = RemindSQLiteHelper.getAllList()
        for (remindData in list) {
            if (remindData.isRemind == 0 && remindData.time <= System.currentTimeMillis()) {
                mListener?.run {
                    this(remindData)
                    remindData.isRemind = 1
                    RemindSQLiteHelper.updateRemindData(remindData.id, remindData)
                }
                break
            }
        }
    }

    fun onPause() {
        mRun = false
        mHandler.removeCallbacks(mRunnable)
    }

    fun onResume() {
        mRun = true
        mHandler.removeCallbacks(mRunnable)
        mHandler.post(mRunnable)
    }

    fun quit() {
        mFinish = true
        mHandler.removeCallbacks(mRunnable)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mHandlerThread.quitSafely()
        } else {
            mHandlerThread.quit()
        }
    }

}