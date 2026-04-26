package com.songscaffold.app.model

data class SongIdea(
    val topic: TopicPrompt? = null,
    val pointOfView: String? = null,
    val deliveryMode: String? = null,
    val phrasingStyle: String? = null,
    val emotionalIntensity: String? = null,
    val chordProgression: ChordProgression? = null,
    val songKey: String? = null,
    val renderedChords: List<String> = emptyList(),
    val startingNote: String? = null,
    val secondNoteDirection: String? = null,
    val rhymeScheme: String? = null
)
