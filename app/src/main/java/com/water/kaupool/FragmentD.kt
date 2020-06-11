package com.water.kaupool

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import com.water.kaupool.LoginActivity.Companion.loginName

class FragmentD : Fragment() {
    //static DatabaseReference dbTable;
    //static DatabaseReference db_message;
    private var sendBtn: Button? = null
    private var editText: EditText? = null
    private var listView: ListView? = null
    private val list = ArrayList<String>()
    private var arrayAdapter: ArrayAdapter<String>? = null
    private var name: String? = null
    private var chat_msg: String? = null
    private var chat_user: String? = null
    private val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("message")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment4, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

        sendBtn!!.setOnClickListener {
            val map: Map<String, Any> = HashMap()
            val key = reference.push().key
            reference.updateChildren(map)

            val root = reference.child(key!!)
            val objectMap: MutableMap<String, Any> = HashMap()

            objectMap["name"] = loginName!!
            objectMap["text"] = editText!!.text.toString()

            root.updateChildren(objectMap)
            editText!!.setText("")
        }

        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                chatConversation(dataSnapshot)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                chatConversation(dataSnapshot)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun chatConversation(dataSnapshot: DataSnapshot) {
        val i: Iterator<*> = dataSnapshot.children.iterator()
        while (i.hasNext()) {
            chat_user = (i.next() as DataSnapshot).value as String?
            chat_msg = (i.next() as DataSnapshot).value as String?
            arrayAdapter!!.add("$chat_user : $chat_msg")
        }
        arrayAdapter!!.notifyDataSetChanged()
    }

    private fun init() {
        //dbTable = FirebaseDatabase.getInstance().getReference("kaupool");
        //db_message = dbTable.child("chat_info");
        listView = activity!!.findViewById<View>(R.id.list) as ListView
        sendBtn = activity!!.findViewById<View>(R.id.sendBtn) as Button
        editText = activity!!.findViewById<View>(R.id.editText) as EditText
        arrayAdapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, list)
        listView!!.adapter = arrayAdapter
    }

    interface OnFragmentInteractionListener
}