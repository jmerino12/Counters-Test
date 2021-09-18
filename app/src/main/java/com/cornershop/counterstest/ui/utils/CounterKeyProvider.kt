package com.cornershop.counterstest.ui.utils

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.ui.main.CounterAdapter

class CounterKeyProvider(private val adapter: CounterAdapter) :
    ItemKeyProvider<String>(SCOPE_CACHED) {
    override fun getKey(position: Int): String? {
        return adapter.currentList[position].id
    }

    override fun getPosition(key: String): Int {
        return adapter.currentList.indexOfFirst { counter -> counter.id == key }
    }
}

class CounterDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<String>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view: View? = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val holder = recyclerView.getChildViewHolder(view)
            if (holder is CounterAdapter.ViewHolder) {
                return holder.details
            }
        }
        return null
    }

}
