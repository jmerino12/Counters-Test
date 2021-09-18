package com.cornershop.counterstest.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.ActionMode
import androidx.core.text.bold
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ActivityMainScreenBinding
import com.cornershop.counterstest.ui.addcounter.AddCounter
import com.cornershop.counterstest.ui.common.*
import com.cornershop.counterstest.ui.utils.CounterDetailsLookup
import com.cornershop.counterstest.ui.utils.CounterKeyProvider
import com.jmb.domain.Counter
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainScreen : ScopeActivity(), SwipeRefreshLayout.OnRefreshListener,
    CounterAdapter.OnOptionsCounterListener {
    private var actionMode: ActionMode? = null
    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var adapter: CounterAdapter
    private lateinit var rvObserver: RvEmptyObserver
    private lateinit var tracker: SelectionTracker<String>
    private lateinit var list: ArrayList<Counter>
    lateinit var counterUpdating: Counter


    private val viewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CountersNormal)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        adapter = CounterAdapter(this)
        viewModel.model.observe(this, Observer(::updateUi))
        viewModel.modelCounter.observe(this, Observer(::updateUiCounter))

        binding.rvCounterInclude.swiperefresh.setColorSchemeResources(R.color.orange)
        binding.rvCounterInclude.swiperefresh.setOnRefreshListener(this)
        binding.contentError.btnRety.setOnClickListener { getCounters() }

        binding.rvCounterInclude.rvCounter.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCounterInclude.rvCounter.adapter = adapter

        tracker = SelectionTracker.Builder(
            "counter-tracker",
            binding.rvCounterInclude.rvCounter,
            CounterKeyProvider(adapter),
            CounterDetailsLookup(binding.rvCounterInclude.rvCounter),
            StorageStrategy.createStringStorage()
        ).build()

        adapter.selectionTracker = tracker


        tracker.addObserver(object : SelectionTracker.SelectionObserver<String>() {
            override fun onSelectionChanged() {
                if (tracker.hasSelection()) {
                    if (actionMode == null) {
                        actionMode = startSupportActionMode(actionModeCallBack)
                    }
                    binding.appbar.visibility = View.GONE
                    updateContextualActionBarTitle()
                } else {
                    actionMode?.finish()
                }
            }
        })

        binding.addCounter.setOnClickListener {
            val fragment = AddCounter()
            fragment.show(
                supportFragmentManager.beginTransaction(),
                AddCounter.TAG
            )
        }
        list = ArrayList()
    }

    private fun updateContextualActionBarTitle() {
        actionMode?.title = "${tracker.selection.size()} seleccionados"
    }

    private fun updateUi(model: UiModel<List<Counter>>) {
        list.clear()
        binding.progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        binding.contentError.root.isVisible = model is UiModel.Error
        binding.noContent.root.isVisible = model is UiModel.Content
        when (model) {
            is UiModel.Content -> {
                rvObserver =
                    RvEmptyObserver(binding.rvCounterInclude.rvCounter, binding.noContent.root)
                (binding.rvCounterInclude.rvCounter.adapter as RecyclerView.Adapter).registerAdapterDataObserver(
                    rvObserver
                )
                list = model.data as ArrayList<Counter>
                adapter.submitList(model.data)
                if (model.data.isNotEmpty()) {
                    binding.counterItems.text = SpannableStringBuilder().bold {
                        append(
                            "${
                                getString(
                                    R.string.n_items,
                                    model.data.size
                                )
                            } "
                        )
                    }.append(getString(R.string.n_times, model.data.size))
                }
            }
            is UiModel.Error -> {
                Log.e("Main", model.error.toString())
            }
        }
        binding.rvCounterInclude.swiperefresh.isRefreshing = false

    }

    private fun updateUiCounter(model: UiModel<List<Counter>>) {
        binding.progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is UiModel.Content -> {
                getCounters()
            }
            is UiModel.Error -> {
                alert {
                    setTitle(
                        getString(
                            R.string.error_updating_counter_title,
                            counterUpdating.title,
                            counterUpdating.count
                        )
                    )
                    setMessage(getString(R.string.connection_error_description))
                    negativeButton(text = getString(R.string.retry)) { }
                    positiveButton(text = getString(R.string.dismiss)) { }

                }
            }
        }

    }

    private val actionModeCallBack = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_contextual, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_delete -> {
                    deleteCounter()
                    true
                }
                R.id.action_share -> {
                    shared()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            tracker.clearSelection()
            actionMode = null
            binding.appbar.visibility = View.VISIBLE
        }

    }

    private fun deleteCounter() {
        var delete = ""
        list.forEach { counters ->
            tracker.selection.forEach { id ->
                if (counters.id.equals(id, true)) {
                    delete += "${counters.title} "
                }
            }
        }

        alert {
            setTitle(getString(R.string.delete_x_question, delete))
            positiveButton(getString(R.string.delete)) {
                tracker.selection.map { viewModel.deleteCounter(Counter(id = it)) }
                actionMode!!.finish()

            }
            negativeButton(getString(R.string.cancel)) {}
        }

    }

    private fun shared() {
        val sharedIntent = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "This is my text ti send")
            putExtra(Intent.EXTRA_TITLE, "Introducing content previews")
            type = "text/plain"
        }, getString(R.string.share))
        startActivity(sharedIntent)
    }

    fun getCounters() {
        viewModel.getCounters()
    }

    override fun onResume() {
        super.onResume()
        getCounters()
    }

    override fun onRefresh() {
        getCounters()
    }

    override fun increment(item: Counter, position: Int) {
        viewModel.increseCounter(item)
        counterUpdating = item
        adapter.notifyItemChanged(position)
    }

    override fun decrement(item: Counter, position: Int) {
        if (item.count > 0) {
            viewModel.decreseCounter(item)
            counterUpdating = item

        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        Log.i("dada", item.toString())
        return true
    }

}