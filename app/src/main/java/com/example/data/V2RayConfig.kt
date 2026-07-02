package com.example.data

data class V2RayConfig(
    val id: String,
    val name: String,
    val type: String,
    val rawConfig: String,
    val address: String,
    val port: Int,
    var ping: Long = -1L,
    val isPremium: Boolean
)
