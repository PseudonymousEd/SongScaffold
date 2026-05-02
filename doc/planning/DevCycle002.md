# DevCycle 002: Rhyme Word Step

**Status:** Work Complete
**Start Date:** 2026-04-30
**Target Completion:** TBD
**Focus:** Add a "Rhyme Word" step as step 2 in the SongScaffold flow, with random word generation seeded from a curated word list.

---

## Goal

Insert a new Rhyme Word step immediately after the Topic step, giving users a word to anchor their rhyming scheme before moving on to performance and musical choices. Like the Topic step, the user can tap a button to generate a random word and re-randomize as many times as they like — there is no selectable list to browse.

## Desired Outcome

- A "Rhyme Word" step appears as step 2 in the enabled step sequence (after Topic, before Point of View).
- Users can tap "Random Word" to get a randomly selected word from the seed list, and re-tap to get a different one.
- The selected rhyme word appears on the Summary screen.
- The step can be toggled on/off in Settings like every other step.
- All downstream step numbers shift correctly (Point of View becomes step 3, etc.).

---

## Tasks

### Phase 1: Data and Models

**Status:** Work Complete

- [ ] Add `RHYME_WORD` to `SongStep` enum, inserted between `TOPIC` and `POINT_OF_VIEW`
- [ ] Add `rhymeWord: String? = null` field to `SongIdea`
- [ ] Add `rhymeWordEnabled: Boolean = true` to `StepSettings`
- [ ] Add `rhymeWords: List<String>` to `PromptRepository`, seeded from the word list below
- [ ] Add DataStore key for `rhymeWordEnabled` in `SettingsRepository`

**Rhyme Word Seed List (222 words):**

All words are 1 syllable. Store as a flat `List<String>` in `PromptRepository`:

light, rain, blue, time, day, night, song, heart, face, hand, star, door, tree, fire, road, wave, moon, sun, sky, wind, fall, love, dream, high, cry, rise, shine, fine, mine, line, stay, play, pray, way, bright, might, fight, right, sight, white, game, flame, name, came, pain, gain, chain, train, break, shake, wake, make, take, fly, die, try, lie, buy, ride, hide, wide, side, pride, stone, bone, lone, moan, phone, tone, zone, call, hall, tall, wall, ball, cold, bold, hold, gold, told, old, ground, sound, found, round, bound, word, heard, burn, turn, learn, yearn, gone, on, run, done, one, fun, gun, sing, ring, bring, thing, king, spring, sting, swing, back, track, crack, black, red, bed, head, dead, bread, led, said, thread, grace, chase, race, pace, trust, dust, rust, must, glow, flow, show, know, grow, slow, blow, throw, sweet, meet, feet, beat, heat, seat, street, deep, keep, sleep, weep, creep, well, bell, sell, tell, fell, spell, shell, yell, still, fill, hill, will, chill, thrill, spill, kill, sail, tail, trail, fail, jail, pale, tale, sale, free, sea, me, be, key, see, flee, knee, raw, draw, saw, claw, thaw, jaw, law, flaw, more, floor, grease, cease, share, wear, yay, scare, rest, ed, cool, rule, earn, meow, now, how, stop, top, fee, wealth, health, guy, born, shorn, eye, kid, did, war, hour, here, dear, brain, spine, voice, choice

**Technical Notes:**
`RHYME_WORD` must be inserted at enum position 2 (after `TOPIC`, before `POINT_OF_VIEW`). The `SUMMARY` sentinel at the end is unaffected. `SongIdea` is a data class — adding `rhymeWord` is a non-breaking addition with a `null` default. The DataStore key should be named `"rhyme_word_enabled"` to match the existing naming convention.

---

### Phase 2: ViewModel

**Status:** Work Complete

- [ ] Add `setRhymeWord(word: String)` method to `SongIdeaViewModel`
- [ ] Update `SongIdeaViewModel` step sequence builder to insert `RHYME_WORD` when `rhymeWordEnabled` is true
- [ ] Expose `fun randomRhymeWord(): String` on `SongIdeaViewModel` (delegates to `PromptRepository`)

