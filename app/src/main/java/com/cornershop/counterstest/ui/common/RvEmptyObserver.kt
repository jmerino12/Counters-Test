package com.cornershop.counterstest.ui.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RvEmptyObserver(private val recyclerView: RecyclerView, private val emptyView: View):
RecyclerView.AdapterDataObserver(){

    init {
        checkIfEmpty()
    }

    private fun checkIfEmpty() {
        if (recyclerView.adapter != null){
            val emptyViewVisible = recyclerView.adapter!!.itemCount == 0
            emptyView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }

    override fun onChanged() {
        checkIfEmpty()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

}