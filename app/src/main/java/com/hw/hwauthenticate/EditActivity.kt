package com.hw.hwauthenticate

import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hw.hwauthenticate.Bean.ToDoBean
import com.hw.hwauthenticate.remind.RemindSQLiteHelper
import com.hw.hwauthenticate.utils.ExpandUtils.toast
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity : AppCompatActivity() {

    companion object {
        const val DATA_KEY = "EditActivity.key"
    }

    private var mTodoBean: ToDoBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        deleteBtn.setOnClickListener{
            RemindSQLiteHelper.deleteRemindData(mTodoBean!!.id)
            toast(this,"the reminder has been deleted!")
        }
        ivBackPage.setOnClickListener { finish() }
        tvAdd.setOnClickListener {
            if (edTodo.text.toString().isNotBlank()) {
                val calendar = Calendar.getInstance()
                val dataDialog = DatePickerDialog(
                    this,
                    { view, year, month, dayOfMonth ->
                        val timeDialog = TimePickerDialog(
                            this,
                            { view, hourOfDay, minute ->
                                calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                                if (mTodoBean != null && mTodoBean!!.id >= 0) {
                                    RemindSQLiteHelper.deleteRemindData(mTodoBean!!.id)
                                }
                                RemindSQLiteHelper.addRemindData(
                                    RemindSQLiteHelper.RemindData(
                                        edTodo.text.toString(),
                                        calendar.time.time
                                    )
                                )
                                finish()
                            },
                            calendar[Calendar.HOUR_OF_DAY],
                            calendar[Calendar.MINUTE],
                            true
                        )
                        timeDialog.show()
                    },
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
                )
                dataDialog.show()
            } else {
                toast(this, getString(R.string.input_todo_details))
            }
        }
        mTodoBean = intent.getParcelableExtra(DATA_KEY)
        mTodoBean.let {
            edTodo.setText(mTodoBean?.details)
        }
    }

    fun OpenNotification(view: View){

        var intent = Intent(this, MainActivity::class.java)
        val reminder = RemindSQLiteHelper.searchRemindDataByID(remindID)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("message")
                .setContentText(reminder.content)
                .setPriority(NotificationCompat.PRIORITY_MAX )
                .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    fun CurrentLocation(view: View)
    {
        val intent = Intent(this, MapsActivity2::class.java).apply {  }
        startActivity(intent)
    }

    fun ReminderLocation(view: View)
    {
        val intent = Intent(this, MapsActivity::class.java).apply {  }
        startActivity(intent)
    }

}