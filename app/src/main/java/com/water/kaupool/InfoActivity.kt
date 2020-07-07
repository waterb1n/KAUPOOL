package com.water.kaupool

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.water.kaupool.LoginActivity.Companion.loginName

class InfoActivity : AppCompatActivity() {
    var checkBox1: CheckBox? = null
    var checkBox2: CheckBox? = null
    var checkBox3: CheckBox? = null
    var checkBox4: CheckBox? = null

    var tv1: TextView? = null
    var tv2: TextView? = null
    var tv3: TextView? = null

    var infoName: String? = null
    var infoId: String? = null
    var infoAge: String? = null
    var infoGender: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        init()
        userProfile()
    }

    fun init() {
        dbTable = FirebaseDatabase.getInstance().getReference("kaupool")
        db_member = dbTable!!.child("member_info")
        db_manager = dbTable!!.child("manager_info")
    }

    fun userProfile() {
        val query = db_manager!!.orderByChild("master").equalTo(loginName)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val m = data!!.getValue(member::class.java)

                    if (loginName != m?.member_name) {
                        infoName = m?.member_name
                        infoId = m?.member_id
                        infoAge = m?.member_age
                        infoGender = m?.member_gender

                        tv1?.setText(infoName + "님의 카풀요청")
                        tv2?.setText(infoAge + "세 " + infoGender)

                        var result: String = ""

                        if (checkBox1?.isChecked() == true) result += checkBox1?.text.toString()
                        if (checkBox2?.isChecked() == true) result += checkBox2?.text.toString()
                        if (checkBox3?.isChecked() == true) result += checkBox3?.text.toString()
                        if (checkBox4?.isChecked() == true) result += checkBox4?.text.toString()
                        tv3?.setText(result)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun goto_Access(view: View?) {
        Toast.makeText(this@InfoActivity, "카풀요청이 승인되었습니다.", Toast.LENGTH_LONG).show()
        val i_register = Intent(this@InfoActivity, IntroActivity::class.java)
        startActivity(i_register)
    }

    fun goto_Decline(view: View?) {
        Toast.makeText(this@InfoActivity, "카풀요청이 승인되지 않았습니다.", Toast.LENGTH_LONG).show()
    }

    companion object {
        var dbTable: DatabaseReference? = null
        var db_manager: DatabaseReference? = null
        var db_member: DatabaseReference? = null
    }
}