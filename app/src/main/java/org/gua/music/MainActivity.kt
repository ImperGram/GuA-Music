/*
 This is the source code for GuAMusic for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package org.gua.music

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.gua.music.settings.SettingsConfig
import org.gua.music.ui.theme.GuAMusicTheme
import ru.gua.soundcloud.auth.AuthSoundCloud

class MainActivity : ComponentActivity() {

    private val scAuth = AuthSoundCloud()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleIntent(intent)

        setContent {
            GuAMusicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainTitle(scAuth)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data != null && data.toString().startsWith(Extra.redirectURI)) {
            val code = data.getQueryParameter("code")
            if (code != null) {
                scAuth.exchangeCodeForToken(
                    context = this,
                    clientID = Extra.clientID,
                    clientSecret = Extra.clientSecret,
                    redirectURI = Extra.redirectURI,
                    code = code
                ) { tokenResponse, error ->
                    if (tokenResponse != null) {
                        saveTokenData(
                            tokenResponse.accessToken,
                            tokenResponse.tokenType,
                            tokenResponse.refreshToken
                        )
                        runOnUiThread {
                            Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show()
                        }
                    } else if (error != null) {
                        runOnUiThread {
                            Toast.makeText(this, "Ошибка обмена: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun saveTokenData(token: String, type: String, refreshToken: String?) {
        getSharedPreferences("gua_music_prefs", Context.MODE_PRIVATE).edit()
            .putString("sc_access_token", token)
            .putString("sc_token_type", type)
            .putString("sc_refresh_token", refreshToken)
            .apply()
    }

    private fun refreshMyToken(onComplete: (Boolean) -> Unit) {
        val prefs = getSharedPreferences("gua_music_prefs", Context.MODE_PRIVATE)
        val rt = prefs.getString("sc_refresh_token", null)

        if (rt == null) {
            onComplete(false)
            return
        }

        scAuth.refreshToken(
            clientID = Extra.clientID,
            clientSecret = Extra.clientSecret,
            refreshToken = rt
        ) { response, error ->
            if (response != null) {
                saveTokenData(response.accessToken, response.tokenType, response.refreshToken)
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }
}

@Composable
fun MainTitle(scAuth: AuthSoundCloud) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val isSoundCloud = SettingsConfig.musicService == SettingsConfig.SERVICE_SOUNDCLOUD

        Button(onClick = {
            if (isSoundCloud) {
                scAuth.auth(context, Extra.clientID, Extra.redirectURI)
            }
        }) {
            Icon(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Авторизоваться в SoundCloud")
        }
    }
}