package com.cornershop.counterstest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.ui.common.UiModel
import com.jmb.domain.Counter
import com.jmb.usecases.DecreseCounter
import com.jmb.usecases.DeleteCounter
import com.jmb.usecases.GetCounters
import com.jmb.usecases.IncreseCounter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val getCounters: GetCounters,
    private val increseCounter: IncreseCounter,
    private val decreseCounter: DecreseCounter,
    private val deleteCounter: DeleteCounter
) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _model = MutableLiveData<UiModel<List<Counter>>>()
    val model: LiveData<UiModel<List<Counter>>>
        get() {
            if (_model.value == null) getCounters()
            return _model
        }

    private val _modelCounter = MutableLiveData<UiModel<List<Counter>>>()
    val modelCounter: LiveData<UiModel<List<Counter>>> get() = _modelCounter

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

    fun increseCounter(product: Counter) {
        uiScope.launch {
            _modelCounter.value = UiModel.Loading
            try {
                _modelCounter.value = UiModel.Content(increseCounter.invoke(product))
            } catch (e: Exception) {
                _modelCounter.value = UiModel.Error(e)
            }
        }
    }

    fun decreseCounter(product: Counter) {
        uiScope.launch {
            _modelCounter.value = UiModel.Loading
            try {
                _modelCounter.value = UiModel.Content(decreseCounter.invoke(product))
            } catch (e: Exception) {
                _modelCounter.value = UiModel.Error(e)
            }
        }
    }

    fun deleteCounter(product: Counter) {
        uiScope.launch {
            _modelCounter.value = UiModel.Loading
            try {
                _modelCounter.value = UiModel.Content(deleteCounter.invoke(product))
            } catch (e: Exception) {
                _modelCounter.value = UiModel.Error(e)
            }
        }
    }
}