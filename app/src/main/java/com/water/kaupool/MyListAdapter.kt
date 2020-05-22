package com.water.kaupool

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

import com.water.kaupool.FragmentB.Companion.manage_list
import com.water.kaupool.LoginActivity.Companion.db_manager
import com.water.kaupool.LoginActivity.Companion.loginName

/**
 * http://blog.naver.com/kittoboy/110133423266
 * https://blog.naver.com/yangin20/221453061549
 */
class MyListAdapter(val context: Context, var layout: Int, var data: ArrayList<manager>) : BaseAdapter() {
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.data_list, parent, false)
        }
        val start = convertView?.findViewById<View>(R.id.Start) as TextView
        val end = convertView?.findViewById<View>(R.id.End) as TextView
        val date = convertView?.findViewById<View>(R.id.date) as TextView
        val time = convertView?.findViewById<View>(R.id.time) as TextView
        val num = convertView?.findViewById<View>(R.id.Num) as TextView
        val Btn = convertView?.findViewById<View>(R.id.Btn) as ImageView

        var Date = ""
        Date += data[position].date!!.substring(0, 4) + "년 "
        Date += data[position].date!!.substring(4, 6) + "월 "
        Date += data[position].date!!.substring(6, 8) + "일"
        date.text = Date

        var Time = ""
        Time += data[position].time!!.substring(0, 2) + "시"
        Time += data[position].time!!.substring(2, 4) + "분"
        time.text = Time

        num.text = "${data[position].num} 명"
        end.text = data[position].end
        start.text = data[position].start


        val check: manager? = manage_list?.get(position)
        val us = check?.user
        val us_list = us!!.split(",").toTypedArray()
        val tmp_list = ArrayList<String>()
        for (i in us_list.indices) {
            tmp_list.add(us_list[i])
        }
        for (i in tmp_list.indices) {
            if (tmp_list[i] == loginName) {
                Btn.setImageResource(R.drawable.exit)
            } else {
                Btn.setImageResource(R.drawable.join)
            }
        }

        val f_date: String = data[position].date

        Btn.setOnClickListener {
            val `in` = Btn.drawable
            val in1 = context.resources.getDrawable(R.drawable.join)
            val tmpBitmap = (`in` as BitmapDrawable).bitmap
            val tmpBitmap1 = (in1 as BitmapDrawable).bitmap

            if (tmpBitmap == tmpBitmap1) {
                Btn.setImageResource(R.drawable.exit)
                var ischeck = false
                val newMan: manager? = manage_list?.get(position)
                val previous_num = newMan?.num

                val people = newMan?.user
                val p_list = people!!.split(",").toTypedArray()
                val new_list = ArrayList<String?>()
                for (i in p_list.indices) {
                    new_list.add(p_list[i])
                }
                for (i in new_list.indices) {
                    if (new_list[i] == loginName) {
                        ischeck = true
                    }
                }
                if (ischeck) {
                } else {
                    newMan.num = previous_num!!.plus(1)
                    var new_str = ""
                    new_list.add(loginName)
                    for (i in new_list.indices) {
                        if (new_list[i] === "") continue
                        new_str += if (i == new_list.size - 1) {
                            new_list[i]
                        } else {
                            new_list[i] + ","
                        }
                    }
                    newMan.user = new_str
                    db_manager?.child(f_date)?.setValue(newMan)
                }
            } else {
                //Btn.setImageResource(R.drawable.button_in);
                val newMan: manager? = manage_list?.get(position)
                val people = newMan?.user
                val p_list = people!!.split(",").toTypedArray()
                val new_list = ArrayList<String>()
                for (i in p_list.indices) {
                    new_list.add(p_list[i])
                }
                for (i in new_list.indices) {
                    if (new_list[i] == loginName) {
                        val previous_num = newMan.num
                        newMan.num = previous_num - 1
                        new_list.removeAt(i)
                    }
                }
                var new_str = ""
                for (i in new_list.indices) {
                    if (new_list[i] === "") continue
                    new_str += if (i == new_list.size - 1) {
                        new_list[i]
                    } else {
                        new_list[i] + ","
                    }
                }
                newMan.user = new_str
                db_manager?.child(f_date)?.setValue(newMan)
            }
        }
        return convertView
    }

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}

