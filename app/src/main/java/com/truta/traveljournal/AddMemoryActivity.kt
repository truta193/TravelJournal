package com.truta.traveljournal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.truta.traveljournal.databinding.ActivityAddMemoryBinding

class AddMemoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMemoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMemoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.addMemoryToolbar
        setSupportActionBar(toolbar)
        title = "Add Memory"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}