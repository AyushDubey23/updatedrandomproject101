package com.messfit.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.messfit.ai.data.model.*
import com.messfit.ai.ui.components.*
import com.messfit.ai.ui.theme.NeonGreen
import com.messfit.ai.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(onComplete: (UserProfile) -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("20") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    var height by remember { mutableStateOf("170") }
    var currentWeight by remember { mutableStateOf("70") }
    var goalWeight by remember { mutableStateOf("75") }
    var months by remember { mutableStateOf("6") }
    var activity by remember { mutableStateOf(ActivityLevel.MODERATE) }
    var experience by remember { mutableStateOf(GymExperience.BEGINNER) }
    var vegetarian by remember { mutableStateOf(true) }
    var hostel by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("1000") }
    var supplements by remember { mutableStateOf("") }
    var sleep by remember { mutableStateOf("7") }
    var steps by remember { mutableStateOf("5000") }

    val totalSteps = 4

    GradientBackground {
        Column(Modifier.fillMaxSize().padding(24.dp)) {
            LinearProgressIndicator(
                progress = { (step + 1f) / totalSteps },
                modifier = Modifier.fillMaxWidth(),
                color = NeonGreen
            )
            Spacer(Modifier.height(24.dp))

            Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                when (step) {
                    0 -> {
                        SectionHeader("About You", "Let's personalize your fitness journey")
                        MessFitTextField(name, { name = it }, "Full Name")
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(age, { age = it }, "Age")
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(height, { height = it }, "Height (cm)")
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(currentWeight, { currentWeight = it }, "Current Weight (kg)")
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(goalWeight, { goalWeight = it }, "Goal Weight (kg)")
                    }
                    1 -> {
                        SectionHeader("Fitness Profile")
                        Text("Gender", color = TextPrimary)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Gender.entries.forEach { g ->
                                FilterChip(
                                    selected = gender == g,
                                    onClick = { gender = g },
                                    label = { Text(g.name) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NeonGreen)
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(months, { months = it }, "Months Available")
                        Spacer(Modifier.height(12.dp))
                        Text("Activity Level", color = TextPrimary)
                        ActivityLevel.entries.forEach { level ->
                            Row(Modifier.fillMaxWidth()) {
                                RadioButton(selected = activity == level, onClick = { activity = level })
                                Text(level.name.replace('_', ' '), color = TextPrimary)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Gym Experience", color = TextPrimary)
                        GymExperience.entries.forEach { exp ->
                            Row(Modifier.fillMaxWidth()) {
                                RadioButton(selected = experience == exp, onClick = { experience = exp })
                                Text(exp.name, color = TextPrimary)
                            }
                        }
                    }
                    2 -> {
                        SectionHeader("Hostel Details")
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Switch(checked = vegetarian, onCheckedChange = { vegetarian = it })
                            Text("Vegetarian", color = TextPrimary, modifier = Modifier.padding(start = 8.dp))
                        }
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(hostel, { hostel = it }, "Hostel Name")
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(college, { college = it }, "College Name")
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(budget, { budget = it }, "Monthly Budget (₹)")
                    }
                    3 -> {
                        SectionHeader("Lifestyle")
                        MessFitTextField(supplements, { supplements = it }, "Current Supplements")
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(sleep, { sleep = it }, "Sleep Hours")
                        Spacer(Modifier.height(12.dp))
                        MessFitTextField(steps, { steps = it }, "Daily Walking Steps")
                    }
                }
            }

            if (step < totalSteps - 1) {
                NeonButton("Continue", onClick = { step += 1 })
            } else {
                NeonButton("Start My Journey", onClick = {
                    onComplete(
                        UserProfile(
                            name = name.ifBlank { "Student" },
                            age = age.toIntOrNull() ?: 20,
                            gender = gender,
                            heightCm = height.toFloatOrNull() ?: 170f,
                            currentWeightKg = currentWeight.toFloatOrNull() ?: 70f,
                            goalWeightKg = goalWeight.toFloatOrNull() ?: 75f,
                            monthsAvailable = months.toIntOrNull() ?: 6,
                            activityLevel = activity,
                            gymExperience = experience,
                            isVegetarian = vegetarian,
                            hostelName = hostel,
                            collegeName = college,
                            monthlyBudget = budget.toIntOrNull() ?: 1000,
                            currentSupplements = supplements,
                            sleepHours = sleep.toFloatOrNull() ?: 7f,
                            dailyWalkingSteps = steps.toIntOrNull() ?: 5000,
                            onboardingComplete = true
                        )
                    )
                })
            }
            CreatedByFooter()
        }
    }
}
