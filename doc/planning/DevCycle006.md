# DevCycle 006: Improve Chord Progression Steps

**Status:** Work Complete
**Start Date:** 2026-05-11
**Target Completion:** 2026-05-11
**Focus:** Make chord progression selection more musically intentional, then add an optional third rendered chord section transposed one whole step above the song key.

---

## Goal

Improve SongScaffold's chord progression workflow in two phases. First, implement the role-aware first and second chord progression selection described in `doc/planning/ideas/separate_chord_progressions_plan.md`. Second, add an optional "Chord Progression 3" result in the final Song Idea that reuses Chord Progression 1 but renders it in a key one full step above the selected song key.

This cycle should make the chord progression steps feel more musically purposeful while also adding a simple modulation-style output for writers who want an additional harmonic lift.

## Desired Outcome

- Chord Progression 1 and Chord Progression 2 no longer draw from one undifferentiated progression pool by default.
- Progressions are tagged by harmonic suitability, using behavior-oriented tags such as `OPEN`, `CADENTIAL`, `LIFT`, `LOOP`, `COLOR`, and `PIVOT`.
- `ChordMapper` renders every roman numeral used by the progression library, including uppercase `VI`.
- Manual selection, per-step randomization, and full Random Idea generation all use the same role-aware filtering.
- Existing style/category grouping remains available and distinct from suitability tags.
- Settings includes a persisted `Enable Chord Progression 3` toggle under Chord Progression Options.
- When enabled, the final Song Idea shows `Chords 3`, using Chord Progression 1 rendered in a key one whole step above the selected song key.
- Existing chord progression behavior remains predictable when the new setting is disabled.

---

## Tasks

### Phase 1: Role-Aware First and Second Chord Progressions

**Status:** Work Complete

- [x] Review and implement the recommendations in `doc/planning/ideas/separate_chord_progressions_plan.md`.
- [x] Treat this DevCycle document as the canonical implementation plan where it differs from older exploratory notes.
- [x] Add `VI` support to `ChordMapper` for every supported key before relying on `Major Six Turnaround`.
- [x] Add a suitability model for chord progressions, using harmonic-behavior tags rather than section-name tags.
- [x] Tag every existing progression intentionally; avoid leaving finished data with an empty suitability set.
- [x] Preserve the existing `category` field for style grouping.
- [x] Add shared repository accessors for first-step and second-step progression candidates.
- [x] Ensure the existing `disableTwoChordProgressions` setting applies to both first and second candidate pools.
- [x] Update manual Chord Progression 1 selection to use the first/open/setup-oriented pool.
- [x] Update manual Chord Progression 2 selection to use the second/payoff/contrast-oriented pool.
- [x] Update `Random Idea` generation to use the role-aware pools.
- [x] Update per-step randomization to use the role-aware pools.
- [x] Avoid selecting the exact same progression twice when possible.
- [x] Implement shared fallback helpers so manual/random candidate rules cannot drift.

**Technical Notes:**
Primary files are likely:

- `app/src/main/java/com/songscaffold/app/model/ChordProgression.kt`
- `app/src/main/java/com/songscaffold/app/data/PromptRepository.kt`
- `app/src/main/java/com/songscaffold/app/viewmodel/SongIdeaViewModel.kt`
- `app/src/main/java/com/songscaffold/app/MainActivity.kt`
- `app/src/main/java/com/songscaffold/app/ui/StepScreen.kt`

The recommended suitability tags from the planning discussion are:

- `OPEN`: questioning, suspended, unresolved, or setup-oriented
- `CADENTIAL`: strongly resolves or points toward tonic resolution
- `LIFT`: brightens, widens, or intensifies the section
- `LOOP`: works as a repeating harmonic bed
- `COLOR`: uses borrowed, modal, chromatic, or distinctive harmonic color
- `PIVOT`: useful for bridge-like contrast, redirection, or sectional turn

This is the canonical enum for implementation. Do not use the older exploratory `RESOLVED` or `BRIDGE` tags from prior planning notes. `RESOLVED` is too easily confused with strict final-chord resolution, and `BRIDGE` names a section rather than harmonic behavior.

The second progression pool should not mean "strictly resolved only." Some strong second-section progressions end on `IV` or `V` but still provide lift, momentum, or payoff. Suitability should describe musical function, not only final-chord resolution.

Selection fallback order should be explicit and shared:

1. For Chord Progression 2, prefer the second-step pool excluding Chord Progression 1.
2. If that is empty, use the second-step pool including Chord Progression 1.
3. If that is empty, use the combined available pool excluding Chord Progression 1.
4. If that is empty, use the combined available pool.
5. If that is empty, leave the current selection unchanged or null rather than throwing.

