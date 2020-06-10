package com.water.kaupool

import androidx.appcompat.app.AppCompatActivity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


import com.water.kaupool.FragmentB.Companion.manage_list
import com.water.kaupool.LoginActivity.Companion.db_manager
import com.water.kaupool.LoginActivity.Companion.loginName
import java.util.*


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

                showNoti();

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

    /**
     * https://codechacha.com/ko/notifications-in-android/
     * https://developer.android.com/training/notify-user/build-notification?hl=ko
     */

    private fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean,
                                          name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNoti() {
        //clearExistingNotifications(NOTIFICATION_ID)
        //clearExistingNotifications(NOTIFICATION_ID_2)
        createNotificationChannel(context, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
                "KAUPOOL", "App notification channel")

        val channelId = "kaupool"
        val title = "KAUPOOL"
        val content = "새로운 카풀 참여요청이 있습니다."

        val intent = Intent(context, InfoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
        builder.setSmallIcon(R.drawable.car)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


    companion object {
        const val NOTIFICATION_ID = 1001
        const val NOTIFICATION_ID_2 = 1002
    }


    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}

