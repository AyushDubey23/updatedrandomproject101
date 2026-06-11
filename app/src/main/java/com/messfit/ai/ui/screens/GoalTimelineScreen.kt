package com.messfit.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.messfit.ai.ui.components.GlassCard
import com.messfit.ai.ui.components.GradientBackground
import com.messfit.ai.ui.components.ScoreBar
import com.messfit.ai.ui.components.SectionHeader
import com.messfit.ai.ui.theme.*
import com.messfit.ai.ui.viewmodel.MessFitUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalTimelineScreen(state: MessFitUiState, onBack: () -> Unit) {
    val timeline = state.goalTimeline

    GradientBackground {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Goal Timeline", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black.copy(alpha = 0.5f))
            )

            Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
                SectionHeader("Your Journey", "AI-calculated targets")

                timeline?.let { t ->
                    GlassCard(Modifier.fillMaxWidth()) {
                        TimelineRow("Current Weight", "${t.currentWeightKg} kg")
                        TimelineRow("Goal Weight", "${t.goalWeightKg} kg")
                        TimelineRow("Timeline", "${t.monthsAvailable} months")
                        TimelineRow("Weekly Target", String.format("%.2f kg", t.weeklyTargetKg))
                        TimelineRow("Monthly Target", String.format("%.2f kg", t.monthlyTargetKg))
                        TimelineRow("Expected Fat Loss", String.format("%.1f kg", t.expectedFatLossKg))
                        TimelineRow("Expected Muscle Gain", String.format("%.1f kg", t.expectedMuscleGainKg))
                        Spacer(Modifier.height(12.dp))
                        ScoreBar("Goal Completion", t.completionPercent)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextSecondary)
        Text(value, color = NeonGreen, fontWeight = FontWeight.SemiBold)
    }
}
