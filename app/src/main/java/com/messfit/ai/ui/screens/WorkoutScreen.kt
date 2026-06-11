package com.messfit.ai.ui.screens

import androidx.compose.foundation.horizontalScroll
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
import com.messfit.ai.ui.viewmodel.MessFitUiState
import com.messfit.ai.data.model.WorkoutSplit
import com.messfit.ai.ui.components.GlassCard
import com.messfit.ai.ui.components.GradientBackground
import com.messfit.ai.ui.components.SectionHeader
import com.messfit.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    state: MessFitUiState,
    onBack: () -> Unit,
    onSplitChange: (WorkoutSplit) -> Unit
) {
    GradientBackground {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("AI Fitness Planner", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black.copy(alpha = 0.5f))
            )

            Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
                SectionHeader("Workout Split", state.profile?.gymExperience?.name ?: "BEGINNER")

                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    WorkoutSplit.entries.forEach { split ->
                        FilterChip(
                            selected = state.selectedWorkoutSplit == split,
                            onClick = { onSplitChange(split) },
                            label = { Text(split.name.replace('_', ' '), style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NeonGreen)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                state.workoutPlan.forEach { day ->
                    GlassCard(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Text("${day.day}: ${day.title}", color = NeonGreen, fontWeight = FontWeight.Bold)
                        if (day.exercises.isEmpty()) {
                            Text("Rest Day", color = TextSecondary)
                        }
                        day.exercises.forEach { ex ->
                            Spacer(Modifier.height(12.dp))
                            Text(ex.name, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("${ex.sets} sets × ${ex.reps} | Rest: ${ex.restSeconds}s", color = TextSecondary)
                            Text("Target: ${ex.targetMuscle}", color = NeonGreen)
                            Text("Technique: ${ex.technique}", color = TextSecondary)
                            Text("Avoid: ${ex.commonMistakes}", color = ErrorRed.copy(alpha = 0.8f))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Cardio Recommendations", style = MaterialTheme.typography.titleLarge, color = NeonGreen)
                state.cardioRecommendations.forEach { cardio ->
                    GlassCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(cardio.type, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text("${cardio.durationMinutes} min • ${cardio.frequency} • ${cardio.intensity}", color = TextSecondary)
                        Text(cardio.reason, color = NeonGreen)
                    }
                }
            }
        }
    }
}
