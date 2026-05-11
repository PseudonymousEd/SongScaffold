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
        if (s.rhymeWordEnabled) add(SongStep.RHYME_WORD)
        if (s.pointOfViewEnabled) add(SongStep.POINT_OF_VIEW)
        if (s.deliveryModeEnabled) add(SongStep.DELIVERY_MODE)
        if (s.phrasingStyleEnabled) add(SongStep.PHRASING_STYLE)
        if (s.emotionalIntensityEnabled) add(SongStep.EMOTIONAL_INTENSITY)
        if (s.chordProgressionEnabled) add(SongStep.CHORD_PROGRESSION)
        if (s.chordProgressionEnabled && s.secondChordProgressionEnabled) add(SongStep.SECOND_CHORD_PROGRESSION)
        // Song Key only appears when Chord Progression is also enabled
        if (s.chordProgressionEnabled && s.songKeyEnabled) add(SongStep.SONG_KEY)
        if (s.startingNoteEnabled) add(SongStep.STARTING_NOTE)
        if (s.secondNoteDirectionEnabled) add(SongStep.SECOND_NOTE_DIRECTION)
        if (s.rhymeSchemeEnabled) add(SongStep.RHYME_SCHEME)
    }

    fun setTopic(topic: TopicPrompt) = _songIdea.update { it.copy(topic = topic) }

    fun setRhymeWord(value: String) = _songIdea.update { it.copy(rhymeWord = value) }

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

    fun setSecondChordProgression(value: ChordProgression) {
        _songIdea.update { idea ->
            val rendered = idea.songKey?.let { ChordMapper.renderProgression(it, value) } ?: emptyList()
            idea.copy(secondChordProgression = value, secondRenderedChords = rendered)
        }
    }

    fun setSongKey(value: String) {
        _songIdea.update { idea ->
            val rendered = idea.chordProgression?.let { ChordMapper.renderProgression(value, it) } ?: emptyList()
            val secondRendered = idea.secondChordProgression?.let { ChordMapper.renderProgression(value, it) } ?: emptyList()
            idea.copy(songKey = value, renderedChords = rendered, secondRenderedChords = secondRendered)
        }
    }

    fun setStartingNote(value: String) = _songIdea.update { it.copy(startingNote = value) }

    fun setSecondNoteDirection(value: String) = _songIdea.update { it.copy(secondNoteDirection = value) }

    fun setRhymeScheme(value: String) = _songIdea.update { it.copy(rhymeScheme = value) }

    fun randomTopic(): TopicPrompt = PromptRepository.topics.random()

    fun randomRhymeWord(): String = PromptRepository.rhymeWords.random()

    fun randomizeAll(settings: StepSettings) {
        val steps = buildEnabledSteps(settings)
        _enabledSteps.value = steps
        val firstProgressions = PromptRepository.availableFirstChordProgressions(settings.disableTwoChordProgressions)
        val allProgressions = PromptRepository.availableChordProgressions(settings.disableTwoChordProgressions)
        val key   = if (SongStep.SONG_KEY in steps) PromptRepository.majorKeys.random() else null
        val prog  = if (SongStep.CHORD_PROGRESSION in steps)
            firstProgressions.ifEmpty { allProgressions }.randomOrNull()
        else null
        val prog2 = if (SongStep.SECOND_CHORD_PROGRESSION in steps)
            PromptRepository.secondChordProgressionCandidates(
                disableTwoChordProgressions = settings.disableTwoChordProgressions,
                firstProgression = prog
            ).randomOrNull()
        else null
        _songIdea.value = SongIdea(
            topic               = if (SongStep.TOPIC in steps) PromptRepository.topics.random() else null,
            rhymeWord           = if (SongStep.RHYME_WORD in steps) PromptRepository.rhymeWords.random() else null,
            pointOfView         = if (SongStep.POINT_OF_VIEW in steps) PromptRepository.pointOfViewOptions.random() else null,
            deliveryMode        = if (SongStep.DELIVERY_MODE in steps) PromptRepository.deliveryModes.random() else null,
            phrasingStyle       = if (SongStep.PHRASING_STYLE in steps) PromptRepository.phrasingStyles.random() else null,
            emotionalIntensity  = if (SongStep.EMOTIONAL_INTENSITY in steps) PromptRepository.emotionalIntensityOptions.random() else null,
            chordProgression    = prog,
            secondChordProgression = prog2,
            songKey             = key,
            renderedChords      = if (key != null && prog != null) ChordMapper.renderProgression(key, prog) else emptyList(),
            secondRenderedChords = if (key != null && prog2 != null) ChordMapper.renderProgression(key, prog2) else emptyList(),
            startingNote        = if (SongStep.STARTING_NOTE in steps) PromptRepository.startingNoteOptions.random() else null,
            secondNoteDirection = if (SongStep.SECOND_NOTE_DIRECTION in steps) PromptRepository.secondNoteDirectionOptions.random() else null,
            rhymeScheme         = if (SongStep.RHYME_SCHEME in steps) PromptRepository.rhymeSchemes.random() else null
        )
    }

    fun reset() {
        _songIdea.value = SongIdea()
    }
}
