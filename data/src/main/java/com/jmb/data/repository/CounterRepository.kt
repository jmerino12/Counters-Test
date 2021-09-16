package com.jmb.data.repository

import com.jmb.data.source.LocalDataSource
import com.jmb.data.source.RemoteDataSource

class CounterRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun getCounters() = remoteDataSource.getCounters()
}