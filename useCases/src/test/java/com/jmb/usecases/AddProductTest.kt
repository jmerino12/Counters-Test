package com.jmb.usecases

import com.jmb.data.repository.CounterRepository
import com.jmb.testshared.mockedCounter
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddProductTest {

    @Mock
    lateinit var repository: CounterRepository

    lateinit var addProduct: AddProduct

    @Before
    fun setUp() {
        addProduct = AddProduct(repository)
    }


    @Test
    fun `invoke calls movies repository`() = runBlocking {
        val counters = listOf(mockedCounter.copy(id = "1"))

        whenever(repository.addProduct(any())).thenReturn(counters)

        val result = addProduct.invoke(mockedCounter)

        Assert.assertEquals(counters, result)
    }
}