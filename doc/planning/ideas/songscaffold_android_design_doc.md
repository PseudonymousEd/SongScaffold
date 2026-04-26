# SongScaffold Android App — Design Document

## 1. Overview

This Android app, called SongScaffold, helps users generate constraints for improvised songs.

The app is not intended to write songs for the user. Instead, it guides the user through a short sequence of musical and performance decisions that reduce blank-page pressure and encourage varied improvisation practice.

At the end of the flow, the app displays all selected constraints on a single summary page so the user can begin improvising.

A future version may add chord progression playback, allowing the selected progression to loop until stopped.

---

## 2. Core Concept

The user completes a sequence of configurable steps. Each step presents a category of constraint.

For each step, the user can:

- Select one of the shown options
- Randomize the selection
- Ignore / skip the step

Each step can be enabled or disabled in Settings. Disabled steps are omitted from the generation flow.

The app should support quick, low-friction use. The user should be able to generate a usable improv prompt in under a minute.

---

## 3. Primary User Flow

### 3.1 Home Screen

The Home screen contains:

- App title
- Button: `Start Song Idea`
- Button: `Settings`

Optional future buttons:

- `Quick Random Song Idea`
- `Practice History`
- `Favorites`

For the initial version, only `Start Song Idea` and `Settings` are required.

---

### 3.2 Song Idea Generation Flow

When the user taps `Start Song Idea`, the app begins a sequence of enabled steps.

Recommended default order:

1. Topic
2. Point of View
3. Delivery Mode
4. Phrasing Style
5. Emotional Intensity
6. Chord Progression
7. Song Key
8. Starting Note
9. Second Note Direction
10. Rhyme Scheme
11. Summary

Important clarification: `Topic` is a single step. It does not split into separate screens for emotion, place, goal, obstacle, twist, etc. Instead, the Topic step draws options from multiple topic prompt pools.

---

## 4. Step Behavior

Each step screen should use the same general layout:

- Step title
- Short explanation
- Current option list or selected option
- Button: `Random`
- Button: `Skip`
- Button: `Next`
- Optional: `Back`

The user should not be forced to make a selection. Skipped steps should appear as `Skipped` or be omitted from the final summary, depending on implementation preference.

Recommended behavior:

- If the user selects an option, store that value and enable `Next`.
- If the user taps `Random`, choose a random option, store it, and display it as selected.
- If the user taps `Skip`, store no value and move to the next enabled step.
- `Back` returns to the previous enabled step and preserves the prior selection.

---

## 5. Step Definitions

## 5.1 Topic

### Purpose

The Topic step gives the user the central idea or image for the improvised song.

This is one combined step, not multiple steps.

### Topic Sources

The topic option pool should be composed from several internal lists:

- Emotions
- Places
- Goals
- Obstacles
- Twists
- Elements
- Themes
- Images
- Characters
- Situations

The app may either:

1. Show all topic options mixed together, or
2. Randomly choose one topic source first, then show options from that source, or
3. Display one randomly selected topic from any source.

Recommended initial implementation:

- The Topic step has a `Random Topic` button.
- Pressing it selects one item from the combined topic pool.
- The displayed topic may include a small label showing its source, such as `Place`, `Goal`, or `Twist`.

Example:

```text
Topic: watching someone else live your old life
Source: Situation
```

### Topic Data Model

Each topic item should have:

```kotlin
data class TopicPrompt(
    val text: String,
    val category: TopicCategory
)

enum class TopicCategory {
    THEME,
    IMAGE,
    CHARACTER,
    SITUATION,
    EMOTION,
    PLACE,
    GOAL,
    OBSTACLE,
    TWIST,
    ELEMENT
}
```

---

## 5.2 Point of View

### Purpose

Point of View defines who the song is being sung as or addressed to.

This should appear immediately after Topic.

### Options

Recommended initial options:

```kotlin
val pointOfViewOptions = listOf(
    "First person — I / me / my",
    "Second person — you",
    "Third person — he / she / they",
    "Direct address — singing to someone present",
    "Memory voice — singing from the past",
    "Future self — singing to who you might become",
    "Observer — describing someone else without entering their mind"
)
```

### Design Notes

Point of View is a powerful constraint because it changes the lyric approach without requiring a new story idea.

Examples:

- Topic: `a lighthouse in fog`
- First person: `I am lost and looking for a signal`
- Second person: `You are the light I can barely see`
- Observer: `Someone waits by the water every night`

