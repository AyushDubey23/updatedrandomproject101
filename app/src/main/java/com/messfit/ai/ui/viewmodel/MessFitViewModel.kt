package com.messfit.ai.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.messfit.ai.data.model.*
import com.messfit.ai.data.repository.MessFitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

data class MessFitUiState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val bodyAnalysis: BodyAnalysis? = null,
    val weeklyMenu: WeeklyMenu? = null,
    val dietPlan: List<DayDietPlan> = emptyList(),
    val todayMeals: List<DailyMealPlan> = emptyList(),
    val shoppingList: ShoppingList? = null,
    val workoutPlan: List<WorkoutDay> = emptyList(),
    val cardioRecommendations: List<CardioRecommendation> = emptyList(),
    val goalTimeline: GoalTimeline? = null,
    val proteinDeficit: ProteinDeficit? = null,
    val todayCompletion: DailyCompletion? = null,
    val selectedWorkoutSplit: WorkoutSplit = WorkoutSplit.PUSH_PULL_LEGS,
    val error: String? = null,
    val menuScanning: Boolean = false
)

@HiltViewModel
class MessFitViewModel @Inject constructor(
    private val repository: MessFitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessFitUiState())
    val uiState: StateFlow<MessFitUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.profileFlow,
                repository.latestMenuFlow,
                repository.observeTodayCompletion()
            ) { profile, menu, completion ->
                Triple(profile, menu, completion)
            }.collect { (profile, menu, completion) ->
                refreshState(profile, menu, completion)
            }
        }
    }

    private fun refreshState(profile: UserProfile?, menu: WeeklyMenu?, completion: DailyCompletion?) {
        if (profile == null) {
            _uiState.update { it.copy(profile = null) }
            return
        }
        val analysis = repository.getBodyAnalysis(profile)
        val meals = menu?.meals?.ifEmpty { null }?.let { menu } ?: WeeklyMenu()
        val diet = repository.generateDietPlan(profile, meals)
        val today = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val todayMeals = diet.find { it.day.equals(today, ignoreCase = true) }?.meals ?: diet.firstOrNull()?.meals.orEmpty()
        val shopping = repository.generateShoppingList(profile, analysis)
        val split = _uiState.value.selectedWorkoutSplit
        val workout = repository.generateWorkoutPlan(profile, split)
        val cardio = repository.getCardioRecommendations(analysis)
        val timeline = repository.getGoalTimeline(profile, completion?.goalCompletionPercent ?: 0)
        val consumed = completion?.totalProteinG ?: todayMeals.sumOf { it.plan.macros.proteinG.toDouble() }.toFloat() * 0.5f
        val deficit = repository.detectProteinDeficit(analysis.recommendedProteinG, consumed, profile.isVegetarian)

        _uiState.update {
            it.copy(
                profile = profile,
                bodyAnalysis = analysis,
                weeklyMenu = meals,
                dietPlan = diet,
                todayMeals = todayMeals,
                shoppingList = shopping,
                workoutPlan = workout,
                cardioRecommendations = cardio,
                goalTimeline = timeline,
                proteinDeficit = deficit,
                todayCompletion = completion
            )
        }
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.saveProfile(profile.copy(onboardingComplete = true))
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun scanMenuFromText(text: String) {
        val profile = _uiState.value.profile ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(menuScanning = true, error = null) }
            try {
                repository.scanMenuFromText(text, profile.isVegetarian)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
            _uiState.update { it.copy(menuScanning = false) }
        }
    }

    fun scanMenuFromImage(bitmap: Bitmap) {
        val profile = _uiState.value.profile ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(menuScanning = true, error = null) }
            try {
                repository.scanMenuFromImage(bitmap, profile.isVegetarian)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
            _uiState.update { it.copy(menuScanning = false) }
        }
    }

    fun completeTodayDiet() {
        val state = _uiState.value
        val analysis = state.bodyAnalysis ?: return
        val meals = state.todayMeals
        if (meals.isEmpty()) return
        viewModelScope.launch {
            repository.completeTodayDiet(
                meals,
                analysis.recommendedCalories,
                analysis.recommendedProteinG,
                analysis.dailyWaterMl / 3
            )
        }
    }

    fun setWorkoutSplit(split: WorkoutSplit) {
        val profile = _uiState.value.profile ?: return
        _uiState.update {
            it.copy(
                selectedWorkoutSplit = split,
                workoutPlan = repository.generateWorkoutPlan(profile, split)
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
