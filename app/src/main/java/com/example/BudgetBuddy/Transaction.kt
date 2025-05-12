package com.example.BudgetBuddy


import java.util.Date

data class Transaction(
    val id: Int,
    val description: String,
    val category: String,
    val amount: Double,
    val date: Date,
    val type: Int
) {
    companion object {
        const val TYPE_INCOME = 1
        const val TYPE_EXPENSE = 2
    }
}
