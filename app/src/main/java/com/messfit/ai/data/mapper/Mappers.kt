package com.messfit.ai.data.mapper

import com.messfit.ai.data.local.entity.DailyCompletionEntity
import com.messfit.ai.data.local.entity.UserProfileEntity
import com.messfit.ai.data.local.entity.WeeklyMenuEntity
import com.messfit.ai.data.model.*
import com.messfit.ai.domain.engine.MenuIntelligenceEngine
import com.messfit.ai.domain.engine.MessQualityEngine
import org.json.JSONArray
import org.json.JSONObject

fun UserProfileEntity.toDomain() = UserProfile(
    id = id, name = name, age = age,
    gender = Gender.valueOf(gender),
    heightCm = heightCm, currentWeightKg = currentWeightKg,
    goalWeightKg = goalWeightKg, monthsAvailable = monthsAvailable,
    activityLevel = ActivityLevel.valueOf(activityLevel),
    gymExperience = GymExperience.valueOf(gymExperience),
    isVegetarian = isVegetarian, hostelName = hostelName,
    collegeName = collegeName, monthlyBudget = monthlyBudget,
    currentSupplements = currentSupplements, sleepHours = sleepHours,
    dailyWalkingSteps = dailyWalkingSteps, onboardingComplete = onboardingComplete
)

fun UserProfile.toEntity() = UserProfileEntity(
    id = 1, name = name, age = age, gender = gender.name,
    heightCm = heightCm, currentWeightKg = currentWeightKg,
    goalWeightKg = goalWeightKg, monthsAvailable = monthsAvailable,
    activityLevel = activityLevel.name, gymExperience = gymExperience.name,
    isVegetarian = isVegetarian, hostelName = hostelName,
    collegeName = collegeName, monthlyBudget = monthlyBudget,
    currentSupplements = currentSupplements, sleepHours = sleepHours,
    dailyWalkingSteps = dailyWalkingSteps, onboardingComplete = onboardingComplete
)

fun WeeklyMenuEntity.toDomain(): WeeklyMenu {
    val meals = MenuIntelligenceEngine.parseMenuFromJson(mealsJson)
    val score = try {
        val obj = JSONObject(qualityScoreJson)
        MessQualityScore(
            messRating = obj.optInt("messRating"),
            proteinScore = obj.optInt("proteinScore"),
            carbQualityScore = obj.optInt("carbQualityScore"),
            fatQualityScore = obj.optInt("fatQualityScore"),
            micronutrientScore = obj.optInt("micronutrientScore"),
            bulkingSuitabilityScore = obj.optInt("bulkingSuitabilityScore"),
            cuttingSuitabilityScore = obj.optInt("cuttingSuitabilityScore"),
            vegetarianFriendlinessScore = obj.optInt("vegetarianFriendlinessScore")
        )
    } catch (_: Exception) { MessQualityScore() }
    return WeeklyMenu(id, scannedAt, meals, score)
}

fun WeeklyMenu.toEntity(): WeeklyMenuEntity {
    val mealsJson = JSONArray().apply {
        meals.forEach { meal ->
            put(JSONObject().apply {
                put("day", meal.day)
                put("mealType", meal.mealType.name)
                put("items", JSONArray(meal.items))
            })
        }
    }.toString()
    val scoreJson = JSONObject().apply {
        put("messRating", qualityScore.messRating)
        put("proteinScore", qualityScore.proteinScore)
        put("carbQualityScore", qualityScore.carbQualityScore)
        put("fatQualityScore", qualityScore.fatQualityScore)
        put("micronutrientScore", qualityScore.micronutrientScore)
        put("bulkingSuitabilityScore", qualityScore.bulkingSuitabilityScore)
        put("cuttingSuitabilityScore", qualityScore.cuttingSuitabilityScore)
        put("vegetarianFriendlinessScore", qualityScore.vegetarianFriendlinessScore)
    }.toString()
    return WeeklyMenuEntity(id, scannedAt, mealsJson, scoreJson)
}

fun DailyCompletionEntity.toDomain() = DailyCompletion(
    date = date, totalCalories = totalCalories,
    totalProteinG = totalProteinG, totalCarbsG = totalCarbsG,
    totalFatG = totalFatG, waterMl = waterMl,
    goalCompletionPercent = goalCompletionPercent, completed = completed
)

fun DailyCompletion.toEntity() = DailyCompletionEntity(
    date = date, totalCalories = totalCalories,
    totalProteinG = totalProteinG, totalCarbsG = totalCarbsG,
    totalFatG = totalFatG, waterMl = waterMl,
    goalCompletionPercent = goalCompletionPercent, completed = completed
)
