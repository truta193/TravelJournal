package com.truta.traveljournal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.truta.traveljournal.databinding.ItemMemoryBinding
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.viewmodel.HomeViewModel
import java.io.File

class MemoryAdapter(
    private val viewModel: HomeViewModel, private val onItemClick: (Memory) -> Unit
) : RecyclerView.Adapter<MemoryAdapter.MemoryViewHolder>() {

    inner class MemoryViewHolder(
        val itemBinding: ItemMemoryBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(viewModel.memories.value?.get(position)!!)
                }
            }
        }

        fun bind(item: Memory) {
            itemBinding.item = item
            itemBinding.viewModel = viewModel

            if (item.pictures != null) {
                if (item.pictures!!.isNotEmpty())
                    Glide.with(itemBinding.thumbnailView.context)
                        .load(File(item.pictures!![0]))
                        .into(itemBinding.thumbnailView)
            }

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

