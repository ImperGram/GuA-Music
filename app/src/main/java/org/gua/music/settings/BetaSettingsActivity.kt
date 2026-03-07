/*
 This is the source code for GuAMusic for Android.
 License: GNU General Public License v.3.

 Copyright 2026 Gleb Obitotsky
*/

package org.gua.music.settings

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gua.music.R
import org.gua.music.ui.components.ExpandableSelector
import org.gua.music.ui.components.SettingsContainer
import org.gua.music.ui.theme.GuAMusicTheme

class BetaSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuAMusicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BetaSettingTitle()
                }
            }
        }
    }
}

@Composable
fun BetaSettingTitle() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var selectedService by remember { 
        mutableStateOf(
            if (SettingsConfig.musicService == SettingsConfig.SERVICE_SPOTIFY) "Spotify" else "SoundCloud"
        ) 
    }

    var apiKey by remember(selectedService) {
        mutableStateOf(
            if (selectedService == "Spotify") SettingsConfig.spotifyApiKey else SettingsConfig.soundCloudApiKey
        )
    }

    val currentIcon = if (selectedService == "Spotify") {
        R.drawable.spotify_icon
    } else {
        R.drawable.soundcloud_icon
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(37.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            FilledTonalIconButton(
                onClick = { (context as? Activity)?.finish() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
            }

            Text(
                text = "Beta Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsContainer {
            ExpandableSelector(
                title = "Музыкальный сервис",
                selectedOption = selectedService,
                options = listOf(
                    "Spotify" to R.drawable.spotify_icon,
                    "SoundCloud" to R.drawable.soundcloud_icon
                ),
                onOptionSelected = { name ->
                    selectedService = name
                    SettingsConfig.musicService = if (name == "Spotify") {
                        SettingsConfig.SERVICE_SPOTIFY
                    } else {
                        SettingsConfig.SERVICE_SOUNDCLOUD
                    }
                },
                mainIcon = currentIcon
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (selectedService == "Spotify") "Spotify Client ID" else "SoundCloud Client ID",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { 
                        apiKey = it
                        if (selectedService == "Spotify") {
                            SettingsConfig.spotifyApiKey = it
                        } else {
                            SettingsConfig.soundCloudApiKey = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.settings_music_client_id)) },
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }

    }
}