The same helper should be used by full Random Idea generation and per-step randomization.

### Phase 2: Optional Chord Progression 3 Summary Section

**Status:** Work Complete

- [x] Add `enableChordProgression3` or similarly named Boolean to `StepSettings`, defaulting to `false`.
- [x] Add a DataStore key for the new setting in `SettingsRepository`.
- [x] Persist the setting alongside the existing chord progression options.
- [x] Add a Settings screen toggle labeled `Enable Chord Progression 3` under `Chord Progression Options`.
- [x] Disable or clearly gate the toggle when Chord Progression is disabled, because Chord Progression 3 depends on Chord Progression 1.
- [x] Render a `Chords 3` row in the final Song Idea when the setting is enabled and Chord Progression 1 plus Song Key are available.
- [x] Compute `Chords 3` by rendering Chord Progression 1 in the key one full step higher than the selected song key.
- [x] Ensure the transposed key wraps correctly through the existing `PromptRepository.majorKeys` list.
- [x] Show the transposed key label alongside `Chords 3` if the row can stay compact.
- [x] Verify `Chords 3` updates correctly when the user changes Chord Progression 1 or Song Key.

**Technical Notes:**
Primary files are likely:

- `app/src/main/java/com/songscaffold/app/model/StepSettings.kt`
- `app/src/main/java/com/songscaffold/app/data/SettingsRepository.kt`
- `app/src/main/java/com/songscaffold/app/ui/SettingsScreen.kt`
- `app/src/main/java/com/songscaffold/app/model/SongIdea.kt`
- `app/src/main/java/com/songscaffold/app/viewmodel/SongIdeaViewModel.kt`
- `app/src/main/java/com/songscaffold/app/ui/SummaryScreen.kt`
- `app/src/main/java/com/songscaffold/app/music/ChordMapper.kt`
- `app/src/main/java/com/songscaffold/app/data/PromptRepository.kt`

Chord Progression 3 is not a new progression-selection step. It is a derived final-summary output:

- Source progression: `songIdea.chordProgression`
- Source key: `songIdea.songKey`
- Derived key: selected key transposed up one whole step
- Display: rendered chords for Chord Progression 1 in the derived key

This should be presented as a transposed restatement or modulation-style option, not as guaranteed harmonic payoff. If Chord Progression 1 is open or loop-oriented, `Chords 3` will still be open or loop-oriented, just rendered in the higher key.

Given the current major key list:

```text
C, Db, D, Eb, E, F, Gb, G, Ab, A, Bb, B
```

One full step higher means advancing two semitone positions in that list:

- `C` -> `D`
- `Db` -> `Eb`
- `D` -> `E`
- `E` -> `Gb`
- `Bb` -> `C`
- `B` -> `Db`

This feature depends on Song Key. If no song key is selected or rendered, the app should not show `Chords 3`. Use `ChordMapper.renderProgression(derivedKey, songIdea.chordProgression)` rather than parsing or transposing rendered chord-name strings.

### Phase 3: Verification

**Status:** Work Complete

- [x] Build with `.\gradlew.bat assembleDebug`.
- [ ] Manually verify first and second progression steps show their intended candidate pools.
- [ ] Manually verify Random Idea uses different first/second candidate pools.
- [ ] Manually verify per-step Random uses the correct pool for each progression step.
- [ ] Manually verify `Disable 2-chord chord progressions` still applies to both role-aware pools.
- [ ] Manually verify `Major Six Turnaround` renders uppercase `VI` as a real chord in multiple keys.
- [ ] Manually verify `Enable Chord Progression 3` appears under Chord Progression Options.
- [ ] Manually verify `Chords 3` appears only when enabled and the required Chord Progression 1 plus Song Key data exists.
- [ ] Manually verify `Chords 3` is one whole step above the selected key and wraps correctly at the end of the key list.
- [ ] Add focused unit tests if the project has or gains a suitable test harness for progression filtering or key transposition.

**Technical Notes:**
The highest-risk areas are duplicated candidate filtering, missing roman numeral mappings, and key transposition edge cases. Prefer shared helpers for progression selection and whole-step key transposition so manual and random flows do not drift.

---

## Open Questions

1. **Should the second progression pool be selected by broad suitability tags or by a fixed list of second-step tags?**
   Recommendation: Use fixed accessors that define the desired default tag mix. For example, first-step selection can prefer `OPEN`, `LOOP`, and some foundational `COLOR`, while second-step selection can prefer `CADENTIAL`, `LIFT`, `PIVOT`, and payoff-oriented `COLOR`.

