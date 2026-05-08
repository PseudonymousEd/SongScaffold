# DevCycle 005: Disable 2-Chord Chord Progressions Setting

**Status:** Work Complete
**Start Date:** 2026-05-08
**Target Completion:** 2026-05-08
**Focus:** Add a settings toggle that prevents 2-chord progressions from being selected manually or chosen randomly.

---

## Goal

Add a new settings toggle labeled "Disable 2-chord chord progressions" after the existing "Enabled Steps" settings. When enabled, SongScaffold should hide or disable chord progressions whose `chords` list contains exactly two entries, and all random selection paths should exclude those progressions. This gives users who want fuller harmonic movement a simple way to keep short two-chord loops out of their song ideas.

## Desired Outcome

- Settings includes a persisted `Disable 2-chord chord progressions` toggle immediately after the enabled step toggles.
- When the toggle is enabled, users cannot select any progression with exactly two chords from either chord progression step.
- When the toggle is enabled, `Random Idea`, per-step randomization, and second progression randomization never choose 2-chord progressions.
- Existing behavior remains unchanged when the toggle is disabled.
- If an existing selected 2-chord progression becomes disallowed during a session, the app handles it predictably by clearing it or replacing it with an allowed progression before summary.

---

## Tasks

### Phase 1: Settings Model and Persistence

**Status:** Work Complete

- [x] Add a `disableTwoChordProgressions` Boolean to `StepSettings`, defaulting to `false`.
- [x] Add a DataStore key for the new setting in `SettingsRepository`.
- [x] Read and write the setting alongside the existing enabled-step preferences.
- [x] Add the toggle to `SettingsScreen` immediately after the existing enabled-step section.

**Technical Notes:**
The main files are `app/src/main/java/com/songscaffold/app/model/StepSettings.kt`, `app/src/main/java/com/songscaffold/app/data/SettingsRepository.kt`, and `app/src/main/java/com/songscaffold/app/ui/SettingsScreen.kt`. Keep the setting opt-in so existing users continue seeing the full chord progression library by default.

### Phase 2: Shared Progression Filtering

**Status:** Work Complete

- [x] Introduce a single filtering helper for chord progressions so manual lists and random choices use the same rule.
- [x] Define "2-chord chord progression" as `ChordProgression.romanNumerals.size == 2`.
- [x] Ensure the helper preserves all progressions when `disableTwoChordProgressions` is false.
- [x] Ensure second chord progression selection still excludes the first selected progression after applying the 2-chord filter.

**Technical Notes:**
Today, chord progressions are read directly from `PromptRepository.chordProgressions` in `StepScreen`, `SongIdeaViewModel.randomizeAll`, and `MainActivity.applyRandom`. A small helper in the data or viewmodel layer can prevent drift between UI selection and randomization behavior.

Known current 2-chord progressions include:
- Two Chord Open Loop: `I - IV`
- Two Five Loop: `ii - V`
- Drone Loop: `I - bVII`
- Suspended Loop: `I - Vsus4`
- Minor Oscillation: `i - bVI`

### Phase 3: Manual Selection Behavior

**Status:** Work Complete

- [x] Pass the current setting into the chord progression step UI.
- [x] Prevent disallowed progressions from being selectable in both first and second chord progression steps.
- [x] Prefer hiding disallowed progressions entirely unless product review decides disabled rows with helper text are clearer.
- [x] Confirm category headers do not render empty categories when all progressions in a category are filtered out.

**Technical Notes:**
`ChordProgressionStepContent` currently reads `PromptRepository.chordProgressions` directly and groups by category. It should receive an already-filtered list or receive settings and call the shared filter. Passing an explicit list keeps the composable easier to preview and test.

### Phase 4: Randomization Behavior

**Status:** Work Complete

- [x] Update `SongIdeaViewModel.randomizeAll(settings)` to choose from the filtered progression list.
- [x] Update `MainActivity.applyRandom` for `SongStep.CHORD_PROGRESSION`.
- [x] Update `MainActivity.applyRandom` for `SongStep.SECOND_CHORD_PROGRESSION`, preserving the "different from first progression" rule.
- [x] Handle empty candidate pools defensively even though the current library still has many non-2-chord options.

**Technical Notes:**
Random behavior must honor the same setting whether the user taps "Random Idea" from Home/Summary or taps "Random" inside a chord progression step. If the filtered pool is unexpectedly empty, avoid throwing from `.random()` and leave the current selection unchanged or clear it intentionally.

### Phase 5: Verification

**Status:** Work Complete

- [x] Build the app with `.\gradlew.bat assembleDebug`.
- [ ] Manually verify the toggle appears below the enabled-step settings.
- [ ] Manually verify 2-chord progressions are unavailable when the toggle is on and available when off.
- [ ] Manually verify repeated randomization does not produce 2-chord progressions when the toggle is on.
- [x] Add or update focused unit tests if the project has an existing suitable test harness for settings or progression filtering.

**Technical Notes:**
The highest-risk behavior is duplicated randomization logic. Verification should cover both all-at-once random generation and per-step random buttons.

---

## Open Questions

1. **Should disallowed 2-chord progressions be hidden or shown disabled?**
   Recommendation: Hide them. This keeps the chord progression step uncluttered and matches the user's goal that users can neither select nor have randomly chosen 2-chord progressions.

2. **What should happen to an already selected 2-chord progression if the setting changes mid-session?**
   Recommendation: Clear the disallowed selection the next time the chord step or randomization logic evaluates candidates. This avoids showing a summary that violates the active setting.

---

## Notes and Risks

- The current implementation reads `PromptRepository.chordProgressions` from several places, so a shared filter is important.
- The second chord progression random path needs two filters: remove 2-chord progressions when disabled, then remove the first selected progression.
- Current progression symbols use Unicode accidentals in source data. The filter should use chord count only, not roman numeral text.

---

## Completion Summary

**Completion Date:** 2026-05-08
**Phases Completed:** All
**Work Deferred:** Manual device/UI verification remains pending; no existing unit test harness was present for focused progression filtering tests.

**Accomplishments:**
- Added a persisted `disableTwoChordProgressions` setting.
- Added the Settings screen toggle and wired it into manual chord progression selection.
- Added shared progression filtering and applied it to Random Idea and per-step randomization.
- Preserved second progression randomization's "different from first progression" behavior.

**Metrics:**
- Files modified: 8
- Build/test status: `.\gradlew.bat assembleDebug` passed

**Lessons / Notes:**
The `ChordProgression` model stores roman numeral tokens in `romanNumerals`, so the implementation defines 2-chord progressions by `romanNumerals.size == 2`.
