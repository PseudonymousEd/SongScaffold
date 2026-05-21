# DevCycle 008: Chord Progression Audio Playback

**Status:** Planning
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

**Status:** Planning

- [ ] Create directory `app/src/main/assets/notes/`.
- [ ] Download `C4.ogg`, `Ds4.ogg`, `Fs4.ogg`, `A4.ogg` from the Tone.js Salamander mirror.
- [ ] Verify all four files are present and non-zero before continuing.

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

**Status:** Planning

- [ ] Create `app/src/main/java/com/songscaffold/app/audio/AudioEngine.kt`.
- [ ] Implement `AudioEngine` using `SoundPool` exactly as described in `How_to_play_chords.md`.
- [ ] Verify the package declaration matches (`com.songscaffold.app.audio`).
- [ ] Build to confirm no compile errors.

**Technical Notes:**
`AudioEngine` is a standalone class that takes a `Context` (for `AssetManager`) and exposes:
- `suspend fun playChord(chordName: String, durationMs: Long)` — plays a chord and suspends for the duration.
- `fun release()` — frees the `SoundPool`.

`SoundPool` and `AudioAttributes` are part of the Android SDK; no new dependencies needed. `kotlinx-coroutines-android` is already available transitively via `lifecycle-runtime-ktx`.

### Phase 3: ViewModel — Playback State and Logic

**Status:** Planning

- [ ] Change `SongIdeaViewModel` from `ViewModel` to `AndroidViewModel(application: Application)`.
- [ ] Add `AudioEngine` as a private field, created in the ViewModel constructor.
- [ ] Add playback state: `isPlaying`, `activeProgressionIndex` (1/2/3), `queuedProgressionIndex`.
- [ ] Expose playback state as `StateFlow` fields consumed by `SummaryScreen`.
- [ ] Add `fun play(chords: List<String>, bpm: Int)` — cancels any running job and starts a new looping coroutine on `Dispatchers.IO`.
- [ ] Add `fun pause()` — cancels the playback job without releasing `AudioEngine`.
- [ ] Add `fun queueProgression(index: Int)` — sets `queuedProgressionIndex`; the loop checks it after each full pass and switches if set.
- [ ] Call `audioEngine.release()` in `onCleared()`.
- [ ] Build to confirm no compile errors.

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

**Status:** Planning

- [ ] Add `bpm: Int = 60` to `StepSettings`.
- [ ] Add `intPreferencesKey("bpm")` to `SettingsRepository.Keys`.
- [ ] Read/write BPM in `SettingsRepository.stepSettings` flow and `updateSettings`.
- [ ] Add a BPM numeric input field to `SettingsScreen` (visible when chord progression is enabled).
- [ ] Add `fun updateBpm(bpm: Int)` to `SettingsViewModel` (or reuse the existing settings update path).
- [ ] Build to confirm no compile errors.

**Technical Notes:**
BPM lives in `StepSettings` rather than a separate model. It is only relevant when chord progressions are enabled, so the settings UI should conditionally show it. Input should accept integers only, with a reasonable range (e.g., 20–300). The chord duration formula: `chordDurationMs = (60_000L / bpm) * 4L`.

### Phase 5: Summary Screen UI

**Status:** Planning

- [ ] Add Play/Pause button to `SummaryScreen` (shown only when `settings.chordProgressionEnabled` and `renderedChords.isNotEmpty()`).
- [ ] Add 1st/2nd/3rd selector buttons following the visibility rules from the plan:
  - **(1st)** shown when the 2nd or 3rd progression is active.
  - **(2nd)** shown when a 2nd chord progression exists and it is not currently active.
  - **(3rd)** shown when `settings.enableChordProgression3` is true and it is not currently active.
- [ ] Wire Play/Pause to ViewModel `play()` / `pause()`.
- [ ] Wire selector buttons to ViewModel `queueProgression(index)`.
- [ ] Pass `settings.bpm` into `play()` so tempo is applied from settings.
- [ ] Build to confirm no compile errors.
- [ ] Manually verify playback and button behavior on a device/emulator.

**Technical Notes:**
`SummaryScreen` receives both `songIdea` and `settings`. The ViewModel's playback `StateFlow` needs to be collected in `SummaryScreen` via `collectAsState()` — this means either passing the playback state down as parameters or passing the ViewModel itself. Since `SummaryScreen` already receives its data via parameters, the cleanest path is to pass `isPlaying`, `activeProgressionIndex`, and the three callback lambdas as parameters, keeping the composable testable and stateless.

The selector buttons queue the next progression rather than switching immediately. A queued value replaces any previous queued value — last tap wins, consistent with the plan.

### Phase 6: Build and Verification

**Status:** Planning

- [ ] Run `.\gradlew.bat assembleDebug` — must pass.
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
