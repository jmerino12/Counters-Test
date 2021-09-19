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
class DecreseCounterTest {

    @Mock
    lateinit var repository: CounterRepository

    lateinit var increseCounter: IncreseCounter

    @Before
    fun setUp() {
        increseCounter = IncreseCounter(repository)
    }

    @Test
    fun `invoke call DecreseCounter repository`() = runBlocking {
        val counters = listOf(mockedCounter.copy("1"))

        whenever(repository.increseCounter(any())).thenReturn(counters)

        val result = increseCounter.invoke(mockedCounter)

        Assert.assertEquals(counters, result)
    }
}