---

## 5.3 Delivery Mode

### Purpose

Delivery Mode defines how the idea is expressed vocally and dramatically.

### Options

```kotlin
val deliveryModes = listOf(
    "Performing — outward, shaped, intentional",
    "Thinking — inward, discovering, conversational"
)
```

### Design Notes

- `Performing` encourages theatrical clarity and projection.
- `Thinking` encourages conversational phrasing and discovery.

---

## 5.4 Phrasing Style

### Purpose

Phrasing Style defines how the words flow.

### Options

```kotlin
val phrasingStyles = listOf(
    "Hold — sustain vowels, connect phrases",
    "Break — pause, fragment, interrupt"
)
```

### Design Notes

This should affect how the user sings, not what the user sings about.

---

## 5.5 Emotional Intensity

### Purpose

Emotional Intensity defines how strongly the emotion is projected.

### Options

```kotlin
val emotionalIntensityOptions = listOf(
    "Low — internal, contained",
    "High — projected, expressive"
)
```

---

## 5.6 Chord Progression

### Purpose

The chord progression provides the harmonic structure for the improvised song.

The user should select a progression by roman numeral pattern. The app should display the concrete chords in the selected key.

### Initial Progressions

```kotlin
data class ChordProgression(
    val name: String,
    val category: String,
    val romanNumerals: List<String>
)
```

Recommended initial data:

```kotlin
val chordProgressions = listOf(
    ChordProgression("Classic Cadence", "Classic / Standard", listOf("I", "IV", "V", "I")),
    ChordProgression("Pop Axis", "Classic / Standard", listOf("I", "V", "vi", "IV")),
    ChordProgression("Minor Pop Loop", "Classic / Standard", listOf("vi", "IV", "I", "V")),

    ChordProgression("Two Five One", "Musical Theatre / Jazz", listOf("ii", "V", "I")),
    ChordProgression("Circle Turnaround", "Musical Theatre / Jazz", listOf("I", "vi", "ii", "V")),
    ChordProgression("Extended Turnaround", "Musical Theatre / Jazz", listOf("iii", "vi", "ii", "V")),
    ChordProgression("Borrowed Minor Four", "Musical Theatre / Jazz", listOf("I", "I7", "IV", "iv")),

    ChordProgression("Secondary Dominant Lift", "Expressive / Color", listOf("I", "V/vi", "vi", "IV")),
    ChordProgression("Major To Minor Four", "Expressive / Color", listOf("I", "IV", "iv", "I")),
    ChordProgression("Minor To Resolution", "Expressive / Color", listOf("vi", "ii", "V", "I")),
    ChordProgression("Flat Seven Color", "Expressive / Color", listOf("I", "♭VII", "IV", "I")),

    ChordProgression("Two Chord Open Loop", "Loops", listOf("I", "IV")),
    ChordProgression("Minor Pop Loop", "Loops", listOf("vi", "IV", "I", "V")),
    ChordProgression("Two Five Loop", "Loops", listOf("ii", "V"))
)
```

### Future Feature

In a future version, the selected chord progression should play in a loop until the user presses a stop button.

Initial version does not need audio playback.

---

## 5.7 Song Key

### Purpose

The Song Key determines how the selected roman numeral chord progression is rendered as real chords.

### Initial Keys

Recommended major keys:

```kotlin
val majorKeys = listOf(
    "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"
)
```

Optional minor keys for a later version:

```kotlin
val minorKeys = listOf(
    "Cm", "C#m", "Dm", "Ebm", "Em", "Fm", "F#m", "Gm", "G#m", "Am", "Bbm", "Bm"
)
```

Recommended initial implementation:

- Support major keys only.
- Store the selected key.
- Convert roman numerals to chords for display.

---

## 5.8 Starting Note

### Purpose

Starting Note defines where the melody begins relative to the first chord.

### Options

```kotlin
val startingNoteOptions = listOf(
    "Root (1) — grounded, direct",
    "3rd — emotional, expressive",
    "5th — open, searching"
)
```

---

## 5.9 Second Note Direction

### Purpose

Second Note Direction gives the user one more melodic constraint after the first note.

### Options

```kotlin
val secondNoteDirectionOptions = listOf(
    "Step up",
    "Step down",
    "Leap up",
    "Leap down",
    "Repeat the first note"
)
```

---

## 5.10 Rhyme Scheme

### Purpose

Rhyme Scheme defines how line endings relate to each other.

