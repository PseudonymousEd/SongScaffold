# DevCycle 009: New I–V Chord Progressions

**Status:** Work Complete
**Start Date:** 2026-05-31
**Target Completion:** TBD
**Focus:** Add 11 new I–V chord progressions and support for first-inversion I/3 chords.

---

## Goal

Expand the chord progression library with 11 new progressions identified in `doc/planning/ideas/I_to_V_progressions.md`. All 11 start on I and end on V, adding more variety to the open/lift/pivot end of the pool. One of the new progressions (`Love is an Open Door`) uses a first-inversion I chord (`I/3`), which the `ChordMapper` does not currently support, so that mapping must be added across all 12 keys first.

## Desired Outcome

- All 11 new progressions appear in the chord progression selection step.
- `I/3` renders correctly as a slash chord (e.g. `C/E` in C, `D/F#` in D) in all 12 keys.
- Existing progressions and all other app behaviour are unchanged.

---

## Tasks

### Phase 1: Add I/3 to ChordMapper

**Status:** Work Complete

- [x] Add `"I/3"` entry to all 12 key maps in `ChordMapper.kt`.

**Technical Notes:**
`I/3` is the tonic triad in first inversion — root chord with the major third in the bass. Mappings:

| Key | I/3    |
|-----|--------|
| C   | C/E    |
| Db  | Db/F   |
| D   | D/F#   |
| Eb  | Eb/G   |
| E   | E/G#   |
| F   | F/A    |
| Gb  | Gb/Bb  |
| G   | G/B    |
| Ab  | Ab/C   |
| A   | A/C#   |
| Bb  | Bb/D   |
| B   | B/D#   |

File: `app/src/main/java/com/songscaffold/app/music/ChordMapper.kt`

### Phase 2: Add New Progressions to PromptRepository

**Status:** Work Complete

- [x] Add all 11 new chord progressions to the `chordProgressions` list in `PromptRepository.kt`.

**Technical Notes:**
All 11 progressions to add, in the order they appear in the candidates table:

| Name | Category | Roman Numerals | Suitability |
|---|---|---|---|
| Easy | Musical Theatre / Jazz | I, iii, ii, V | OPEN, LIFT, PIVOT |
| Mostly Me | Classic / Standard | I, IV, ii, V | LIFT, CADENTIAL |
| Love is an Open Door | Classic / Standard | I, I/3, IV, V | OPEN |
| Stepwise Lift | Classic / Standard | I, ii, IV, V | LIFT, OPEN |
| Sal Tlay | Cinematic / Modern | I, iii, IV, V | LIFT, OPEN, COLOR |
| Who Knew | Expressive / Color | I, ii, vi, V | OPEN, PIVOT, COLOR |
| Thirds Descent | Expressive / Color | I, iii, vi, V | OPEN, COLOR, PIVOT |
| More Than a Feeling | Classic / Standard | I, IV, vi, V | LIFT, LOOP, OPEN |
| Ascending Walk | Classic / Standard | I, ii, iii, V | LIFT, OPEN |
| Four Three Drop | Expressive / Color | I, IV, iii, V | COLOR, PIVOT |
| Life Would Suck | Expressive / Color | I, vi, iii, V | OPEN, COLOR, LOOP |

File: `app/src/main/java/com/songscaffold/app/data/PromptRepository.kt`

### Phase 3: Build and Verify

**Status:** In Progress

- [x] Run `.\gradlew.bat assembleDebug` — BUILD SUCCESSFUL in 25s.
- [ ] Install on device/emulator and navigate to the chord progression step.
- [ ] Confirm all 11 new progressions appear in the list.
- [ ] Select `Love is an Open Door` with a key (e.g. D) and confirm `D/F#` renders on the summary screen.

---

## Notes and Risks

- No new files, no architecture changes — this is purely additive data work in two files.
- `I/3` is only used by `Love is an Open Door` right now, but once added to `ChordMapper` it is available for any future progression.

---

## Completion Summary

*Fill in when the cycle closes. Move this document to `doc/planning/completed/` afterward.*

**Completion Date:** [YYYY-MM-DD]
**Phases Completed:** [List or "All"]
**Work Deferred:** [What was not done and why, or "None"]

**Accomplishments:**
- [What was built or changed]

**Metrics:**
- Files modified: 2
- Build/test status: [Command and result]

**Lessons / Notes:**
[Anything worth noting for future data-expansion cycles.]
