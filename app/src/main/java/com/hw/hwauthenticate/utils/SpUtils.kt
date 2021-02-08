package com.hw.hwauthenticate.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * SharedPreferences Utils
 * Manage local data
 */

object SpUtils {

    private lateinit var mContext: Context
    private const val SHARED_PREFERENCES_NAME = "SP"

    @JvmStatic
    fun init(context: Context) {
        mContext = context.applicationContext
    }

    private fun getSPInstance(): SharedPreferences {
        val kv: SharedPreferences by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
        return kv
    }

    @JvmStatic
    fun putString(key: String, value: String, commit: Boolean = false) {
        val sp = getSPInstance()
        sp.edit(commit) {
            putString(key, value)
        }
    }

    @JvmStatic
    fun getString(key: String, default: String = ""): String {
        val sp = getSPInstance()
        return sp.getString(key, default) ?: ""
    }

    @JvmStatic
    fun putInt(key: String, value: Int, commit: Boolean = false) {
        val sp = getSPInstance()
        sp.edit(commit) { putInt(key, value) }
    }

    @JvmStatic
    fun getInt(key: String, default: Int = 0): Int {
        val sp = getSPInstance()
        return sp.getInt(key, default)
    }

    @JvmStatic
    fun putLong(key: String, value: Long, commit: Boolean = false) {
        val sp = getSPInstance()
        sp.edit(commit) { putLong(key, value) }
    }

    @JvmStatic
    fun getLong(key: String, default: Long = 0L): Long {
        val sp = getSPInstance()
        return sp.getLong(key, default)
    }

    @JvmStatic
    fun putFloat(key: String, value: Float, commit: Boolean = false) {
        val sp = getSPInstance()
        sp.edit(commit) { putFloat(key, value) }
    }

    @JvmStatic
    fun getFloat(key: String, default: Float = 0F): Float {
        val sp = getSPInstance()
        return sp.getFloat(key, default)
    }

    @JvmStatic
    fun putBoolean(key: String, value: Boolean, commit: Boolean = false) {
        val sp = getSPInstance()
        sp.edit(commit) { putBoolean(key, value) }
    }

    @JvmStatic
    fun getBoolean(key: String, default: Boolean = false): Boolean {
        val sp = getSPInstance()
        return sp.getBoolean(key, default)
    }

    @JvmStatic
    fun putStringSet(
            key: String,
            value: MutableSet<String>?,
            commit: Boolean = false
    ) {
        val sp = getSPInstance()
        sp.edit(commit) { putStringSet(key, value) }
    }

    @JvmStatic
    fun getStringSet(key: String): MutableSet<String>? {
        val sp = getSPInstance()
        return sp.getStringSet(key, null)
    }

}