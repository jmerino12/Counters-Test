package com.cornershop.counterstest.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Counter::class], version = 1)
abstract class CounterDatabase : RoomDatabase() {
    companion object {
        fun build(context: Context) = Room.databaseBuilder(
            context,
            CounterDatabase::class.java,
            "counter-db"
        ).build()
    }

    abstract fun counterDao(): CounterDao

}
