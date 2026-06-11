package com.messfit.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Long = 1,
    val name: String,
    val age: Int,
    val gender: String,
    val heightCm: Float,
    val currentWeightKg: Float,
    val goalWeightKg: Float,
    val monthsAvailable: Int,
    val activityLevel: String,
    val gymExperience: String,
    val isVegetarian: Boolean,
    val hostelName: String,
    val collegeName: String,
    val monthlyBudget: Int,
    val currentSupplements: String,
    val sleepHours: Float,
    val dailyWalkingSteps: Int,
    val onboardingComplete: Boolean
)

@Entity(tableName = "weekly_menu")
data class WeeklyMenuEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scannedAt: Long,
    val mealsJson: String,
    val qualityScoreJson: String
)

@Entity(tableName = "daily_completion")
data class DailyCompletionEntity(
    @PrimaryKey val date: String,
    val totalCalories: Int,
    val totalProteinG: Float,
    val totalCarbsG: Float,
    val totalFatG: Float,
    val waterMl: Int,
    val goalCompletionPercent: Int,
    val completed: Boolean
)
