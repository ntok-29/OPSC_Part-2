package com.example.BudgetBuddy


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.BudgetBuddy.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // Set up pie chart
        setupPieChart()

        // Set up bar chart
        setupBarChart()

        // Set up spending insights
        setupSpendingInsights()
    }

    private fun setupPieChart() {
        // In a real app, this data would come from the database
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(1300f, "Food"))
        entries.add(PieEntry(675f, "Transport"))
        entries.add(PieEntry(4500f, "Rent"))
        entries.add(PieEntry(300f, "Entertainment"))
        entries.add(PieEntry(750f, "Shopping"))

        val dataSet = PieDataSet(entries, "Expense Categories")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 16f

        val data = PieData(dataSet)
        binding.pieChart.data = data
        binding.pieChart.description.isEnabled = false
        binding.pieChart.centerText = "Expenses"
        binding.pieChart.animate()
    }

    private fun setupBarChart() {
        // This would set up a bar chart for monthly spending
        // For now, we'll just use a placeholder
    }

    private fun setupSpendingInsights() {
        // This would provide AI-driven insights based on spending patterns
        // For now, we'll just use placeholder text
        binding.tvInsight1.text = "You spent 20% more on Food this month compared to last month."
        binding.tvInsight2.text = "Your Transport expenses are lower than usual. Great job!"
        binding.tvInsight3.text = "Tip: Consider setting aside R500 for savings each month."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
