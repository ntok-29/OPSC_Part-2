package com.example.BudgetBuddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.BudgetBuddy.databinding.DialogAddBudgetBinding

class AddBudgetDialogFragment : DialogFragment() {

    private var _binding: DialogAddBudgetBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private var selectedColor = "#4CAF50" // Default color

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // Set up category spinner
        val categories = arrayOf("Food", "Transport", "Rent", "Entertainment", "Shopping", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        // Set up color selection
        setupColorSelection()

        // Set up save button
        binding.btnSave.setOnClickListener {
            saveBudget()
        }

        // Set up cancel button
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setupColorSelection() {
        // This would set up color selection buttons
        // For now, we'll just use a placeholder implementation
        binding.colorGreen.setOnClickListener {
            selectedColor = "#4CAF50"
            updateSelectedColor()
        }

        binding.colorBlue.setOnClickListener {
            selectedColor = "#2196F3"
            updateSelectedColor()
        }

        binding.colorOrange.setOnClickListener {
            selectedColor = "#FF5722"
            updateSelectedColor()
        }

        binding.colorPurple.setOnClickListener {
            selectedColor = "#9C27B0"
            updateSelectedColor()
        }

        binding.colorYellow.setOnClickListener {
            selectedColor = "#FFC107"
            updateSelectedColor()
        }
    }

    private fun updateSelectedColor() {
        // This would update the UI to show the selected color
        // For now, we'll just use a placeholder implementation
    }

    private fun saveBudget() {
        val amount = binding.etAmount.text.toString()
        val category = binding.spinnerCategory.selectedItem.toString()

        if (amount.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        // In a real app, this would save to the database
        // For now, we'll just show a success message and dismiss
        Toast.makeText(requireContext(), "Budget saved", Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
