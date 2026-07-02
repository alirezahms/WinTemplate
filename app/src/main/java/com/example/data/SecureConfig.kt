package com.example.data

object SecureConfig {
    // Shifting key
    private const val SHIFT = 3

    // Obfuscated string of "https://alirez.n-cpanel.xyz/Sub/Plans/Full.txt"
    private const val OBFUSCATED_FULL = "kwwsv=22doluh}1q0fsdqho1{|}2Vxe2Sodqv2Iuxoo1w{w"

    // Obfuscated string of "https://alirez.n-cpanel.xyz/Sub/Plans/Free.txt"
    private const val OBFUSCATED_FREE = "kwwsv=22doluh}1q0fsdqho1{|}2Vxe2Sodqv2Iuhh1w{w"

    private fun decrypt(encrypted: String): String {
        return encrypted.map { (it.code - SHIFT).toChar() }.joinToString("")
    }

    val FULL_URL: String by lazy { decrypt(OBFUSCATED_FULL) }
    val FREE_URL: String by lazy { decrypt(OBFUSCATED_FREE) }
}
