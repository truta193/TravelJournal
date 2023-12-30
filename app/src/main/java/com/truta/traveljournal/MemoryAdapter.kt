package com.truta.traveljournal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.truta.traveljournal.databinding.ItemMemoryBinding
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.viewmodel.HomeViewModel

class MemoryAdapter(private val viewModel: HomeViewModel) :
    RecyclerView.Adapter<MemoryAdapter.MemoryViewHolder>() {

    inner class MemoryViewHolder(val itemBinding: ItemMemoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
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
        val item = viewModel.memories.value?.get(position)!!
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return viewModel.memories.value?.size ?: 0
    }

}
