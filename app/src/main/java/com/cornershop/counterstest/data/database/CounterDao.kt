package com.cornershop.counterstest.data.database

import androidx.room.*

@Dao
interface CounterDao {

    @Query("SELECT * FROM Counter")
    fun getAll(): List<Counter>

    @Query("SELECT COUNT(id) FROM Counter")
    fun counterCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCounters(counters: List<Counter>)

    @Query("DELETE FROM Counter WHERE id = :id")
    fun deleteCounter(id: String)

    @Delete
    fun deleteCounters(counter: List<Counter>)
}