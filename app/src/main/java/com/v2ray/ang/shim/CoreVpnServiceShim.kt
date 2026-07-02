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
        // Forwarding to real v2rayNG CoreVpnService is intentionally disabled here
        // because this project doesn't compile the v2rayNG module sources as part of the same Gradle build.
        // Keep the app buildable on CI.
        // TODO: If/when v2rayNG is integrated as a module/library, restore real forwarding.
        return START_STICKY
    }
}

