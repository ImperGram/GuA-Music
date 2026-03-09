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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.gua.music.settings.SettingsActivity
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
                        saveTokenData(tokenResponse.accessToken, tokenResponse.tokenType)
                    } else if (error != null) {
                        runOnUiThread {
                            Toast.makeText(this, "Ошибка входа: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun saveTokenData(token: String, type: String) {
        getSharedPreferences("gua_music_prefs", Context.MODE_PRIVATE).edit()
            .putString("sc_access_token", token)
            .putString("sc_token_type", type)
            .apply()
    }
}

@Composable
fun MainTitle(scAuth: AuthSoundCloud) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(37.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.main_activity_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Row {
                FilledTonalIconButton(onClick = { /* TODO: Уведомления */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Уведомления")
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalIconButton(onClick = {
                    context.startActivity(Intent(context, SettingsActivity::class.java))
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "Настройки")
                }

                Spacer(modifier = Modifier.width(8.dp))

                val isSoundCloud = SettingsConfig.musicService == SettingsConfig.SERVICE_SOUNDCLOUD
                FilledTonalIconButton(onClick = {
                    if (isSoundCloud) {
                        scAuth.auth(context, Extra.clientID, Extra.redirectURI)
                    }
                }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Аккаунт")
                }
            }
        }
    }
}