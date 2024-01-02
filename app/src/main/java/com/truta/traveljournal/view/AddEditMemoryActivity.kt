package com.truta.traveljournal.view

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.truta.traveljournal.BuildConfig
import com.truta.traveljournal.MarginItemDecoration
import com.truta.traveljournal.R
import com.truta.traveljournal.TravelJournalApplication
import com.truta.traveljournal.adapter.MemoryAdapter
import com.truta.traveljournal.adapter.PictureAdapter
import com.truta.traveljournal.databinding.ActivityAddEditMemoryBinding
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.viewmodel.AddMemoryModelFactory
import com.truta.traveljournal.viewmodel.AddEditMemoryViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class AddEditMemoryActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAddEditMemoryBinding
    private lateinit var viewModel: AddEditMemoryViewModel

    private lateinit var nameView: TextInputEditText
    private lateinit var dateView: TextInputEditText
    private lateinit var typeView: AutoCompleteTextView
    private lateinit var moodView: Slider
    private lateinit var notesView: TextInputEditText
    private lateinit var doneFab: FloatingActionButton
    private lateinit var switchView: SwitchMaterial
    private lateinit var inputMapSearch: ImageButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var placesClient: PlacesClient

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    if (place.name == null || place.latLng == null)
                        return@registerForActivityResult
                    moveMarker(place.name!!, place.latLng!!)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng!!))
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Log.i("TAG", "User canceled autocomplete")
            }
        }

    private var pictureSelectorLauncher =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty())
                for (uri in uris) {
                    Log.e("URITEST", uri.toString())
                    viewModel.pictureUris.add(uri)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
        }

    private val requestPermissionForImageLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                pictureSelectorLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }



    private val calendar: Calendar = Calendar.getInstance()

    private lateinit var geocoder: Geocoder
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditMemoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
//region Init stuff
        if (!Places.isInitialized()) {
            Places.initialize(
                applicationContext,
                BuildConfig.MAPS_API_KEY,
                Locale.US
            );
            placesClient = Places.createClient(applicationContext)
        }

        geocoder = Geocoder(this, Locale.getDefault())

        viewModel = ViewModelProvider(
            this,
            AddMemoryModelFactory((this.application as TravelJournalApplication).repository)
        )[AddEditMemoryViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.inputMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
//endregion
        switchView = binding.switch1
        switchView.setOnCheckedChangeListener { button, b ->
            if (b) {
                binding.mapContainer.visibility = View.VISIBLE
                inputMapSearch.visibility = View.VISIBLE

            } else {
                binding.mapContainer.visibility = View.GONE
                inputMapSearch.visibility = View.GONE
                viewModel.marker?.remove()
                viewModel.marker = null
            }
        }

        inputMapSearch = binding.inputMapSearch
        inputMapSearch.setOnClickListener {
            launchPlacesSearch()
        }


        val adapter = PictureAdapter(viewModel) { memory ->
            run {}
        }
        recyclerView = binding.addEditPictureRecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            MarginItemDecoration(16)
        )


        nameView = binding.inputName
        dateView = binding.inputDate
        typeView = binding.inputType
        moodView = binding.inputMoodSlider
        notesView = binding.inputNotes

        val toolbar = binding.addMemoryToolbar
        setSupportActionBar(toolbar)

        if (intent.hasExtra("MEMORY_ID")) {
            title = "Edit Memory"
            val memory = viewModel.getMemoryById(intent.extras!!.getInt("MEMORY_ID"))
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yy")

            nameView.setText(memory.title)
            dateView.setText(memory.date.format(formatter))
            typeView.setText(memory.type)
            moodView.value = memory.mood.toFloat()
            notesView.setText(memory.notes)
            switchView.isChecked = memory.placeLatitude != null && memory.placeLongitude != null
            //viewModel.pictureUris = memory.pictures?.toMutableList() ?: mutableListOf()
        } else
            title = "Add Memory"

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val travelTypes = resources.getStringArray(R.array.travel_types)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown, travelTypes)
        binding.inputType.setAdapter(arrayAdapter)


        val date =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }

        dateView.setOnClickListener {
            this.let { it1 ->
                DatePickerDialog(
                    it1,
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        binding.buttonAddPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionForImageLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                pictureSelectorLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

        }

        doneFab = binding.fabDone
        doneFab.setOnClickListener {
            if (nameView.text.toString().trim().isEmpty() || dateView.text.toString().trim()
                    .isEmpty()
            ) {
                Toast.makeText(this, "Title and Date cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formatter = DateTimeFormatter.ofPattern("MM/dd/yy")
            val memory: Memory = Memory(
                nameView.text.toString(),
                viewModel.marker?.title,
                viewModel.marker?.position?.latitude,
                viewModel.marker?.position?.longitude,
                false,
                LocalDate.parse(dateView.text.toString(), formatter),
                typeView.text.toString(),
                moodView.value.toDouble(),
                notesView.text.toString(),
                viewModel.pictureUris
            )
            if (intent.hasExtra("MEMORY_ID")) {
                val mem = viewModel.getMemoryById(intent.extras!!.getInt("MEMORY_ID"))
                memory.isFavorite = mem.isFavorite
                memory.id = mem.id
            }
            viewModel.upsertMemory(memory)
            this.finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
        dateView.setText(dateFormat.format(calendar.time))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (intent.hasExtra("MEMORY_ID") && !this::mMap.isInitialized) {
            mMap = googleMap
            val memory = viewModel.getMemoryById(intent.extras!!.getInt("MEMORY_ID"))
            if (memory.placeLatitude != null && memory.placeLongitude != null)
                moveMarker(memory.title, LatLng(memory.placeLatitude!!, memory.placeLongitude!!))
        } else {
            mMap = googleMap
        }

        if (viewModel.marker != null) moveMarker(
            viewModel.marker!!.title!!,
            viewModel.marker!!.position
        )

        mMap.setOnMapClickListener { latlng ->
            run {
                val addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
                if (addresses?.size == 0) return@run;
                val address = addresses?.get(0)

                moveMarker(
                    address?.getAddressLine(0) ?: "${latlng.latitude} ${latlng.longitude}",
                    latlng
                )
            }
        }

        mMap.setOnPoiClickListener { poi ->
            moveMarker(poi.name, poi.latLng)
        }
    }

    private fun launchPlacesSearch() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startAutocomplete.launch(intent)
    }

    private fun moveMarker(title: String, location: LatLng) {
        viewModel.marker?.remove()
        viewModel.marker = mMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(title)
        )
    }

    private fun fillEditFields() {

    }
}