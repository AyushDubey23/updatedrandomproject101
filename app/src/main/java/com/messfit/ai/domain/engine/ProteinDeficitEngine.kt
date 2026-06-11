package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.BudgetItem
import com.messfit.ai.data.model.ProteinDeficit

object ProteinDeficitEngine {

    fun detect(requiredG: Int, currentG: Float, isVegetarian: Boolean): ProteinDeficit {
        val deficit = (requiredG - currentG).coerceAtLeast(0f)
        val recommendations = if (deficit <= 0f) {
            emptyList()
        } else {
            suggestCheapestSources(deficit, isVegetarian)
        }
        return ProteinDeficit(requiredG, currentG, deficit, recommendations)
    }

    private fun suggestCheapestSources(deficitG: Float, isVegetarian: Boolean): List<BudgetItem> {
        val sources = IndianFoodDatabase.cheapProteinSources(isVegetarian)
            .sortedBy { it.macros.proteinG / it.costInr.coerceAtLeast(1).toFloat() }
            .reversed()

        val result = mutableListOf<BudgetItem>()
        var remaining = deficitG

        for (source in sources) {
            if (remaining <= 0) break
            val proteinPerServing = source.macros.proteinG.coerceAtLeast(1f)
            val servings = (remaining / proteinPerServing).toInt().coerceAtLeast(1)
            result.add(
                BudgetItem(
                    name = source.name,
                    quantity = "${servings}x serving",
                    costInr = source.costInr * servings,
                    proteinPerServingG = proteinPerServing
                )
            )
            remaining -= proteinPerServing * servings
        }
        return result.take(4)
    }
}
