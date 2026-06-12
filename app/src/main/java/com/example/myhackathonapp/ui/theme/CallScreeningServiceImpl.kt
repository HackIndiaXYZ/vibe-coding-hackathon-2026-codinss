package com.example.myhackathonapp.ui.theme

import android.telecom.CallScreeningService
import android.telecom.CallScreeningService.CallResponse
import android.telecom.Call
import android.content.Intent
import com.example.myhackathonapp.SecurityOverlayBanner

class CallScreeningServiceImpl : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        // 1. Jaise hi koi call aayegi, ye service background me chalegi
        if (callDetails.callDirection == Call.Details.DIRECTION_INCOMING) {

            // System ko bolo ki call ko normally chalne de
            val response = CallResponse.Builder().build()
            respondToCall(callDetails, response)

            // 2. Alert banner popup screen par trigger karo
            val intent = Intent(applicationContext, SecurityOverlayBanner::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }
    }
}