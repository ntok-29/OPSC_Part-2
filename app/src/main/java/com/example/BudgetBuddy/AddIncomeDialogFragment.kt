package com.example.BudgetBuddy




import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.BudgetBuddy.databinding.DialogAddIncomeBinding
import java.text.SimpleDateFormat
import java.util.*

class AddIncomeDialogFragment : DialogFragment() {

    private var _binding: DialogAddIncomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private var selectedDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // Set up source spinner
        val sources = arrayOf("Salary", "Freelance", "Gift", "Investment", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sources)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSource.adapter = adapter

        // Set up date picker
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.etDate.setText(dateFormat.format(selectedDate.time))

        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        // Set up save button
        binding.btnSave.setOnClickListener {
            saveIncome()
        }

        // Set up cancel button
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.etDate.setText(dateFormat.format(selectedDate.time))
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun saveIncome() {
        val amount = binding.etAmount.text.toString()
        val description = binding.etDescription.text.toString()
        val source = binding.spinnerSource.selectedItem.toString()

        if (amount.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // In a real app, this would save to the database
        // For now, we'll just show a success message and dismiss
        Toast.makeText(requireContext(), "Income saved", Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
