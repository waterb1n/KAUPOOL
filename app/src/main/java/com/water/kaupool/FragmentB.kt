package com.water.kaupool

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

@RequiresApi(api = Build.VERSION_CODES.N)
class FragmentB : Fragment() {
    var listView: ListView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        UserData()
    }

    private fun init() {
        manage_list = ArrayList()
        listView = activity!!.findViewById<View>(R.id.listView) as ListView

        listView!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val Tab = (activity as MainActivity?)!!.fragment
            val Frag = activity!!.supportFragmentManager.findFragmentByTag(Tab) as FragmentA?
            Frag!!.setClickedData(position)
            (activity as MainActivity?)?.getViewPager()?.currentItem = 0
        }

        listView!!.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            DeleteData(position)
            true
        }
    }

    fun UserData() {
        val manage_query = LoginActivity.db_manager!!.orderByChild("date")
        manage_list!!.clear()

        manage_query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                manage_list!!.clear()
                for (data in dataSnapshot.children) {
                    if (data.value != null) {
                        val m = data.getValue(manager::class.java)
                        if (m!!.check) {
                            manage_list!!.add(m)
                        }
                    }
                }

                if (activity != null) {
                    val adapter = MyListAdapter(activity!!, R.layout.data_list, manage_list!!)
                    listView!!.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun DeleteData(pos: Int) {
        val manage_query = LoginActivity.db_manager!!.orderByChild("date")
        manage_list!!.clear()

        manage_query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                manage_list!!.clear()
                for (data in dataSnapshot.children) {
                    if (data.value != null) {
                        val m = data.getValue(manager::class.java)
                        if (m!!.check) {
                            manage_list!!.add(m)
                        }
                    }
                }
                if (manage_list!!.size != null) {
                    manage_list!![pos]!!.check = false
                    LoginActivity.db_manager!!.child(manage_list!![pos]!!.date!!).setValue(manage_list!![pos])
                }


                if (activity != null) {
                    val adapter = MyListAdapter(activity!!, R.layout.data_list, manage_list!!)
                    listView!!.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    interface OnFragmentInteractionListener {}

    companion object {
        var manage_list: ArrayList<manager>? = null
    }
}