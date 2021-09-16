package com.cornershop.counterstest.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Counter(
    @PrimaryKey val id: String,
    val title: String,
    val count: Int
)
