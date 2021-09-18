package com.jmb.domain

data class Counter(
    val id: String?,
    val title: String? = null,
    var count: Int = 0,
    var isSelected: Boolean = false
)
