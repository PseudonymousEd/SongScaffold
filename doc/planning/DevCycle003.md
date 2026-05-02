# DevCycle 003: Random Idea + Second Chord Progression

**Status:** Planning
**Start Date:** 2026-05-02
**Target Completion:** TBD
**Focus:** Add a "Random Idea" shortcut on the Home screen and a new Second Chord Progression step.

---

## Goal

Two independent but complementary additions. First, a "Random Idea" button on the Home screen lets users skip the step-by-step flow entirely and land directly on the Summary screen with all enabled steps randomly filled — useful for a quick spark of inspiration. Second, a new Second Chord Progression step (inserted immediately after the Chord Progression step) lets users pick an optional second progression for their song, e.g. a verse progression and a chorus progression.

## Desired Outcome

- A "Random Idea" button appears on the Home screen. Tapping it randomizes all enabled steps and navigates directly to the Summary screen.
- A "Random Idea" button also appears on the Summary screen (between "Start Over" and "Home"), allowing the user to re-roll a fresh random idea without leaving the results.
- A "Second Chord Progression" step is available in the flow, appearing right after the Chord Progression step.
- The second progression can be toggled on/off in Settings and is off by default.
- The second progression's rendered chords (keyed to Song Key) appear on the Summary screen when set.
- Both features work correctly in the step-by-step and random-idea flows.

---

## Tasks

### Phase 1: Data and Models

**Status:** Planning

- [ ] Add `SECOND_CHORD_PROGRESSION` to `SongStep` enum, inserted between `CHORD_PROGRESSION` and `SONG_KEY`
- [ ] Add `secondChordProgression: ChordProgression? = null` and `secondRenderedChords: List<String> = emptyList()` to `SongIdea`
- [ ] Add `secondChordProgressionEnabled: Boolean = false` to `StepSettings` (default off — see Open Questions)
- [ ] Add `SECOND_CHORD_PROGRESSION = booleanPreferencesKey("second_chord_progression")` to `SettingsRepository.Keys` and wire it into `stepSettings` flow and `updateSettings`

**Technical Notes:**
`SECOND_CHORD_PROGRESSION` sits between `CHORD_PROGRESSION` and `SONG_KEY` in the enum. `SongIdea` is a data class — adding both new fields with defaults is non-breaking. The DataStore key is `"second_chord_progression"` to match the existing naming convention. `secondRenderedChords` mirrors `renderedChords`: it is populated whenever both `songKey` and `secondChordProgression` are set, using `ChordMapper.renderProgression`.

---

### Phase 2: ViewModel

**Status:** Planning

- [ ] Update `buildEnabledSteps` to insert `SECOND_CHORD_PROGRESSION` when `s.chordProgressionEnabled && s.secondChordProgressionEnabled` (the first progression must also be enabled)
- [ ] Add `setSecondChordProgression(value: ChordProgression)` to `SongIdeaViewModel` — mirrors `setChordProgression`, populating `secondRenderedChords` using the current `songKey`
- [ ] Update `setSongKey` to also re-render `secondRenderedChords` when `secondChordProgression` is set
- [ ] Add `randomizeAll(settings: StepSettings)` method to `SongIdeaViewModel` — builds enabled steps, picks random values for every step, and sets `_songIdea` in one shot (used by the Random Idea button)

**Technical Notes:**
`setSecondChordProgression`:
```kotlin
fun setSecondChordProgression(value: ChordProgression) {
    _songIdea.update { idea ->
        val rendered = idea.songKey?.let { ChordMapper.renderProgression(it, value) } ?: emptyList()
        idea.copy(secondChordProgression = value, secondRenderedChords = rendered)
    }
}
```

Updated `setSongKey` must now also re-render the second progression:
```kotlin
fun setSongKey(value: String) {
    _songIdea.update { idea ->
        val rendered = idea.chordProgression?.let { ChordMapper.renderProgression(value, it) } ?: emptyList()
        val secondRendered = idea.secondChordProgression?.let { ChordMapper.renderProgression(value, it) } ?: emptyList()
        idea.copy(songKey = value, renderedChords = rendered, secondRenderedChords = secondRendered)
    }
}
```

`randomizeAll` computes the key and both progressions first (since chord rendering depends on key), then constructs `SongIdea` in one assignment:
```kotlin
fun randomizeAll(settings: StepSettings) {
    val steps = buildEnabledSteps(settings)
    _enabledSteps.value = steps
    val key  = if (SongStep.SONG_KEY in steps) PromptRepository.majorKeys.random() else null
    val prog = if (SongStep.CHORD_PROGRESSION in steps) PromptRepository.chordProgressions.random() else null
    val prog2 = if (SongStep.SECOND_CHORD_PROGRESSION in steps) PromptRepository.chordProgressions.random() else null
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
```

---

### Phase 3: UI

**Status:** Planning

