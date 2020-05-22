package com.water.kaupool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class FragmentC : Fragment() {
    var listView2: ListView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment3, container, false)
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
                        if (m!!.check == false) {
                            if (m.master == LoginActivity.loginName) manage_list!!.add(m)
                        }
                    }
                }

                if (activity != null) {
                    val adapter = MyListAdapter2(activity!!, R.layout.data_list2, manage_list!!)
                    listView2!!.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    interface OnFragmentInteractionListener {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        manage_list = ArrayList()
        listView2 = activity!!.findViewById<View>(R.id.listView2) as ListView
        UserData()
    }

    companion object {
        var manage_list: ArrayList<manager>? = null
    }
}