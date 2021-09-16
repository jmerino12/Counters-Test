package com.cornershop.counterstest.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ActivityMainScreenBinding
import com.cornershop.counterstest.ui.addcounter.AddCounter
import com.cornershop.counterstest.ui.common.RvEmptyObserver
import com.cornershop.counterstest.ui.common.UiModel
import com.jmb.domain.Counter
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainScreen : ScopeActivity(), SwipeRefreshLayout.OnRefreshListener {

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
            val fragment = AddCounter()
            fragment.show(
                supportFragmentManager.beginTransaction(),
                AddCounter.TAG
            )
        }
        binding.rvCounterInclude.swiperefresh.setColorSchemeResources(R.color.orange)
        binding.rvCounterInclude.swiperefresh.setOnRefreshListener(this)
        binding.rvCounterInclude.rvCounter.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCounterInclude.rvCounter.adapter = adapter

        binding.contentError.btnRety.setOnClickListener { onRefresh() }
    }

    private fun updateUi(model: UiModel<List<Counter>>) {

        binding.progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        binding.contentError.root.isVisible = model is UiModel.Error
        binding.noContent.root.isVisible = model is UiModel.Content
        binding.rvCounterInclude.swiperefresh.isRefreshing = model is UiModel.Loading

        when (model) {
            is UiModel.Content -> {
                rvObserver =
                    RvEmptyObserver(binding.rvCounterInclude.rvCounter, binding.noContent.root)
                (binding.rvCounterInclude.rvCounter.adapter as RecyclerView.Adapter).registerAdapterDataObserver(
                    rvObserver
                )
                Log.i("Main", model.data.toString())
            }
            is UiModel.Error -> {
                Log.e("Main", model.error.toString())
            }

        }
    }

    override fun onRefresh() {
        viewModel.getCounters()
    }
}