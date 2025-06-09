package com.roc.projecte1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (SessionManager.getInstance(this).isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_start)

        Log.d("ACTIVITY", "StartActivity")

        findViewById<Button>(R.id.activity_start_login_button).setOnClickListener()
        { startActivity(Intent(this, LogInActivity::class.java)) }

        findViewById<Button>(R.id.activity_start_register_button).setOnClickListener()
        { startActivity(Intent(this, RegisterActivity::class.java)) }

    }

}
