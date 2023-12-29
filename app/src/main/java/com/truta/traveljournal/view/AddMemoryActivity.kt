package com.truta.traveljournal.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.truta.traveljournal.R
import com.truta.traveljournal.databinding.ActivityAddMemoryBinding
import com.truta.traveljournal.model.Memory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddMemoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMemoryBinding
    private lateinit var nameView: TextInputEditText
    private lateinit var dateView: TextInputEditText
    private lateinit var typeView: TextInputEditText
    private lateinit var moodView: TextInputEditText
    private lateinit var notesView: TextInputEditText
    private lateinit var doneFab: FloatingActionButton
    val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMemoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameView = binding.inputName
        dateView = binding.inputDate

        val toolbar = binding.addMemoryToolbar
        setSupportActionBar(toolbar)
        title = "Add Memory"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val travelTypes = resources.getStringArray(R.array.travel_types)
        var arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown, travelTypes)
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

        doneFab = binding.fabDone
        doneFab.setOnClickListener {
            val memory : Memory = Memory(nameView.text.toString(), "Hi", false)

            val data = Intent()
            data.putExtra("memory", memory)
            setResult(RESULT_OK, data)
            this.finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(RESULT_CANCELED)
        finish()
        return true
    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        dateView.setText(dateFormat.format(calendar.time))
    }
}