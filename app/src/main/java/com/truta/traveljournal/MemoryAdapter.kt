package com.truta.traveljournal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.truta.traveljournal.databinding.ItemMemoryBinding

class MemoryAdapter(private val viewModel: HomeViewModel) :
    RecyclerView.Adapter<MemoryAdapter.MemoryViewHolder>() {

    inner class MemoryViewHolder(val itemBinding: ItemMemoryBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Memory) {
            itemBinding.item = item
            itemBinding.viewModel = viewModel
            itemBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        val binding = ItemMemoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MemoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        val item = viewModel.itemList.value?.get(position)!!
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return viewModel.itemList.value?.size ?: 0
    }

}
