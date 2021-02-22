package com.hw.hwauthenticate

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

}