**HomeScreen**
- [ ] Add `onRandomIdea: () -> Unit` parameter to `HomeScreen`
- [ ] Add an `OutlinedButton` labeled "Random Idea" between the existing "Start Song Idea" and "Settings" buttons (with the same `Spacer(height = 16.dp)` spacing)

**StepScreen**
- [ ] Add `selectedSecondProgression: ChordProgression?` and `onSecondProgressionSelected: (ChordProgression) -> Unit` parameters to `StepScreen`
- [ ] Add a dispatch case for `SongStep.SECOND_CHORD_PROGRESSION` in `StepScreen`'s step type switch — renders the existing `ChordProgressionStepContent` composable using `selectedSecondProgression` and `onSecondProgressionSelected`. Step description: "Pick a second chord progression (e.g. for the chorus)."

**SummaryScreen**
- [ ] Update the chord section in `SummaryScreen` to display `secondChordProgression` and `secondRenderedChords` when set
- [ ] When both progressions are present, relabel them "Chord Prog. 1" / "Chord Prog. 2" and "Chords 1" / "Chords 2"; when only one is present, keep the existing "Chord Progression" / "Chords" labels
- [ ] Add `onRandomIdea: () -> Unit` parameter to `SummaryScreen`
- [ ] Add an `OutlinedButton` labeled "Random Idea" between the existing "Start Over" and "Home" buttons

**SettingsScreen**
- [ ] Add a toggle row for "Second Chord Progression" immediately after the Chord Progression toggle row

**MainActivity**
- [ ] Wire `onRandomIdea` in the `HomeScreen` composable: call `songIdeaViewModel.randomizeAll(settings)`, then navigate to `"summary"` with `popUpTo("home") { inclusive = false }`
- [ ] Wire `onRandomIdea` in the `SummaryScreen` composable: call `songIdeaViewModel.randomizeAll(settings)`, then navigate to `"summary"` replacing itself (`popUpTo("summary") { inclusive = true }`) so the back stack stays clean
- [ ] Pass `selectedSecondProgression = songIdea.secondChordProgression` and `onSecondProgressionSelected = { songIdeaViewModel.setSecondChordProgression(it) }` to the `StepScreen` composable
- [ ] Add `SongStep.SECOND_CHORD_PROGRESSION -> vm.setSecondChordProgression(PromptRepository.chordProgressions.random())` to `applyRandom`

**Technical Notes:**
`HomeScreen`'s new button layout (top to bottom): "Start Song Idea" (filled), "Random Idea" (outlined), spacer, "Settings" (outlined). The Random Idea button should be the same width as the others (`fillMaxWidth`) but can be the same height as Settings (`height(48.dp)`) since it is a secondary action.

`SummaryScreen`'s button row expands from two buttons to three: "Start Over" (outlined), "Random Idea" (outlined), "Home" (filled). All three share equal `weight(1f)` with `Arrangement.spacedBy(12.dp)`.

For the chord relabeling logic in `SummaryScreen`: check `songIdea.secondChordProgression != null` to decide whether to use numbered labels. This keeps the label "Chord Progression" unchanged for users who never enable the second step.

The `SECOND_CHORD_PROGRESSION` step in `StepScreen` reuses `ChordProgressionStepContent` directly — no new composable needed. The step title in the top bar is derived from `step.name` humanized, so it will display as "Second Chord Progression" automatically if a `displayName` or `name` property is used; verify the existing title-derivation logic and adjust if needed.

---

## Open Questions

1. **Should `secondChordProgressionEnabled` default to `false` or `true`?**
   Decision: `false`. The second chord progression is an optional extra that most users won't use on a first run; defaulting it off keeps the standard flow clean.

2. **Where should "Random Idea" sit in the Home screen button stack?**
   Decision: Between "Start Song Idea" and "Settings". Also add a "Random Idea" button on the Summary screen between "Start Over" and "Home", so users can keep re-rolling from the results screen without going back to Home.
---

## Notes and Risks

- `SECOND_CHORD_PROGRESSION` is inserted between `CHORD_PROGRESSION` and `SONG_KEY` in the enum. Ordinal values of `SONG_KEY` and later steps shift by one. The step navigation uses index into the runtime-built `enabledSteps` list (not enum ordinals), so this is safe.
- `buildEnabledSteps` has the existing comment `// Song Key only appears when Chord Progression is also enabled` — the second progression follows the same dependency (`chordProgressionEnabled && secondChordProgressionEnabled`) for consistency and to avoid orphaned state.
- `randomizeAll` must not call `startSession` internally (which resets `_songIdea` to empty), since `randomizeAll` builds and populates in one operation. Keep them as separate paths.
- The Random Idea navigation bypasses all step screens. The `enabledSteps` list is still populated by `randomizeAll` so that if the user later navigates to `"step/0"` via Start Over from the Summary screen, the steps are available.

---

## Completion Summary

*Fill in when the cycle closes. Move this document to `doc/planning/completed/` afterward.*

**Completion Date:**
**Phases Completed:**
**Work Deferred:**

**Accomplishments:**

**Metrics:**
- Files modified:

**Lessons / Notes:**
