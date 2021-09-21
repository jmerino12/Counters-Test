package com.cornershop.counterstest.ui.search


import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.text.bold
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.databinding.RecyclerHeaderBinding
import com.jmb.domain.Counter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val HEADER_VIEW_TYPE = 1
private const val TYPE_ITEM = 2

class SearchAdapter(private val listener: OnOptionsCounterListener) :
    ListAdapter<SearchAdapter.DataItem, RecyclerView.ViewHolder>(CounterDiffCallback()),
    Filterable {


    class CounterDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == oldItem
        }

    }

    private val adapterScope = CoroutineScope(Dispatchers.IO)
    private lateinit var listCounter: ArrayList<Counter>
    lateinit var listCounterCopy: ArrayList<Counter>


    fun addHeaderAndSubmitList(list: List<Counter>) {
        adapterScope.launch {
            listCounter = list as ArrayList<Counter>
            val items = listOf(DataItem.Header) + list.map { DataItem.CounterItem(it) }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    interface OnOptionsCounterListener {
        fun increment(item: Counter, position: Int)
        fun decrement(item: Counter, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                CounterViewHolder(
                    ItemCounterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            HEADER_VIEW_TYPE -> {
                HeaderViewHolder(
                    RecyclerHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                throw ClassCastException("No se encuentra viewtype $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CounterViewHolder -> {
                holder.bind(
                    getItem(position) as DataItem.CounterItem,
                    position,
                    false
                )
            }
            is HeaderViewHolder -> {
                holder.bind(
                    holder.itemView.context.getString(R.string.n_items, listCounter.size),
                    holder.itemView.context.getString(R.string.n_items, getCounterItems())
                )
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_VIEW_TYPE
        } else {
            TYPE_ITEM
        }
    }

    private fun getCounterItems(): Int {
        var counter = 0
        listCounter.forEach {
            counter += it.count
        }
        return counter
    }


    inner class CounterViewHolder(
        private val binding: ItemCounterBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem.CounterItem, position: Int, selected: Boolean) {
            binding.count.text = item.counter.count.toString()
            binding.nameItem.text = item.counter.title


            binding.root.isChecked = selected
            binding.group.visibility = if (!selected) View.VISIBLE else View.GONE


            if (item.counter.count <= 0) {
                binding.btnLess.isEnabled = false
                binding.btnLess.setColorFilter(Color.rgb(136, 139, 144));
            } else {
                binding.btnLess.isEnabled = true
                binding.btnLess.setColorFilter(Color.rgb(255, 149, 0));
            }


            binding.btnPlus.setOnClickListener { listener.increment(item.counter, position) }
            binding.btnLess.setOnClickListener { listener.decrement(item.counter, position) }
        }
    }

    inner class HeaderViewHolder(private val binding: RecyclerHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(n_items: String, n_times: String) {
            binding.headerTitle.text = SpannableStringBuilder().bold {
                append(
                    n_items
                )
            }.append(" $n_times")
        }
    }

    sealed class DataItem {
        data class CounterItem(val counter: Counter) : DataItem() {
            override val id: String = counter.id!!
        }

        object Header : DataItem() {
            override val id: String = ""
        }

        abstract val id: String
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.isEmpty()) {
                    filterResults.count = listCounterCopy.size
                    filterResults.values = listCounterCopy
                } else {
                    val search = constraint.toString().lowercase()

                    val userList = ArrayList<Counter>()
                    for (item in listCounterCopy) {
                        if (item.title!!.lowercase().contains(search)) {
                            userList.add(item)
                        }
                    }
                    filterResults.count = userList.size
                    filterResults.values = userList
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val list = results!!.values as ArrayList<Counter>
                addHeaderAndSubmitList(list)
                notifyItemRangeChanged(0, list.size)
            }

        }
    }


}