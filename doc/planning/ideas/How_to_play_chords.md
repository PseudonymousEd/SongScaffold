# How to Play Chord Progressions (PlayChords Implementation Reference)

This document describes the exact technique PlayChords uses to play chord progressions so it can be ported to another Android app.

---

## Overview

The playback system has three parts:

1. **Assets** — four sampled piano notes (OGG files) bundled in the APK
2. **AudioEngine** — loads the samples via `SoundPool` and plays any chord by name
3. **ViewModel integration** — drives playback from a coroutine, tracks current chord state

The entry point is a single suspend function:

```kotlin
audioEngine.playChord(chordName: String, durationMs: Long)
```

`chordName` is a string like `"Am"`, `"Gmaj7"`, `"G/B"`. `durationMs` is how long the chord sounds before the function returns — the caller controls tempo.

---

## Step 1: Add the Piano Samples

Create the directory `app/src/main/assets/notes/` and add these four OGG files:

| File | Note |
|------|------|
| `C4.ogg` | C4 |
| `Ds4.ogg` | Eb4 (D-sharp 4) |
| `Fs4.ogg` | Gb4 (F-sharp 4) |
| `A4.ogg` | A4 |

These are the four base samples used to cover all 12 semitones via pitch shifting. Download them from the Tone.js Salamander mirror (Salamander Grand Piano, CC-BY 3.0):

```
https://tonejs.github.io/audio/salamander/C4.ogg
https://tonejs.github.io/audio/salamander/Ds4.ogg
https://tonejs.github.io/audio/salamander/Fs4.ogg
https://tonejs.github.io/audio/salamander/A4.ogg
```

Attribution: Salamander Grand Piano by Alexander Holm, CC-BY 3.0.

---

## Step 2: Add the Dependency

`AudioEngine` uses coroutines (`delay`). If your app doesn't already have the coroutines dependency:

```kotlin
// build.gradle.kts
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

`SoundPool` and `AudioAttributes` are part of the Android SDK — no extra dependency needed.

---

## Step 3: Copy AudioEngine

Copy `AudioEngine.kt` directly into your app (adjust the package name). The full class is below with inline explanation.

```kotlin
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import kotlinx.coroutines.delay
import kotlin.math.pow

class AudioEngine(context: Context) {

    private val assetManager = context.assets

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(8)  // enough for a 4-note chord + 1 bass note
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        .build()

    // Indices 0–3 correspond to C4, Eb4, Gb4, A4
    private val baseNoteFiles = listOf("C4.ogg", "Ds4.ogg", "Fs4.ogg", "A4.ogg")
    private val baseSoundIds = IntArray(4)

    @Volatile private var loadedCount = 0

    // For each semitone 0–11: (base sample index, semitones above that base)
    // The base samples divide the octave into groups of 3.
    // e.g. D (semitone 2) = C4 sample (index 0) pitched up 2 semitones
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

    // Suspends until all 4 samples are loaded. Resolves in well under a second.
    private suspend fun awaitLoaded() {
        while (loadedCount < baseNoteFiles.size) delay(50)
    }

    private data class ParsedChord(
        val rootSemitone: Int,
        val intervals: List<Int>,
        val bassSemitone: Int?  // non-null for slash chords like "G/B"
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

        // Semitone intervals above root for each chord quality
        val intervals = when {
            quality.endsWith("maj7")                           -> listOf(0, 4, 7, 11)
            quality.endsWith("7b5")                            -> listOf(0, 3, 6, 10)
            quality.endsWith("dim")                            -> listOf(0, 3, 6)
            quality.endsWith("sus4")                           -> listOf(0, 5, 7)
            quality.endsWith("7") && !quality.contains("maj") -> listOf(0, 4, 7, 10)
            quality.startsWith("m")                            -> listOf(0, 3, 7)
            else                                               -> listOf(0, 4, 7)  // major
        }

        val bassSemitone = bassNoteStr?.let { noteToSemitone[it] }

        return ParsedChord(rootSemitone, intervals, bassSemitone)
    }