**Technical Notes:**
The step sequence builder in `SongIdeaViewModel` already dynamically includes/excludes steps based on `StepSettings`. Adding `RHYME_WORD` follows the same pattern as the other non-conditional steps. `randomRhymeWord()` should draw from the full combined pool (same pattern as `randomTopic()`).

---

### Phase 3: UI

**Status:** Work Complete

- [ ] Build `RhymeWordStepContent` composable — displays the current rhyme word (or a prompt to tap Random) with a "Random Word" button that re-randomizes; no browse list
- [ ] Wire `RhymeWordStepContent` into `StepScreen`'s step type dispatch (alongside the existing `TOPIC` case)
- [ ] Update `SummaryScreen` to display the rhyme word when set (label: "Rhyme Word")
- [ ] Update `SettingsScreen` to include a toggle row for "Rhyme Word" (positioned after the Topic toggle)

**Technical Notes:**
`RhymeWordStepContent` should mirror the structure of `TopicStepContent`: show the word in a large, readable style with a category-style label if useful (or none, since all entries are from a single pool). The "Random Word" button triggers `viewModel.randomRhymeWord()` and calls `viewModel.setRhymeWord(word)` with the result. The step description can read: "Pick a word your song should rhyme with."

---

## Open Questions

1. **Should the rhyme word display any metadata (e.g., part of speech, category)?**
   Recommendation: No — keep it as plain as the Topic step's "random word" display. A category label can always be added later if the word list is organized into groups.

2. **Should "Random Word" be pre-called on step entry so the user always has a starting word?**
   Recommendation: Yes, for consistency with the Topic step — auto-randomize on first entry so the user can tap Next immediately if the first word works.

---

## Notes and Risks

- Inserting `RHYME_WORD` into the `SongStep` enum changes the ordinal values of all subsequent steps. If any code depends on ordinal values (e.g., serialized step indices in DataStore or navigation routes using `step/{stepIndex}`), those must be verified. The current nav route uses index into the *enabled* step list derived at runtime, not the enum ordinal — so this should be safe.

---

## Completion Summary

*Fill in when the cycle closes. Move this document to `doc/planning/completed/` afterward.*

**Completion Date:** 2026-04-30
**Phases Completed:** All (1–3)
**Work Deferred:** None

**Accomplishments:**
- Added `RHYME_WORD` to `SongStep` enum (position 2, between `TOPIC` and `POINT_OF_VIEW`)
- Added `rhymeWord: String?` field to `SongIdea`
- Added `rhymeWordEnabled: Boolean` to `StepSettings`
- Added 222-word `rhymeWords` list to `PromptRepository`
- Added `"rhyme_word"` DataStore key to `SettingsRepository`
- Added `setRhymeWord` and `randomRhymeWord` to `SongIdeaViewModel`; `RHYME_WORD` inserted in step sequence builder
- Built `RhymeWordStepContent` composable with auto-randomize on first entry and "New Random Word" re-randomization
- Wired `RhymeWordStepContent` into `StepScreen` dispatch; bottom Random button suppressed for `RHYME_WORD` (matches Topic pattern)
- Added "Rhyme Word" to `SummaryScreen` (displayed between Topic and Point of View sections)
- Added "Rhyme Word" toggle to `SettingsScreen` (after Topic)
- Wired `rhymeWord` and `onRhymeWordRandom` through `MainActivity`

**Metrics:**
- Files modified: 10 (SongStep, SongIdea, StepSettings, PromptRepository, SettingsRepository, SongIdeaViewModel, StepScreen, SummaryScreen, SettingsScreen, MainActivity)

**Lessons / Notes:**
- Auto-randomize on step entry uses `LaunchedEffect(Unit)` with a `word == null` guard, so re-entering the step (via Back) preserves the previously selected word.
