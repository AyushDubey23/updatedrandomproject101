package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.BodyAnalysis
import com.messfit.ai.data.model.DailyMealPlan
import com.messfit.ai.data.model.DayDietPlan
import com.messfit.ai.data.model.DietInstruction
import com.messfit.ai.data.model.MacroNutrients
import com.messfit.ai.data.model.MealType
import com.messfit.ai.data.model.MessMeal
import com.messfit.ai.data.model.UserProfile

object DietPlanEngine {

    private val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    fun generateWeeklyPlan(
        menu: List<MessMeal>,
        profile: UserProfile,
        analysis: BodyAnalysis
    ): List<DayDietPlan> {
        return days.map { day ->
            val dayMeals = menu.filter { it.day.equals(day, ignoreCase = true) }
            DayDietPlan(day, buildDayMeals(day, dayMeals, profile, analysis))
        }
    }

    private fun buildDayMeals(
        day: String,
        dayMeals: List<MessMeal>,
        profile: UserProfile,
        analysis: BodyAnalysis
    ): List<DailyMealPlan> {
        val breakfast = dayMeals.find { it.mealType == MealType.BREAKFAST }
        val lunch = dayMeals.find { it.mealType == MealType.LUNCH }
        val dinner = dayMeals.find { it.mealType == MealType.DINNER }

        val breakfastItems = breakfast?.items ?: listOf("Poha", "Tea")
        val lunchItems = lunch?.items ?: listOf("Rice", "Dal", "Paneer")
        val dinnerItems = dinner?.items ?: listOf("Roti", "Chole")

        val breakfastMacros = IndianFoodDatabase.estimateMealMacros(breakfastItems)
        val lunchMacros = IndianFoodDatabase.estimateMealMacros(lunchItems)
        val dinnerMacros = IndianFoodDatabase.estimateMealMacros(dinnerItems)

        val snackProtein = if (analysis.recommendedProteinG > 130) 52f else 30f
        val snackCost = if (profile.isVegetarian) 12 else 15

        return listOf(
            DailyMealPlan(
                day, MealType.BREAKFAST,
                DietInstruction(
                    messItems = breakfastItems,
                    instruction = "Eat full serving from mess.",
                    addOns = listOf("500ml Milk", "50g Roasted Chana"),
                    macros = MacroNutrients(
                        breakfastMacros.calories + 350,
                        breakfastMacros.proteinG + 18f,
                        breakfastMacros.carbsG + 30f,
                        breakfastMacros.fatG + 8f
                    )
                )
            ),
            DailyMealPlan(
                day, MealType.LUNCH,
                DietInstruction(
                    messItems = lunchItems,
                    instruction = buildLunchInstruction(lunchItems),
                    macros = MacroNutrients(
                        lunchMacros.calories,
                        lunchMacros.proteinG,
                        lunchMacros.carbsG,
                        lunchMacros.fatG
                    )
                )
            ),
            DailyMealPlan(
                day, MealType.SNACK,
                DietInstruction(
                    outsideFood = if (profile.isVegetarian) "100g Soya Chunks" else "4 Boiled Eggs",
                    instruction = "Buy from local store near hostel.",
                    costInr = snackCost,
                    macros = MacroNutrients(
                        if (profile.isVegetarian) 120 else 312,
                        snackProtein,
                        if (profile.isVegetarian) 8f else 2f,
                        if (profile.isVegetarian) 0.5f else 22f
                    )
                )
            ),
            DailyMealPlan(
                day, MealType.DINNER,
                DietInstruction(
                    messItems = dinnerItems,
                    instruction = buildDinnerInstruction(dinnerItems),
                    addOns = listOf("200g Curd"),
                    macros = MacroNutrients(
                        dinnerMacros.calories + 98,
                        dinnerMacros.proteinG + 11f,
                        dinnerMacros.carbsG,
                        dinnerMacros.fatG + 4f
                    )
                )
            )
        )
    }

    private fun buildLunchInstruction(items: List<String>): String {
        val hasPaneer = items.any { it.contains("paneer", true) }
        val hasDal = items.any { it.contains("dal", true) }
        val hasRice = items.any { it.contains("rice", true) }
        return buildString {
            if (hasPaneer) append("Eat full paneer serving. ")
            if (hasDal) append("Eat one bowl dal. ")
            if (hasRice) append("Limit rice to one serving. ")
            if (isEmpty()) append("Eat balanced portions from mess.")
        }.trim()
    }

    private fun buildDinnerInstruction(items: List<String>): String {
        val hasRoti = items.any { it.contains("roti", true) || it.contains("chapati", true) }
        val hasChole = items.any { it.contains("chole", true) || it.contains("rajma", true) }
        return buildString {
            if (hasRoti) append("Eat 4 rotis. ")
            if (hasChole) append("Eat 2 bowls chole/rajma. ")
            if (isEmpty()) append("Eat full mess serving with protein focus.")
        }.trim()
    }

    fun calculateDayTotals(meals: List<DailyMealPlan>): MacroNutrients {
        return meals.fold(MacroNutrients()) { acc, meal ->
            MacroNutrients(
                acc.calories + meal.plan.macros.calories,
                acc.proteinG + meal.plan.macros.proteinG,
                acc.carbsG + meal.plan.macros.carbsG,
                acc.fatG + meal.plan.macros.fatG
            )
        }
    }
}
