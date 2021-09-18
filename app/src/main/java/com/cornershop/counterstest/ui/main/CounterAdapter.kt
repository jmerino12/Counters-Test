package com.cornershop.counterstest.ui.main


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.ui.common.basicDiffUtil
import com.jmb.domain.Counter

class CounterAdapter(private val listener: OnOptionsCounterListener) :
    RecyclerView.Adapter<CounterAdapter.ViewHolder>() {

    var counters: ArrayList<Counter> by basicDiffUtil(
        ArrayList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

    var selectionTracker: SelectionTracker<String>? = null

    interface OnOptionsCounterListener {
        fun increment(item: Counter, position: Int)
        fun decrement(item: Counter, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =
            ItemCounterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                selectionTracker?.let {
                    holder.bind(counters[position], position, it.isSelected(counters[position].id))
                }
            }
            else -> {
                throw IllegalStateException("ViewType no declarado ")
            }
        }
    }

    override fun getItemCount(): Int = counters.size

    public fun clearData() {
        counters.clear()
        notifyDataSetChanged()
    }


    inner class ViewHolder(
        private val binding: ItemCounterBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val details
            get() = object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition(): Int = bindingAdapterPosition

                override fun getSelectionKey(): String? = counters[bindingAdapterPosition].id

            }

        fun bind(item: Counter, position: Int, selected: Boolean) {
            binding.count.text = item.count.toString()
            binding.nameItem.text = item.title

            binding.root.isChecked = selected
            binding.group.visibility = if (!selected) View.VISIBLE else View.GONE

            if (item.count <= 0) {
                binding.btnLess.isEnabled = false
                binding.btnLess.setColorFilter(Color.rgb(136, 139, 144));
            } else {
                binding.btnLess.isEnabled = true
                binding.btnLess.setColorFilter(Color.rgb(255, 149, 0));
            }


            binding.btnPlus.setOnClickListener { listener.increment(item, position) }
            binding.btnLess.setOnClickListener { listener.decrement(item, position) }

        }
    }
}