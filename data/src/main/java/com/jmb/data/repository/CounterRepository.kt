package com.jmb.data.repository

import com.jmb.data.source.LocalDataSource
import com.jmb.data.source.RemoteDataSource
import com.jmb.domain.Counter

class CounterRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getCounters() = remoteDataSource.getCounters()
    suspend fun addProduct(product: Counter) = remoteDataSource.addProduct(product)
    suspend fun increseCounter(product: Counter) = remoteDataSource.increseCounter(product)
    suspend fun decreseCounter(product: Counter) = remoteDataSource.decreseCounter(product)
    suspend fun deleteCounter(id: String) = remoteDataSource.deleteCounter(id)
}