package com.example.BudgetBuddy

import android.app.DatePickerDialog
import android.app.Dialog
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BudgetBuddy.R
import com.example.BudgetBuddy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // Set up RecyclerView for recent transactions
        binding.rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())
        transactionAdapter = TransactionAdapter(getRecentTransactions())
        binding.rvRecentTransactions.adapter = transactionAdapter

        // Set up summary data
        updateSummaryData()

        // Set up add expense and income buttons
        binding.fabAddExpense.setOnClickListener {
            showAddExpenseDialog()
        }

        binding.fabAddIncome.setOnClickListener {
            showAddIncomeDialog()
        }
    }

    private fun updateSummaryData() {
        // In a real app, this would come from the database
        val totalIncome = 15000.0
        val totalExpenses = 9500.0
        val balance = totalIncome - totalExpenses

        binding.tvTotalIncome.text = String.format("R %.2f", totalIncome)
        binding.tvTotalExpenses.text = String.format("R %.2f", totalExpenses)
        binding.tvBalance.text = String.format("R %.2f", balance)

        // Update progress bars
        binding.progressFood.progress = 65
        binding.progressTransport.progress = 45
        binding.progressRent.progress = 100
        binding.progressEntertainment.progress = 30
    }

    private fun getRecentTransactions(): List<Transaction> {
        // In a real app, this would come from the database
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val transactions = mutableListOf<Transaction>()

        transactions.add(Transaction(1, "Grocery Shopping", "Food", -850.0, dateFormat.parse("15 Apr 2025")!!, Transaction.TYPE_EXPENSE))
        transactions.add(Transaction(2, "Salary", "Income", 15000.0, dateFormat.parse("01 Apr 2025")!!, Transaction.TYPE_INCOME))
        transactions.add(Transaction(3, "Uber Ride", "Transport", -150.0, dateFormat.parse("10 Apr 2025")!!, Transaction.TYPE_EXPENSE))
        transactions.add(Transaction(4, "Rent Payment", "Housing", -4500.0, dateFormat.parse("05 Apr 2025")!!, Transaction.TYPE_EXPENSE))
        transactions.add(Transaction(5, "Movie Night", "Entertainment", -300.0, dateFormat.parse("12 Apr 2025")!!, Transaction.TYPE_EXPENSE))

        return transactions
    }


    // In your HomeFragment.kt or wherever your fabAddExpense button is defined
    private fun showAddExpenseDialog() {
        // Create a dialog
        val dialog = Dialog(requireContext())

        // Set the custom layout
        dialog.setContentView(R.layout.dialog_add_expense)

        // Set dialog width to match parent
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Initialize views in the dialog
        val etAmount = dialog.findViewById<TextInputEditText>(R.id.etAmount)
        val etDescription = dialog.findViewById<TextInputEditText>(R.id.etDescription)
        val spinnerCategory = dialog.findViewById<Spinner>(R.id.spinnerCategory)
        val etDate = dialog.findViewById<TextInputEditText>(R.id.etDate)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        // Set up category spinner
        val categories = arrayOf("Food", "Transport", "Rent", "Entertainment", "Shopping", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Set up date picker
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etDate.setText(dateFormat.format(calendar.time))

        etDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    etDate.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Set up button click listeners
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            // Validate inputs
            val amount = etAmount.text.toString()
            val description = etDescription.text.toString()
            val category = spinnerCategory.selectedItem.toString()

            if (amount.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the expense (implement your saving logic here)
            saveExpense(amount.toDouble(), description, category, calendar.time)

            // Dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    private fun saveExpense(amount: Double, description: String, category: String, date: Date) {
        // Implement your logic to save the expense to the database
        // For example:
        val transaction = Transaction(
            id = 0, // Database will generate ID
            description = description,
            category = category,
            amount = -amount, // Negative for expenses
            date = date,
            type = Transaction.TYPE_EXPENSE
        )

        // In a real app, you would save to database:
        // dbHelper.addTransaction(transaction)

        // For now, just show a success message
        Toast.makeText(requireContext(), "Expense saved", Toast.LENGTH_SHORT).show()

        // Refresh your UI to show the new expense
        // updateUI()
    }

    private fun showAddIncomeDialog() {
        // Create a dialog
        val dialog = Dialog(requireContext())

        // Set the custom layout (assuming you have a dialog_add_income.xml similar to dialog_add_expense.xml)
        dialog.setContentView(R.layout.dialog_add_income)

        // Set dialog width to match parent
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Initialize views in the dialog
        val etAmount = dialog.findViewById<TextInputEditText>(R.id.etAmount)
        val etDescription = dialog.findViewById<TextInputEditText>(R.id.etDescription)
        val spinnerSource = dialog.findViewById<Spinner>(R.id.spinnerSource)
        val etDate = dialog.findViewById<TextInputEditText>(R.id.etDate)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        // Set up source spinner
        val sources = arrayOf("Salary", "Freelance", "Gift", "Investment", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sources)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSource.adapter = adapter

        // Set up date picker
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etDate.setText(dateFormat.format(calendar.time))

        etDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    etDate.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Set up button click listeners
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            // Validate inputs
            val amount = etAmount.text.toString()
            val description = etDescription.text.toString()
            val source = spinnerSource.selectedItem.toString()

            if (amount.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the income
            saveIncome(amount.toDouble(), description, source, calendar.time)

            // Dismiss the dialog
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    private fun saveIncome(amount: Double, description: String, source: String, date: Date) {
        // Implement your logic to save the income to the database
        // For example:
        val transaction = Transaction(
            id = 0, // Database will generate ID
            description = description,
            category = source, // Using source as category for income
            amount = amount, // Positive for income
            date = date,
            type = Transaction.TYPE_INCOME
        )

        // In a real app, you would save to database:
        // dbHelper.addTransaction(transaction)

        // For now, just show a success message
        Toast.makeText(requireContext(), "Income saved", Toast.LENGTH_SHORT).show()

        // Refresh your UI to show the new income
        // updateUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
