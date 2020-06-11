package com.water.kaupool

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val checkBox1 = findViewById<View>(R.id.checkBox1) as CheckBox
        val checkBox2 = findViewById<View>(R.id.checkBox2) as CheckBox
        val checkBox3 = findViewById<View>(R.id.checkBox3) as CheckBox
        val checkBox4 = findViewById<View>(R.id.checkBox4) as CheckBox
    }

    fun checkId(view: View?) { // ID 확인
        val user_id = JoinId!!.text.toString()
        val id_query: Query = LoginActivity.db_member!!.orderByChild("member_id").equalTo(user_id)
        id_query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value == null) { //사용가능
                    Toast.makeText(this@RegisterActivity, "사용가능한 아이디입니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun checkName(view: View?) { // Name 확인
        val user_name = JoinName!!.text.toString()
        val name_query: Query = LoginActivity.Companion.db_member!!.orderByChild("member_name").equalTo(user_name)
        name_query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value == null) { //사용가능
                    Toast.makeText(this@RegisterActivity, "사용가능한 닉네임입니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "이미 존재하는 닉네임입니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun RegisterToLogin(view: View?) {

        val user_id = JoinId!!.text.toString()
        val user_pw = JoinPw!!.text.toString()
        val user_name = JoinName!!.text.toString()
        val user_phone = JoinPhone!!.text.toString()
        val user_age = JoinAge!!.text.toString()

        val id_query: Query = LoginActivity.Companion.db_member!!.orderByChild("member_id").equalTo(user_id)
        id_query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value == null) { //사용가능
                    val name_query: Query = LoginActivity.Companion.db_member!!.orderByChild("member_name").equalTo(user_name)
                    name_query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.value == null) { //사용가능한 닉네임
                                val m = member(user_id, user_pw, user_name, user_phone, user_age, "여성", true, true, true, true)
                                LoginActivity.Companion.db_member!!.child(user_id).setValue(m)
                                Toast.makeText(applicationContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                } else {
                    Toast.makeText(this@RegisterActivity, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}