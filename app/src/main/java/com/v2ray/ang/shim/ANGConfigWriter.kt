package com.v2ray.ang.shim

import android.content.Context
import com.example.data.V2RayConfig

/**
 * Adapter placeholder.
 *
 * v2rayNG stores profiles into MMKV (MmkvManager).
 * A real implementation must map V2RayConfig/rawConfig to v2rayNG's internal entities.
 *
 * This file is intentionally minimal so the project compiles; it must be completed
 * after we wire MMKV dependencies.
 */
object ANGConfigWriter {
    /**
     * Writes the currently selected config into v2rayNG's MMKV storage.
     *
     * The real v2rayNG core uses:
     *  - MmkvManager.setSelectServer()
     *  - MmkvManager.decodeServerConfig(guid)
     */
    fun writeSelectedProfile(context: Context, config: V2RayConfig) {
        // v2rayNG already ships parsing + import logic that converts:
        //   vmess:// / vless:// / trojan:// / ss://(as supported) ...
        // into ProfileItem and then persists to MmkvManager.
        //
        // We can reuse AngConfigManager.importBatchConfig.

        // 1) Ensure we have a raw scheme line v2rayNG can parse.
        //    Your app's V2RayConfig.rawConfig is already a single link.
        val raw = config.rawConfig.trim()
        if (raw.isEmpty()) return

        // 2) Import into v2rayNG storage.
        //    Use a stable subscription id so serverList grouping is consistent.
        //    We don't have subscriptionId in your app model, so we use the login mode as a grouping key.
        //    If you want strict matching, extend V2RayConfig to include subId.
        val subId = "APP_SUB_${config.isPremium}_${config.type}".replace(Regex("[^A-Za-z0-9_-]"), "_")

        // Import as batch with append=false to keep only the currently selected set.
        // append=true would accumulate; for simplicity we reset to avoid stale configs.
        // AngConfigManager.importBatchConfig is located in com.v2ray.ang.handler.AngConfigManager
        val result = com.v2ray.ang.handler.AngConfigManager.importBatchConfig(
            server = raw,
            subid = subId,
            append = false
        )

        // Best-effort: after import, ensure selected server exists.
        val serverList = com.v2ray.ang.handler.MmkvManager.decodeServerList(subId)
        val guidToSelect = serverList.firstOrNull { it.isNotBlank() } ?: com.v2ray.ang.handler.MmkvManager.getSelectServer()
        if (!guidToSelect.isNullOrBlank()) {
            com.v2ray.ang.handler.MmkvManager.setSelectServer(guidToSelect)
        }

        @Suppress("UNUSED_VARIABLE")
        val _ = result
    }
}


