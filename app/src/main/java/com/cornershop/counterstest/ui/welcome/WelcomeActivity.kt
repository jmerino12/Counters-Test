package com.cornershop.counterstest.ui.welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ActivityWelcomeBinding
import com.cornershop.counterstest.databinding.LayoutWelcomeContentBinding
import com.cornershop.counterstest.ui.main.MainScreen

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var content: LayoutWelcomeContentBinding
    private lateinit var intentActivity: Intent
    private lateinit var sharedPref: SharedPreferences


    override fun onStart() {
        super.onStart()
        intentActivity = Intent(this, MainScreen::class.java)
        sharedPref =
            getSharedPreferences(getString(R.string.preference_welcome), Context.MODE_PRIVATE)
        val isFirstTimeInApp = sharedPref.getBoolean(getString(R.string.preference_welcome), true)
        if (!isFirstTimeInApp) {
            startActivity(intentActivity)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        content = binding.contentWelcome!!
        content.buttonStart.setOnClickListener {
            with(sharedPref.edit()) {
                putBoolean(getString(R.string.preference_welcome), false)
                commit()
            }
            startActivity(intentActivity)
        }

    }
}
