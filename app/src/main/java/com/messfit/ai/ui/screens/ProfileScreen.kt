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
import com.messfit.ai.ui.components.*
import com.messfit.ai.ui.theme.*
import com.messfit.ai.ui.viewmodel.MessFitUiState

@Composable
fun ProfileScreen(state: MessFitUiState, onNavigate: (String) -> Unit) {
    val profile = state.profile ?: return

    GradientBackground {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Text("Profile", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
            Spacer(Modifier.height(20.dp))

            GlassCard(Modifier.fillMaxWidth()) {
                Box(
                    Modifier.size(80.dp).align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, "Profile", tint = NeonGreen, modifier = Modifier.size(48.dp))
                }
                Spacer(Modifier.height(12.dp))
                Text(profile.name, style = MaterialTheme.typography.headlineMedium, color = TextPrimary, modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("${profile.collegeName} • ${profile.hostelName}", color = TextSecondary, modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Spacer(Modifier.height(16.dp))

            ProfileRow("Age", "${profile.age}")
            ProfileRow("Height", "${profile.heightCm} cm")
            ProfileRow("Weight", "${profile.currentWeightKg} kg → ${profile.goalWeightKg} kg")
            ProfileRow("Diet", if (profile.isVegetarian) "Vegetarian" else "Non-Vegetarian")
            ProfileRow("Budget", "₹${profile.monthlyBudget}/month")
            ProfileRow("Experience", profile.gymExperience.name)
            ProfileRow("Activity", profile.activityLevel.name.replace('_', ' '))

            Spacer(Modifier.height(16.dp))

            GlassCard(Modifier.fillMaxWidth(), onClick = { onNavigate("body_analysis") }) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Body Analysis", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ChevronRight, null, tint = NeonGreen)
                }
            }
            Spacer(Modifier.height(8.dp))
            GlassCard(Modifier.fillMaxWidth(), onClick = { onNavigate("goal_timeline") }) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Goal Timeline", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ChevronRight, null, tint = NeonGreen)
                }
            }
            Spacer(Modifier.height(8.dp))
            GlassCard(Modifier.fillMaxWidth(), onClick = { onNavigate("about") }) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("About MESSFIT AI", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ChevronRight, null, tint = NeonGreen)
                }
            }

            Spacer(Modifier.height(24.dp))
            CreatedByFooter()
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    GlassCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = TextSecondary)
            Text(value, color = TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}
