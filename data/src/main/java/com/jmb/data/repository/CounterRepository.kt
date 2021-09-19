package com.jmb.data.repository

import com.jmb.data.source.LocalDataSource
import com.jmb.data.source.RemoteDataSource
import com.jmb.domain.Counter

class CounterRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getCounters(): List<Counter> {
        val counter = remoteDataSource.getCounters()
        localDataSource.saveCounter(counter)
        return localDataSource.getCounters()
    }

    suspend fun addProduct(product: Counter) = remoteDataSource.addProduct(product)
    suspend fun increseCounter(product: Counter) = remoteDataSource.increseCounter(product)
    suspend fun decreseCounter(product: Counter) = remoteDataSource.decreseCounter(product)
    suspend fun deleteCounter(id: String): List<Counter> {
        val deleteCounter = remoteDataSource.deleteCounter(id)
        localDataSource.deleteCounter(id)
        return localDataSource.getCounters()
    }
}