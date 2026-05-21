# DevCycle 008: Chord Progression Audio Playback

**Status:** Work Complete
**Start Date:** 2026-05-20
**Target Completion:** TBD
**Focus:** Play chord progressions as audio on the Summary screen, with play/pause and progression-switching controls.

---

## Goal

Add audio playback of chord progressions to the Final Song Idea (Summary) screen. The user will be able to press Play to hear the chord progression loop continuously, switch between the 1st, 2nd, and 3rd progressions mid-loop, and pause at any time. Playback uses sampled piano notes rendered via Android's `SoundPool`, pitched via a small set of base OGG samples. A new BPM setting controls tempo.

## Desired Outcome

- The Summary screen shows Play/Pause and progression-selector buttons when chord progressions are enabled in settings and at least one chord has been rendered.
- Pressing Play starts the current chord progression looping; pressing Pause stops it; pressing Play again resumes.
- The 1st/2nd/3rd selector buttons queue the indicated progression to start after the current loop completes.
- BPM is configurable in Settings (default 60) and persisted across sessions.
- Navigating away from the Summary screen stops playback automatically.
- Each chord lasts 4 beats (one measure) at the configured BPM.

---

## Tasks

### Phase 1: Add Piano Samples

**Status:** Work Complete

- [x] Create directory `app/src/main/assets/notes/`.
- [x] Download `C4.ogg`, `Ds4.ogg`, `Fs4.ogg`, `A4.ogg` from the Tone.js Salamander mirror.
- [x] Verify all four files are present and non-zero before continuing.

**Technical Notes:**
The four OGG files are the only audio assets needed. They are loaded once at `AudioEngine` construction and cover all 12 semitones via pitch-shifting. URLs from the reference doc:
```
https://tonejs.github.io/audio/salamander/C4.ogg
https://tonejs.github.io/audio/salamander/Ds4.ogg
https://tonejs.github.io/audio/salamander/Fs4.ogg
https://tonejs.github.io/audio/salamander/A4.ogg
```
Attribution: Salamander Grand Piano by Alexander Holm, CC-BY 3.0.

### Phase 2: Add AudioEngine

**Status:** Work Complete

- [x] Create `app/src/main/java/com/songscaffold/app/audio/AudioEngine.kt`.
- [x] Implement `AudioEngine` using `SoundPool` exactly as described in `How_to_play_chords.md`.
- [x] Verify the package declaration matches (`com.songscaffold.app.audio`).
- [x] Build to confirm no compile errors.

**Technical Notes:**
`AudioEngine` is a standalone class that takes a `Context` (for `AssetManager`) and exposes:
- `suspend fun playChord(chordName: String, durationMs: Long)` — plays a chord and suspends for the duration.
- `fun release()` — frees the `SoundPool`.

`SoundPool` and `AudioAttributes` are part of the Android SDK; no new dependencies needed. `kotlinx-coroutines-android` is already available transitively via `lifecycle-runtime-ktx`.

### Phase 3: ViewModel — Playback State and Logic

**Status:** Work Complete

- [x] Change `SongIdeaViewModel` from `ViewModel` to `AndroidViewModel(application: Application)`.
- [x] Add `AudioEngine` as a private field, created in the ViewModel constructor.
- [x] Add playback state: `isPlaying`, `activeProgressionIndex` (1/2/3), `queuedProgressionIndex`.
- [x] Expose playback state as `StateFlow` fields consumed by `SummaryScreen`.
- [x] Add `fun play(bpm: Int)` — starts a new looping coroutine on `Dispatchers.IO`.
- [x] Add `fun pause()` — cancels the playback job without releasing `AudioEngine`.
- [x] Add `fun queueProgression(index: Int)` — sets `queuedProgressionIndex`; the loop checks it after each full pass and switches if set.
- [x] Call `audioEngine.release()` in `onCleared()`.
- [x] Build to confirm no compile errors.

**Technical Notes:**
`AndroidViewModel` is the standard way to give a ViewModel access to `AssetManager` without leaking an Activity context. The Compose `viewModel()` factory handles `AndroidViewModel` automatically — no custom factory needed.

Loop structure:
```kotlin
while (true) {
    for (chord in currentChords) {
        ensureActive()
        audioEngine.playChord(chord, chordDurationMs)
    }
    // After each full pass, apply any queued switch
    queuedProgressionIndex?.let { next ->
        activeProgressionIndex = next
        currentChords = chordsForIndex(next)
        queuedProgressionIndex = null
    }
}
```

