package com.jmb.data.source

import com.jmb.domain.Counter

interface LocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun saveCounter(counter: List<Counter>)
    suspend fun getCounters(): List<Counter>
    suspend fun deleteCounters(counter: List<Counter>)
    suspend fun deleteCountersServerEmpty()
    suspend fun deleteCounter(id: String)

}