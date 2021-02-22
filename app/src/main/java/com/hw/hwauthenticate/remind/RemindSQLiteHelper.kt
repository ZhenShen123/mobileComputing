package com.hw.hwauthenticate.remind

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hw.hwauthenticate.utils.Constants
import com.hw.hwauthenticate.utils.SpUtils


/**
 *  RemindSQLite Utils
 */
object RemindSQLiteHelper {

    lateinit var mRemindSQLite: RemindSQLite
    var mUserId: String? = null

    @JvmStatic
    @Synchronized
    fun init(context: Context) {
        val userId = SpUtils.getString(Constants.CURRENT_USER_DATA_KEY)
        if (mUserId == null || !mUserId.equals(userId)) {
            mRemindSQLite = RemindSQLite(context, userId)
        }
    }

    @Synchronized
    fun addRemindData(remindData: RemindData) {
        val db = mRemindSQLite.writableDatabase
        val insert =
            "insert into ${mRemindSQLite.getTableName()} (content,time,locationX,locationY) values ('${remindData.content}',${remindData.time},${remindData.location_x},${remindData.location_y})"
        db.execSQL(insert)
        db.close()
    }

    @Synchronized
    fun deleteRemindData(id: Long) {
        val db = mRemindSQLite.writableDatabase
        val delete = "delete from ${mRemindSQLite.getTableName()} where id = $id"
        db.execSQL(delete)
        db.close()
    }

    @Synchronized
    fun updateRemindData(id: Long, remindData: RemindData) {
        val db = mRemindSQLite.writableDatabase
        val sql =
            "update ${mRemindSQLite.getTableName()} set content = '${remindData.content}',time = ${remindData.time},isRemind = ${remindData.isRemind},locationX = ${remindData.location_x},locationX = ${remindData.location_y} where id = $id"
        db.execSQL(sql)
        db.close()
    }

    @Synchronized
    fun getAllList(): List<RemindData> {
        val list = mutableListOf<RemindData>()
        val db = mRemindSQLite.readableDatabase
        val sql = "select * from " + mRemindSQLite.getTableName() + " order by id desc"
        val cursor: Cursor = db.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val time = cursor.getLong(cursor.getColumnIndex("time"))
            val isRemind = cursor.getInt(cursor.getColumnIndex("isRemind"))
            val location_x = cursor.getLong(cursor.getColumnIndex("locationX"))
            val location_y = cursor.getLong(cursor.getColumnIndex("locationY"))
            list.add(RemindData(id, content, time, isRemind, location_x, location_y))
        }
        cursor.close()
        db.close()
        return list
    }

    class RemindSQLite(
        context: Context?,
        userId: String
    ) : SQLiteOpenHelper(context, "SQ", null, 2) {

        private var TABLE_NAME = "Remind_$userId"
        private var CREATE_TABLE =
            "create table if not exists $TABLE_NAME (id INTEGER primary key autoincrement, content TEXT,time INTEGER,isRemind NUMERIC,locationX INTEGER,locationY INTEGER)"
        private val DROP_TABLE = "drop table if exists $TABLE_NAME"

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL(DROP_TABLE)
            db?.execSQL(CREATE_TABLE)
        }

        override fun onOpen(db: SQLiteDatabase?) {
            super.onOpen(db)
            db?.execSQL(CREATE_TABLE)
        }

        fun getTableName(): String {
            return TABLE_NAME
        }

    }

    data class RemindData(var content: String, var time: Long, var isRemind: Int = 0,
                          var location_x: Long = 0, var location_y: Long = 0) {

        var id: Long = 0

        constructor(id: Long, content: String, time: Long, isRemind: Int,
        location_x: Long, location_y: Long) : this(
            content,
            time,
            isRemind,
            location_x,
            location_y
        ) {
            this.id = id
        }


    }

}