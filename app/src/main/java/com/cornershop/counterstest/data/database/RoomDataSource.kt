package com.cornershop.counterstest.data.database

import com.cornershop.counterstest.data.toDomainCounter
import com.cornershop.counterstest.data.toRoomCounter
import com.jmb.data.source.LocalDataSource
import com.jmb.domain.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(private val db: CounterDatabase) : LocalDataSource {

    private val counterDao = db.counterDao()

    override suspend fun isEmpty(): Boolean = withContext(Dispatchers.IO) {
        counterDao.counterCount() <= 0
    }

    override suspend fun saveCounter(counter: List<Counter>) = withContext(Dispatchers.IO) {
        counterDao.insertCounters(counter.map { it.toRoomCounter() })
    }

    override suspend fun getCounters(): List<Counter> = withContext(Dispatchers.IO) {
        counterDao.getAll().map { it.toDomainCounter() }
    }

    override suspend fun deleteCounters(counter: List<Counter>) = withContext(Dispatchers.IO) {
        counterDao.deleteCounters(counter.map { it.toRoomCounter() })
    }

    override suspend fun deleteCounter(id: String) = withContext(Dispatchers.IO) {
        counterDao.deleteCounter(id)
    }
}