package com.v2ray.ang.shim

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat


/**
 * Shim to avoid package/namespace issues.
 *
 * IMPORTANT: In order for this shim to work, the real v2rayNG sources must be compiled into this app.
 * Otherwise, this file is inert.
 */
class CoreVpnServiceShim : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Forwarding disabled for now: v2rayNG core is not wired into this Gradle build.
        return START_STICKY
    }
}

