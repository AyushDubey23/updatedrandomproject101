package com.messfit.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.messfit.ai.ui.viewmodel.MessFitUiState
import com.messfit.ai.ui.components.*
import com.messfit.ai.ui.theme.*

@Composable
fun DashboardScreen(
    state: MessFitUiState,
    onNavigate: (String) -> Unit,
    onCompleteDiet: () -> Unit
) {
    val profile = state.profile ?: return
    val analysis = state.bodyAnalysis ?: return
    val completion = state.todayCompletion
    val calories = completion?.totalCalories ?: state.todayMeals.sumOf { it.plan.macros.calories }
    val protein = completion?.totalProteinG ?: state.todayMeals.sumOf { it.plan.macros.proteinG.toDouble() }.toFloat()

    GradientBackground {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Hey, ${profile.name.split(" ").first()}!", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                    Text("${profile.hostelName.ifBlank { "Hostel" }} • ${profile.collegeName.ifBlank { "College" }}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                IconButton(onClick = { onNavigate("settings") }) {
                    Icon(Icons.Default.Settings, "Settings", tint = NeonGreen)
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ProgressRing(
                    progress = calories / analysis.recommendedCalories.toFloat(),
                    label = "Calories",
                    value = "$calories/${analysis.recommendedCalories}"
                )
                ProgressRing(
                    progress = protein / analysis.recommendedProteinG,
                    label = "Protein",
                    value = "${protein.toInt()}/${analysis.recommendedProteinG}g"
                )
                ProgressRing(
                    progress = (completion?.waterMl ?: 0) / analysis.dailyWaterMl.toFloat(),
                    label = "Water",
                    value = "${(completion?.waterMl ?: 0) / 1000}L",
                    color = NeonGreenDim
                )
            }

            Spacer(Modifier.height(20.dp))

            GlassCard(Modifier.fillMaxWidth()) {
                Text("Goal Progress", style = MaterialTheme.typography.titleLarge, color = NeonGreen)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Current", "${profile.currentWeightKg} kg")
                    StatItem("Target", "${profile.goalWeightKg} kg")
                    StatItem("Timeline", "${profile.monthsAvailable} mo")
                }
                state.goalTimeline?.let { timeline ->
                    Spacer(Modifier.height(12.dp))
                    ScoreBar("Goal Completion", timeline.completionPercent)
                }
            }

            Spacer(Modifier.height(16.dp))

            state.proteinDeficit?.let { deficit ->
                if (deficit.deficitG > 0) {
                    GlassCard(Modifier.fillMaxWidth()) {
                        Text("⚡ Protein Deficit Detector", style = MaterialTheme.typography.titleMedium, color = NeonGreen)
                        Spacer(Modifier.height(8.dp))
                        Text("Required: ${deficit.requiredG}g | Current: ${deficit.currentG.toInt()}g | Deficit: ${deficit.deficitG.toInt()}g", color = TextSecondary)
                        deficit.recommendations.forEach { item ->
                            Text("• ${item.name} (${item.quantity}) — ₹${item.costInr}", color = TextPrimary, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }

            state.weeklyMenu?.qualityScore?.let { score ->
                GlassCard(Modifier.fillMaxWidth(), onClick = { onNavigate("menu_scan") }) {
                    Text("Weekly Mess Analysis", style = MaterialTheme.typography.titleLarge, color = NeonGreen)
                    Spacer(Modifier.height(12.dp))
                    ScoreBar("Mess Rating", score.messRating)
                    Spacer(Modifier.height(8.dp))
                    ScoreBar("Protein Score", score.proteinScore)
                }
                Spacer(Modifier.height(16.dp))
            }

            Text("Today's Diet Plan", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Spacer(Modifier.height(8.dp))
            state.todayMeals.forEach { meal ->
                GlassCard(Modifier.fillMaxWidth().padding(vertical = 4.dp), onClick = { onNavigate("diet_plan") }) {
                    Text(meal.mealType.name, color = NeonGreen, fontWeight = FontWeight.Bold)
                    meal.plan.messItems.forEach { Text("Mess: $it", color = TextPrimary) }
                    if (meal.plan.instruction.isNotBlank()) Text(meal.plan.instruction, color = TextSecondary)
                    meal.plan.addOns.forEach { Text("Add: $it", color = NeonGreen) }
                    meal.plan.outsideFood?.let { Text("Buy: $it — ₹${meal.plan.costInr}", color = NeonGreen) }
                    Text("Protein: ${meal.plan.macros.proteinG.toInt()}g | ${meal.plan.macros.calories} cal", color = TextSecondary)
                }
            }

            Spacer(Modifier.height(16.dp))
            if (completion?.completed != true) {
                NeonButton("I Completed Today's Diet", onCompleteDiet)
            } else {
                GlassCard(Modifier.fillMaxWidth()) {
                    Text("✓ Day Complete — ${completion.goalCompletionPercent}%", color = NeonGreen, fontWeight = FontWeight.Bold)
                    Text("Calories: ${completion.totalCalories} | Protein: ${completion.totalProteinG.toInt()}g", color = TextSecondary)
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickNavCard("Workout", Icons.Default.FitnessCenter, Modifier.weight(1f)) { onNavigate("workout") }
                QuickNavCard("Shopping", Icons.Default.ShoppingCart, Modifier.weight(1f)) { onNavigate("shopping") }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, color = NeonGreen, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}

@Composable
private fun QuickNavCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    GlassCard(modifier = modifier, onClick = onClick) {
        Icon(icon, title, tint = NeonGreen)
        Spacer(Modifier.height(8.dp))
        Text(title, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
