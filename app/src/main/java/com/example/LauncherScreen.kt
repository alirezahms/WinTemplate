package com.example

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun LauncherScreen(
    darkTheme: Boolean,
    onStart: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(7) }

    // Auto-transition after 7 seconds
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        onStart()
    }

    // Elegant dark neon gradient background
    val bgBrush = if (darkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF07050F), // Deep space black-purple
                Color(0xFF0D0B21), // Slate dark violet
                Color(0xFF090814)  // Very dark base
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF3F4F6),
                Color(0xFFE5E7EB),
                Color(0xFFD1D5DB)
            )
        )
    }

    val primaryAccent = VpnBlue
    val secondaryAccent = Color(0xFF8B5CF6) // Royal Purple

    // Infinite transition for orbital glow and pulsing effects
    val infiniteTransition = rememberInfiniteTransition(label = "launcher_pulse")
    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "outer_pulse"
    )

    val pulseScale2 by infiniteTransition.animateFloat(
        initialValue = 1.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "inner_pulse"
    )

    val orbitRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        // Decorative background glowing spots for dark mode
        if (darkTheme) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-80).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(primaryAccent.copy(alpha = 0.15f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = 100.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(secondaryAccent.copy(alpha = 0.12f), Color.Transparent)
                        ),
                        shape = CircleShape
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Developer Badge & App Label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Developer badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (darkTheme) Color(0xFF161426) else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, primaryAccent.copy(alpha = 0.3f)),
                    modifier = Modifier.shadow(if (darkTheme) 0.dp else 2.dp, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Developer",
                            tint = primaryAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "HesamWeb",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (darkTheme) Color.White else Color.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Text(
                    text = "V2RAY CLIENT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryAccent.copy(alpha = 0.8f),
                    letterSpacing = 2.sp
                )
            }

            // Middle: Centered Branding Icon with Pulsing Orbits
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(260.dp)
                ) {
                    // Outer Pulsing Glow
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .scale(pulseScale1)
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(primaryAccent.copy(alpha = 0.4f), Color.Transparent)
                                ),
                                shape = CircleShape
                            )
                    )

                    // Inner Pulsing Glow
                    Box(
                        modifier = Modifier
                            .size(175.dp)
                            .scale(pulseScale2)
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Transparent, secondaryAccent.copy(alpha = 0.35f))
                                ),
                                shape = CircleShape
                            )
                    )

                    // Card carrying the generated App Icon
                    Card(
                        modifier = Modifier
                            .size(124.dp)
                            .shadow(20.dp, CircleShape),
                        shape = CircleShape,
                        border = BorderStroke(2.dp, Brush.linearGradient(colors = listOf(primaryAccent, secondaryAccent)))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon),
                            contentDescription = "Win2ray Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Typography App Title with linear gradient highlight
                Text(
                    text = "Win2ray",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    color = if (darkTheme) Color.White else Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "The Ultimate Secure V2Ray Tunnel Client",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (darkTheme) Color(0xFF9CA3AF) else Color(0xFF4B5563),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Minimal feature summary
                Text(
                    text = "Bypass censorship smoothly, monitor live latencies, split-tunnel your apps, and configure Telegram proxies securely on a unified dark cyber shield.",
                    fontSize = 11.sp,
                    color = if (darkTheme) Color(0xFF6B7280) else Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    lineHeight = 16.sp
                )
            }

            // Bottom: Features Row & Beautiful Action Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 3 Pillar Highlights row (Secure, Fast, Global)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FeaturePill(icon = Icons.Default.Lock, text = "SECURE", darkTheme = darkTheme)
                    FeaturePill(icon = Icons.Default.Speed, text = "FAST", darkTheme = darkTheme)
                    FeaturePill(icon = Icons.Default.Public, text = "GLOBAL", darkTheme = darkTheme)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Beautiful Non-clickable Status Indicator Badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(12.dp, RoundedCornerShape(16.dp), ambientColor = primaryAccent, spotColor = primaryAccent)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(primaryAccent, secondaryAccent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "SECURE TUNNEL INITIALIZING... (${timeLeft}s)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Small Footer Credits
                Text(
                    text = "HesamWeb © 2026 • Secure Tunnel Engine v1.0.0",
                    fontSize = 10.sp,
                    color = if (darkTheme) Color(0xFF4B5563) else Color(0xFF9CA3AF),
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
fun FeaturePill(
    icon: ImageVector,
    text: String,
    darkTheme: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (darkTheme) Color(0xFF161426) else Color(0xFFE5E7EB))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = VpnBlue,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (darkTheme) Color(0xFF9CA3AF) else Color(0xFF4B5563),
            letterSpacing = 0.5.sp
        )
    }
}