The 3rd chord progression chord list (transposed) will be computed inside the ViewModel using `ChordMapper.renderProgressionOneWholeStepHigher`, mirroring how `SummaryScreen` computes it today. This avoids storing a third list in `SongIdea`.

### Phase 4: BPM Setting

**Status:** Work Complete

- [x] Add `bpm: Int = 60` to `StepSettings`.
- [x] Add `intPreferencesKey("bpm")` to `SettingsRepository.Keys`.
- [x] Read/write BPM in `SettingsRepository.stepSettings` flow and `updateSettings`.
- [x] Add a BPM numeric input field to `SettingsScreen` (visible when chord progression is enabled).
- [x] Reused existing `onToggle` path via `s.copy(bpm = it)` — no separate method needed.
- [x] Build to confirm no compile errors.

**Technical Notes:**
BPM lives in `StepSettings` rather than a separate model. It is only relevant when chord progressions are enabled, so the settings UI should conditionally show it. Input should accept integers only, with a reasonable range (e.g., 20–300). The chord duration formula: `chordDurationMs = (60_000L / bpm) * 4L`.

### Phase 5: Summary Screen UI

**Status:** Work Complete

- [x] Add Play/Pause button to `SummaryScreen` (shown only when `settings.chordProgressionEnabled` and `renderedChords.isNotEmpty()`).
- [x] Add 1st/2nd/3rd selector buttons following the visibility rules from the plan.
- [x] Wire Play/Pause to ViewModel `play()` / `pause()`.
- [x] Wire selector buttons to ViewModel `queueProgression(index)`.
- [x] Pass `settings.bpm` into `play()` so tempo is applied from settings.
- [x] Build to confirm no compile errors.
- [ ] Manually verify playback and button behavior on a device/emulator.

**Technical Notes:**
`SummaryScreen` receives both `songIdea` and `settings`. The ViewModel's playback `StateFlow` needs to be collected in `SummaryScreen` via `collectAsState()` — this means either passing the playback state down as parameters or passing the ViewModel itself. Since `SummaryScreen` already receives its data via parameters, the cleanest path is to pass `isPlaying`, `activeProgressionIndex`, and the three callback lambdas as parameters, keeping the composable testable and stateless.

The selector buttons queue the next progression rather than switching immediately. A queued value replaces any previous queued value — last tap wins, consistent with the plan.

### Phase 6: Build and Verification

**Status:** In Progress

- [x] Run `.\gradlew.bat assembleDebug` — passed.
- [ ] Install on device/emulator: `.\gradlew.bat installDebug`.
- [ ] Manually test: Play starts looping, Pause stops, Resume continues, 2nd/3rd queue correctly, navigation away stops playback.
- [ ] Manually test BPM setting: change BPM, confirm tempo changes on next Play.
- [ ] Verify buttons show/hide correctly based on settings and active progression.

---

## Open Questions

1. **Should `SongIdeaViewModel` become `AndroidViewModel`, or should audio live in a separate `AudioViewModel`?**
   **Decision:** Change `SongIdeaViewModel` to `AndroidViewModel`.

2. **Should BPM be part of `StepSettings` or a new separate model?**
   **Decision:** Keep it in `StepSettings`.

3. **What should happen if chord progressions are enabled but no key was selected (so `renderedChords` is empty)?**
   **Decision:** Hide the Play/Pause button entirely when `renderedChords.isEmpty()`.

4. **Should the 3rd progression chord list be stored in `SongIdea` or computed on-the-fly in the ViewModel?**
   **Decision:** Compute on-the-fly in the ViewModel using `ChordMapper.renderProgressionOneWholeStepHigher`.

5. **Should the coroutines dependency be added explicitly or relied upon transitively?**
   **Decision:** Rely on the transitive dependency; add explicitly only if the build fails to resolve coroutine symbols.

6. **What BPM input UI widget is most appropriate for the settings screen?**
   **Decision:** Plain text `OutlinedTextField` with `keyboardType = KeyboardType.Number`.

---

## Bugs

### BUG-001: Pause then Play restarts the progression instead of resuming

**Status:** Fixed

**Description:** When the user presses Pause and then presses Play again, the progression starts over from the first chord rather than resuming from where it was paused.

