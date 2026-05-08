package com.songscaffold.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.songscaffold.app.model.StepSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "step_settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val TOPIC = booleanPreferencesKey("topic")
        val RHYME_WORD = booleanPreferencesKey("rhyme_word")
        val POINT_OF_VIEW = booleanPreferencesKey("point_of_view")
        val DELIVERY_MODE = booleanPreferencesKey("delivery_mode")
        val PHRASING_STYLE = booleanPreferencesKey("phrasing_style")
        val EMOTIONAL_INTENSITY = booleanPreferencesKey("emotional_intensity")
        val CHORD_PROGRESSION = booleanPreferencesKey("chord_progression")
        val SECOND_CHORD_PROGRESSION = booleanPreferencesKey("second_chord_progression")
        val SONG_KEY = booleanPreferencesKey("song_key")
        val STARTING_NOTE = booleanPreferencesKey("starting_note")
        val SECOND_NOTE_DIRECTION = booleanPreferencesKey("second_note_direction")
        val RHYME_SCHEME = booleanPreferencesKey("rhyme_scheme")
        val DISABLE_TWO_CHORD_PROGRESSIONS = booleanPreferencesKey("disable_two_chord_progressions")
    }

    val stepSettings: Flow<StepSettings> = context.dataStore.data.map { prefs ->
        StepSettings(
            topicEnabled = prefs[Keys.TOPIC] ?: true,
            rhymeWordEnabled = prefs[Keys.RHYME_WORD] ?: true,
            pointOfViewEnabled = prefs[Keys.POINT_OF_VIEW] ?: true,
            deliveryModeEnabled = prefs[Keys.DELIVERY_MODE] ?: true,
            phrasingStyleEnabled = prefs[Keys.PHRASING_STYLE] ?: true,
            emotionalIntensityEnabled = prefs[Keys.EMOTIONAL_INTENSITY] ?: true,
            chordProgressionEnabled = prefs[Keys.CHORD_PROGRESSION] ?: true,
            secondChordProgressionEnabled = prefs[Keys.SECOND_CHORD_PROGRESSION] ?: false,
            songKeyEnabled = prefs[Keys.SONG_KEY] ?: true,
            startingNoteEnabled = prefs[Keys.STARTING_NOTE] ?: true,
            secondNoteDirectionEnabled = prefs[Keys.SECOND_NOTE_DIRECTION] ?: true,
            rhymeSchemeEnabled = prefs[Keys.RHYME_SCHEME] ?: true,
            disableTwoChordProgressions = prefs[Keys.DISABLE_TWO_CHORD_PROGRESSIONS] ?: false
        )
    }

    suspend fun updateSettings(settings: StepSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.TOPIC] = settings.topicEnabled
            prefs[Keys.RHYME_WORD] = settings.rhymeWordEnabled
            prefs[Keys.POINT_OF_VIEW] = settings.pointOfViewEnabled
            prefs[Keys.DELIVERY_MODE] = settings.deliveryModeEnabled
            prefs[Keys.PHRASING_STYLE] = settings.phrasingStyleEnabled
            prefs[Keys.EMOTIONAL_INTENSITY] = settings.emotionalIntensityEnabled
            prefs[Keys.CHORD_PROGRESSION] = settings.chordProgressionEnabled
            prefs[Keys.SECOND_CHORD_PROGRESSION] = settings.secondChordProgressionEnabled
            prefs[Keys.SONG_KEY] = settings.songKeyEnabled
            prefs[Keys.STARTING_NOTE] = settings.startingNoteEnabled
            prefs[Keys.SECOND_NOTE_DIRECTION] = settings.secondNoteDirectionEnabled
            prefs[Keys.RHYME_SCHEME] = settings.rhymeSchemeEnabled
            prefs[Keys.DISABLE_TWO_CHORD_PROGRESSIONS] = settings.disableTwoChordProgressions
        }
    }
}
