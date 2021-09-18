package com.cornershop.counterstest.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.databinding.ActivityWelcomeBinding
import com.cornershop.counterstest.databinding.LayoutWelcomeContentBinding
import com.cornershop.counterstest.ui.main.MainScreen

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var content: LayoutWelcomeContentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        content = binding.contentWelcome!!
        content.buttonStart.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
        }
    }
}
