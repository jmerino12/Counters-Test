package com.cornershop.counterstest.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.SearchContentBinding
import com.cornershop.counterstest.ui.common.RvEmptyObserver
import com.jmb.domain.Counter


class SearchCounter(val list: ArrayList<Counter>) : DialogFragment(),
    SearchAdapter.OnOptionsCounterListener {

    private var _binding: SearchContentBinding? = null
    private lateinit var adapter: SearchAdapter
    private val binding get() = _binding!!
    private lateinit var rvObserver: RvEmptyObserver

    companion object {
        const val TAG = "Search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            R.style.FullScreenDialogTransparent
        )
        adapter = SearchAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchContentBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
        adapter.listCounterCopy = list
        adapter.addHeaderAndSubmitList(list)
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.rvCounter.adapter = adapter
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val menuItem = menu.findItem(R.id.search)
        menuItem.expandActionView()
        val search = menu.findItem(R.id.search).actionView as SearchView
        search.clearFocus()
        search.queryHint = getString(R.string.search_counters)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return true

            }
        })
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                this@SearchCounter.dismiss()
                return true
            }

        })
        super.onCreateOptionsMenu(menu, inflater);
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun increment(item: Counter, position: Int) {
    }

    override fun decrement(item: Counter, position: Int) {
    }
}