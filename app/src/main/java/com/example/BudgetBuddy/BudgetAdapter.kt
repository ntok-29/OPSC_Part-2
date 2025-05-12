package com.example.BudgetBuddy


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.BudgetBuddy.databinding.ItemBudgetBinding

class BudgetAdapter(private val budgets: List<Budget>) :
    RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    class BudgetViewHolder(val binding: ItemBudgetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget = budgets[position]

        holder.binding.tvCategory.text = budget.category
        holder.binding.tvAmount.text = String.format("R %.2f / R %.2f", budget.spent, budget.amount)
        holder.binding.tvPercentage.text = String.format("%d%%", budget.getPercentage())

        holder.binding.progressBar.progress = budget.getPercentage()

        try {
            holder.binding.progressBar.progressTintList = android.content.res.ColorStateList.valueOf(Color.parseColor(budget.color))
        } catch (e: Exception) {
            // If color parsing fails, use a default color
        }

        if (budget.isOverBudget()) {
            holder.binding.tvOverBudget.visibility = ViewGroup.VISIBLE
        } else {
            holder.binding.tvOverBudget.visibility = ViewGroup.GONE
        }
    }

    override fun getItemCount(): Int = budgets.size

}
