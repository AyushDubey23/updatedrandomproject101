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
import com.messfit.ai.ui.components.SectionHeader
import com.messfit.ai.ui.theme.*
import com.messfit.ai.ui.viewmodel.MessFitUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyAnalysisScreen(state: MessFitUiState, onBack: () -> Unit) {
    val analysis = state.bodyAnalysis

    GradientBackground {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Body Analysis", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black.copy(alpha = 0.5f))
            )

            Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
                SectionHeader("Your Metrics", "Scientifically calculated for hostel students")

                analysis?.let { a ->
                    AnalysisCard("BMI", String.format("%.1f", a.bmi))
                    AnalysisCard("BMR", "${a.bmr} kcal")
                    AnalysisCard("TDEE", "${a.tdee} kcal")
                    AnalysisCard("Maintenance Calories", "${a.maintenanceCalories} kcal")
                    AnalysisCard("Recommended Calories", "${a.recommendedCalories} kcal", highlight = true)
                    AnalysisCard("Recommended Protein", "${a.recommendedProteinG}g")
                    AnalysisCard("Recommended Fat", "${a.recommendedFatG}g")
                    AnalysisCard("Recommended Carbs", "${a.recommendedCarbsG}g")
                    AnalysisCard("Daily Water Intake", "${a.dailyWaterMl} ml")
                    AnalysisCard("Goal", a.goal.name)
                    AnalysisCard(
                        if (a.goal.name == "CUT") "Expected Weekly Weight Loss" else "Expected Weekly Weight Change",
                        String.format("%.2f kg/week", kotlin.math.abs(a.expectedWeeklyWeightChangeKg))
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalysisCard(label: String, value: String, highlight: Boolean = false) {
    GlassCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = TextSecondary)
            Text(value, color = if (highlight) NeonGreen else TextPrimary, fontWeight = FontWeight.Bold)
        }
    }
}
