package com.example.BudgetBuddy


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.BudgetBuddy.R
import com.example.BudgetBuddy.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.binding.root.context

        holder.binding.tvDescription.text = transaction.description
        holder.binding.tvCategory.text = transaction.category

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.binding.tvDate.text = dateFormat.format(transaction.date)

        if (transaction.type == Transaction.TYPE_INCOME) {
            holder.binding.tvAmount.text = String.format("+ R %.2f", transaction.amount)
            holder.binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.income_green))
            holder.binding.ivTransactionType.setImageResource(R.drawable.ic_income)
        } else {
            holder.binding.tvAmount.text = String.format("- R %.2f", Math.abs(transaction.amount))
            holder.binding.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.expense_red))
            holder.binding.ivTransactionType.setImageResource(R.drawable.ic_expense)
        }
    }

    override fun getItemCount(): Int = transactions.size
}
