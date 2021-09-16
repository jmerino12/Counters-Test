package com.cornershop.counterstest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.ui.common.UiModel
import com.jmb.domain.Counter
import com.jmb.usecases.GetCounters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val getCounters: GetCounters) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _model = MutableLiveData<UiModel<List<Counter>>>()
    val model: LiveData<UiModel<List<Counter>>>
        get() {
            if (_model.value == null) getCounters()
            return _model
        }

    fun getCounters() {
        uiScope.launch {
            _model.value = UiModel.Loading
            try {
                _model.value = UiModel.Content(getCounters.invoke())
            } catch (e: Exception) {
                _model.value = UiModel.Error(e)
            }
        }

    }
}