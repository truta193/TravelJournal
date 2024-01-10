package com.truta.traveljournal.view

import android.content.Intent
import com.truta.traveljournal.R
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.truta.traveljournal.BuildConfig
import com.truta.traveljournal.MarginItemDecoration
import com.truta.traveljournal.TravelJournalApplication
import com.truta.traveljournal.adapter.PictureAdapterD
import com.truta.traveljournal.databinding.ActivityDetailsBinding
import com.truta.traveljournal.service.WeatherService
import com.truta.traveljournal.viewmodel.DetailsModelFactory
import com.truta.traveljournal.viewmodel.DetailsViewModel
import com.bumptech.glide.Glide
import java.io.File


class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var viewModel: DetailsViewModel
    private var mMap: GoogleMap? = null

    private lateinit var titleView: TextView
    private lateinit var dateView: TextView
    private lateinit var moodView: ImageView
    private lateinit var mapLayout: FrameLayout
    private lateinit var typeView: TextView
    private lateinit var notesView: TextView
    private lateinit var recyclerView: RecyclerView
    private var marker: Marker? = null



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
            this, DetailsModelFactory(
                (this.application as TravelJournalApplication).repository,
                WeatherService.weatherService
            )
        )[DetailsViewModel::class.java]

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        viewModel.weatherGetSuccess.observe(this) {
            binding.weatherDetail.text = viewModel.details.value
            binding.weatherTemp.text = viewModel.temp.value
            Glide.with(this)
                .load(viewModel.icon.value)
                .into(binding.weatherIcon)
        }
        viewModel.weatherGetFailure.observe(this) {
            if (it) {
                Toast.makeText(
                    this,
                    "Failed Weather",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.eventWeatherGetFailureFinish()
            }
        }

        viewModel.memories.observe(this) {
            viewModel.currentMemory = viewModel.getMemoryById(intent.extras?.getInt("MEMORY_ID")!!)
            if (viewModel.currentMemory != null)
                updateUI()
        }

        recyclerView = binding.detailsPicturesRecyclerView

        val adapter = PictureAdapterD(viewModel, applicationContext) { memory ->
            run {}
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            MarginItemDecoration(16)
        )


        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.detailsInputMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.detailsEditMenu -> {
            val i = Intent(this, AddEditMemoryActivity::class.java)
            i.putExtra("MEMORY_ID", viewModel.currentMemory?.id)
            startActivity(i);
            true
        }
        R.id.detailsDeleteMenu -> {
            if (viewModel.currentMemory != null)
                viewModel.deleteMemory(viewModel.currentMemory!!)
                finish()
            true
        }

        else -> super.onOptionsItemSelected(item)

    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val isSatellite = PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(getString(R.string.map_satellite_view_settings), false)
        if (isSatellite) {
            mMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
        } else {
            mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        }

        if (viewModel.currentMemory!!.placeLongitude != null && viewModel.currentMemory!!.placeLatitude != null) {
            marker =
                mMap?.addMarker(
                    MarkerOptions().title(viewModel.currentMemory?.title).position(
                        LatLng(
                            viewModel.currentMemory!!.placeLatitude!!,
                            viewModel.currentMemory!!.placeLongitude!!
                        )
                    )
                )

            viewModel.getWeatherData(
                viewModel.currentMemory!!.placeLatitude!!,
                viewModel.currentMemory!!.placeLatitude!!,
                BuildConfig.WEATHER_API_KEY
            )
        }

        if (viewModel.currentMemory?.pictures != null) {
            if (viewModel.currentMemory?.pictures!!.isNotEmpty()) {
                Glide.with(binding.detailsThumbnail.context)
                    .load(File(viewModel.currentMemory?.pictures!![0]))
                    .into(binding.detailsThumbnail)
            }
        }

    }

    fun updateUI() {
        titleView.text = viewModel.currentMemory?.title
        dateView.text = viewModel.currentMemory?.date.toString()

        if (viewModel.currentMemory?.placeLatitude == null || viewModel.currentMemory?.placeLongitude == null) {
            binding.detailsWeatherContainer.visibility = View.GONE
            mapLayout.visibility = View.GONE
        } else {
            binding.detailsWeatherContainer.visibility = View.VISIBLE
            mapLayout.visibility = View.VISIBLE
        }

        when (viewModel.currentMemory!!.mood) {
            in 0.0..0.33 -> moodView.setImageDrawable(
                AppCompatResources.getDrawable(
                    applicationContext, R.drawable.ic_mood_sad
                )
            )

            in 0.34..0.66 -> moodView.setImageDrawable(
                AppCompatResources.getDrawable(
                    applicationContext, R.drawable.ic_mood_neutral
                )
            )

            else -> moodView.setImageDrawable(
                AppCompatResources.getDrawable(
                    applicationContext, R.drawable.ic_mood_happy
                )
            )
        }

        typeView.text = viewModel.currentMemory?.type
        notesView.text = viewModel.currentMemory?.notes

        marker?.remove()
        if (viewModel.currentMemory!!.placeLongitude != null && viewModel.currentMemory!!.placeLatitude != null) marker =
            mMap?.addMarker(
                MarkerOptions().title(viewModel.currentMemory?.title).position(
                    LatLng(
                        viewModel.currentMemory!!.placeLatitude!!,
                        viewModel.currentMemory!!.placeLongitude!!
                    )
                )
            )

        viewModel.picturePaths = viewModel.currentMemory?.pictures?.toMutableList() ?: mutableListOf()
        Log.e("DETAILSPIC", viewModel.picturePaths.toString())
        recyclerView.adapter?.notifyDataSetChanged()
    }
}