2. **What should happen if Chord Progression 3 is enabled but Song Key is disabled?**
   Recommendation: Do not render `Chords 3`, and consider disabling the setting unless both Chord Progression and Song Key are enabled. The feature needs both a progression and a key to produce useful output.

3. **Should Chord Progression 3 be stored in `SongIdea` or computed directly in `SummaryScreen`?**
   Recommendation: Store derived rendered chords or a derived key in `SongIdea` only if the viewmodel already owns chord rendering consistency. Otherwise, compute through a shared helper and keep the summary simple. Avoid duplicating render logic in the UI.

---

## Notes and Risks

- Suitability tags introduce a second classification dimension. Keep category and suitability distinct.
- The term "resolved" should be avoided in implementation names; not every payoff or chorus-friendly progression ends on tonic.
- All existing progressions should receive at least one suitability tag before implementation is considered complete.
- `Major Six Turnaround` uses uppercase `VI`; `ChordMapper` must support it in every key before the progression is user-facing.
- `Enable Chord Progression 3` depends on both Chord Progression 1 and Song Key. The UI should avoid showing an empty or misleading `Chords 3` row.
- Whole-step transposition should follow the app's supported major-key list rather than ad hoc string manipulation.
- Existing source data includes Unicode accidentals and some encoding artifacts. Transposition should rely on `ChordMapper` and key-list indexing, not text parsing of rendered chord names.
- Phase 2 does not always produce harmonic payoff; it produces a higher-key restatement of Chord Progression 1.

---

## Claude's Comments

### `VI` is missing from ChordMapper and will silently fail

The "Major Six Turnaround" added to `PromptRepository` today uses the roman numeral `VI` (uppercase — major chord on the 6th degree). `ChordMapper.kt` does not have a mapping for `VI` in any of its 12 key maps. When this progression is rendered, every `VI` will fall through to the raw string `"VI"` rather than the actual chord name. This needs to be added to all 12 key entries in `ChordMapper` before this progression produces usable output. In C, `VI` = A major; in G, `VI` = E major; and so on — one whole step above the diatonic `vi` minor.

### The suitability tag enum has two conflicting versions

The plan document's enum includes `RESOLVED` and `BRIDGE`. The DevCycle technical notes replace both with `CADENTIAL` and `PIVOT` respectively, which is the right call. Before implementation, pick one canonical enum and make sure neither document's version is used as a reference during coding. The DevCycle version (`OPEN`, `CADENTIAL`, `LIFT`, `LOOP`, `COLOR`, `PIVOT`) is the one to use.

### "Avoid selecting the same progression twice" needs a defined fallback order

The task list mentions this but doesn't specify what happens when it can't be honoured — for example, if both pools contain only one progression after the two-chord filter is applied, or if Chord Progression 1 was selected from a pool that overlaps heavily with Chord Progression 2's pool. The recommended fallback from the plan document is "fall back to the combined available pool." This should be written as a shared helper with explicit precedence: (1) prefer second pool excluding Chord Progression 1; (2) if that's empty, use second pool including it; (3) if that's still empty, use combined pool excluding Chord Progression 1. Leaving this implicit risks the two random flows (per-step and full Random Idea) implementing it differently.

### Phase 2's musical premise may conflict with Phase 1's OPEN pool

Phase 1 steers Chord Progression 1 toward `OPEN` and `LOOP` progressions — ones that withhold resolution. Phase 2 takes Chord Progression 1 and presents it transposed up a whole step as a "harmonic lift." But an unresolved, questioning progression transposed up a step is still an unresolved, questioning progression — just higher. The "lift" effect in modulation usually comes from resolution landing somewhere new, not from restating an open loop at a different pitch. This may not matter in practice — the feature is lightweight and users can choose any Chord Progression 1 — but it is worth knowing that the two phases don't always combine cleanly. If Phase 1 produces an OPEN Chord Progression 1, Chords 3 will be an open loop in a higher key, not a payoff.

### ChordMapper is well-suited for Phase 2 key transposition

`ChordMapper.renderProgression(key, progression)` already handles all 12 keys and all roman numerals in current use. Phase 2 can be implemented by indexing `PromptRepository.majorKeys` to find the key two positions ahead (wrapping at the end), then calling `renderProgression` with that derived key. No new parsing or string manipulation is needed. This is the right approach and the existing structure supports it cleanly.

---

## Claude Code Review

