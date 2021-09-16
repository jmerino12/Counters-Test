package com.cornershop.counterstest.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.ui.common.basicDiffUtil
import com.jmb.domain.Counter

class CounterAdapter :
    RecyclerView.Adapter<CounterAdapter.ViewHolder>() {

    var counters: List<Counter> by basicDiffUtil(
        emptyList(),
        areItemsTheSame = { old, new -> old.id == new.id }
    )

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
                holder.bind(counters[position], position)
            }
            else -> {
                throw IllegalStateException("ViewType no declarado ")
            }
        }
    }

    override fun getItemCount(): Int = counters.size


    inner class ViewHolder(
        private val binding: ItemCounterBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Counter, position: Int) {
            binding.count.text = item.count.toString()
            binding.nameItem.text = item.title
        }
    }
}