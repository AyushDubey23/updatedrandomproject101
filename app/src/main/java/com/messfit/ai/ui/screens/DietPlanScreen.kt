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
import com.messfit.ai.ui.viewmodel.MessFitUiState
import com.messfit.ai.ui.components.GlassCard
import com.messfit.ai.ui.components.GradientBackground
import com.messfit.ai.ui.components.SectionHeader
import com.messfit.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlanScreen(state: MessFitUiState, onBack: () -> Unit) {
    GradientBackground {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Daily Diet Plan", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black.copy(alpha = 0.5f))
            )

            Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
                SectionHeader("Your Personalized Plan", "Exact foods to eat from mess + budget add-ons")

                state.dietPlan.forEach { dayPlan ->
                    Text(dayPlan.day.uppercase(), style = MaterialTheme.typography.headlineMedium, color = NeonGreen, modifier = Modifier.padding(vertical = 12.dp))

                    dayPlan.meals.forEach { meal ->
                        GlassCard(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Text(meal.mealType.name, color = NeonGreen, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))

                            if (meal.plan.messItems.isNotEmpty()) {
                                Text("Mess:", color = TextSecondary)
                                meal.plan.messItems.forEach { Text("  • $it", color = TextPrimary) }
                            }
                            if (meal.plan.instruction.isNotBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Text("Instruction: ${meal.plan.instruction}", color = TextPrimary)
                            }
                            meal.plan.addOns.forEach {
                                Text("Add: $it", color = NeonGreen)
                            }
                            meal.plan.outsideFood?.let {
                                Text("Outside Food — Buy: $it", color = NeonGreen)
                                Text("Cost: ₹${meal.plan.costInr}", color = TextSecondary)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Protein: ${meal.plan.macros.proteinG.toInt()}g | Calories: ${meal.plan.macros.calories}",
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
