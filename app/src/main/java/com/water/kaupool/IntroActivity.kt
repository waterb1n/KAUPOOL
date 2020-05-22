package com.water.kaupool

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    var handler_intro: Handler? = null
    var run_into = Runnable {
        val i_Login = Intent(this@IntroActivity, LoginActivity::class.java)
        startActivity(i_Login)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        handler_intro = Handler()
        handler_intro!!.postDelayed(run_into, 3000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}