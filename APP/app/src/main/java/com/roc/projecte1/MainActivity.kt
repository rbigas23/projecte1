package com.roc.projecte1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sm = SessionManager.getInstance(this)

        if (!sm.isLoggedIn()) {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        val container = findViewById<View>(R.id.nav_host_fragment)
        bottomNav.post {
            val bottomNavHeight = bottomNav.height
            container.setPadding(0, 0, 0, bottomNavHeight)
        }

    }
}