package com.songscaffold.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.songscaffold.app.model.ChordProgression
import com.songscaffold.app.model.SongStep
import com.songscaffold.app.model.TopicPrompt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepScreen(
    step: SongStep,
    stepIndex: Int,
    totalSteps: Int,
    topic: TopicPrompt?,
    selectedOption: String?,
    selectedProgression: ChordProgression?,
    onTopicRandom: () -> Unit,
    onOptionSelected: (String) -> Unit,
    onProgressionSelected: (ChordProgression) -> Unit,
    onRandom: () -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val meta = stepMeta(step)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = meta.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Step ${stepIndex + 1} of $totalSteps",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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
        ) {
            // Description
            Text(
                text = meta.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Step-specific content
            Box(modifier = Modifier.weight(1f)) {
                when (step) {
                    SongStep.TOPIC -> TopicStepContent(
                        topic = topic,
                        onRandom = onTopicRandom
                    )
                    SongStep.CHORD_PROGRESSION -> ChordProgressionStepContent(
                        selected = selectedProgression,
                        onSelect = onProgressionSelected
                    )
                    else -> OptionListStepContent(
                        options = optionsFor(step),
                        selected = selectedOption,
                        onSelect = onOptionSelected
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Bottom action row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onSkip,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Skip")
                }
                if (step != SongStep.TOPIC) {
                    OutlinedButton(
                        onClick = onRandom,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Random")
                    }
                }
                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Next")
                }
            }
        }
    }
}

@Composable
fun TopicStepContent(
    topic: TopicPrompt?,
    onRandom: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (topic != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = topic.text,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = topic.category.displayName,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Text(
                text = "Tap Random Topic to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = onRandom,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = if (topic != null) "New Random Topic" else "Random Topic",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun OptionListStepContent(
    options: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .selectableGroup()
    ) {
        items(options) { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = option == selected,
                        onClick = { onSelect(option) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option == selected,
                    onClick = null
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (option == selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
fun ChordProgressionStepContent(
    selected: ChordProgression?,
    onSelect: (ChordProgression) -> Unit
) {
    val progressions = com.songscaffold.app.data.PromptRepository.chordProgressions
    val grouped = progressions.groupBy { it.category }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .selectableGroup()
    ) {
        grouped.forEach { (category, items) ->
            item {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
                )
            }
            items(items) { progression ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = progression == selected,
                            onClick = { onSelect(progression) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = progression == selected,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = progression.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (progression == selected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = progression.romanDisplay,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

private data class StepMeta(val title: String, val description: String)

private fun stepMeta(step: SongStep): StepMeta = when (step) {
    SongStep.TOPIC -> StepMeta(
        "Topic",
        "The central idea or image for your song."
    )
    SongStep.POINT_OF_VIEW -> StepMeta(
        "Point of View",
        "Who is singing, or who is being addressed?"
    )
    SongStep.DELIVERY_MODE -> StepMeta(
        "Delivery Mode",
        "How is the idea expressed — outward or inward?"
    )
    SongStep.PHRASING_STYLE -> StepMeta(
        "Phrasing Style",
        "How do the words flow?"
    )
    SongStep.EMOTIONAL_INTENSITY -> StepMeta(
        "Emotional Intensity",
        "How strongly is the emotion projected?"
    )
    SongStep.CHORD_PROGRESSION -> StepMeta(
        "Chord Progression",
        "The harmonic structure for the song."
    )
    SongStep.SONG_KEY -> StepMeta(
        "Song Key",
        "Which key will the progression be played in?"
    )
    SongStep.STARTING_NOTE -> StepMeta(
        "Starting Note",
        "Where does the melody begin relative to the first chord?"
    )
    SongStep.SECOND_NOTE_DIRECTION -> StepMeta(
        "Second Note Direction",
        "Where does the melody go after the first note?"
    )
    SongStep.RHYME_SCHEME -> StepMeta(
        "Rhyme Scheme",
        "How do line endings relate to each other?"
    )
    SongStep.SUMMARY -> StepMeta("Summary", "")
}

private fun optionsFor(step: SongStep): List<String> {
    val r = com.songscaffold.app.data.PromptRepository
    return when (step) {
        SongStep.POINT_OF_VIEW -> r.pointOfViewOptions
        SongStep.DELIVERY_MODE -> r.deliveryModes
        SongStep.PHRASING_STYLE -> r.phrasingStyles
        SongStep.EMOTIONAL_INTENSITY -> r.emotionalIntensityOptions
        SongStep.SONG_KEY -> r.majorKeys
        SongStep.STARTING_NOTE -> r.startingNoteOptions
        SongStep.SECOND_NOTE_DIRECTION -> r.secondNoteDirectionOptions
        SongStep.RHYME_SCHEME -> r.rhymeSchemes
        else -> emptyList()
    }
}
