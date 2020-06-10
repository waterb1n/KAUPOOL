package com.water.kaupool

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    /**
     * https://blog.naver.com/ajh794/221011436910
     */
    fun init() {
        dbTable = FirebaseDatabase.getInstance().getReference("kaupool")
        db_member = dbTable!!.child("member_info")
        db_manager = dbTable!!.child("manager_info")
    }

    fun goto_Register(view: View?) {
        val i_register = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(i_register)
    }

    fun goto_Main(view: View?) {
        val user_id = loginId!!.text.toString()
        val user_pw = loginPw!!.text.toString()
        val query = db_member!!.orderByKey().equalTo(user_id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            //핸들러로 쿼리 내용 확인
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val m = data!!.getValue(member::class.java)

                    if (user_pw == m?.member_pw) {
                        val loginIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        loginName = m?.member_name
                        loginPhone = m?.member_phone
                        //loginId = m?.member_id
                        Toast.makeText(this@LoginActivity, "환영합니다.", Toast.LENGTH_SHORT).show()
                        startActivity(loginIntent)

                    } else {
                        loginId!!.setText("")
                        loginPw!!.setText("")
                        Toast.makeText(this@LoginActivity, "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    companion object {
        var dbTable: DatabaseReference? = null
        var db_member: DatabaseReference? = null
        var db_manager: DatabaseReference? = null
        var loginName: String? = null
        var loginPhone: String? = null
        //var loginId: String? = null
    }
}