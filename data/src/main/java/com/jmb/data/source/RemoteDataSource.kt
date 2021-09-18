package com.jmb.data.source

import com.jmb.domain.Counter

interface RemoteDataSource {
    suspend fun getCounters(): List<Counter>
    suspend fun addProduct(product: Counter): List<Counter>
    suspend fun increseCounter(product: Counter): List<Counter>
    suspend fun decreseCounter(product: Counter): List<Counter>
    suspend fun deleteCounter(id: String): List<Counter>
}