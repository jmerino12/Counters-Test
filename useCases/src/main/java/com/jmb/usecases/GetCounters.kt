package com.jmb.usecases

import com.jmb.data.repository.CounterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCounters(private val repository: CounterRepository) {

    suspend fun invoke() = withContext(Dispatchers.IO) {
        repository.getCounters()
    }
}