package com.truta.traveljournal.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.truta.traveljournal.MarginItemDecoration
import com.truta.traveljournal.viewmodel.HomeViewModel
import com.truta.traveljournal.MemoryAdapter
import com.truta.traveljournal.TravelJournalApplication
import com.truta.traveljournal.databinding.FragmentHomeBinding
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.viewmodel.MemoryModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels({
        requireActivity()
    }) {
        MemoryModelFactory((requireActivity().application as TravelJournalApplication).repository)
    }

    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        val adapter = MemoryAdapter(viewModel)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(16)
        )
        viewModel.memories.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }



        fab = binding.fabAdd
        fab.setOnClickListener {
            val mem = Memory("Name", "Place", false)
            viewModel.upsertMemory(mem)
//            val i = Intent(this.context, AddMemoryActivity::class.java)
//            startActivity(i)
        }

        return binding.root

    }
}