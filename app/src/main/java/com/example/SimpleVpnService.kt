package com.example

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import com.example.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SimpleVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    companion object {
        const val ACTION_CONNECT = "com.example.vpn.CONNECT"
        const val ACTION_DISCONNECT = "com.example.vpn.DISCONNECT"
        private const val TAG = "SimpleVpnService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                ACTION_CONNECT -> {
                    startVpn()
                }
                ACTION_DISCONNECT -> {
                    stopVpn()
                    stopSelf()
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun startVpn() {
        Log.d(TAG, "Starting VPN...")
        stopVpn() // Ensure any existing interface is closed

        serviceScope.launch {
            try {
                val db = AppDatabase.getDatabase(this@SimpleVpnService)
                val selectedApps = db.vpnDao().getSelectedPackageNames()

                val builder = Builder()
                    .setSession("Win2ray Shield")
                    .setMtu(1500)
                    .addAddress("172.19.0.1", 30) // Standard private range used by v2rayNG
                    .addRoute("0.0.0.0", 0)       // Route all IPv4 traffic (v2rayNG style)
                    .addRoute("::", 0)            // Route all IPv6 traffic (v2rayNG style)
                    .addDnsServer("1.1.1.1")      // Cloudflare anti-censorship DNS
                    .addDnsServer("8.8.8.8")      // Google DNS

                if (selectedApps.isNotEmpty()) {
                    Log.d(TAG, "Applying Split Tunneling for ${selectedApps.size} apps")
                    for (appPackage in selectedApps) {
                        try {
                            builder.addAllowedApplication(appPackage)
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to add split-tunneling app: $appPackage", e)
                        }
                    }
                } else {
                    Log.d(TAG, "No specific split tunneling apps selected. Routing all traffic.")
                }

                vpnInterface = builder.establish()
                Log.d(TAG, "VPN Interface established successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error starting VPN", e)
                stopVpn()
            }
        }
    }

    private fun stopVpn() {
        try {
            vpnInterface?.close()
            vpnInterface = null
            Log.d(TAG, "VPN Interface closed")
        } catch (e: Exception) {
            Log.e(TAG, "Error closing VPN Interface", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        stopVpn()
    }
}
