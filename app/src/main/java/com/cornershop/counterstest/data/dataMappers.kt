package com.cornershop.counterstest.data

import com.jmb.domain.Counter
import com.cornershop.counterstest.data.database.Counter as LocalCounter
import com.cornershop.counterstest.data.server.TheCounterDbResult as ServerCounter

fun Counter.toRoomCounter(): LocalCounter = LocalCounter(
    id, title, count
)

fun LocalCounter.toDomainCounter(): Counter = Counter(
    id, title, count
)

fun ServerCounter.toDomainCounter(): Counter = Counter(
    id, title, count
)