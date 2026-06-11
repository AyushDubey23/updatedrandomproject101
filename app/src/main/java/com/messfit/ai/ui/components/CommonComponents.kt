package com.messfit.ai.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.messfit.ai.ui.theme.*

@Composable
fun CreatedByFooter(modifier: Modifier = Modifier) {
    Text(
        text = "Created by Ayush Dubey",
        style = MaterialTheme.typography.labelSmall,
        color = TextSecondary,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)
    )
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = listOf(GlassWhite, Color(0x0DFFFFFF)),
                    start = Offset(0f, 0f),
                    end = Offset(500f, 500f)
                )
            )
            .border(1.dp, GlassBorder, shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(20.dp),
        content = content
    )
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val view = LocalView.current
    Button(
        onClick = {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            onClick()
        },
        modifier = modifier.fillMaxWidth().height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonGreen,
            contentColor = Black,
            disabledContainerColor = LightGrey,
            disabledContentColor = TextSecondary
        )
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProgressRing(
    progress: Float,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    color: Color = NeonGreen
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "ring"
    )

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        CanvasRing(progress = animatedProgress, size = size, color = color)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
private fun CanvasRing(progress: Float, size: Dp, color: Color) {
    val strokeWidth = 10.dp
    androidx.compose.foundation.Canvas(modifier = Modifier.size(size)) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        drawArc(
            color = LightGrey,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke
        )
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = stroke
        )
    }
}

@Composable
fun ScoreBar(label: String, score: Int, modifier: Modifier = Modifier) {
    val animatedScore by animateFloatAsState(
        targetValue = score / 100f,
        animationSpec = tween(800),
        label = "score"
    )
    Column(modifier = modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            Text("$score", style = MaterialTheme.typography.bodyMedium, color = NeonGreen, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        Box(
            Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(LightGrey)
        ) {
            Box(
                Modifier
                    .fillMaxWidth(animatedScore)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(NeonGreen)
            )
        }
    }
}

@Composable
fun MessFitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonGreen,
            unfocusedBorderColor = GlassBorder,
            focusedLabelColor = NeonGreen,
            cursorColor = NeonGreen,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
        ),
        singleLine = true
    )
}

@Composable
fun SectionHeader(title: String, subtitle: String? = null) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        subtitle?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}

@Composable
fun FounderSection(modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier.fillMaxWidth()) {
        Box(
            Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(listOf(NeonGreen.copy(alpha = 0.3f), DarkGrey))
                )
                .border(2.dp, NeonGreen.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("AD", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
        }
        Spacer(Modifier.height(20.dp))
        Text(
            "Ayush Dubey",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Founder & Developer of MESSFIT AI",
            style = MaterialTheme.typography.titleMedium,
            color = NeonGreen,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "MESSFIT AI was built after observing that thousands of Indian hostel students struggle to achieve their fitness goals due to limited food choices, poor nutrition awareness, and budget constraints. The app aims to bridge the gap between hostel mess food and scientific fitness planning.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            lineHeight = 26.sp
        )
        Spacer(Modifier.height(12.dp))
        CreatedByFooter()
    }
}

@Composable
fun GradientBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Black)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonGreen.copy(alpha = 0.08f), Color.Transparent),
                        center = Offset(size.width * 0.8f, size.height * 0.1f),
                        radius = size.width * 0.6f
                    ),
                    radius = size.width * 0.6f,
                    center = Offset(size.width * 0.8f, size.height * 0.1f)
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonGreen.copy(alpha = 0.05f), Color.Transparent),
                        center = Offset(size.width * 0.2f, size.height * 0.7f),
                        radius = size.width * 0.5f
                    ),
                    radius = size.width * 0.5f,
                    center = Offset(size.width * 0.2f, size.height * 0.7f)
                )
            },
        content = content
    )
}
