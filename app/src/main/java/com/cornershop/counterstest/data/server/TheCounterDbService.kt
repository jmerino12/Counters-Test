package com.cornershop.counterstest.data.server

import com.jmb.domain.Counter
import retrofit2.http.*

interface TheCounterDbService {

    @GET("counters")
    suspend fun listCounters(): List<TheCounterDbResult>

    @POST("counter")
    suspend fun saveCounterDbRemote(@Body params: Counter)
            : List<TheCounterDbResult>

    @POST("counter/inc")
    suspend fun incrementalCounter(@Body params: Counter)
            : List<TheCounterDbResult>

    @POST("counter/dec")
    suspend fun decrementalCounter(@Body params: Counter)
            : List<TheCounterDbResult>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "counter", hasBody = true)
    suspend fun deleteCounter(@Field("id") id: String)
            : List<TheCounterDbResult>

}