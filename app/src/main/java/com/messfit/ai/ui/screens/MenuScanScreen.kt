package com.messfit.ai.ui.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.messfit.ai.ui.viewmodel.MessFitUiState
import com.messfit.ai.ui.components.*
import com.messfit.ai.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScanScreen(
    state: MessFitUiState,
    onBack: () -> Unit,
    onScanText: (String) -> Unit,
    onScanImage: (android.graphics.Bitmap) -> Unit
) {
    var menuText by remember { mutableStateOf("") }
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { stream ->
                BitmapFactory.decodeStream(stream)?.let(onScanImage)
            }
        }
    }

    GradientBackground {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Scan Hostel Menu", color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = NeonGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Black.copy(alpha = 0.5f))
            )

            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
            ) {
                SectionHeader(
                    "AI Menu Intelligence",
                    "Upload your weekly hostel mess menu — PDF, screenshot, or image"
                )

                GlassCard(Modifier.fillMaxWidth()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        NeonButton("Pick Image", { imagePicker.launch("image/*") }, Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = menuText,
                        onValueChange = { menuText = it },
                        label = { Text("Or paste menu text") },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            cursorColor = NeonGreen,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    NeonButton("Analyze Menu", { onScanText(menuText) }, enabled = menuText.isNotBlank())
                }

                if (state.menuScanning) {
                    Spacer(Modifier.height(24.dp))
                    CircularProgressIndicator(color = NeonGreen, modifier = Modifier.fillMaxWidth().wrapContentWidth())
                    Text("AI is extracting meals...", color = TextSecondary, modifier = Modifier.padding(top = 8.dp))
                }

                state.weeklyMenu?.let { menu ->
                    Spacer(Modifier.height(24.dp))
                    GlassCard(Modifier.fillMaxWidth()) {
                        Text("Mess Quality Score", style = MaterialTheme.typography.titleLarge, color = NeonGreen)
                        Spacer(Modifier.height(16.dp))
                        ScoreBar("Mess Rating", menu.qualityScore.messRating)
                        Spacer(Modifier.height(8.dp))
                        ScoreBar("Protein Score", menu.qualityScore.proteinScore)
                        Spacer(Modifier.height(8.dp))
                        ScoreBar("Carb Quality", menu.qualityScore.carbQualityScore)
                        Spacer(Modifier.height(8.dp))
                        ScoreBar("Fat Quality", menu.qualityScore.fatQualityScore)
                        Spacer(Modifier.height(8.dp))
                        ScoreBar("Micronutrients", menu.qualityScore.micronutrientScore)
                        Spacer(Modifier.height(8.dp))
                        ScoreBar("Bulking Suitability", menu.qualityScore.bulkingSuitabilityScore)
                        Spacer(Modifier.height(8.dp))
                        ScoreBar("Cutting Suitability", menu.qualityScore.cuttingSuitabilityScore)
                        Spacer(Modifier.height(8.dp))
                        ScoreBar("Vegetarian Friendly", menu.qualityScore.vegetarianFriendlinessScore)
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Extracted Meals", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    menu.meals.forEach { meal ->
                        GlassCard(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text("${meal.day} — ${meal.mealType.name}", color = NeonGreen)
                            Text(meal.items.joinToString(", "), color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}
