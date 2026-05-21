package com.songscaffold.app.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import kotlinx.coroutines.delay
import kotlin.math.pow

class AudioEngine(context: Context) {

    private val assetManager = context.assets

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(8)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        .build()

    private val baseNoteFiles = listOf("C4.ogg", "Ds4.ogg", "Fs4.ogg", "A4.ogg")
    private val baseSoundIds = IntArray(4)

    @Volatile private var loadedCount = 0

    private val semitoneToBase = arrayOf(
        0 to 0,  // C
        0 to 1,  // Db
        0 to 2,  // D
        1 to 0,  // Eb
        1 to 1,  // E
        1 to 2,  // F
        2 to 0,  // Gb
        2 to 1,  // G
        2 to 2,  // Ab
        3 to 0,  // A
        3 to 1,  // Bb
        3 to 2   // B
    )

    private val noteToSemitone = mapOf(
        "C" to 0, "C#" to 1, "Db" to 1, "D" to 2, "D#" to 3, "Eb" to 3,
        "E" to 4, "F" to 5, "F#" to 6, "Gb" to 6, "G" to 7, "G#" to 8,
        "Ab" to 8, "A" to 9, "A#" to 10, "Bb" to 10, "B" to 11
    )

    init {
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) loadedCount++
        }
        baseNoteFiles.forEachIndexed { i, filename ->
            baseSoundIds[i] = soundPool.load(assetManager.openFd("notes/$filename"), 1)
        }
    }

    private suspend fun awaitLoaded() {
        while (loadedCount < baseNoteFiles.size) delay(50)
    }

    private data class ParsedChord(
        val rootSemitone: Int,
        val intervals: List<Int>,
        val bassSemitone: Int?
    )

    private fun parseChord(chordName: String): ParsedChord? {
        val bassNoteStr = if ('/' in chordName) chordName.substringAfter('/') else null
        val main = chordName.substringBefore('/')

        val root: String
        val quality: String
        if (main.length >= 2 && (main[1] == '#' || main[1] == 'b')) {
            root = main.substring(0, 2)
            quality = main.substring(2)
        } else {
            root = main.substring(0, 1)
            quality = main.substring(1)
        }

        val rootSemitone = noteToSemitone[root] ?: return null

        val intervals = when {
            quality.endsWith("maj7")                           -> listOf(0, 4, 7, 11)
            quality.endsWith("7b5")                            -> listOf(0, 3, 6, 10)
            quality.endsWith("dim")                            -> listOf(0, 3, 6)
            quality.endsWith("sus4")                           -> listOf(0, 5, 7)
            quality.endsWith("7") && !quality.contains("maj") -> listOf(0, 4, 7, 10)
            quality.startsWith("m")                            -> listOf(0, 3, 7)
            else                                               -> listOf(0, 4, 7)
        }

        val bassSemitone = bassNoteStr?.let { noteToSemitone[it] }

        return ParsedChord(rootSemitone, intervals, bassSemitone)
    }

    private fun playNote(semitone: Int, octaveRate: Float, volume: Float) {
        val (baseIdx, offset) = semitoneToBase[semitone % 12]
        val rate = (2.0.pow(offset / 12.0) * octaveRate).toFloat().coerceIn(0.5f, 2.0f)
        soundPool.play(baseSoundIds[baseIdx], volume, volume, 1, 0, rate)
    }

    suspend fun playChord(chordName: String, durationMs: Long) {
        awaitLoaded()
        val parsed = parseChord(chordName) ?: return

        parsed.bassSemitone?.let { bass ->
            playNote(bass, 0.5f, 0.7f)
        }

        for (interval in parsed.intervals) {
            playNote((parsed.rootSemitone + interval) % 12, 1.0f, 1.0f)
        }

        delay(durationMs)
    }

    fun release() {
        soundPool.release()
    }
}
