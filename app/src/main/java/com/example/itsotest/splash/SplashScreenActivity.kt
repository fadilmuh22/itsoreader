package com.example.itsotest.splash

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.itsotest.R
import com.example.itsotest.ui.login.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var img_splash : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        img_splash = findViewById(R.id.img_splash)

        img_splash.alpha = 0f
        img_splash.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

    }
}