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
import com.messfit.ai.domain.engine.BudgetEngine
import com.messfit.ai.ui.components.GlassCard
import com.messfit.ai.ui.components.GradientBackground
import com.messfit.ai.ui.components.SectionHeader
import com.messfit.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(state: MessFitUiState, onBack: () -> Unit) {
    val profile = state.profile
    val monthlyItems = if (profile != null) BudgetEngine.monthlyBudgetBreakdown(profile.monthlyBudget, profile.isVegetarian) else emptyList()

    GradientBackground {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("AI Shopping List", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black.copy(alpha = 0.5f))
            )

            Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp)) {
                SectionHeader("Weekly Shopping List", "Optimized for your ₹${profile?.monthlyBudget ?: 1000}/month budget")

                state.shoppingList?.items?.forEach { item ->
                    GlassCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(item.name, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text(item.quantity, color = TextSecondary)
                                Text("${item.proteinPerServingG.toInt()}g protein/serving", color = NeonGreen)
                            }
                            Text("₹${item.costInr}", color = NeonGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                state.shoppingList?.let { list ->
                    Spacer(Modifier.height(16.dp))
                    GlassCard(Modifier.fillMaxWidth()) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Weekly Total", color = TextPrimary, fontWeight = FontWeight.Bold)
                            Text("₹${list.totalCostInr}", color = NeonGreen, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
                Text("Monthly Budget Breakdown", style = MaterialTheme.typography.titleLarge, color = NeonGreen)
                monthlyItems.forEach { item ->
                    GlassCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(item.name, color = TextPrimary)
                                Text(item.quantity, color = TextSecondary)
                            }
                            Text("₹${item.costInr}", color = NeonGreen)
                        }
                    }
                }
            }
        }
    }
}