**Root cause:** `pause()` cancels the coroutine job entirely, discarding the current position within the chord list. `play()` always starts a fresh loop from the beginning.

**Expected behavior:** Play after Pause should resume from the next chord in the sequence that was playing when Pause was pressed, not from the start.

**Fix approach:** Track the index of the chord that was being played when Pause was called. When Play is pressed, start the loop from that index rather than from zero. Reset the saved index to zero when a new song idea is generated or when the progression is switched.

---

### BUG-002: Start Over / Random / Home do not stop playback or reset progression state

**Status:** Fixed

**Description:** If a chord progression is playing and the user presses Start Over, Random, or Home, the audio continues and the progression selector buttons are not reset. For example, if 2nd and 3rd selector buttons were showing (because progression 1 was active), after navigating away and back those buttons should be gone and the state should show progression 1 as active again.

**Root cause:** `onStartOver`, `onRandomIdea`, and `onHome` callbacks do not call `pause()` or reset `activeProgressionIndex`. The ViewModel's `reset()` function calls `stopPlayback()` and resets `activeProgressionIndex`, but `startSession()` (called by Start Over) and `randomizeAll()` (called by Random) do not.

**Expected behavior:**
- Start Over, Random, and Home all stop any active playback immediately.
- `activeProgressionIndex` resets to 1 and `queuedProgressionIndex` clears.
- The Play/Pause button returns to "Play" state.

**Fix approach:** Call `stopPlayback()` and reset `activeProgressionIndex = 1` and `queuedProgressionIndex = null` at the start of both `startSession()` and `randomizeAll()` in `SongIdeaViewModel`. The `reset()` function (used by Home) already does this correctly.

### BUG-003: Resume after Pause replays the chord that was interrupted instead of advancing to the next one

**Status:** Fixed

**Description:** When the user pauses mid-chord and then resumes, the same chord plays again from the start rather than moving on to the next chord in the sequence.

**Root cause:** `pausedChordIndex` is set to `chordIndex` before `playChord` is called, so cancelling during a chord's delay records the index of the chord currently playing. On resume, that same chord plays again.

**Expected behavior:** Resuming should start on the chord *after* the one that was playing when Pause was pressed.

**Fix approach:** Set `pausedChordIndex = chordIndex + 1` (wrapping to 0 if it equals `chords.size`) instead of `chordIndex` when saving position. Or equivalently, increment `chordIndex` before saving, then save.

---

### BUG-004: Play button label does not indicate which progression is active

**Status:** Fixed

**Description:** The Play button always reads "Play" (or "Pause"), giving the user no indication of which chord progression is currently queued or playing.

**Expected behavior:** The button label should include the active progression number — e.g. "Play (1)", "Play (2)", "Pause (1)".

**Fix approach:** In `SummaryScreen`, append the `activeProgressionIndex` to the button label: `if (isPlaying) "Pause ($activeProgressionIndex)" else "Play ($activeProgressionIndex)"`.

---

## Notes and Risks

- `SoundPool` has a maximum number of simultaneous streams. The engine sets `maxStreams = 8`, which is enough for a 4-note chord plus a bass note.
- OGG sample loading is asynchronous; `AudioEngine.awaitLoaded()` polls until all 4 samples are ready. The first `playChord` call after cold start may have a small delay (<1 second).
- Pitch shifting via `SoundPool` rate can only go 0.5× to 2.0× (one octave down to one octave up). The `semitoneToBase` mapping in `AudioEngine` keeps all shifts within this window.
- `SoundPool.release()` must be called in `onCleared()`. If the user backgrounds the app mid-playback, the coroutine job should be cancelled to avoid ghost audio.
- The plan specifies that switching progressions takes effect after the current loop completes — this is implemented via `queuedProgressionIndex` checked between loop iterations. If the user taps a selector rapidly, only the last tap matters (last-write-wins on the queue).

---

## Completion Summary

*Fill in when the cycle closes. Move this document to `doc/planning/completed/` afterward.*

**Completion Date:** [YYYY-MM-DD]
**Phases Completed:** [List or "All"]
**Work Deferred:** [What was not done and why, or "None"]

**Accomplishments:**
- [What was built or changed]

**Metrics:**
- Files added: [N]
- Files modified: [N]
- Build/test status: [Command and result]

**Lessons / Notes:**
[Anything worth remembering for future audio or playback work.]
