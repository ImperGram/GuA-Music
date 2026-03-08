/*
 This is the source code for GuAMusic for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package org.gua.music

import android.content.Intent
import android.os.Bundle
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
import org.gua.music.settings.BetaSettingsActivity
import org.gua.music.settings.SettingsActivity
import org.gua.music.settings.SettingsConfig
import org.gua.music.ui.theme.GuAMusicTheme
import ru.gua.soundcloud.auth.AuthSoundCloud
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuAMusicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainTitle()
                }
            }
        }
    }
}

@Composable
fun MainTitle() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val scAuth = androidx.compose.runtime.remember { AuthSoundCloud() }

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

                    val intent = Intent(context, SettingsActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "Настройки")
                }

                Spacer(modifier = Modifier.width(8.dp))

                if ( SettingsConfig.useUserSoundCloudApi ) {
                    FilledTonalIconButton(onClick = {
                        scAuth.auth(
                            context = context,
                            clientID = Extra.clientID,
                            redirectURI = Extra.redirectURI
                        )
                    }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Аккаунт")
                    }
                }
            }
        }
    }
}