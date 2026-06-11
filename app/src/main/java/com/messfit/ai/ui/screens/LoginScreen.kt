package com.messfit.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.messfit.ai.ui.components.*
import com.messfit.ai.ui.theme.NeonGreen
import com.messfit.ai.ui.theme.TextSecondary

@Composable
fun LoginScreen(onLogin: () -> Unit, onSignUp: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    GradientBackground {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))
            Text("MESSFIT", fontSize = 42.sp, fontWeight = FontWeight.Black, color = NeonGreen)
            Text("AI", fontSize = 42.sp, fontWeight = FontWeight.Black, color = NeonGreen)
            Spacer(Modifier.height(8.dp))
            Text(
                "Built for Indian Hostel Students",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(Modifier.height(48.dp))

            GlassCard(Modifier.fillMaxWidth()) {
                Text("Welcome Back", style = MaterialTheme.typography.headlineMedium, color = NeonGreen)
                Spacer(Modifier.height(24.dp))
                MessFitTextField(email, { email = it }, "Email")
                Spacer(Modifier.height(16.dp))
                MessFitTextField(password, { password = it }, "Password")
                Spacer(Modifier.height(24.dp))
                NeonButton("Sign In", onLogin)
                Spacer(Modifier.height(12.dp))
                NeonButton("Create Account", onSignUp)
            }

            Spacer(Modifier.weight(1f))
            CreatedByFooter(Modifier.padding(bottom = 16.dp))
        }
    }
}
