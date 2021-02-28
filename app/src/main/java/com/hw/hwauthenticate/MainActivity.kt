package com.hw.hwauthenticate

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hw.hwauthenticate.Bean.ToDoBean
import com.hw.hwauthenticate.adaper.ToDoListAdapter
import com.hw.hwauthenticate.remind.RemindHelper
import com.hw.hwauthenticate.remind.RemindSQLiteHelper
import com.hw.hwauthenticate.utils.Constants
import com.hw.hwauthenticate.utils.SpUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {

        const val ADD_KEY = 100
    }

    private val todoList = ArrayList<ToDoBean>()
    private val mRemindHelper = RemindHelper()
    private lateinit var mToDoListAdapter: ToDoListAdapter
    private var itemPosition = -1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RemindSQLiteHelper.init(this)
        ivAddItem.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, EditActivity::class.java), ADD_KEY)
        }
        getListData("noH")
        mToDoListAdapter = ToDoListAdapter(this, R.layout.item_todo, todoList)
        listView.adapter = mToDoListAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val get = todoList[position]
            val intent = Intent(this@MainActivity, EditActivity::class.java)
            intent.putExtra(EditActivity.DATA_KEY, get)
            itemPosition = position
            startActivityForResult(intent, ADD_KEY)
        }
        mRemindHelper.setOnThreadRemindListener {
            runOnUiThread {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.title_remind))
                builder.setMessage(it.content)
                builder.setPositiveButton(getString(R.string.txt_ok)) { _: DialogInterface, _: Int -> }
                builder.show()
            }
        }
        tvSetting.setOnClickListener {
            startActivity(Intent(this@MainActivity,PersonCenterActivity::class.java))
        }
    }

    private fun getListData(string: String) {
        todoList.clear()
        val list = RemindSQLiteHelper.getAllList()
        val date = Date()
        list.forEach {
            date.time = it.time
            if(string == "H"){
                if (it.isRemind == 1){
                    todoList.add(
                            ToDoBean(
                                    it.id,
                                    it.isRemind == 1,
                                    SimpleDateFormat(
                                            "yyyy-MM-dd H:mm:ss",
                                            Locale.getDefault()
                                    ).format(date),
                                    it.content,
                                    it.location_x,
                                    it.location_y
                            )
                    )

                }
            }else{
                todoList.add(
                        ToDoBean(
                                it.id,
                                it.isRemind == 0,
                                SimpleDateFormat(
                                        "yyyy-MM-dd H:mm:ss",
                                        Locale.getDefault()
                                ).format(date),
                                it.content,
                                it.location_x,
                                it.location_y
                        )
                )

            }
        }
    }

    override fun onPause() {
        super.onPause()
        mRemindHelper.onPause()
    }

    override fun onResume() {
        super.onResume()
        mRemindHelper.onResume()
        getListData("noH")
        mToDoListAdapter.notifyDataSetChanged()
    }


    override fun onDestroy() {
        super.onDestroy()
        mRemindHelper.quit()
    }

    fun Hide(view: View)
    {
        RemindSQLiteHelper.init(this)
        getListData("H")
        mToDoListAdapter = ToDoListAdapter(this, R.layout.item_todo, todoList)
        listView.adapter = mToDoListAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val get = todoList[position]
            val intent = Intent(this@MainActivity, EditActivity::class.java)
            intent.putExtra(EditActivity.DATA_KEY, get)
            itemPosition = position
            startActivityForResult(intent, ADD_KEY)
        }
    }

    fun NoHide(view: View)
    {
        RemindSQLiteHelper.init(this)
        getListData("noH")
        mToDoListAdapter = ToDoListAdapter(this, R.layout.item_todo, todoList)
        listView.adapter = mToDoListAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val get = todoList[position]
            val intent = Intent(this@MainActivity, EditActivity::class.java)
            intent.putExtra(EditActivity.DATA_KEY, get)
            itemPosition = position
            startActivityForResult(intent, ADD_KEY)
        }
    }
}