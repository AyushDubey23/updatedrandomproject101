package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.MealType
import com.messfit.ai.data.model.MessMeal
import org.json.JSONArray
import org.json.JSONObject

object MenuIntelligenceEngine {

    private val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    private val mealTypes = listOf("breakfast", "lunch", "dinner", "snack")

    fun parseMenuFromText(rawText: String): List<MessMeal> {
        val meals = mutableListOf<MessMeal>()
        var currentDay = days.first()
        var currentMeal = MealType.BREAKFAST

        rawText.lines().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isBlank()) return@forEach

            days.firstOrNull { trimmed.contains(it, ignoreCase = true) }?.let { currentDay = it }

            when {
                trimmed.contains("breakfast", ignoreCase = true) -> {
                    currentMeal = MealType.BREAKFAST
                    extractItems(trimmed, "breakfast")?.let { meals.add(MessMeal(currentDay, currentMeal, it)) }
                }
                trimmed.contains("lunch", ignoreCase = true) -> {
                    currentMeal = MealType.LUNCH
                    extractItems(trimmed, "lunch")?.let { meals.add(MessMeal(currentDay, currentMeal, it)) }
                }
                trimmed.contains("dinner", ignoreCase = true) -> {
                    currentMeal = MealType.DINNER
                    extractItems(trimmed, "dinner")?.let { meals.add(MessMeal(currentDay, currentMeal, it)) }
                }
                else -> {
                    val items = trimmed.split(",", ":", "-", "|", "/")
                        .map { it.trim() }
                        .filter { it.isNotBlank() && it.length > 2 }
                        .map { capitalizeFood(it) }
                    if (items.isNotEmpty() && !mealTypes.any { trimmed.contains(it, ignoreCase = true) }) {
                        meals.add(MessMeal(currentDay, currentMeal, items))
                    }
                }
            }
        }

        return if (meals.isEmpty()) defaultWeeklyMenu() else meals.distinctBy { "${it.day}-${it.mealType}" }
    }

    fun parseMenuFromJson(json: String): List<MessMeal> {
        return try {
            val arr = JSONArray(json)
            buildList {
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    add(
                        MessMeal(
                            day = obj.getString("day"),
                            mealType = MealType.valueOf(obj.getString("mealType")),
                            items = obj.getJSONArray("items").let { items ->
                                List(items.length()) { items.getString(it) }
                            }
                        )
                    )
                }
            }
        } catch (_: Exception) {
            defaultWeeklyMenu()
        }
    }

    fun buildGeminiPrompt(): String = """
        You are MESSFIT AI Menu Intelligence Engine for Indian hostel mess menus.
        Extract ALL meals from the uploaded menu image/text.
        
        Return ONLY valid JSON array with this structure:
        [{"day":"Monday","mealType":"BREAKFAST","items":["Poha","Tea"]}]
        
        mealType must be one of: BREAKFAST, LUNCH, SNACK, DINNER
        day must be: Monday through Sunday
        
        Identify Indian foods: Rice, Dal, Roti, Paneer, Rajma, Chole, Soybean, Milk, Curd,
        Sprouts, Egg, Chicken, Fish, Upma, Poha, Idli, Dosa, Khichdi, Pulao, Biryani and all common items.
    """.trimIndent()

    fun defaultWeeklyMenu(): List<MessMeal> = listOf(
        MessMeal("Monday", MealType.BREAKFAST, listOf("Poha", "Tea")),
        MessMeal("Monday", MealType.LUNCH, listOf("Rice", "Dal", "Paneer")),
        MessMeal("Monday", MealType.DINNER, listOf("Roti", "Chole")),
        MessMeal("Tuesday", MealType.BREAKFAST, listOf("Upma", "Milk")),
        MessMeal("Tuesday", MealType.LUNCH, listOf("Rice", "Rajma", "Salad")),
        MessMeal("Tuesday", MealType.DINNER, listOf("Roti", "Dal", "Sabzi")),
        MessMeal("Wednesday", MealType.BREAKFAST, listOf("Idli", "Chutney")),
        MessMeal("Wednesday", MealType.LUNCH, listOf("Khichdi", "Curd")),
        MessMeal("Wednesday", MealType.DINNER, listOf("Roti", "Paneer", "Dal")),
        MessMeal("Thursday", MealType.BREAKFAST, listOf("Poha", "Tea")),
        MessMeal("Thursday", MealType.LUNCH, listOf("Rice", "Chole", "Salad")),
        MessMeal("Thursday", MealType.DINNER, listOf("Roti", "Dal")),
        MessMeal("Friday", MealType.BREAKFAST, listOf("Dosa", "Sambar")),
        MessMeal("Friday", MealType.LUNCH, listOf("Pulao", "Raita")),
        MessMeal("Friday", MealType.DINNER, listOf("Roti", "Rajma")),
        MessMeal("Saturday", MealType.BREAKFAST, listOf("Paratha", "Curd")),
        MessMeal("Saturday", MealType.LUNCH, listOf("Rice", "Dal", "Paneer")),
        MessMeal("Saturday", MealType.DINNER, listOf("Roti", "Chole")),
        MessMeal("Sunday", MealType.BREAKFAST, listOf("Poha", "Milk")),
        MessMeal("Sunday", MealType.LUNCH, listOf("Biryani", "Raita")),
        MessMeal("Sunday", MealType.DINNER, listOf("Roti", "Dal", "Sabzi"))
    )

    private fun extractItems(line: String, mealKeyword: String): List<String>? {
        val after = line.substringAfter(mealKeyword, "").trim(':', ' ', '-')
        if (after.isBlank()) return null
        return after.split(",", "/", "|").map { capitalizeFood(it.trim()) }.filter { it.isNotBlank() }
    }

    private fun capitalizeFood(name: String): String {
        val lower = name.lowercase().trim()
        return IndianFoodDatabase.findFood(lower)?.name
            ?: name.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
    }
}
