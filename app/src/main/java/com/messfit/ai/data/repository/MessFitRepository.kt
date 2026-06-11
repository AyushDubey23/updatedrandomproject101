package com.messfit.ai.data.repository

import android.graphics.Bitmap
import com.messfit.ai.data.local.dao.DailyCompletionDao
import com.messfit.ai.data.local.dao.UserProfileDao
import com.messfit.ai.data.local.dao.WeeklyMenuDao
import com.messfit.ai.data.mapper.toDomain
import com.messfit.ai.data.mapper.toEntity
import com.messfit.ai.data.model.*
import com.messfit.ai.data.remote.GeminiService
import com.messfit.ai.domain.engine.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessFitRepository @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val weeklyMenuDao: WeeklyMenuDao,
    private val dailyCompletionDao: DailyCompletionDao,
    private val geminiService: GeminiService
) {
    val profileFlow: Flow<UserProfile?> = userProfileDao.observeProfile().map { it?.toDomain() }
    val latestMenuFlow: Flow<WeeklyMenu?> = weeklyMenuDao.observeLatestMenu().map { it?.toDomain() }

    suspend fun getProfile(): UserProfile? = userProfileDao.getProfile()?.toDomain()

    suspend fun saveProfile(profile: UserProfile) {
        userProfileDao.save(profile.toEntity())
    }

    suspend fun scanMenuFromText(text: String, isVegetarian: Boolean): WeeklyMenu {
        val parsed = MenuIntelligenceEngine.parseMenuFromText(text)
        return saveMenu(parsed, isVegetarian)
    }

    suspend fun scanMenuFromImage(bitmap: Bitmap, isVegetarian: Boolean): WeeklyMenu {
        val json = geminiService.extractMenuFromImage(bitmap)
        val meals = if (json == "[]") MenuIntelligenceEngine.defaultWeeklyMenu()
        else MenuIntelligenceEngine.parseMenuFromJson(json)
        return saveMenu(meals, isVegetarian)
    }

    private suspend fun saveMenu(meals: List<MessMeal>, isVegetarian: Boolean): WeeklyMenu {
        val score = MessQualityEngine.scoreMenu(meals, isVegetarian)
        val menu = WeeklyMenu(meals = meals, qualityScore = score)
        val id = weeklyMenuDao.save(menu.toEntity())
        return menu.copy(id = id)
    }

    fun getBodyAnalysis(profile: UserProfile) = BodyAnalysisEngine.analyze(profile)

    fun generateDietPlan(profile: UserProfile, menu: WeeklyMenu): List<DayDietPlan> {
        val analysis = BodyAnalysisEngine.analyze(profile)
        val meals = menu.meals.ifEmpty { MenuIntelligenceEngine.defaultWeeklyMenu() }
        return DietPlanEngine.generateWeeklyPlan(meals, profile, analysis)
    }

    fun generateShoppingList(profile: UserProfile, analysis: BodyAnalysis) =
        BudgetEngine.generateWeeklyShoppingList(profile.monthlyBudget, profile.isVegetarian, analysis.recommendedProteinG)

    fun generateWorkoutPlan(profile: UserProfile, split: WorkoutSplit) =
        WorkoutEngine.generatePlan(profile.gymExperience, split)

    fun getCardioRecommendations(analysis: BodyAnalysis) =
        CardioEngine.recommend(analysis.goal, "")

    fun getGoalTimeline(profile: UserProfile, completionPercent: Int = 0) =
        GoalTimelineEngine.calculate(profile.currentWeightKg, profile.goalWeightKg, profile.monthsAvailable, completionPercent)

    fun detectProteinDeficit(requiredG: Int, currentG: Float, isVegetarian: Boolean) =
        ProteinDeficitEngine.detect(requiredG, currentG, isVegetarian)

    suspend fun completeTodayDiet(plan: List<DailyMealPlan>, targetCalories: Int, targetProtein: Int, waterMl: Int) {
        val totals = DietPlanEngine.calculateDayTotals(plan)
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val calorieDiff = targetCalories - totals.calories
        val proteinPercent = ((totals.proteinG / targetProtein) * 100).toInt().coerceIn(0, 100)
        val caloriePercent = ((totals.calories.toFloat() / targetCalories) * 100).toInt().coerceIn(0, 100)
        val completion = DailyCompletion(
            date = today,
            totalCalories = totals.calories,
            totalProteinG = totals.proteinG,
            totalCarbsG = totals.carbsG,
            totalFatG = totals.fatG,
            waterMl = waterMl,
            calorieDeficit = if (calorieDiff > 0) calorieDiff else 0,
            calorieSurplus = if (calorieDiff < 0) -calorieDiff else 0,
            goalCompletionPercent = (proteinPercent + caloriePercent) / 2,
            completed = true
        )
        dailyCompletionDao.save(completion.toEntity())
    }

    fun observeTodayCompletion(): Flow<DailyCompletion?> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return dailyCompletionDao.observeByDate(today).map { it?.toDomain() }
    }
}
