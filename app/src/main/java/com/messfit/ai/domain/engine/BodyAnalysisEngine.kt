package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.ActivityLevel
import com.messfit.ai.data.model.BodyAnalysis
import com.messfit.ai.data.model.FitnessGoal
import com.messfit.ai.data.model.Gender
import com.messfit.ai.data.model.UserProfile
import kotlin.math.abs

object BodyAnalysisEngine {

    private fun activityMultiplier(level: ActivityLevel): Float = when (level) {
        ActivityLevel.SEDENTARY -> 1.2f
        ActivityLevel.LIGHT -> 1.375f
        ActivityLevel.MODERATE -> 1.55f
        ActivityLevel.ACTIVE -> 1.725f
        ActivityLevel.VERY_ACTIVE -> 1.9f
    }

    fun calculateBmi(weightKg: Float, heightCm: Float): Float {
        val heightM = heightCm / 100f
        return weightKg / (heightM * heightM)
    }

    fun calculateBmr(weightKg: Float, heightCm: Float, age: Int, gender: Gender): Int {
        return when (gender) {
            Gender.MALE -> (10 * weightKg + 6.25 * heightCm - 5 * age + 5).toInt()
            Gender.FEMALE -> (10 * weightKg + 6.25 * heightCm - 5 * age - 161).toInt()
            Gender.OTHER -> (10 * weightKg + 6.25 * heightCm - 5 * age - 78).toInt()
        }
    }

    fun analyze(profile: UserProfile): BodyAnalysis {
        val bmi = calculateBmi(profile.currentWeightKg, profile.heightCm)
        val bmr = calculateBmr(profile.currentWeightKg, profile.heightCm, profile.age, profile.gender)
        val tdee = (bmr * activityMultiplier(profile.activityLevel)).toInt()
        val maintenance = tdee

        val weightDiff = profile.goalWeightKg - profile.currentWeightKg
        val goal = when {
            weightDiff < -1f -> FitnessGoal.CUT
            weightDiff > 1f -> FitnessGoal.BULK
            else -> FitnessGoal.MAINTAIN
        }

        val recommendedCalories = when (goal) {
            FitnessGoal.CUT -> (tdee - 400).coerceAtLeast(1400)
            FitnessGoal.BULK -> tdee + 300
            FitnessGoal.MAINTAIN -> tdee
        }

        val proteinPerKg = when (goal) {
            FitnessGoal.CUT -> 2.0f
            FitnessGoal.BULK -> 1.8f
            FitnessGoal.MAINTAIN -> 1.6f
        }
        val proteinG = (profile.currentWeightKg * proteinPerKg).toInt()
        val fatG = (recommendedCalories * 0.25f / 9f).toInt()
        val carbsG = ((recommendedCalories - proteinG * 4 - fatG * 9) / 4).coerceAtLeast(100)
        val waterMl = (profile.currentWeightKg * 35).toInt()

        val totalChange = abs(weightDiff)
        val weeksAvailable = (profile.monthsAvailable * 4.33f).coerceAtLeast(1f)
        val weeklyChange = when (goal) {
            FitnessGoal.CUT -> -(totalChange / weeksAvailable).coerceIn(0.25f, 0.75f)
            FitnessGoal.BULK -> (totalChange / weeksAvailable).coerceIn(0.15f, 0.4f)
            FitnessGoal.MAINTAIN -> 0f
        }

        return BodyAnalysis(
            bmi = bmi,
            bmr = bmr,
            tdee = tdee,
            maintenanceCalories = maintenance,
            recommendedCalories = recommendedCalories,
            recommendedProteinG = proteinG,
            recommendedFatG = fatG,
            recommendedCarbsG = carbsG,
            dailyWaterMl = waterMl,
            expectedWeeklyWeightChangeKg = weeklyChange,
            goal = goal
        )
    }
}
