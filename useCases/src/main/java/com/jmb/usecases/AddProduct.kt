package com.jmb.usecases

import com.jmb.data.repository.CounterRepository
import com.jmb.domain.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddProduct(private val repository: CounterRepository) {

    suspend fun invoke(product: Counter) = withContext(Dispatchers.IO) {
        repository.addProduct(product)
    }
}