    // Plays a single note. octaveRate: 1.0 = octave 4, 0.5 = octave 3 (one octave down).
    private fun playNote(semitone: Int, octaveRate: Float, volume: Float) {
        val (baseIdx, offset) = semitoneToBase[semitone % 12]
        // 2^(offset/12) raises pitch by `offset` semitones; multiply by octaveRate for octave
        val rate = (2.0.pow(offset / 12.0) * octaveRate).toFloat().coerceIn(0.5f, 2.0f)
        soundPool.play(baseSoundIds[baseIdx], volume, volume, 1, 0, rate)
    }

    // Call from a coroutine on Dispatchers.IO.
    // Plays the chord and suspends for durationMs, then returns.
    suspend fun playChord(chordName: String, durationMs: Long) {
        awaitLoaded()
        val parsed = parseChord(chordName) ?: return

        // Bass note (slash chord): play one octave below the chord tones
        parsed.bassSemitone?.let { bass ->
            playNote(bass, 0.5f, 0.7f)
        }

        // Chord tones: all voiced in octave 4
        for (interval in parsed.intervals) {
            playNote((parsed.rootSemitone + interval) % 12, 1.0f, 1.0f)
        }

        delay(durationMs)
    }

    // Call when the ViewModel is cleared or the screen exits
    fun release() {
        soundPool.release()
    }
}
```

---

## Step 4: Integrate into a ViewModel

`AudioEngine` needs a `Context` (for `AssetManager`). The cleanest way to supply this in an Android ViewModel is to extend `AndroidViewModel` instead of `ViewModel`.

```kotlin
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class YourViewModel(application: Application) : AndroidViewModel(application) {

    private val audioEngine = AudioEngine(application)
    private var playbackJob: Job? = null

    // chords: list of chord name strings, e.g. ["Am", "F", "C", "G"]
    // bpm: beats per minute, e.g. 80
    fun playProgression(chords: List<String>, bpm: Int) {
        playbackJob?.cancel()

        // Each chord lasts 4 beats (one measure)
        val chordDurationMs = (60_000L / bpm) * 4L

        playbackJob = viewModelScope.launch(Dispatchers.IO) {
            for (chord in chords) {
                ensureActive()  // stop if the job was cancelled
                audioEngine.playChord(chord, chordDurationMs)
            }
        }
    }

    fun stopPlayback() {
        playbackJob?.cancel()
    }

    override fun onCleared() {
        playbackJob?.cancel()
        audioEngine.release()
        super.onCleared()
    }
}
```

If your existing ViewModel already extends `ViewModel`, change it to `AndroidViewModel(application: Application)`. The default `viewModel()` factory in Compose (and `ViewModelProvider` elsewhere) handles `AndroidViewModel` automatically — no factory code needed.

---

## Chord Name Format

`playChord` accepts chord names as strings. The parser handles:

| Format | Examples |
|--------|---------|
| Major triad | `"C"`, `"F#"`, `"Bb"` |
| Minor triad | `"Am"`, `"Dbm"`, `"F#m"` |
| Major 7th | `"Cmaj7"`, `"Fmaj7"` |
| Dominant 7th | `"G7"`, `"Bb7"` |
| Half-diminished | `"Dm7b5"` |
| Diminished | `"Cdim"`, `"G#dim"` |
| Suspended 4th | `"Gsus4"` |
| Slash chord | `"G/B"`, `"C/E"` — bass note after `/` |

Unrecognised chord names return silently (no crash).

---

## Tempo Formula

```kotlin
val chordDurationMs = (60_000L / bpm) * beatsPerChord
```

PlayChords uses `beatsPerChord = 4` (one measure per chord). At 80 BPM that gives 3000 ms per chord. Adjust `beatsPerChord` to taste.

---

## Lifecycle Notes

- Create `AudioEngine` once when the ViewModel is created — not per chord or per screen visit. It loads the 4 OGG samples on construction; subsequent `playChord` calls are instant.
- Always call `audioEngine.release()` in `onCleared()`. This releases the `SoundPool` and its loaded samples.
- Run `playChord` on `Dispatchers.IO`. It suspends for `durationMs` using `delay` — keeping it off the main thread avoids any jank.
- Cancel the coroutine job before starting a new sequence, and check `ensureActive()` between chords so cancellation is immediate.
