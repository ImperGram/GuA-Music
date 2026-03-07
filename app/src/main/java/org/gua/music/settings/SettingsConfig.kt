/*
 This is the source code for GuAMusic for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package org.gua.music.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.gua.music.ApplicationLoader

object SettingsConfig : CoroutineScope by CoroutineScope(
    context = SupervisorJob() + Dispatchers.Default
) {

    private val sharedPreferences: SharedPreferences by lazy {
        ApplicationLoader.applicationContext.getSharedPreferences("main_config_gua", Context.MODE_PRIVATE)
    }

    private const val KEY_MUSIC_SERVICE = "sc_sf_mus_immp"
    private const val KEY_SPOTIFY_API_KEY = "sc_sf_mus_sp_api"
    private const val KEY_SOUNDCLOUD_API_KEY = "sc_sf_mus_sc_api"
    private const val KEY_SPOTIFY_API_KEY_SECRET = "sc_sf_mus_sp_api_secret"
    private const val KEY_SOUNDCLOUD_API_KEY_SECRET = "sc_sf_mus_sc_api_secret"
    private const val KEY_USE_USER_SOUNDCLOUD_API = "sc_sf_mus_use_user_sc_api"

    const val SERVICE_SOUNDCLOUD = 0
    const val SERVICE_SPOTIFY = 1

    var musicService: Int
        get() = sharedPreferences.getInt(KEY_MUSIC_SERVICE, SERVICE_SOUNDCLOUD)
        set(value) = sharedPreferences.edit { putInt(KEY_MUSIC_SERVICE, value) }

    var useUserSoundCloudApi: Boolean
        get() = sharedPreferences.getBoolean(KEY_USE_USER_SOUNDCLOUD_API, false)
        set(value) = sharedPreferences.edit { putBoolean(KEY_USE_USER_SOUNDCLOUD_API, value) }

    var spotifyApiKey: String
        get() = sharedPreferences.getString(KEY_SPOTIFY_API_KEY, "") ?: ""
        set(value) = sharedPreferences.edit { putString(KEY_SPOTIFY_API_KEY, value) }

    var soundCloudApiKey: String
        get() = sharedPreferences.getString(KEY_SOUNDCLOUD_API_KEY, "") ?: ""
        set(value) = sharedPreferences.edit { putString(KEY_SOUNDCLOUD_API_KEY, value) }

    var spotifyApiKeySecret: String
        get() = sharedPreferences.getString(KEY_SPOTIFY_API_KEY_SECRET, "") ?: ""
        set(value) = sharedPreferences.edit { putString(KEY_SPOTIFY_API_KEY_SECRET, value) }

    var soundCloudApiKeySecret: String
        get() = sharedPreferences.getString(KEY_SOUNDCLOUD_API_KEY_SECRET, "") ?: ""
        set(value) = sharedPreferences.edit { putString(KEY_SOUNDCLOUD_API_KEY_SECRET, value) }

    fun init() {
    }
}