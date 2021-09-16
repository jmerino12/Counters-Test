package com.cornershop.counterstest.ui.common


sealed class UiModel<out T> {
    object Loading : UiModel<Nothing>()
    data class Content<out T>(val data: T) : UiModel<T>()
    data class Error(val error: Exception) : UiModel<Nothing>()
}