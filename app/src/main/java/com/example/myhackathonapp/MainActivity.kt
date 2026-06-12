package com.example.myhackathonapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {

    private var liveSpokenText by mutableStateOf("System Secure. Ready to scan audio parameters...")
    private var isListeningState by mutableStateOf(false)

    // 🎯 Google ka Native Voice Recognition Launcher
    private val voiceRecognitionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isListeningState = false
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            processVoiceResults(matches)
        } else {
            liveSpokenText = "Scan cancelled or audio stream timed out."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val backgroundColor = Color(0xFF0F172A)
            val cardBackground = Color(0xFF1E293B)
            val neonGreen = Color(0xFF10B981)

            val statusColor by animateColorAsState(
                targetValue = if (isListeningState) neonGreen else Color(0xFF64748B),
                animationSpec = tween(durationMillis = 300)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // Header Section
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        Text(
                            text = "SHIELD CORE",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 4.sp
                        )
                        Text(
                            text = "AI CALL FRAUD DETECTION INTERCEPTOR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8),
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Live Status Display Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp).fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(statusColor)
                                )
                                Text(
                                    text = if (isListeningState) "GOOGLE MIC SUBSYSTEM ACTIVE" else "BUFFER IDLE",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Text(
                                text = liveSpokenText,
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 12.dp)
                            )
                        }
                    }

                    // Cyber Button
                    Button(
                        onClick = { launchGoogleVoiceDialog() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(bottom = 20.dp)
                    ) {
                        Text(
                            text = "LAUNCH GOOGLE MIC INTERFACE",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    private fun launchGoogleVoiceDialog() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            // 🎯 Isse Google ka bada official listening mic screen par pop-up hoga
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening for Scam Telemetry Buffer...")
        }
        try {
            isListeningState = true
            voiceRecognitionLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Google Speech Engine not found on this device!", Toast.LENGTH_SHORT).show()
            isListeningState = false
        }
    }

    private fun processVoiceResults(matches: ArrayList<String>?) {
        if (!matches.isNullOrEmpty()) {
            val originalText = matches[0]
            liveSpokenText = originalText

            val lowerText = originalText.lowercase()
            var scamType = ""
            var scamReason = ""

            if (lowerText.contains("otp") || lowerText.contains("card") || lowerText.contains("money") || lowerText.contains("ओटीपी")) {
                scamType = "FINANCIAL FRAUD DETECTED"
                scamReason = "Acoustic stream check: Captured text matches malicious attempts to pull financial tokens/OTP."
            } else if (lowerText.contains("block") || lowerText.contains("kyc") || lowerText.contains("password")) {
                scamType = "ACCOUNT SUSPENSION THREAT"
                scamReason = "Hostile threat profile detected: Forcing critical logs changes under fake account suspension panic."
            } else if (lowerText.contains("police") || lowerText.contains("lottery")) {
                scamType = "URGENCY & IMPERSONATION SCAM"
                scamReason = "Authority spoofing sequence identified. Inducing compliance through rewards or legal context."
            }

            if (scamType.isNotEmpty()) {
                val intent = Intent(this, SecurityOverlayBanner::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("SCAM_TYPE", scamType)
                    putExtra("REASON", scamReason)
                }
                startActivity(intent)
            }
        }
    }
}