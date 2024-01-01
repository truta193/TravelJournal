package com.truta.traveljournal.view

import com.truta.traveljournal.R
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.truta.traveljournal.databinding.ActivityDetailsBinding


class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.detailsToolbar
        setSupportActionBar(toolbar)
        title = "Details"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}