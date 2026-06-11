package com.messfit.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.messfit.ai.ui.components.*
import com.messfit.ai.ui.theme.*
import com.messfit.ai.ui.viewmodel.MessFitUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(state: MessFitUiState, onBack: () -> Unit, onNavigate: (String) -> Unit) {
    var notifications by remember { mutableStateOf(true) }
    var haptics by remember { mutableStateOf(true) }

    GradientBackground {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Settings", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black.copy(alpha = 0.5f))
            )

            Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
                GlassCard(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Push Notifications", color = TextPrimary)
                        Switch(checked = notifications, onCheckedChange = { notifications = it }, colors = SwitchDefaults.colors(checkedTrackColor = NeonGreen))
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Haptic Feedback", color = TextPrimary)
                        Switch(checked = haptics, onCheckedChange = { haptics = it }, colors = SwitchDefaults.colors(checkedTrackColor = NeonGreen))
                    }
                }

                Spacer(Modifier.height(16.dp))

                GlassCard(Modifier.fillMaxWidth(), onClick = { onNavigate("about") }) {
                    Text("About MESSFIT AI", color = TextPrimary)
                }

                Spacer(Modifier.height(16.dp))

                GlassCard(Modifier.fillMaxWidth()) {
                    Text("Account", style = MaterialTheme.typography.titleMedium, color = NeonGreen)
                    Spacer(Modifier.height(8.dp))
                    state.profile?.let { p ->
                        Text("Name: ${p.name}", color = TextSecondary)
                        Text("Hostel: ${p.hostelName}", color = TextSecondary)
                        Text("College: ${p.collegeName}", color = TextSecondary)
                    }
                }

                Spacer(Modifier.height(24.dp))
                CreatedByFooter()
            }
        }
    }
}
