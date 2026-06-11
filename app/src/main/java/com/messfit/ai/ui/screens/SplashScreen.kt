package com.messfit.ai.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.messfit.ai.ui.components.CreatedByFooter
import com.messfit.ai.ui.components.GradientBackground
import com.messfit.ai.ui.theme.NeonGreen
import com.messfit.ai.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onFinished()
    }

    GradientBackground {
        Column(
            Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "MESSFIT",
                fontSize = 52.sp,
                fontWeight = FontWeight.Black,
                color = NeonGreen,
                modifier = Modifier.scale(scale).alpha(alpha)
            )
            Text(
                "AI",
                fontSize = 52.sp,
                fontWeight = FontWeight.Black,
                color = NeonGreen,
                modifier = Modifier.scale(scale).alpha(alpha)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "India's First AI Fitness Coach\nfor Hostel Students",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.alpha(alpha),
                lineHeight = 26.sp
            )
        }
        CreatedByFooter(Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp).alpha(alpha))
    }
}
