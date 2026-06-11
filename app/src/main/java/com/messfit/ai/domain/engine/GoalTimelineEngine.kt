package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.GoalTimeline
import kotlin.math.abs

object GoalTimelineEngine {

    fun calculate(
        currentWeightKg: Float,
        goalWeightKg: Float,
        monthsAvailable: Int,
        completionPercent: Int = 0
    ): GoalTimeline {
        val diff = goalWeightKg - currentWeightKg
        val weeks = (monthsAvailable * 4.33f).coerceAtLeast(1f)
        val weeklyTarget = diff / weeks
        val monthlyTarget = diff / monthsAvailable.coerceAtLeast(1)

        val isCutting = diff < 0
        val fatLoss = if (isCutting) abs(diff) * 0.85f else 0f
        val muscleGain = if (!isCutting) abs(diff) * 0.6f else abs(diff) * 0.15f

        return GoalTimeline(
            currentWeightKg = currentWeightKg,
            goalWeightKg = goalWeightKg,
            monthsAvailable = monthsAvailable,
            weeklyTargetKg = weeklyTarget,
            monthlyTargetKg = monthlyTarget,
            expectedFatLossKg = fatLoss,
            expectedMuscleGainKg = muscleGain,
            completionPercent = completionPercent.coerceIn(0, 100)
        )
    }
}
