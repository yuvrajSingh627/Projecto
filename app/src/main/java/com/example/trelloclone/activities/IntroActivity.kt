package com.example.trelloclone.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trelloclone.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val tv_app_name_intro = findViewById<TextView>(R.id.tv_app_name_intro)
        val typeface = Typeface.createFromAsset(assets, "JazzyRabbit_Remake.ttf")

        tv_app_name_intro.typeface = typeface

        val btn_sign_in_intro = findViewById<Button>(R.id.btn_sign_in_intro)
        val btn_sign_up_intro = findViewById<Button>(R.id.btn_sign_up_intro)

        btn_sign_in_intro.setOnClickListener {
            startActivity(Intent(this, SignInAcitivity::class.java))
        }

        btn_sign_up_intro.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}