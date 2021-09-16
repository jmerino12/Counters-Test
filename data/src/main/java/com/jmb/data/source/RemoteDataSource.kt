package com.jmb.data.source

import com.jmb.domain.Counter

interface RemoteDataSource {
    suspend fun getCounters(): List<Counter>
    suspend fun addProduct(product: Counter): List<Counter>
}