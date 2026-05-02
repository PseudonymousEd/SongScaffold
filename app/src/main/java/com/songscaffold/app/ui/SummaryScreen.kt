package com.songscaffold.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.songscaffold.app.model.SongIdea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    songIdea: SongIdea,
    onStartOver: () -> Unit,
    onRandomIdea: () -> Unit,
    onHome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Song Idea") },
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
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    songIdea.topic?.let {
                        SummaryRow(label = "Topic", value = it.text)
                        SummaryRow(label = "Source", value = it.category.displayName)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                    songIdea.rhymeWord?.let {
                        SummaryRow(label = "Rhyme Word", value = it)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                    songIdea.pointOfView?.let {
                        SummaryRow(label = "Point of View", value = it)
                    }
                    songIdea.deliveryMode?.let {
                        SummaryRow(label = "Delivery Mode", value = it)
                    }
                    songIdea.phrasingStyle?.let {
                        SummaryRow(label = "Phrasing Style", value = it)
                    }
                    songIdea.emotionalIntensity?.let {
                        SummaryRow(label = "Emotional Intensity", value = it)
                    }

                    val hasChordSection = songIdea.chordProgression != null ||
                        songIdea.secondChordProgression != null || songIdea.songKey != null
                    if (hasChordSection) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        val hasTwo = songIdea.secondChordProgression != null
                        songIdea.chordProgression?.let {
                            SummaryRow(
                                label = if (hasTwo) "Chord Prog. 1" else "Chord Progression",
                                value = it.romanDisplay
                            )
                        }
                        songIdea.secondChordProgression?.let {
                            SummaryRow(label = "Chord Prog. 2", value = it.romanDisplay)
                        }
                        songIdea.songKey?.let {
                            SummaryRow(label = "Key", value = it)
                        }
                        if (songIdea.renderedChords.isNotEmpty()) {
                            SummaryRow(
                                label = if (hasTwo) "Chords 1" else "Chords",
                                value = songIdea.renderedChords.joinToString(" – ")
                            )
                        }
                        if (songIdea.secondRenderedChords.isNotEmpty()) {
                            SummaryRow(
                                label = "Chords 2",
                                value = songIdea.secondRenderedChords.joinToString(" – ")
                            )
                        }
                    }

                    val hasMelodySection = songIdea.startingNote != null || songIdea.secondNoteDirection != null
                    if (hasMelodySection) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        songIdea.startingNote?.let {
                            SummaryRow(label = "Starting Note", value = it)
                        }
                        songIdea.secondNoteDirection?.let {
                            SummaryRow(label = "Second Note", value = it)
                        }
                    }

                    songIdea.rhymeScheme?.let {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        SummaryRow(label = "Rhyme Scheme", value = it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onStartOver,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start Over")
                }
                OutlinedButton(
                    onClick = onRandomIdea,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Random Idea")
                }
                Button(
                    onClick = onHome,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Home")
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(160.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}
