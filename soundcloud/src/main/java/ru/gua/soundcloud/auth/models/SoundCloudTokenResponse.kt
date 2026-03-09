/*
 This is the source code for GuA SoundCloud library for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package ru.gua.soundcloud.auth.models

import org.json.JSONObject

data class SoundCloudTokenResponse(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String?,
    val scope: String?,
    val tokenType: String
) {
    companion object {
        fun fromJson(jsonString: String): SoundCloudTokenResponse {
            val json = JSONObject(jsonString)
            return SoundCloudTokenResponse(
                accessToken = json.getString("access_token"),
                expiresIn = json.getInt("expires_in"),
                refreshToken = json.optString("refresh_token", null),
                scope = json.optString("scope", null),
                tokenType = json.getString("token_type")
            )
        }
    }
}