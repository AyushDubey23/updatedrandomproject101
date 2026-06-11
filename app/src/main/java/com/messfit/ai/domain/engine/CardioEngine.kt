package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.CardioRecommendation
import com.messfit.ai.data.model.FitnessGoal

object CardioEngine {

    fun recommend(goal: FitnessGoal, activityLevel: String): List<CardioRecommendation> {
        return when (goal) {
            FitnessGoal.CUT -> listOf(
                CardioRecommendation("Incline Walking", 30, "5x/week", "Moderate", "Low impact fat burn, hostel-friendly"),
                CardioRecommendation("HIIT", 15, "2x/week", "High", "Maximize calorie burn in short sessions"),
                CardioRecommendation("Cycling", 25, "2x/week", "Moderate", "Joint-friendly cardio option")
            )
            FitnessGoal.BULK -> listOf(
                CardioRecommendation("Walking", 20, "3x/week", "Low", "Maintain cardiovascular health without excess deficit"),
                CardioRecommendation("Light Cycling", 15, "2x/week", "Low", "Active recovery between gym days")
            )
            FitnessGoal.MAINTAIN -> listOf(
                CardioRecommendation("Walking", 30, "4x/week", "Moderate", "Match your daily step goal"),
                CardioRecommendation("Running", 20, "2x/week", "Moderate", "Improve endurance"),
                CardioRecommendation("Cycling", 25, "2x/week", "Moderate", "Balanced cardio variety")
            )
        }
    }
}
