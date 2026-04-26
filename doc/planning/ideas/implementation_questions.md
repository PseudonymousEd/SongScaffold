# SongScaffold Android - Implementation Questions

## Project Setup

1. Is there an existing Android project to build into, or are we starting from scratch?
Starting from scratch
2. If starting from scratch, what package name should be used? The design doc suggests `com.example.songconstraints`, but `com.example.songscaffold` may be more consistent with the app name.
   - Should the package use a final app-style namespace such as `com.songscaffold.app` instead of an example namespace?
com.songscaffold.app
3. What minimum SDK version should we target? (API 26 / Android 8.0 is a common safe floor.)
API 26 sounds good
4. Should the Gradle build files use Kotlin DSL (`build.gradle.kts`) or Groovy (`build.gradle`)?
   Kotlin DSL for Gradle.

---

## UI Approach

5. For steps with many options (e.g., Point of View has 7, Rhyme Scheme has 5), should the options be displayed as:
   - A scrollable list with radio buttons
   - Selectable cards
   - Chips
   - Some other pattern
scrollable list with radio buttons

6. For the Topic step, the design doc recommends a `Random Topic` button that picks one item and displays it. Is that the full intended UI, or should there also be a way to browse or scroll the topic list manually?
Pick a random topic. The user may have the app select another topic multiple times until one is chosen

7. Should the `Back` button be available on every step screen, or only starting from the second step?
Every Step screen

---

## Visual Style

8. Is there a preferred color scheme or visual direction (e.g., dark mode by default, a particular accent color)? Or should we use Material 3 defaults?
Accent color blue, dark mode by default
---

## Content Clarification

9. The chord progressions list in the design doc includes `"Minor Pop Loop"` twice - once under `Classic / Standard` and once under `Loops`, both with roman numerals `vi, IV, I, V`. Is this intentional (same progression appearing in two categories), or a copy error that should be resolved?
It is a copy error that should be resolved

10. For skipped steps in the Summary, should the app show `Skipped`, or omit skipped steps entirely?
Omit skipped steps entirely


11. Should `Song Key` always appear as its own enabled step, or should it only be shown when a chord progression has been selected?
Only appear if a chord progression has been selected


12. If the user skips either `Chord Progression` or `Song Key`, should the Summary omit only rendered chords, or also hide the incomplete chord/key fields?
Omit just the rendered chords
---

## Polish / Product Feel

13. Should the first implementation prioritize a plain functional Compose UI, or should visual polish be part of the initial version so it feels like a rehearsal tool from the start?
Visual Polish should be part of the initial version