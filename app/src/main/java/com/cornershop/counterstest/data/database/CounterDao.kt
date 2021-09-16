package com.cornershop.counterstest.data.database

import androidx.room.*

@Dao
interface CounterDao {

    @Query("SELECT * FROM Counter")
    fun getAll(): List<Counter>

    @Query("SELECT COUNT(id) FROM Counter")
    fun counterCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCounters(counters: List<Counter>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCounter(counter: Counter)

    @Update
    fun update(counter: Counter)

    @Delete
    fun deleteCounter(counter: Counter)

    @Delete
    fun deleteCounters(counter: List<Counter>)
}