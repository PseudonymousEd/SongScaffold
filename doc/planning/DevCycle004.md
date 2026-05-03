# DevCycle 004: Expanded Chord Progression Library + Summary Name Display

**Status:** Work Complete
**Start Date:** 2026-05-02
**Target Completion:** 2026-05-02
**Focus:** Add more chord progressions to the existing library, and show the progression name on the Summary screen.

---

## Goal

Expand the chord progression library in `PromptRepository` to give users more variety when stepping through or randomizing a song idea. The additions extend every existing category and introduce a new "Cinematic / Modern" category. Additionally, the Summary screen is updated to show the progression's name alongside its roman numeral pattern.

## Desired Outcome

- The chord progression list grows from 13 to 32 entries.
- Each existing category (Classic / Standard, Musical Theatre / Jazz, Expressive / Color, Loops) receives four additional progressions.
- A new "Cinematic / Modern" category is added with four progressions.
- The Summary screen shows the chord progression name (e.g. "Classic Cadence") in the "Chord Progression" row, with the roman numeral pattern in a new "Pattern" row below it.
- The same applies to the second chord progression when present ("Chord Prog. 2" / "Pattern 2").

---

## Tasks

- [x] Add four progressions to "Classic / Standard": 50s Progression, Plagal Loop, Descending Bass Line, Axis Variant
- [x] Add four progressions to "Musical Theatre / Jazz": Backdoor Resolution, Rhythm Changes, Minor Two Five One, Chromatic Walk-Up
- [x] Add four progressions to "Expressive / Color": Line Cliche Major, Mixolydian Variant, Chromatic Mediants, Deceptive Cycle
- [x] Add four progressions to "Loops": Drone Loop, Suspended Loop, Minor Oscillation, Pedal Loop
- [x] Add new "Cinematic / Modern" category with four progressions: Epic Rise, Modern Film Loop, Lydian Lift, Ambiguous Loop
- [x] Update `SummaryScreen` to display `it.name` in the "Chord Progression" / "Chord Prog. 1" row and `it.romanDisplay` in a new "Pattern" / "Pattern 1" row beneath it
- [x] Apply the same name + pattern display to the second chord progression ("Chord Prog. 2" / "Pattern 2")
- [x] Expand `ChordMapper` keyMap with all roman numeral tokens introduced by DC4 progressions: `i`, `♭VI`, `♭III`, `III`, `II`, `Imaj7`, `V7`, `Vsus4`, `V/7`, `I#dim`, `iiø`, `ii/I`, `IV/I`, `V/I`
- [x] Reinterpret `I7` in `ChordMapper` as dominant 7th (e.g. G7) and add `Imaj7` for the major 7th (e.g. Gmaj7) — these were previously conflated
- [x] Update "Borrowed Minor Four" in `PromptRepository` to use `Imaj7` instead of `I7` to match the corrected symbol meaning
- [x] When randomizing the 2nd chord progression (both in `randomizeAll` and in the per-step random button), exclude the 1st chord progression from the candidate pool so the two progressions are always different

---

## Files Modified

- `app/src/main/java/com/songscaffold/app/data/PromptRepository.kt`
- `app/src/main/java/com/songscaffold/app/ui/SummaryScreen.kt`
- `app/src/main/java/com/songscaffold/app/music/ChordMapper.kt`

---

## Completion Summary

**Completion Date:** 2026-05-02
**Phases Completed:** All
**Work Deferred:** None

**Accomplishments:**
- Chord progression library expanded from 13 to 32 entries
- Added 4 progressions per existing category
- Introduced "Cinematic / Modern" as a fifth category
- Summary screen now shows the progression name and its roman numeral pattern as separate rows
- `ChordMapper` expanded to cover all 24 roman numeral tokens used across all progressions
- Fixed `I7` / `Imaj7` symbol conflict; corrected "Borrowed Minor Four" accordingly
- Random 2nd chord progression always differs from the 1st

**Metrics:**
- Files modified: 3
