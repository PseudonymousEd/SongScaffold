package com.songscaffold.app.viewmodel

import androidx.lifecycle.ViewModel
import com.songscaffold.app.data.PromptRepository
import com.songscaffold.app.model.ChordProgression
import com.songscaffold.app.model.SongIdea
import com.songscaffold.app.model.SongStep
import com.songscaffold.app.model.StepSettings
import com.songscaffold.app.model.TopicPrompt
import com.songscaffold.app.music.ChordMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SongIdeaViewModel : ViewModel() {

    private val _songIdea = MutableStateFlow(SongIdea())
    val songIdea: StateFlow<SongIdea> = _songIdea.asStateFlow()

    private val _enabledSteps = MutableStateFlow<List<SongStep>>(emptyList())
    val enabledSteps: StateFlow<List<SongStep>> = _enabledSteps.asStateFlow()

    fun startSession(settings: StepSettings) {
        _songIdea.value = SongIdea()
        _enabledSteps.value = buildEnabledSteps(settings)
    }

    private fun buildEnabledSteps(s: StepSettings): List<SongStep> = buildList {
        if (s.topicEnabled) add(SongStep.TOPIC)
        if (s.pointOfViewEnabled) add(SongStep.POINT_OF_VIEW)
        if (s.deliveryModeEnabled) add(SongStep.DELIVERY_MODE)
        if (s.phrasingStyleEnabled) add(SongStep.PHRASING_STYLE)
        if (s.emotionalIntensityEnabled) add(SongStep.EMOTIONAL_INTENSITY)
        if (s.chordProgressionEnabled) add(SongStep.CHORD_PROGRESSION)
        // Song Key only appears when Chord Progression is also enabled
        if (s.chordProgressionEnabled && s.songKeyEnabled) add(SongStep.SONG_KEY)
        if (s.startingNoteEnabled) add(SongStep.STARTING_NOTE)
        if (s.secondNoteDirectionEnabled) add(SongStep.SECOND_NOTE_DIRECTION)
        if (s.rhymeSchemeEnabled) add(SongStep.RHYME_SCHEME)
    }

    fun setTopic(topic: TopicPrompt) = _songIdea.update { it.copy(topic = topic) }

    fun setPointOfView(value: String) = _songIdea.update { it.copy(pointOfView = value) }

    fun setDeliveryMode(value: String) = _songIdea.update { it.copy(deliveryMode = value) }

    fun setPhrasingStyle(value: String) = _songIdea.update { it.copy(phrasingStyle = value) }

    fun setEmotionalIntensity(value: String) = _songIdea.update { it.copy(emotionalIntensity = value) }

    fun setChordProgression(value: ChordProgression) {
        _songIdea.update { idea ->
            val rendered = idea.songKey?.let { ChordMapper.renderProgression(it, value) } ?: emptyList()
            idea.copy(chordProgression = value, renderedChords = rendered)
        }
    }

    fun setSongKey(value: String) {
        _songIdea.update { idea ->
            val rendered = idea.chordProgression?.let { ChordMapper.renderProgression(value, it) } ?: emptyList()
            idea.copy(songKey = value, renderedChords = rendered)
        }
    }

    fun setStartingNote(value: String) = _songIdea.update { it.copy(startingNote = value) }

    fun setSecondNoteDirection(value: String) = _songIdea.update { it.copy(secondNoteDirection = value) }

    fun setRhymeScheme(value: String) = _songIdea.update { it.copy(rhymeScheme = value) }

    fun randomTopic(): TopicPrompt = PromptRepository.topics.random()

    fun reset() {
        _songIdea.value = SongIdea()
    }
}
