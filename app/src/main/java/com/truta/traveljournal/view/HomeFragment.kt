package com.truta.traveljournal.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.truta.traveljournal.MarginItemDecoration
import com.truta.traveljournal.viewmodel.HomeViewModel
import com.truta.traveljournal.adapter.MemoryAdapter
import com.truta.traveljournal.TravelJournalApplication
import com.truta.traveljournal.databinding.FragmentHomeBinding
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.viewmodel.HomeMemoryModelFactory
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels({
        requireActivity()
    }) {
        HomeMemoryModelFactory((requireActivity().application as TravelJournalApplication).repository)
    }

    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        val adapter = MemoryAdapter(viewModel) { memory ->
            run {
                val i = Intent(this.context, DetailsActivity::class.java)
                i.putExtra("MEMORY_ID", memory.id)
                startActivity(i)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(16)
        )
        viewModel.memories.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        viewModel.shareText.observe(viewLifecycleOwner) {
            launchShare()
        }


        fab = binding.fabAdd
        fab.setOnClickListener {
//            val mem = Memory("Name", "Place", false)
//            viewModel.upsertMemory(mem)
            val i = Intent(this.context, AddEditMemoryActivity::class.java)
            startActivity(i);
        }

        return binding.root
    }

    fun launchShare() = viewModel.viewModelScope.launch {
       val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, viewModel.shareText.value)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}