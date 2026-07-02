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
        val result = com.v2ray.ang.handler.AngConfigManager.importBatchConfig(
            server = raw,
            subid = subId,
            append = false
        )

        // 3) AngConfigManager.importBatchConfig will internally call MmkvManager.setSelectServer()
        //    after matching a profile. However, if matching fails, SELECTED_SERVER might remain old.
        //    So we force select by decoding latest stored config for the subId server list.
        //
        //    Best-effort: select first available profile in that server list.
        val serverList = com.v2ray.ang.handler.MmkvManager.decodeServerList(subId)
        val guidToSelect = serverList.firstOrNull { it.isNotBlank() } ?: com.v2ray.ang.handler.MmkvManager.getSelectServer()
        if (!guidToSelect.isNullOrBlank()) {
            com.v2ray.ang.handler.MmkvManager.setSelectServer(guidToSelect)
        }

        // result is currently not used, but import succeeded => we are ready to start CoreVpnService.
        @Suppress("UNUSED_VARIABLE")
        val _ = result
    }
}


