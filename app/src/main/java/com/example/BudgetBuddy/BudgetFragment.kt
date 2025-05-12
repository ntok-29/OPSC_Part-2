package com.example.BudgetBuddy


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BudgetBuddy.databinding.FragmentBudgetBinding
import com.google.android.material.textfield.TextInputEditText

class BudgetFragment : Fragment() {

    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var budgetAdapter: BudgetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // Set up RecyclerView for budgets
        binding.rvBudgets.layoutManager = LinearLayoutManager(requireContext())
        budgetAdapter = BudgetAdapter(getBudgets())
        binding.rvBudgets.adapter = budgetAdapter

        // Set up add budget button
        binding.fabAddBudget.setOnClickListener {
            showAddBudgetDialog()
        }
    }

    private fun getBudgets(): List<Budget> {
        // In a real app, this would come from the database
        val budgets = mutableListOf<Budget>()

        budgets.add(Budget(1, "Food", 2000.0, 1300.0, "#4CAF50"))
        budgets.add(Budget(2, "Transport", 1500.0, 675.0, "#2196F3"))
        budgets.add(Budget(3, "Rent", 4500.0, 4500.0, "#FF5722"))
        budgets.add(Budget(4, "Entertainment", 1000.0, 300.0, "#9C27B0"))
        budgets.add(Budget(5, "Shopping", 1500.0, 750.0, "#FFC107"))

        return budgets
    }


    private fun showAddBudgetDialog() {
        // Create a custom dialog
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_budget)

        // Set dialog to full width
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Initialize views
        val etAmount = dialog.findViewById<TextInputEditText>(R.id.etAmount)
        val spinnerCategory = dialog.findViewById<Spinner>(R.id.spinnerCategory)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        // Color selection views
        val colorGreen = dialog.findViewById<View>(R.id.colorGreen)
        val colorBlue = dialog.findViewById<View>(R.id.colorBlue)
        val colorOrange = dialog.findViewById<View>(R.id.colorOrange)
        val colorPurple = dialog.findViewById<View>(R.id.colorPurple)
        val colorYellow = dialog.findViewById<View>(R.id.colorYellow)

        var selectedColor = "#4CAF50" // default

        // Setup category spinner
        val categories = arrayOf("Food", "Transport", "Rent", "Entertainment", "Shopping", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Setup color selection logic
        fun selectColor(color: String) {
            selectedColor = color
            // Optionally highlight selected color view
        }

        colorGreen.setOnClickListener { selectColor("#4CAF50") }
        colorBlue.setOnClickListener { selectColor("#2196F3") }
        colorOrange.setOnClickListener { selectColor("#FF5722") }
        colorPurple.setOnClickListener { selectColor("#9C27B0") }
        colorYellow.setOnClickListener { selectColor("#FFC107") }

        // Handle cancel
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Handle save
        btnSave.setOnClickListener {
            val amountText = etAmount.text.toString()
            val category = spinnerCategory.selectedItem.toString()

            if (amountText.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save budget (e.g., to database)
            saveBudget(amount, category, selectedColor)

            Toast.makeText(requireContext(), "Budget saved", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }
    private fun saveBudget(amount: Double, category: String, selectedColor: String) {
        // Assuming that 'spent' starts as 0.0 when adding a new budget
        val spent = 0.0

        // Create a Budget object with the provided values
        val budget = Budget(
            id = 0, // ID will be generated by the database
            category = category, // Category passed as argument
            amount = amount, // Amount for the budget
            spent = spent, // Initial spent amount is 0.0
            color = selectedColor // Selected color for the budget
        )

        // Save the budget to the database
        val result = dbHelper.addBudget(budget)


    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
