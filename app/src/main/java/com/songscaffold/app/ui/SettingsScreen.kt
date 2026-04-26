package com.songscaffold.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.songscaffold.app.model.StepSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: StepSettings,
    onToggle: ((StepSettings) -> StepSettings) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Enabled Steps",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
            HorizontalDivider()

            SettingsToggleRow("Topic", settings.topicEnabled) {
                onToggle { s -> s.copy(topicEnabled = it) }
            }
            SettingsToggleRow("Point of View", settings.pointOfViewEnabled) {
                onToggle { s -> s.copy(pointOfViewEnabled = it) }
            }
            SettingsToggleRow("Delivery Mode", settings.deliveryModeEnabled) {
                onToggle { s -> s.copy(deliveryModeEnabled = it) }
            }
            SettingsToggleRow("Phrasing Style", settings.phrasingStyleEnabled) {
                onToggle { s -> s.copy(phrasingStyleEnabled = it) }
            }
            SettingsToggleRow("Emotional Intensity", settings.emotionalIntensityEnabled) {
                onToggle { s -> s.copy(emotionalIntensityEnabled = it) }
            }
            SettingsToggleRow("Chord Progression", settings.chordProgressionEnabled) {
                onToggle { s -> s.copy(chordProgressionEnabled = it) }
            }
            SettingsToggleRow(
                label = "Song Key",
                subtitle = if (!settings.chordProgressionEnabled) "Requires Chord Progression" else null,
                checked = settings.songKeyEnabled,
                enabled = settings.chordProgressionEnabled
            ) {
                onToggle { s -> s.copy(songKeyEnabled = it) }
            }
            SettingsToggleRow("Starting Note", settings.startingNoteEnabled) {
                onToggle { s -> s.copy(startingNoteEnabled = it) }
            }
            SettingsToggleRow("Second Note Direction", settings.secondNoteDirectionEnabled) {
                onToggle { s -> s.copy(secondNoteDirectionEnabled = it) }
            }
            SettingsToggleRow("Rhyme Scheme", settings.rhymeSchemeEnabled) {
                onToggle { s -> s.copy(rhymeSchemeEnabled = it) }
            }
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    checked: Boolean,
    subtitle: String? = null,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}
