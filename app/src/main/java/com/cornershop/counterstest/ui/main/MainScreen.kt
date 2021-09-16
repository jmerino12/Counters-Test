package com.cornershop.counterstest.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.ActivityMainScreenBinding
import com.cornershop.counterstest.ui.addcounter.AddCounter
import com.cornershop.counterstest.ui.common.RvEmptyObserver
import com.cornershop.counterstest.ui.common.UiModel
import com.jmb.domain.Counter
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainScreen : ScopeActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var adapter: CounterAdapter
    private lateinit var rvObserver: RvEmptyObserver


    private val viewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CounterAdapter()
        viewModel.model.observe(this, Observer(::updateUi))

        binding.addCounter.setOnClickListener {
            val fragment: AddCounter = AddCounter()
            fragment.show(
                supportFragmentManager.beginTransaction(),
                AddCounter.TAG
            )
        }
        binding.rvCounterInclude.rvCounter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCounterInclude.rvCounter.adapter = adapter
    }

    private fun updateUi(model: UiModel<List<Counter>>) {

        binding.progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is UiModel.Content -> {
                rvObserver = RvEmptyObserver(binding.rvCounterInclude.rvCounter, binding.noContent.root)
                //(binding.rvCounterInclude.rvCounter.adapter as RecyclerView.Adapter).registerAdapterDataObserver(rvObserver)
                Log.i("Main", model.data.toString())
            }
            is UiModel.Error -> {
                Log.e("Main", model.error.toString())
            }

        }
    }
}