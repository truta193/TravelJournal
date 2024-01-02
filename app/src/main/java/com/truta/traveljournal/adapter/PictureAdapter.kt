package com.truta.traveljournal.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.truta.traveljournal.databinding.ItemMemoryBinding
import com.truta.traveljournal.databinding.ItemPictureBinding
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.viewmodel.AddEditMemoryViewModel

class PictureAdapter(
    private val viewModel: AddEditMemoryViewModel, private val onItemClick: (Memory) -> Unit
) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    inner class PictureViewHolder(
        val itemBinding: ItemPictureBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        init {
//            itemView.setOnClickListener {
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    onItemClick(viewModel.memories.value?.get(position)!!)
//                }
//            }
        }

        fun bind(uri: Uri) {
            itemBinding.galleryPicture.setImageURI(uri)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PictureAdapter.PictureViewHolder {
        val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureAdapter.PictureViewHolder, position: Int) {
        if (viewModel.pictureUris.isEmpty()) return
        val context = holder.itemView.context
        val uri = viewModel.pictureUris[position]
        Glide.with(context).load(Uri.parse(uri)).into(holder.itemBinding.galleryPicture)

    }

    override fun getItemCount(): Int {
        return viewModel.pictureUris.size
    }

}