package com.messfit.ai.data.model

enum class Gender { MALE, FEMALE, OTHER }
enum class ActivityLevel { SEDENTARY, LIGHT, MODERATE, ACTIVE, VERY_ACTIVE }
enum class GymExperience { BEGINNER, INTERMEDIATE, ADVANCED }
enum class FitnessGoal { CUT, BULK, MAINTAIN }
enum class WorkoutSplit {
    PUSH_PULL_LEGS, BRO_SPLIT, UPPER_LOWER, ARNOLD_SPLIT, FULL_BODY, HOME_WORKOUT
}
enum class MealType { BREAKFAST, LUNCH, SNACK, DINNER }

data class UserProfile(
    val id: Long = 0,
    val name: String = "",
    val age: Int = 20,
    val gender: Gender = Gender.MALE,
    val heightCm: Float = 170f,
    val currentWeightKg: Float = 70f,
    val goalWeightKg: Float = 70f,
    val monthsAvailable: Int = 6,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val gymExperience: GymExperience = GymExperience.BEGINNER,
    val isVegetarian: Boolean = true,
    val hostelName: String = "",
    val collegeName: String = "",
    val monthlyBudget: Int = 1000,
    val currentSupplements: String = "",
    val sleepHours: Float = 7f,
    val dailyWalkingSteps: Int = 5000,
    val onboardingComplete: Boolean = false
)

data class MacroNutrients(
    val calories: Int = 0,
    val proteinG: Float = 0f,
    val carbsG: Float = 0f,
    val fatG: Float = 0f,
    val fiberG: Float = 0f
)

data class MicroNutrients(
    val ironMg: Float = 0f,
    val calciumMg: Float = 0f,
    val vitaminDMcg: Float = 0f,
    val vitaminB12Mcg: Float = 0f,
    val zincMg: Float = 0f
)

data class FoodItem(
    val name: String,
    val macros: MacroNutrients,
    val micros: MicroNutrients = MicroNutrients(),
    val isMessFood: Boolean = true,
    val costInr: Int = 0
)

data class MessMeal(
    val day: String,
    val mealType: MealType,
    val items: List<String>
)

data class MessQualityScore(
    val messRating: Int = 0,
    val proteinScore: Int = 0,
    val carbQualityScore: Int = 0,
    val fatQualityScore: Int = 0,
    val micronutrientScore: Int = 0,
    val bulkingSuitabilityScore: Int = 0,
    val cuttingSuitabilityScore: Int = 0,
    val vegetarianFriendlinessScore: Int = 0
)

data class BodyAnalysis(
    val bmi: Float = 0f,
    val bmr: Int = 0,
    val tdee: Int = 0,
    val maintenanceCalories: Int = 0,
    val recommendedCalories: Int = 0,
    val recommendedProteinG: Int = 0,
    val recommendedFatG: Int = 0,
    val recommendedCarbsG: Int = 0,
    val dailyWaterMl: Int = 0,
    val expectedWeeklyWeightChangeKg: Float = 0f,
    val goal: FitnessGoal = FitnessGoal.MAINTAIN
)

data class DietInstruction(
    val messItems: List<String> = emptyList(),
    val instruction: String = "",
    val addOns: List<String> = emptyList(),
    val outsideFood: String? = null,
    val costInr: Int = 0,
    val macros: MacroNutrients = MacroNutrients()
)

data class DailyMealPlan(
    val day: String,
    val mealType: MealType,
    val plan: DietInstruction
)

data class DayDietPlan(
    val day: String,
    val meals: List<DailyMealPlan>
)

data class DailyCompletion(
    val date: String,
    val totalCalories: Int = 0,
    val totalProteinG: Float = 0f,
    val totalCarbsG: Float = 0f,
    val totalFatG: Float = 0f,
    val waterMl: Int = 0,
    val calorieDeficit: Int = 0,
    val calorieSurplus: Int = 0,
    val goalCompletionPercent: Int = 0,
    val completed: Boolean = false
)

data class BudgetItem(
    val name: String,
    val quantity: String,
    val costInr: Int,
    val proteinPerServingG: Float
)

data class ShoppingList(
    val items: List<BudgetItem>,
    val totalCostInr: Int
)

data class GoalTimeline(
    val currentWeightKg: Float,
    val goalWeightKg: Float,
    val monthsAvailable: Int,
    val weeklyTargetKg: Float,
    val monthlyTargetKg: Float,
    val expectedFatLossKg: Float,
    val expectedMuscleGainKg: Float,
    val completionPercent: Int
)

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: String,
    val restSeconds: Int,
    val technique: String,
    val commonMistakes: String,
    val targetMuscle: String,
    val videoUrl: String = ""
)

data class WorkoutDay(
    val day: String,
    val title: String,
    val exercises: List<Exercise>
)

data class CardioRecommendation(
    val type: String,
    val durationMinutes: Int,
    val frequency: String,
    val intensity: String,
    val reason: String
)

data class ProteinDeficit(
    val requiredG: Int,
    val currentG: Float,
    val deficitG: Float,
    val recommendations: List<BudgetItem>
)

data class WeeklyMenu(
    val id: Long = 0,
    val scannedAt: Long = System.currentTimeMillis(),
    val meals: List<MessMeal> = emptyList(),
    val qualityScore: MessQualityScore = MessQualityScore()
)
