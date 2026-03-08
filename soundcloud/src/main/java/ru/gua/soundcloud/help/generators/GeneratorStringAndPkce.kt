/*
 This is the source code for GuA SoundCloud library for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package ru.gua.soundcloud.help.generators

import java.security.SecureRandom
import android.util.Base64
import java.security.MessageDigest

object GeneratorStringAndPkce {
    private val secureRandom = SecureRandom()

    fun generateRandomString(length: Int = 64): String {
        val bytes = ByteArray(length)
        secureRandom.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            .take(length)
    }

    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(Charsets.US_ASCII)
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)

        return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }
}