/*
 This is the source code for GuA SoundCloud library for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package ru.gua.soundcloud.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.gua.soundcloud.help.generators.GeneratorStringAndPkce

class AuthSoundCloud {

    fun auth(context: Context, clientID: String, redirectURI: String) {
        val state = GeneratorStringAndPkce.generateRandomString(32)
        val codeVerifier = GeneratorStringAndPkce.generateRandomString(64)
        val codeChallenge = GeneratorStringAndPkce.generateCodeChallenge(codeVerifier)

        val prefs = context.getSharedPreferences("gua_sc_auth_internal", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("last_verifier", codeVerifier)
            .putString("last_state", state)
            .apply()

        val authUri = Uri.parse("https://secure.soundcloud.com/authorize")
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
}