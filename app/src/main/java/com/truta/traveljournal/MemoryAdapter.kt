package com.truta.traveljournal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.truta.traveljournal.databinding.ItemMemoryBinding

class MemoryAdapter(private val memories: List<Memory>) :
    RecyclerView.Adapter<MemoryAdapter.MemoryViewHolder>() {

    inner class MemoryViewHolder(val itemBinding: ItemMemoryBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        val binding = ItemMemoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MemoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        holder.itemBinding.placeName.text = memories[position].placeName
    }

    override fun getItemCount(): Int {
        return memories.size
    }

}
