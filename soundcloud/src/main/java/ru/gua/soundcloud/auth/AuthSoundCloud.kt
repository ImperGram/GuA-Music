/*
 This is the source code for GuA SoundCloud library for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package ru.gua.soundcloud.auth

import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import androidx.core.net.toUri
import okhttp3.*
import ru.gua.soundcloud.auth.models.SoundCloudTokenResponse
import ru.gua.soundcloud.help.generators.GeneratorStringAndPkce
import java.io.IOException

class AuthSoundCloud {

    fun auth(context: Context, clientID: String, redirectURI: String) {
        val state = GeneratorStringAndPkce.generateRandomString(32)
        val codeVerifier = GeneratorStringAndPkce.generateRandomString(64)
        val codeChallenge = GeneratorStringAndPkce.generateCodeChallenge(codeVerifier)

        val prefs = context.getSharedPreferences("gua_sc_auth_internal", Context.MODE_PRIVATE)
        prefs.edit {
            putString("last_verifier", codeVerifier)
            putString("last_state", state)
        }

        val authUri = "https://secure.soundcloud.com/authorize".toUri()
            .buildUpon()
            .appendQueryParameter("client_id", clientID)
            .appendQueryParameter("redirect_uri", redirectURI)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("state", state)
            .build()

        val intent = Intent(Intent.ACTION_VIEW, authUri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun exchangeCodeForToken(
        context: Context,
        clientID: String,
        clientSecret: String,
        redirectURI: String,
        code: String,
        onResult: (SoundCloudTokenResponse?, Throwable?) -> Unit
    ) {
        val prefs = context.getSharedPreferences("gua_sc_auth_internal", Context.MODE_PRIVATE)
        val codeVerifier = prefs.getString("last_verifier", null)

        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", clientID.trim())
            .add("client_secret", clientSecret.trim())
            .add("redirect_uri", redirectURI.trim())
            .add("code", code.trim())
            .add("code_verifier", codeVerifier ?: "")
            .build()

        val request = Request.Builder()
            .url("https://secure.soundcloud.com/oauth/token")
            .post(formBody)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    onResult(SoundCloudTokenResponse.fromJson(body), null)
                } else {
                    onResult(null, IOException("Status: ${response.code} Body: $body"))
                }
            }
        })
    }
}