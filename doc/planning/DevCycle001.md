# DevCycle 001: Initial Android App Implementation

**Status:** Work Complete
**Start Date:** 2026-04-25
**Target Completion:** TBD
**Focus:** Build the complete initial version of SongScaffold for Android from scratch.

---

## Goal

Build the first working version of SongScaffold — an Android app that guides users through a configurable sequence of musical and performance constraints, then displays a summary they can use to begin improvising.

This DevCycle implements the full design described in `doc/planning/ideas/songscaffold_android_design_doc.md`, using the topic data in `topics.md` and the decisions recorded in `implementation_questions.md`. The result should meet all 11 acceptance criteria from Section 16 of the design doc.

## Desired Outcome

A working Android app that:
- Lets the user start a song idea from a Home screen
- Walks the user through all enabled constraint steps in order
- Lets the user select, randomize, or skip each step
- Renders chord progressions in the selected key
- Shows a clean summary of all selected constraints
- Persists step enable/disable settings across restarts
- Feels visually polished from the start (dark mode, blue accent, Material 3)

---

## Tasks

### Phase 1: Project Setup

**Status:** Work Complete

- [ ] Create a new Android project with package `com.songscaffold.app`, min SDK API 26, Kotlin DSL Gradle (`build.gradle.kts`)
- [ ] Add dependencies: Jetpack Compose, Material 3, Navigation Compose, Jetpack DataStore, Compose UI tooling
- [ ] Set up package structure: `/data`, `/model`, `/ui`, `/ui/theme`, `/viewmodel`, `/music`
- [ ] Configure Material 3 dark theme with blue accent color as the app default
- [ ] Add a basic `MainActivity` that hosts the Compose `NavHost`
- [ ] Verify the project builds and runs (blank screen is fine)

**Technical Notes:**
Package: `com.songscaffold.app`. Min SDK: 26. Build: Kotlin DSL. Theme: Material 3 dark, blue accent. The NavHost lives in `MainActivity`. No splash screen or onboarding needed.

---

### Phase 2: Models and Data

**Status:** Work Complete

- [ ] Define `TopicCategory` enum (THEME, IMAGE, CHARACTER, SITUATION, EMOTION, PLACE, GOAL, OBSTACLE, TWIST, ELEMENT)
- [ ] Define `TopicPrompt(val text: String, val category: TopicCategory)`
- [ ] Define `ChordProgression(val name: String, val category: String, val romanNumerals: List<String>)`
- [ ] Define `SongIdea` data class (all fields nullable, `renderedChords: List<String> = emptyList()`)
- [ ] Define `SongStep` enum (TOPIC through RHYME_SCHEME, plus SUMMARY)
- [ ] Define `StepSettings` data class (all steps enabled by default)
- [ ] Implement `PromptRepository` with all topic data from `topics.md` (all 10 categories hardcoded)
- [ ] Implement `PromptRepository` chord progression data — deduplicate the `"Minor Pop Loop"` entry (appears in both `Classic / Standard` and `Loops`; keep it only under `Loops`)
- [ ] Implement `PromptRepository` with all option lists: `pointOfViewOptions`, `deliveryModes`, `phrasingStyles`, `emotionalIntensityOptions`, `majorKeys`, `startingNoteOptions`, `secondNoteDirectionOptions`, `rhymeSchemes`
- [ ] Implement `ChordMapper` with a lookup table for all 12 major keys, supporting numerals: I, ii, iii, IV, V, vi, I7, V/vi, iv, ♭VII

**Technical Notes:**
All data is hardcoded in `PromptRepository.kt` for the initial version. `ChordMapper` uses a fixed map keyed by root note — no music theory parsing. The duplicate `Minor Pop Loop` resolution: the `Classic / Standard` category already has three entries (Classic Cadence, Pop Axis, Minor Pop Loop); remove the duplicate from the `Loops` category, leaving `Loops` with Two Chord Open Loop and Two Five Loop.

---

### Phase 3: ViewModels and Navigation

**Status:** Work Complete

- [ ] Implement `SettingsRepository` wrapping Jetpack DataStore — reads and writes `StepSettings`
- [ ] Implement `SettingsViewModel` exposing `StepSettings` as `StateFlow`, with toggle methods per step
- [ ] Implement `SongIdeaViewModel` holding current `SongIdea` state and an ordered list of enabled steps
- [ ] `SongIdeaViewModel` provides: `setTopic`, `setPointOfView`, `setDeliveryMode`, `setPhrasing`, `setIntensity`, `setChordProgression`, `setSongKey`, `setStartingNote`, `setSecondNoteDirection`, `setRhymeScheme`, `skipStep`, `goBack`
- [ ] `SongIdeaViewModel` derives the enabled step sequence from `StepSettings` (respects Song Key conditional: only included when Chord Progression is enabled)
- [ ] `SongIdeaViewModel` computes `renderedChords` whenever both `chordProgression` and `songKey` are set
- [ ] Define navigation routes: `home`, `step/{stepIndex}`, `summary`, `settings`
- [ ] Wire the `NavHost` in `MainActivity` with all routes

**Technical Notes:**
Song Key visibility rule: `songKeyEnabled` in `StepSettings` still exists as a setting, but the step is only inserted into the active step sequence when Chord Progression is also enabled. The ViewModel builds the enabled step list dynamically each session start. `goBack` pops the step stack within the ViewModel so the NavController can navigate back correctly. `SongIdeaViewModel` is scoped to the nav back stack entry so state survives configuration changes but resets on `Start Over`.

---

### Phase 4: Screens

**Status:** Work Complete

