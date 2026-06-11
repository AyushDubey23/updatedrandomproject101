package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.BudgetItem
import com.messfit.ai.data.model.ShoppingList

object BudgetEngine {

    fun generateWeeklyShoppingList(monthlyBudget: Int, isVegetarian: Boolean, proteinTargetG: Int): ShoppingList {
        val weeklyBudget = (monthlyBudget * 0.35f).toInt()

        val baseItems = mutableListOf(
            BudgetItem("Milk", "7 litres", 420, 8f),
            BudgetItem("Roasted Chana", "500g", 90, 9f),
            BudgetItem("Soya Chunks", "1kg", 180, 52f),
            BudgetItem("Curd", "2kg", 100, 11f)
        )

        if (!isVegetarian) {
            baseItems.add(BudgetItem("Eggs", "30 pieces", 210, 6f))
        } else {
            baseItems.add(BudgetItem("Paneer", "500g", 200, 18f))
            baseItems.add(BudgetItem("Sattu", "500g", 50, 12f))
        }

        if (proteinTargetG > 140) {
            baseItems.add(BudgetItem("Peanuts", "250g", 40, 7f))
        }

        var total = baseItems.sumOf { it.costInr }
        val optimized = baseItems.toMutableList()

        while (total > weeklyBudget && optimized.size > 2) {
            val removed = optimized.removeAt(optimized.lastIndex)
            total -= removed.costInr
        }

        return ShoppingList(optimized, total)
    }

    fun monthlyBudgetBreakdown(monthlyBudget: Int, isVegetarian: Boolean): List<BudgetItem> {
        val items = mutableListOf(
            BudgetItem("Soya Chunks", "2kg/month", 360, 52f),
            BudgetItem("Milk", "28 litres", 1680, 8f),
            BudgetItem("Roasted Chana", "2kg", 360, 9f),
            BudgetItem("Curd", "8kg", 400, 11f)
        )
        if (isVegetarian) {
            items.add(BudgetItem("Paneer", "2kg", 800, 18f))
            items.add(BudgetItem("Sattu", "1kg", 100, 12f))
        } else {
            items.add(BudgetItem("Eggs", "60 pieces", 420, 6f))
        }

        var total = items.sumOf { it.costInr }
        while (total > monthlyBudget && items.size > 3) {
            items.removeAt(items.lastIndex)
            total = items.sumOf { it.costInr }
        }
        return items
    }
}
