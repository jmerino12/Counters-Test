package com.cornershop.counterstest.ui.addcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.ui.common.UiModel
import com.jmb.domain.Counter
import com.jmb.usecases.AddProduct
import kotlinx.coroutines.*

class AddCounterViewModel(private val addProduct: AddProduct) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _model = MutableLiveData<UiModel<List<Counter>>>()
    val model: LiveData<UiModel<List<Counter>>> get() = _model


    fun addProduct(product: Counter) {
        uiScope.launch {
            _model.value = UiModel.Loading
            try {
                _model.value = UiModel.Content(addProduct.invoke(product))
            } catch (e: Exception) {
                _model.value = UiModel.Error(e)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}