package com.messfit.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.messfit.ai.ui.components.*
import com.messfit.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    GradientBackground {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("About", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black.copy(alpha = 0.5f))
            )

            Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
                SectionHeader("MESSFIT AI", "India's First AI Fitness Coach for Hostel Students")

                GlassCard(Modifier.fillMaxWidth()) {
                    Text(
                        "Most fitness apps assume users cook their own food, have unlimited choices, and can afford expensive diets. Indian hostel students cannot.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "MESSFIT AI understands Indian mess food, hostel lifestyle, student budgets, vegetarian diets, and cheap protein sources.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary
                    )
                }

                Spacer(Modifier.height(24.dp))
                Text("Founder", style = MaterialTheme.typography.titleLarge, color = NeonGreen)
                Spacer(Modifier.height(12.dp))
                FounderSection()

                Spacer(Modifier.height(24.dp))
                GlassCard(Modifier.fillMaxWidth()) {
                    Text("Version 1.0.0", color = TextSecondary)
                    Text("Built with Kotlin • Jetpack Compose • Gemini AI", color = TextSecondary, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
