# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## RULE: Never Begin Implementation Without Explicit Instruction

**You are never allowed to begin implementation — writing code, editing files, downloading assets, or any other concrete change — without an explicit instruction from the user to do so.**

Confirming recommendations, answering questions, or approving a plan does NOT constitute a start instruction. Wait for the user to explicitly say something like "start," "go ahead," "implement this," or equivalent direct language before doing any implementation work.

**Exception: planning documents are not implementation.** Creating or updating dev cycle documents (`DevCycleNNN.md`), design docs, and idea files in `doc/` is planning work, not implementation. Do this immediately when requested — do not wait for a start instruction.

**When asked to create a dev cycle, the first and immediate action is to produce the `DevCycleNNN.md` document.** Do not summarise the plan in chat and wait — write the document first, then report what was created.

## What This App Does

SongScaffold generates structured creative prompts for improvised songs. It is not AI songwriting — it gives musicians random constraints (topic, key, chord progression, rhyme scheme, etc.) to spark live improvisation.

## Build Commands

```powershell
# Build
./gradlew build

# Install on connected device/emulator
./gradlew installDebug

# Run unit tests (none currently exist)
./gradlew test

# Clean
./gradlew clean
```

No tests exist yet. There are no `test/` or `androidTest/` directories.

## Architecture

Single-activity MVVM app using Jetpack Compose, Material 3 (dark theme only), and Compose Navigation.

**Data flow:** `SettingsRepository` (DataStore) ← `SettingsViewModel` → screens. `PromptRepository` (static in-memory data) ← `SongIdeaViewModel` → screens. Both ViewModels expose `StateFlow`; `MainActivity` collects them as Compose `State` and passes down.

**Navigation** (all in `MainActivity.kt`):
- `"home"` → `HomeScreen` — Start, Random, Settings
- `"step/{stepIndex}"` → `StepScreen` — wizard that walks through enabled steps
- `"summary"` → `SummaryScreen` — displays the completed song idea
- `"settings"` → `SettingsScreen` — toggle steps on/off, configure chord options

Forward navigation increments `stepIndex`; when steps are exhausted the app navigates to `"summary"`. Back from step 0 returns to home. Nav stack is popped appropriately to prevent back-stack buildup.

**Key source directories:**
- `data/` — `PromptRepository` (static topics/progressions/options), `SettingsRepository` (DataStore reads/writes)
- `model/` — `SongIdea`, `SongStep` (enum of 12 step types), `StepSettings`, `ChordProgression`, `ChordProgressionSuitability`
- `music/` — `ChordMapper` maps roman numerals + key → actual chord names; supports transposing one whole step up
- `ui/` — one file per screen plus `theme/`
- `viewmodel/` — `SongIdeaViewModel`, `SettingsViewModel`

## Key Domain Concepts

- **SongStep**: enum of 12 named steps (TOPIC, SONG_KEY, CHORD_PROGRESSION, etc.) plus SUMMARY. Each step can be enabled/disabled in settings.
- **ChordProgression**: has a name, category, list of roman numerals, and `ChordProgressionSuitability` tags that filter which progressions appear based on settings (e.g. hide 2-chord progressions).
- **PromptRepository**: all static data — 200+ topics in 10 categories, 40+ chord progressions, option lists for each step. This is the place to add new content.
- **StepSettings**: 14 boolean flags (one per step enablement + chord progression filters). Persisted via DataStore.
- **Transposition**: SummaryScreen can show chord progression transposed one whole step higher ("Chord Progression 3" display).

## Tech Stack

- Kotlin, AGP 8.x, Kotlin DSL (`*.gradle.kts`)
- Compile/Target SDK 35, Min SDK 26
- Jetpack Compose (BOM 2024.09.03), Material 3
- Navigation Compose, DataStore Preferences, ViewModel/StateFlow
- No networking, no database, no third-party libraries
