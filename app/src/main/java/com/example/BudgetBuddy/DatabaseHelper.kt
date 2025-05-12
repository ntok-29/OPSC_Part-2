package com.example.BudgetBuddy



import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "smartbudget.db"
        private const val DATABASE_VERSION = 1

        // Tables
        private const val TABLE_TRANSACTIONS = "transactions"
        private const val TABLE_BUDGETS = "budgets"

        // Common columns
        private const val KEY_ID = "id"

        // Transaction table columns
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_CATEGORY = "category"
        private const val KEY_AMOUNT = "amount"
        private const val KEY_DATE = "date"
        private const val KEY_TYPE = "type"

        // Budget table columns
        private const val KEY_BUDGET_CATEGORY = "category"
        private const val KEY_BUDGET_AMOUNT = "amount"
        private const val KEY_BUDGET_COLOR = "color"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create transactions table
        val createTransactionsTable = """
            CREATE TABLE $TABLE_TRANSACTIONS (
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_DESCRIPTION TEXT,
                $KEY_CATEGORY TEXT,
                $KEY_AMOUNT REAL,
                $KEY_DATE TEXT,
                $KEY_TYPE INTEGER
            )
        """.trimIndent()

        // Create budgets table
        val createBudgetsTable = """
            CREATE TABLE $TABLE_BUDGETS (
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_BUDGET_CATEGORY TEXT UNIQUE,
                $KEY_BUDGET_AMOUNT REAL,
                $KEY_BUDGET_COLOR TEXT
            )
        """.trimIndent()

        db.execSQL(createTransactionsTable)
        db.execSQL(createBudgetsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BUDGETS")

        // Create tables again
        onCreate(db)
    }

    // Add a new transaction
    fun addTransaction(transaction: Transaction): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_DESCRIPTION, transaction.description)
        values.put(KEY_CATEGORY, transaction.category)
        values.put(KEY_AMOUNT, transaction.amount)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        values.put(KEY_DATE, dateFormat.format(transaction.date))

        values.put(KEY_TYPE, transaction.type)

        // Insert row
        val id = db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()

        return id
    }

    // Add a new budget
    fun addBudget(budget: Budget): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(KEY_BUDGET_CATEGORY, budget.category)
        values.put(KEY_BUDGET_AMOUNT, budget.amount)
        values.put(KEY_BUDGET_COLOR, budget.color)

        // Insert row
        val id = db.insert(TABLE_BUDGETS, null, values)
        db.close()

        return id
    }

    // Get all transactions
    fun getAllTransactions(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()

        val selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS ORDER BY $KEY_DATE DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val descriptionIndex = cursor.getColumnIndex(KEY_DESCRIPTION)
            val categoryIndex = cursor.getColumnIndex(KEY_CATEGORY)
            val amountIndex = cursor.getColumnIndex(KEY_AMOUNT)
            val dateIndex = cursor.getColumnIndex(KEY_DATE)
            val typeIndex = cursor.getColumnIndex(KEY_TYPE)

            do {
                val id = if (idIndex >= 0) cursor.getInt(idIndex) else 0
                val description = if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else ""
                val category = if (categoryIndex >= 0) cursor.getString(categoryIndex) else ""
                val amount = if (amountIndex >= 0) cursor.getDouble(amountIndex) else 0.0
                val dateStr = if (dateIndex >= 0) cursor.getString(dateIndex) else ""
                val type = if (typeIndex >= 0) cursor.getInt(typeIndex) else Transaction.TYPE_EXPENSE

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = try {
                    dateFormat.parse(dateStr) ?: Date()
                } catch (e: Exception) {
                    Date()
                }

                val transaction = Transaction(id, description, category, amount, date, type)
                transactions.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return transactions
    }

    // Get all budgets
    fun getAllBudgets(): List<Budget> {
        val budgets = mutableListOf<Budget>()

        val selectQuery = "SELECT * FROM $TABLE_BUDGETS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val categoryIndex = cursor.getColumnIndex(KEY_BUDGET_CATEGORY)
            val amountIndex = cursor.getColumnIndex(KEY_BUDGET_AMOUNT)
            val colorIndex = cursor.getColumnIndex(KEY_BUDGET_COLOR)

            do {
                val id = if (idIndex >= 0) cursor.getInt(idIndex) else 0
                val category = if (categoryIndex >= 0) cursor.getString(categoryIndex) else ""
                val amount = if (amountIndex >= 0) cursor.getDouble(amountIndex) else 0.0
                val color = if (colorIndex >= 0) cursor.getString(colorIndex) else "#4CAF50"

                // Calculate spent amount for this category
                val spent = getSpentAmountForCategory(category)

                val budget = Budget(id, category, amount, spent, color)
                budgets.add(budget)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return budgets
    }

    // Get spent amount for a category
    private fun getSpentAmountForCategory(category: String): Double {
        var spent = 0.0

        val selectQuery = "SELECT SUM($KEY_AMOUNT) FROM $TABLE_TRANSACTIONS WHERE $KEY_CATEGORY = ? AND $KEY_TYPE = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(category, Transaction.TYPE_EXPENSE.toString()))

        if (cursor.moveToFirst()) {
            spent = cursor.getDouble(0)
        }

        cursor.close()

        return Math.abs(spent)
    }

}