- [ ] Build `HomeScreen` — app title, `Start Song Idea` button, `Settings` button
- [ ] Build generic `StepScreen` composable — step title, short description, content area, `Random` button, `Skip` button, `Next` button, `Back` button (visible on all steps including the first)
- [ ] Build `TopicStepContent` — shows currently selected topic (text + category label) or a prompt to tap Random; `Random Topic` button rerandomizes from the combined pool; no list to browse
- [ ] Build `OptionListStepContent` — scrollable list with radio buttons for steps that show a fixed option list (Point of View, Delivery Mode, Phrasing Style, Emotional Intensity, Starting Note, Second Note Direction, Rhyme Scheme)
- [ ] Build `ChordProgressionStepContent` — scrollable list with radio buttons grouped by category; shows roman numeral pattern alongside the name
- [ ] Build `SongKeyStepContent` — scrollable list with radio buttons for 12 major keys
- [ ] Compose each step's content into `StepScreen` via the step type from `SongStep`
- [ ] Build `SummaryScreen` — displays all non-skipped constraints; shows both roman numeral progression and rendered chords when both are present; omits skipped steps entirely; `Start Over` and `Home` buttons
- [ ] Build `SettingsScreen` — toggle list for all steps; Song Key toggle is shown but labelled "(requires Chord Progression)"

**Technical Notes:**
All step content screens reuse `OptionListStepContent` where the data is a flat list. Only Topic and Chord Progression need custom content composables. Skipped steps are stored as `null` in `SongIdea` and are omitted from the Summary — no "Skipped" label shown. Back on the first step navigates back to Home (discarding the current session). `Start Over` clears the `SongIdeaViewModel` state and navigates to `step/0`. The chord progression display in `ChordProgressionStepContent` shows the roman numeral sequence beneath the progression name.

---

### Phase 5: Settings Persistence

**Status:** Work Complete

- [ ] Implement DataStore proto or preferences schema for `StepSettings`
- [ ] Wire `SettingsRepository` so changes from `SettingsScreen` are written to DataStore immediately
- [ ] Load persisted settings on app start — default all steps enabled on first launch
- [ ] Verify settings survive app restart

**Technical Notes:**
Use Preferences DataStore (not Proto DataStore) for simplicity — store each step's enabled boolean as a named key. Default values are all `true`. No migration needed for the initial version.

---

### Phase 6: Polish

**Status:** Work Complete

- [ ] Verify dark theme and blue accent are applied consistently across all screens
- [ ] Review font sizes, spacing, and padding for readability in a rehearsal context (large readable text preferred)
- [ ] Ensure the step flow feels low-friction: tapping `Random` then `Next` should require minimal effort
- [ ] Add step progress indicator (e.g., "Step 3 of 7") to `StepScreen`
- [ ] Review Summary layout for clarity — all constraints should be scannable at a glance
- [ ] Smoke test the full flow: start → all steps → summary → start over

**Technical Notes:**
The step count in the progress indicator should reflect only enabled steps. Chord Progression group headers in the option list should use a slightly muted style so they read as headers, not selectable items.

---

## Open Questions

*None — all setup and design questions were resolved in `implementation_questions.md` before this cycle began.*

---

## Notes and Risks

- The duplicate `Minor Pop Loop` in the chord progressions data (design doc section 5.6) is a known copy error. The resolution is documented in Phase 2.
- Song Key conditional visibility (only active when Chord Progression is enabled) adds a small amount of state complexity to the ViewModel step sequencing. This is the only non-trivial conditional in the step order.
- DataStore writes are asynchronous. The `SettingsViewModel` should expose a `StateFlow` that reads the current persisted value and updates optimistically on toggle so the UI feels instant.
- The topic pool is large (~250 items across 10 categories). Randomization should draw from the full combined pool, not from a randomly chosen category first.
- No network access, accounts, or permissions are needed in this version.

---

## Completion Summary

*Fill in when the cycle closes. Move this document to `doc/planning/completed/` afterward.*

**Completion Date:** 2026-04-25
**Phases Completed:** All (1–6)
**Work Deferred:** None

**Accomplishments:**
- Created complete Android project from scratch (package `com.songscaffold.app`, API 26, Kotlin DSL)
- Implemented all data models: `TopicPrompt`, `ChordProgression`, `SongIdea`, `SongStep`, `StepSettings`
- Implemented `PromptRepository` with all topic data (~250 items across 10 categories) and all option lists; deduplicated "Minor Pop Loop"
- Implemented `ChordMapper` with lookup table for all 12 major keys supporting 10 roman numeral types
- Implemented `SettingsRepository` with Jetpack DataStore (Preferences)
- Implemented `SongIdeaViewModel` and `SettingsViewModel`
- Built all screens: `HomeScreen`, `StepScreen` (with `TopicStepContent`, `OptionListStepContent`, `ChordProgressionStepContent`), `SummaryScreen`, `SettingsScreen`
- Wired full `NavHost` navigation in `MainActivity`
- Material 3 dark theme with blue accent applied throughout
- Step progress indicator ("Step N of M") on every step screen
- Song Key conditional (only in step sequence when Chord Progression is enabled)

**Metrics:**
- Files created: 17 Kotlin source files + project scaffolding
- Build: `assembleDebug` passes, APK produced (11 MB debug)

**Lessons / Notes:**
- `android.useAndroidX=true` must be set in `gradle.properties` — Gradle does not add this automatically when creating a project manually.
- `android:Theme.Material.NoTitleBar` is not in the SDK; `android:Theme.DeviceDefault.NoActionBar` is the correct system theme for a Compose-only app.
