package com.water.kaupool

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.*

class MyListAdapter2(var context: Context, var layout: Int, var data: ArrayList<manager>) : BaseAdapter() {
    var inflater: LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(i: Int): Any {
        return data[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.data_list2, parent, false)
        }
        val start = convertView?.findViewById<View>(R.id.Start2) as? TextView
        val end = convertView?.findViewById<View>(R.id.End2) as? TextView
        val date = convertView?.findViewById<View>(R.id.date2) as? TextView
        val time = convertView?.findViewById<View>(R.id.time2) as? TextView

        var Date = ""
        Date += data[position].date.substring(0, 4) + "년 "
        Date += data[position].date.substring(4, 6) + "월 "
        Date += data[position].date.substring(6, 8) + "일"
        date?.text = Date

        var Time = ""
        Time += data[position].time!!.substring(0, 2) + "시"
        Time += data[position].time!!.substring(2, 4) + "분"
        time?.text = Time

        end?.text = data[position].end
        start?.text = data[position].start

        return convertView
    }

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}