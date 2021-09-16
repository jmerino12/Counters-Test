package com.cornershop.counterstest.data.server

import com.cornershop.counterstest.data.toDomainCounter
import com.jmb.data.source.RemoteDataSource
import com.jmb.domain.Counter

class TheCounterDbDataSource(private val theCounterDb: TheCounterDb) : RemoteDataSource {

    override suspend fun getCounters(): List<Counter> {
        return theCounterDb.service.listCounters().map {
            it.toDomainCounter()
        }
    }

}