package com.v2ray.ang.shim

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.v2ray.ang.service.CoreVpnService

/**
 * Shim to avoid package/namespace issues.
 *
 * IMPORTANT: In order for this shim to work, the real v2rayNG sources must be compiled into this app.
 * Otherwise, this file is inert.
 */
class CoreVpnServiceShim : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Forward start to the real CoreVpnService
        val realIntent = Intent(this, CoreVpnService::class.java).apply {
            putExtras(intent)
        }
        ContextCompat.startForegroundService(this, realIntent)
        return START_STICKY
    }
}

