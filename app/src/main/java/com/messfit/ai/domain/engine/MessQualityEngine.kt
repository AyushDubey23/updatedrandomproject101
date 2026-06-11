package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.MessMeal
import com.messfit.ai.data.model.MessQualityScore

object MessQualityEngine {

    private val highProteinFoods = setOf("paneer", "dal", "rajma", "chole", "soybean", "egg", "chicken", "fish", "soya", "sprouts")
    private val qualityCarbs = setOf("roti", "poha", "upma", "idli", "dosa", "khichdi", "sprouts")
    private val vegFriendly = setOf("paneer", "dal", "rajma", "chole", "soybean", "roti", "rice", "poha", "idli", "dosa", "khichdi", "sprouts", "curd", "milk")

    fun scoreMenu(meals: List<MessMeal>, isVegetarian: Boolean): MessQualityScore {
        if (meals.isEmpty()) return MessQualityScore()

        val allItems = meals.flatMap { it.items }.map { it.lowercase() }
        val uniqueItems = allItems.distinct()

        val proteinHits = uniqueItems.count { item -> highProteinFoods.any { item.contains(it) } }
        val carbHits = uniqueItems.count { item -> qualityCarbs.any { item.contains(it) } }
        val vegHits = uniqueItems.count { item -> vegFriendly.any { item.contains(it) } }

        val proteinScore = ((proteinHits.toFloat() / uniqueItems.size.coerceAtLeast(1)) * 100).toInt().coerceIn(0, 100)
        val carbScore = ((carbHits.toFloat() / uniqueItems.size.coerceAtLeast(1)) * 100).toInt().coerceIn(0, 100)
        val fatScore = if (uniqueItems.any { it.contains("paneer") || it.contains("curd") }) 75 else 55
        val microScore = if (uniqueItems.any { it.contains("dal") || it.contains("sabzi") || it.contains("salad") }) 70 else 45
        val bulkScore = (proteinScore * 0.5 + carbScore * 0.3 + fatScore * 0.2).toInt()
        val cutScore = (proteinScore * 0.6 + microScore * 0.25 + (100 - fatScore) * 0.15).toInt()
        val vegScore = if (isVegetarian) {
            ((vegHits.toFloat() / uniqueItems.size.coerceAtLeast(1)) * 100).toInt()
        } else 80

        val messRating = listOf(proteinScore, carbScore, fatScore, microScore, vegScore).average().toInt()

        return MessQualityScore(
            messRating = messRating,
            proteinScore = proteinScore,
            carbQualityScore = carbScore,
            fatQualityScore = fatScore,
            micronutrientScore = microScore,
            bulkingSuitabilityScore = bulkScore.coerceIn(0, 100),
            cuttingSuitabilityScore = cutScore.coerceIn(0, 100),
            vegetarianFriendlinessScore = vegScore.coerceIn(0, 100)
        )
    }
}
