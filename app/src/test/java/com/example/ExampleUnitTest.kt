package com.example

import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun generateObfuscatedUrls() {
    val key = 7
    val fullUrl = "https://alirez.n-cpanel.xyz/Sub/Plans/Full.txt"
    val freeUrl = "https://alirez.n-cpanel.xyz/Sub/Plans/Free.txt"

    val encryptedFull = fullUrl.map { (it.code + key).toChar() }.joinToString("")
    val encryptedFree = freeUrl.map { (it.code + key).toChar() }.joinToString("")

    println("ENCRYPTED FULL: $encryptedFull")
    println("ENCRYPTED FREE: $encryptedFree")

    // Verify decryption
    val decryptedFull = encryptedFull.map { (it.code - key).toChar() }.joinToString("")
    val decryptedFree = encryptedFree.map { (it.code - key).toChar() }.joinToString("")

    assertEquals(fullUrl, decryptedFull)
    assertEquals(freeUrl, decryptedFree)
  }
}