### Separator inconsistency in SummaryScreen

`SummaryScreen.kt` lines 127–130 join rendered chords using `" – "` (en dash) for Chords 1 and Chords 2. Line 139 joins Chords 3 using `" - "` (hyphen). These will look visually different on screen. The Chords 3 line should use `" – "` to match.

### `supportedMajorKeys` in ChordMapper duplicates `PromptRepository.majorKeys`

`ChordMapper` declares its own private `supportedMajorKeys` list rather than referencing `PromptRepository.majorKeys`. Both lists are identical and in the same order. If the supported key set ever changes in one place, the other could silently go out of sync and produce incorrect transpositions. `ChordMapper` could instead accept a key list as a parameter or reference the repository list directly — though given the app's current scope the duplication is low risk, it is worth being aware of.

### Pool overlap is wider than it may appear

`COLOR` appears in both `firstProgressionSuitability` and `secondProgressionSuitability`, and many progressions carry a COLOR tag. The practical result is that 27 of 33 progressions qualify for the first pool and 30 of 33 qualify for the second. A user selecting manually will see nearly identical lists for both steps. The bias is musically real — Classic Cadence, Two Five One, Plagal Loop, Minor To Resolution, Axis Variant, and Deceptive Cycle are correctly excluded from the first pool, and the three pure loop progressions are excluded from the second — but the differentiation is subtler than the planning documents implied. This is not a bug, and the "bias not a rule" framing from the plan document accurately describes what was built. Worth knowing if the pools feel underdifferentiated in use.

### Second Chord Progression step description was not updated

The DevCycle plan noted that the second progression step description could be changed from `"Pick a second chord progression (e.g. for the chorus)."` to something more intentional like `"Pick a contrasting progression for the next section."` The original text remains in `StepScreen.kt` line 442. This is a minor missed item — not a bug, but an easy follow-up if the "for the chorus" framing feels too narrow.

### `suitability` field has no default value — this is correct

`ChordProgression.suitability` is declared as `Set<ChordProgressionSuitability>` with no default. This means every progression must provide tags at construction time and there is no way to accidentally create an untagged progression. This directly addresses the risk flagged in the planning discussion and is the right design choice.

### Fallback chain is correctly implemented

`PromptRepository.secondChordProgressionCandidates` implements the four-step fallback priority in the correct order: second pool excluding first, second pool including first, combined pool excluding first, combined pool including first. The same candidates are used by both per-step randomization (`applyRandom` in `MainActivity`) and full randomization (`randomizeAll` in `SongIdeaViewModel`). The shared-helper requirement from the plan is satisfied.

### `ChordMapper.keyOneWholeStepHigher` and `renderProgressionOneWholeStepHigher` are clean and independently testable

Both functions are pure: they take explicit inputs and return values without side effects. `keyOneWholeStepHigher` uses modulo wrapping correctly and handles an unknown key by returning null rather than throwing. The null is propagated through `renderProgressionOneWholeStepHigher` and handled gracefully in `SummaryScreen`. The Phase 3 manual verification checklist covers the wrap-around case; these functions are also good candidates for unit tests if a test harness is added.

### `VI` mappings verified

All 12 keys now include `"VI"` and the values are correct: `VI` in each key maps to the major chord whose root is the same as the diatonic `vi` minor (e.g. C → A, G → E, Bb → G). The Major Six Turnaround will now render correctly.

---

## Completion Summary

*Fill in when the cycle closes. Move this document to `doc/planning/completed/` afterward.*

**Completion Date:** 2026-05-11
**Phases Completed:** All implementation phases
**Work Deferred:** Manual device/UI verification remains pending.

**Accomplishments:**
- Added suitability tags and role-aware first/second chord progression candidate pools.
- Added shared fallback behavior for second progression randomization.
- Added `VI` rendering support to `ChordMapper`.
- Added persisted `Enable Chord Progression 3` setting.
- Added `Chords 3` summary output using Chord Progression 1 rendered one whole step above the selected song key.

**Metrics:**
- Files modified: 11
- Build/test status: `.\gradlew.bat assembleDebug` passed

**Lessons / Notes:**
Suitability tags are now behavior-oriented rather than section-oriented. `Chords 3` is implemented as a higher-key restatement, not as a guaranteed harmonic payoff.
Claude review follow-up: matched the `Chords 3` separator to the existing chord rows and updated the second progression step copy to emphasize contrast rather than chorus-only framing.
Pool differentiation follow-up: `COLOR` remains a descriptive suitability tag, but it no longer qualifies a progression for either the first or second progression pool by itself.


