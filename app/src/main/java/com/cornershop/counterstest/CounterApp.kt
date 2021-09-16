package com.cornershop.counterstest

import android.app.Application

class CounterApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}