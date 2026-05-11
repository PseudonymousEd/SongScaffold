package com.songscaffold.app.model

data class StepSettings(
    val topicEnabled: Boolean = true,
    val rhymeWordEnabled: Boolean = true,
    val pointOfViewEnabled: Boolean = true,
    val deliveryModeEnabled: Boolean = true,
    val phrasingStyleEnabled: Boolean = true,
    val emotionalIntensityEnabled: Boolean = true,
    val chordProgressionEnabled: Boolean = true,
    val secondChordProgressionEnabled: Boolean = false,
    val songKeyEnabled: Boolean = true,
    val startingNoteEnabled: Boolean = true,
    val secondNoteDirectionEnabled: Boolean = true,
    val rhymeSchemeEnabled: Boolean = true,
    val disableTwoChordProgressions: Boolean = false,
    val enableChordProgression3: Boolean = false
)
