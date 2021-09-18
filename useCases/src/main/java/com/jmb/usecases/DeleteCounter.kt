package com.jmb.usecases

import com.jmb.data.repository.CounterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteCounter(private val repository: CounterRepository) {
    suspend fun invoke(id: String) = withContext(Dispatchers.IO) {
        repository.deleteCounter(id)
    }
}