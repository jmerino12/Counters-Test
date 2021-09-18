package com.cornershop.counterstest.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ActivityMainScreenBinding
import com.cornershop.counterstest.ui.addcounter.AddCounter
import com.cornershop.counterstest.ui.common.*
import com.cornershop.counterstest.ui.utils.CounterDetailsLookup
import com.cornershop.counterstest.ui.utils.CounterKeyProvider
import com.jmb.domain.Counter
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainScreen : ScopeActivity(), CounterAdapter.OnOptionsCounterListener,
    SwipeRefreshLayout.OnRefreshListener, AddCounter.OnReloadData {

    private var actionMode: ActionMode? = null
    private lateinit var tracker: SelectionTracker<String>

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var adapter: CounterAdapter
    private lateinit var rvObserver: RvEmptyObserver
    lateinit var counterUpdating: Counter
    private lateinit var list: ArrayList<Counter>
    private var incrementOrDecrement: Boolean = false
    private var show: Boolean = false
    var transition: Transition = Fade()


    private val viewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CounterAdapter(this)
        viewModel.model.observe(this, Observer(::updateUi))
        viewModel.modelCounter.observe(this, Observer(::updateUiCounter))
        viewModel.modelDeleteCounter.observe(this, Observer(::updateUiCounterDelete))

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
                    updateContextualActionBarTitle()
                    transition.duration = 250
                    transition.addTarget(binding.searchView.id)
                    TransitionManager.beginDelayedTransition(binding.root, transition)
                    binding.searchView.visibility = View.GONE
                } else {
                    actionMode?.finish()
                }
            }
        })

        binding.contentError.btnRety.setOnClickListener { getCounters() }
        list = ArrayList()
    }

    private fun updateUi(model: UiModel<List<Counter>>) {
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
                adapter.counters = model.data
                Log.i("Main", model.data.toString())
            }
            is UiModel.Error -> {
                Log.e("Main", model.error.toString())
            }
        }
        binding.rvCounterInclude.swiperefresh.isRefreshing = false
    }

    private fun updateUiCounterDelete(model: UiModel<List<Counter>>) {
        binding.progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is UiModel.Content -> {
                getCounters()
            }
            is UiModel.Error -> {
                errorDeleteCounter()
                Log.e("error", model.error.toString())
            }
        }
    }

    private fun errorDeleteCounter() {
        if (!show) {
            alert {
                setTitle(
                    getString(R.string.error_deleting_counter_title)
                )
                setMessage(getString(R.string.connection_error_description))
                positiveButton(text = getString(R.string.ok)) { }

            }
            show = true
        }
    }

    private fun updateUiCounter(model: UiModel<List<Counter>>) {
        binding.progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is UiModel.Content -> {
                getCounters()
            }
            is UiModel.Error -> {
                Log.e("error", model.error.toString())
                alert {
                    setTitle(
                        getString(
                            R.string.error_updating_counter_title,
                            counterUpdating.title,
                            if (incrementOrDecrement) counterUpdating.count + 1
                            else counterUpdating.count - 1
                        )
                    )
                    setMessage(getString(R.string.connection_error_description))
                    negativeButton(text = getString(R.string.retry)) { }
                    positiveButton(text = getString(R.string.dismiss)) { }


                }
            }
        }
    }

    private fun updateContextualActionBarTitle() {
        actionMode?.title = "${tracker.selection.size()} seleccionados"
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
            binding.searchView.visibility = View.VISIBLE

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
                tracker.selection.map { viewModel.deleteCounter(it) }
                actionMode!!.finish()
                show = false
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

    override fun onRefresh() {
        adapter.clearData()
        getCounters()
    }

    override fun increment(item: Counter, position: Int) {
        viewModel.increseCounter(item)
        counterUpdating = item
        incrementOrDecrement = true
        adapter.notifyItemChanged(position)
    }

    override fun decrement(item: Counter, position: Int) {
        if (item.count > 0) {
            viewModel.decreseCounter(item)
            counterUpdating = item
            incrementOrDecrement = false
            adapter.notifyItemChanged(position)
        }
    }

    override fun onReload(boolean: Boolean) {
        if (boolean) getCounters()
    }
}

