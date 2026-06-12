package com.example.myhackathonapp

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.accessibility.AccessibilityEvent
import java.util.Locale

class VoiceScannerService : AccessibilityService() {

    private var speechRecognizer: SpeechRecognizer? = null
    // 🎯 Yahan typo fix kar diya hai (SPEECH)
    private val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private var isServiceActive = false
    private val handler = Handler(Looper.getMainLooper())

    private val runnableCode = object : Runnable {
        override fun run() {
            if (isServiceActive) {
                restartTriggerEngine()
                handler.postDelayed(this, 4000)
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        isServiceActive = true

        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

        handler.post(runnableCode)
    }

    private fun restartTriggerEngine() {
        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}

            override fun onResults(results: Bundle?) {
                evaluateStream(results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION))
            }

            override fun onPartialResults(partialResults: Bundle?) {
                evaluateStream(partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION))
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        try {
            speechRecognizer?.startListening(speechIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun evaluateStream(matches: ArrayList<String>?) {
        if (!matches.isNullOrEmpty()) {
            val spokenText = matches[0].lowercase()
            var scamType = ""
            var scamReason = ""

            if (spokenText.contains("otp") || spokenText.contains("card") || spokenText.contains("money")) {
                scamType = "FINANCIAL FRAUD DETECTED"
                scamReason = "Real-time stream match: Caller is actively demanding secure banking credentials or OTP transfer."
            } else if (spokenText.contains("block") || spokenText.contains("kyc") || spokenText.contains("password")) {
                scamType = "ACCOUNT SUSPENSION THREAT"
                scamReason = "Threat indicator active: Caller trying to induce panic regarding immediate account or KYC freeze."
            } else if (spokenText.contains("police") || spokenText.contains("lottery")) {
                scamType = "URGENCY & IMPERSONATION SCAM"
                scamReason = "Authority impersonation signature matched. Utilizing false legal context or lottery triggers."
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

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() { isServiceActive = false }

    override fun onDestroy() {
        isServiceActive = false
        handler.removeCallbacks(runnableCode)
        speechRecognizer?.destroy()
        super.onDestroy()
    }
}