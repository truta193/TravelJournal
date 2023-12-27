package com.truta.traveljournal

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.truta.traveljournal.databinding.FragmentAddMemoryBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AddMemoryFragment : Fragment() {
    private lateinit var viewModel: AddMemoryViewModel
    private lateinit var binding: FragmentAddMemoryBinding
    private lateinit var editText: TextInputEditText
    private lateinit var doneFab: FloatingActionButton
    private lateinit var homeViewModel: HomeViewModel
    val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMemoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AddMemoryViewModel::class.java]

        val travelTypes = resources.getStringArray(R.array.travel_types)
        var arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, travelTypes)
        binding.inputType.setAdapter(arrayAdapter)

        editText = binding.inputDate

        val date =
            OnDateSetListener { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateLabel()
            }

        editText.setOnClickListener {
            this?.context?.let { it1 ->
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
            findNavController().popBackStack()
        }

        return binding.root

    }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        editText.setText(dateFormat.format(calendar.time))
    }
}