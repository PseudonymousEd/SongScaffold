# Separate Lists for First and Second Chord Progressions

## Context

SongScaffold currently has separate steps for a first chord progression and an optional second chord progression, but both steps draw from the same shared progression list in `PromptRepository.kt`.

That works mechanically, but it treats the two progression slots as interchangeable. From a songwriting and music theory perspective, they probably are not interchangeable. The first progression usually establishes the song's harmonic home, while the second progression often creates contrast, lift, release, or a different section identity.

This document investigates whether SongScaffold should keep separate progression pools for the first and second chord progression steps.

## Short Recommendation

Separate lists are a good idea.

The first chord progression list should favor clear, stable, broadly usable harmonic foundations. The second chord progression list should favor progressions that create section contrast: chorus lift, bridge color, modal shift, harmonic intensification, or a return path back into the main progression.

This would make the app's suggestions feel more like song structure guidance and less like drawing two unrelated items from the same bag.

## Why the Two Slots Mean Different Things

In common songwriting practice, the first chord progression is usually the listener's first harmonic frame. It tells the ear where "home" is, how stable the song feels, and what kind of melodic behavior will make sense over it.

The second chord progression is usually not just "another progression." It often implies a new section:

- A chorus that opens up after a verse
- A pre-chorus that increases harmonic tension
- A bridge that changes perspective
- A refrain that simplifies the harmonic world
- A contrasting loop for an instrumental or outro

Because those roles are different, the app can make better suggestions if it distinguishes between "good starting harmonic worlds" and "good contrasting harmonic moves."

## Music Theory Rationale

### 1. First Progressions Should Establish Tonal Gravity

A first progression often benefits from being easy to hear as a home base. Progressions like `I - IV - V - I`, `I - V - vi - IV`, `vi - IV - I - V`, and `I - vi - IV - V` clearly define a tonal center and give the writer a stable grid for melody and lyrics.

These are strong first-step candidates because they answer basic musical questions quickly:

- Where is tonic?
- Is the mood major, minor, or ambiguous?
- Does the loop resolve or keep cycling?
- Does the progression support simple melodic entry?

If the first progression is too colorful too early, the writer may get a striking idea, but they may also get less structural clarity.

### 2. Second Progressions Should Create Contrast

A second progression is valuable when it changes the energy of the song without making the song feel unrelated. That contrast can happen in several ways:

- Starting on a different functional area, such as `IV`, `vi`, or `ii`
- Using borrowed chords like `iv`, `bVII`, or `bVI`
- Increasing dominant pull with `ii - V - I` or secondary dominants
- Simplifying into a two-chord loop for release
- Moving into a cinematic or modal color

The second progression can afford to be more characterful because the first progression has already established the listener's reference point.

### 3. The Verse and Chorus Often Need Different Harmonic Jobs

A verse progression often supports information: narrative, image, setup, and emotional interiority. It can be stable, repetitive, restrained, or unresolved.

A chorus progression often supports arrival: summary, hook, emotional projection, and memorability. It may need stronger resolution, wider harmonic motion, or a brighter starting point.

That does not mean the first progression is always a verse and the second is always a chorus, but the mental model is useful. If the app labels the second step as "for the chorus" in the UI, then the second progression pool should lean into progressions that actually feel useful for chorus contrast.

## Possible List Strategy

### First Chord Progression Pool

The first pool should include progressions that are strong foundations:

- Classic cadences and common pop loops
- Clear tonic-centered major progressions
- Clear tonic-centered minor progressions
- Simple loops that are easy to write over
- Progressions with moderate color but low confusion

Examples from the current list that feel especially suitable:

- `Classic Cadence`: `I - IV - V - I`
- `Pop Axis`: `I - V - vi - IV`
- `Minor Pop Loop`: `vi - IV - I - V`
- `50s Progression`: `I - vi - IV - V`
- `Circle Turnaround`: `I - vi - ii - V`
- `Two Chord Open Loop`: `I - IV`

### Second Chord Progression Pool

The second pool should include progressions that produce contrast, lift, or section identity:

- Chorus-friendly inversions or variants of the first pool
- Strong cadential motion
- Borrowed-chord color
- Modal color
- Cinematic lift
- More theatrical or bridge-like movement
- Pedal or drone loops that change the texture

Examples from the current list that feel especially suitable:

- `Plagal Loop`: `IV - I - V - I`
- `Secondary Dominant Lift`: `I - V/vi - vi - IV`
- `Major To Minor Four`: `I - IV - iv - I`
- `Backdoor Resolution`: `ii - bVII - I`
- `Line Cliche Major`: `I - Imaj7 - I7 - IV`
- `Epic Rise`: `vi - IV - I - V`
- `Modern Film Loop`: `i - bVI - III - bVII`
- `Lydian Lift`: `I - II - IV - I`

