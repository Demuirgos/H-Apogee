package utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object SHA256 {
    fun String.encrypt(): String =
        MessageDigest.getInstance("SHA-256").digest(this.toByteArray(StandardCharsets.UTF_8))
            .joinToString("") { "%02x".format(it) }


    fun checkSum(str: String, digest: String): Boolean =
        str.encrypt() == digest
}