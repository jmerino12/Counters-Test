package com.cornershop.counterstest.data.database

import com.jmb.data.source.LocalDataSource

class RoomDataSource(private val db: CounterDatabase): LocalDataSource {

    private val counterDao = db.counterDao()
}