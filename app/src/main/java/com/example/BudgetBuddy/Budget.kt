package com.example.BudgetBuddy

data class Budget(
    val id: Int,
    val category: String,
    val amount: Double,
    val spent: Double,
    val color: String
) {
    fun getPercentage(): Int {
        return ((spent / amount) * 100).toInt()
    }

    fun isOverBudget(): Boolean {
        return spent > amount
    }

    fun getRemaining(): Double {
        return amount - spent
    }
}