This is about rhyme pattern, not rhyme density.

### Options

```kotlin
val rhymeSchemes = listOf(
    "AABB — pairs of matching rhymes",
    "ABAB — alternating rhymes",
    "ABAC — partial repetition with variation",
    "AAAA — same rhyme throughout",
    "Free — no planned rhyme scheme"
)
```

---

# 6. Summary Screen

After all enabled steps are complete, the app displays a single summary page.

The summary page should show all selected constraints clearly.

Example:

```text
Song Idea Summary

Topic: watching someone else live your old life
Point of View: Second person — you
Delivery Mode: Thinking — inward, discovering, conversational
Phrasing Style: Break — pause, fragment, interrupt
Emotional Intensity: Low — internal, contained
Chord Progression: I – IV – iv – I
Key: Bb
Chords: Bb – Eb – Ebm – Bb
Starting Note: 3rd — emotional, expressive
Second Note Direction: Step down
Rhyme Scheme: ABAC — partial repetition with variation
```

Buttons:

- `Start Over`
- `Home`

Optional future buttons:

- `Reroll One Constraint`
- `Save`
- `Play Chords`

---

# 7. Settings Screen

The Settings screen allows users to enable or disable each step.

Recommended settings:

```text
Enabled Steps
[x] Topic
[x] Point of View
[x] Delivery Mode
[x] Phrasing Style
[x] Emotional Intensity
[x] Chord Progression
[x] Song Key
[x] Starting Note
[x] Second Note Direction
[x] Rhyme Scheme
```

If a step is disabled, it should not appear in the Song Idea Generation flow and should not appear in the Summary unless there is a saved previous value. Recommended behavior: omit disabled steps from the Summary.

Settings should persist locally.

---

# 8. Persistence Requirements

Initial version should persist only settings.

Required persistence:

- Enabled / disabled state of each step

Not required for initial version:

- Song idea history
- Favorites
- Saved prompts
- User-created custom lists
- Cloud sync
- Accounts

Recommended storage:

- Jetpack DataStore for settings

---

# 9. Architecture Recommendation

Recommended Android stack:

- Kotlin
- Jetpack Compose
- MVVM-style state management
- Navigation Compose
- DataStore for settings persistence

Suggested package structure:

```text
com.example.songconstraints

/data
    PromptRepository.kt
    SettingsRepository.kt

/model
    SongIdea.kt
    SongStep.kt
    TopicPrompt.kt
    ChordProgression.kt

/ui
    HomeScreen.kt
    StepScreen.kt
    SummaryScreen.kt
    SettingsScreen.kt

/viewmodel
    SongIdeaViewModel.kt
    SettingsViewModel.kt

/music
    ChordMapper.kt
```

---

# 10. Core Data Models

## 10.1 Song Idea

```kotlin
data class SongIdea(
    val topic: TopicPrompt? = null,
    val pointOfView: String? = null,
    val deliveryMode: String? = null,
    val phrasingStyle: String? = null,
    val emotionalIntensity: String? = null,
    val chordProgression: ChordProgression? = null,
    val songKey: String? = null,
    val renderedChords: List<String> = emptyList(),
    val startingNote: String? = null,
    val secondNoteDirection: String? = null,
    val rhymeScheme: String? = null
)
```

## 10.2 Song Step

```kotlin
enum class SongStep {
    TOPIC,
    POINT_OF_VIEW,
    DELIVERY_MODE,
    PHRASING_STYLE,
    EMOTIONAL_INTENSITY,
    CHORD_PROGRESSION,
    SONG_KEY,
    STARTING_NOTE,
    SECOND_NOTE_DIRECTION,
    RHYME_SCHEME,
    SUMMARY
}
```

## 10.3 Step Settings

```kotlin
data class StepSettings(
    val topicEnabled: Boolean = true,
    val pointOfViewEnabled: Boolean = true,
    val deliveryModeEnabled: Boolean = true,
    val phrasingStyleEnabled: Boolean = true,
    val emotionalIntensityEnabled: Boolean = true,
    val chordProgressionEnabled: Boolean = true,
    val songKeyEnabled: Boolean = true,
    val startingNoteEnabled: Boolean = true,
    val secondNoteDirectionEnabled: Boolean = true,
    val rhymeSchemeEnabled: Boolean = true
)
```

---

# 11. Chord Rendering

The app should store progressions as roman numerals and render them in the selected key.

