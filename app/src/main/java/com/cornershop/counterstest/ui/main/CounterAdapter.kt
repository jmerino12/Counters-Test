package com.cornershop.counterstest.ui.main


import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
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

class CounterAdapter(private val listener: OnOptionsCounterListener) :
    ListAdapter<CounterAdapter.DataItem, RecyclerView.ViewHolder>(CounterDiffCallback()) {


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

    var selectionTracker: SelectionTracker<String>? = null

    fun addHeaderAndSubmitList(list: List<Counter>) {
        adapterScope.launch {
            if (list.isNotEmpty()) {
                listCounter = list as ArrayList<Counter>
                val items = listOf(DataItem.Header) + list.map { DataItem.CounterItem(it) }
                withContext(Dispatchers.Main) {
                    submitList(items)
                }
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
                    selectionTracker!!.isSelected(getItem(position).id)
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

    fun clearData() {
        submitList(null)
        notifyDataSetChanged()
    }


    inner class CounterViewHolder(
        private val binding: ItemCounterBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val details
            get() = object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = bindingAdapterPosition

                override fun getSelectionKey(): String? = getItem(bindingAdapterPosition).id

            }

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


}