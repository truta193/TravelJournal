package com.truta.traveljournal.adapter

import android.content.Context
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
import com.truta.traveljournal.viewmodel.DetailsViewModel

class PictureAdapter(
    private val viewModel: AddEditMemoryViewModel, private val context: Context, private val onItemClick: (Memory) -> Unit
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
        val uri = viewModel.pictureUris[position]
        Glide.with(context).load(Uri.parse(uri)).into(holder.itemBinding.galleryPicture)

    }

    override fun getItemCount(): Int {
        return viewModel.pictureUris.size
    }

}

class PictureAdapterD(
    private val viewModel: DetailsViewModel, private val context: Context, private val onItemClick: (Memory) -> Unit
) : RecyclerView.Adapter<PictureAdapterD.PictureViewHolder>() {

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
    ): PictureAdapterD.PictureViewHolder {
        val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PictureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureAdapterD.PictureViewHolder, position: Int) {
        if (viewModel.pictureUris.isEmpty()) return
        val uri = viewModel.pictureUris[position]
        Glide.with(context).load(Uri.parse(uri)).into(holder.itemBinding.galleryPicture)

    }

    override fun getItemCount(): Int {
        return viewModel.pictureUris.size
    }

}