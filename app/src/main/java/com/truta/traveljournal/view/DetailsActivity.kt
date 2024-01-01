package com.truta.traveljournal.view

import com.truta.traveljournal.R
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.truta.traveljournal.TravelJournalApplication
import com.truta.traveljournal.databinding.ActivityDetailsBinding
import com.truta.traveljournal.viewmodel.AddMemoryModelFactory
import com.truta.traveljournal.viewmodel.AddMemoryViewModel
import com.truta.traveljournal.viewmodel.DetailsModelFactory
import com.truta.traveljournal.viewmodel.DetailsViewModel


class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var viewModel: DetailsViewModel
    private lateinit var mMap: GoogleMap

    private lateinit var titleView: TextView
    private lateinit var dateView: TextView
    private lateinit var moodView: ImageView
    private lateinit var mapLayout: FrameLayout
    private lateinit var typeView: TextView
    private lateinit var notesView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleView = binding.detailsTitle
        dateView = binding.detailsDate
        moodView = binding.detailsMood
        mapLayout = binding.detailsMapContainer
        typeView = binding.detailsType
        notesView = binding.detailsNotes

        viewModel = ViewModelProvider(
            this,
            DetailsModelFactory((this.application as TravelJournalApplication).repository)
        )[DetailsViewModel::class.java]

        viewModel.currentMemory =
            (viewModel.getMemoryById((intent.extras?.getInt("MEMORY_ID")!!))!!)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.detailsInputMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        titleView.text = viewModel.currentMemory?.title
        dateView.text = viewModel.currentMemory?.date.toString()
        if (viewModel.currentMemory?.placeLatitude == null || viewModel.currentMemory?.placeLongitude == null) {
            mapLayout.visibility = View.GONE
        } else {
            mapLayout.visibility = View.VISIBLE
        }


        when (viewModel.currentMemory!!.mood) {
            in 0.0..0.33 -> moodView.setImageDrawable(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.ic_mood_sad
                )
            )

            in 0.34..0.66 -> moodView.setImageDrawable(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.ic_mood_neutral
                )
            )

            else -> moodView.setImageDrawable(
                AppCompatResources.getDrawable(
                    applicationContext,
                    R.drawable.ic_mood_happy
                )
            )
        }

        typeView.text = viewModel.currentMemory!!.type
        notesView.text = viewModel.currentMemory!!.notes

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.i("LOGGING_ONMAP", viewModel.currentMemory?.placeLatitude.toString())
        if (viewModel.currentMemory!!.placeLongitude != null && viewModel.currentMemory!!.placeLatitude != null)
            mMap.addMarker(
                MarkerOptions().title(viewModel.currentMemory?.title).position(
                    LatLng(
                        viewModel.currentMemory!!.placeLatitude!!,
                        viewModel.currentMemory!!.placeLongitude!!
                    )
                )
            )

    }

}