Some progressions may reasonably belong in both lists. For example, `I - V - vi - IV` can be a verse loop, a chorus loop, or the whole song. The lists do not have to be mutually exclusive unless the product wants sharper contrast.

## Design Options

### Option A: Two Hard-Coded Lists

Create `firstChordProgressions` and `secondChordProgressions` in `PromptRepository.kt`.

This is simple and easy to reason about. It also matches the app's current repository style.

Potential downside: the same progression must be duplicated if it belongs in both pools.

### Option B: Add Role Tags to Each Progression

Extend the model so each progression can be tagged for one or more roles:

- `FIRST`
- `SECOND`
- `VERSE`
- `CHORUS`
- `BRIDGE`
- `LOOP`
- `COLOR`

This is more flexible and avoids duplication, but it adds model complexity.

This may be overbuilt for the current app unless we expect more section-aware features later.

### Option C: Keep One List, Add Second-Step Weighting

Keep one list but choose second progressions using weighting rules. For example, the second step could prefer progressions in `Expressive / Color`, `Cinematic / Modern`, or specific chorus-friendly patterns.

This preserves the current data structure but hides important behavior in selection logic. It is probably less clear than separate lists.

## Recommended Product Behavior

For manual selection:

- The first chord progression step should show the first progression pool.
- The second chord progression step should show the second progression pool.
- If a progression exists in both pools, selecting it twice could still be allowed, but random selection should avoid repeating the same progression when possible.

For random generation:

- First progression should randomize from the first pool.
- Second progression should randomize from the second pool.
- If the second pool is empty after filters, fall back to the combined pool.
- The existing "Disable 2-chord chord progressions" setting should apply to both pools.

For display:

- Keep the current summary labels: "Chord Prog. 1" and "Chord Prog. 2" when both are present.
- Consider whether the second step description should say "Pick a contrasting progression" instead of only "Pick a second chord progression."

## Open Questions

- Should the second progression list be explicitly chorus-oriented, or should it cover any contrasting section?
- Should the app avoid second progressions in the same category as the first, or is that too restrictive?
- Should some progressions appear in both lists?
- Should the app eventually support named song sections such as verse, chorus, bridge, and pre-chorus?
- Should chord progression categories be musical style categories, functional roles, or both?

## Conclusion

Separate first and second chord progression lists would make the app more musically intentional. The first list can help the writer establish a clear harmonic foundation. The second list can help the writer introduce contrast, lift, or color at the moment when a song usually needs a new section identity.

This is a small product concept with a strong music theory basis: harmonic progressions do not only define chords, they define formal function. Treating the first and second progression steps as different musical roles would make SongScaffold's prompts more useful and more songwriter-aware.

---

## Claude's Response

The core recommendation here is right and I agree with it. The music theory reasoning is sound, the pool examples are well-chosen, and the three design options are the obvious ones. This document is more thorough on the product behaviour side than my own — the note about random selection avoiding repeats, and the fallback behaviour if the second pool is empty after filtering, are both details worth keeping.

A few places where I'd push back or add nuance:

**The "first = stable foundation" framing may be too prescriptive.** Codex frames it as: first progression establishes tonal gravity, second creates contrast. That implies the first progression should lean conservative. But SongScaffold is a constraint generator for improvisation, not a song structure sequencer. A user might pick a cinematic loop as their "first" and want something more grounded as the second. The musical truth isn't really about position — it's about the relationship between the two. Open/questioning and resolved/declarative might be a more durable axis than first/second. Worth considering whether the pools are labelled by position or by harmonic character.

**The verse/chorus mental model is useful but leaks in.** Codex says "that does not mean the first progression is always a verse and the second is always a chorus, but the mental model is useful." I'd go further: it may actively mislead some users. SongScaffold's users are improvisers, not all of whom think in song sections. If the UI ever describes the second step as "for the chorus," it should be opt-in framing rather than the default.

**Option A is the right starting point.** Agreed. The role-tag approach (Option B) is the better long-term model — it avoids duplication and is more honest about what the data represents — but it requires going back and tagging every existing progression, which is non-trivial. Two lists now, refactor to tags later is the pragmatic path.

**Option C should be ruled out explicitly.** Weighting by category hides the musical logic in selection code where no one will find it. I'd drop it from the document as a live option to avoid confusion — it reads as a hedge when the real answer is clearly one of the first two.

**One open question to add:** Should the user be able to disable the separation — i.e., opt back into the current behaviour where both steps draw from the full pool? Some users may have a reason to want two verse-style loops or two chorus-style progressions. A settings toggle for "use separate pools" would be consistent with how the app handles two-chord progressions already.