For initial implementation, support major keys only.

Example:

```text
Key: C
Progression: I – IV – V – I
Rendered: C – F – G – C
```

```text
Key: Bb
Progression: I – IV – V – I
Rendered: Bb – Eb – F – Bb
```

ChordMapper should support:

- I
- ii
- iii
- IV
- V
- vi
- I7
- V/vi
- iv
- ♭VII

Initial implementation can use a lookup table instead of complex music theory parsing.

Example approach:

```kotlin
object ChordMapper {
    fun renderProgression(key: String, progression: ChordProgression): List<String> {
        return progression.romanNumerals.map { numeral ->
            renderChord(key, numeral)
        }
    }
}
```

Recommended: start with a fixed map for each supported major key.

---

# 12. Randomization Rules

Each step should support random selection.

Randomization behavior:

- Random Topic chooses from the combined topic pool.
- Random Point of View chooses from pointOfViewOptions.
- Random Delivery Mode chooses from deliveryModes.
- Random Phrasing Style chooses from phrasingStyles.
- Random Emotional Intensity chooses from emotionalIntensityOptions.
- Random Chord Progression chooses from chordProgressions.
- Random Song Key chooses from majorKeys.
- Random Starting Note chooses from startingNoteOptions.
- Random Second Note Direction chooses from secondNoteDirectionOptions.
- Random Rhyme Scheme chooses from rhymeSchemes.

If both Chord Progression and Song Key are selected, the Summary should display rendered chords.

If either Chord Progression or Song Key is skipped or disabled, rendered chords should be omitted.

---

# 13. Initial Content Data

The app should include hardcoded initial prompt lists based on the source document.

Prompt categories:

- themes
- images
- characters
- situations
- emotions
- places
- goals
- obstacles
- twists
- elements

Implementation can store these lists in `PromptRepository.kt`.

In a future version, these could be moved to JSON resources.

---

# 14. Non-Goals for Initial Version

The initial version should not include:

- AI-generated lyrics
- AI-generated topics
- User accounts
- Cloud sync
- Practice tracking
- Audio recording
- Editing prompt lists
- Saving generated song ideas
- Chord playback
- MIDI export
- Sheet music display

The goal is a lightweight constraint generator, not a full songwriting suite.

---

# 15. Future Enhancements

Possible future additions:

## 15.1 Chord Playback

- Play selected chord progression in a loop
- Stop button
- Tempo control
- Simple piano or pad sound

## 15.2 Reroll One Constraint

On the Summary screen, allow the user to reroll a single selected constraint while preserving the rest.

Example:

- Keep topic, key, chords, and rhyme scheme
- Reroll only Point of View

## 15.3 Quick Random Mode

A one-button mode that generates a full song idea using all enabled steps.

## 15.4 Practice Timer

Allow the user to set a timer:

- 30 seconds
- 1 minute
- 2 minutes
- 5 minutes

## 15.5 Custom Prompt Lists

Allow the user to add custom topics, places, images, or other prompt types.

## 15.6 Difficulty Presets

Optional presets:

- Light: Topic + Point of View + Chords
- Performance: Topic + Delivery + Phrasing + Intensity
- Lyric: Topic + Point of View + Rhyme Scheme
- Full: All enabled constraints

---

# 16. Acceptance Criteria

The initial app is complete when:

1. User can start a new song idea from the Home screen.
2. User is guided through all enabled steps in order.
3. User can select, randomize, or skip each step.
4. Topic is treated as one combined step using multiple prompt categories.
5. Point of View appears immediately after Topic.
6. User can select a chord progression.
7. User can select a major key.
8. Summary displays roman numerals and rendered chords when both progression and key are selected.
9. User can enable or disable steps in Settings.
10. Settings persist after app restart.
11. User can return to Home from Summary.

---

# 17. Implementation Priority

Recommended build order:

1. Define models and hardcoded prompt data.
2. Build Home screen.
3. Build generic Step screen.
4. Implement ViewModel state for current song idea.
5. Implement navigation through enabled steps.
6. Build Summary screen.
7. Build Settings screen.
8. Persist settings with DataStore.
9. Implement chord rendering.
10. Polish UI text and layout.

---

# 18. Design Philosophy

The app should feel like a rehearsal tool, not a productivity app.

The core design goal is to reduce decision fatigue while still forcing useful creative decisions.

The app should avoid overwhelming the user. It should give enough structure to start singing, then get out of the way.

