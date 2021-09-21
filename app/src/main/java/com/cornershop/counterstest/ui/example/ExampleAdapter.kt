package com.cornershop.counterstest.ui.example


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.ItemChipsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ExampleAdapter(private val listener: OnExampleClick) :
    ListAdapter<String, RecyclerView.ViewHolder>(ExampleDiffCallback()) {

    class ExampleDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == oldItem
        }

    }

    private val adapterScope = CoroutineScope(Dispatchers.IO)

    fun addHeaderAndSubmitList(list: ArrayList<String>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ExampleViewHolder(
            ItemChipsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ExampleViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }


    inner class ExampleViewHolder(
        private val binding: ItemChipsBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.chipExample.text = item
            binding.root.setOnClickListener { listener.onClick(item) }
        }
    }


}