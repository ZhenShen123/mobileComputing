package com.hw.hwauthenticate.adaper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hw.hwauthenticate.Bean.ToDoBean
import com.hw.hwauthenticate.R

class ToDoListAdapter(context: Context, private val resourceId: Int, data: List<ToDoBean>) :
    ArrayAdapter<ToDoBean>(context, resourceId, data) {

    inner class ViewHolder(val tvDate: TextView, val tvDetails: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            val tvDate: TextView = view.findViewById(R.id.tvDateTips)
            val tvDetails: TextView = view.findViewById(R.id.tvToDoDetails)
            viewHolder = ViewHolder(tvDate, tvDetails)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val bean = getItem(position)
        if (bean != null) {
            viewHolder.tvDate.text = bean.date
            viewHolder.tvDetails.text = bean.details
        }
        return view
    }
}