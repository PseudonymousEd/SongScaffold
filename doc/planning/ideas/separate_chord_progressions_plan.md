# Separate Chord Progression Lists Plan

## Summary

SongScaffold currently supports a first chord progression step and an optional second chord progression step, but both draw from the same shared pool in `PromptRepository.kt`.

The app would be more musically useful if those two steps drew from different harmonic roles by default. The first progression should usually establish an open, questioning, or foundational harmonic world. The second progression should usually provide answer, lift, arrival, or contrast.

This should be treated as a songwriting bias, not a hard rule. The goal is not to force every song into verse/chorus form. The goal is to help the two chosen progressions relate to each other in a more intentional way.

## Core Musical Idea

The strongest framing is not simply "first progression" and "second progression." It is:

- **Open / questioning / setup**
- **Resolved / declarative / payoff**

This is related to the classical antecedent/consequent idea: one phrase or section poses a question, and the next answers it. The same principle can work at the level of song sections. A progression that withholds resolution can make a later progression feel earned. A progression that lands clearly can make a hook or refrain feel more decisive.

SongScaffold does not need to expose this theory heavily in the UI. But internally, this is a better model than drawing two unrelated progressions from one undifferentiated list.

## Why This Helps

### Better musical contrast

Two progressions from the same pool can accidentally serve the same function. The result may be two resolving sections back to back, or two open loops that never land.

Separate harmonic roles make it easier for the app to suggest a useful before-and-after relationship.

### Better songwriting constraints

SongScaffold is a constraint generator. Good constraints should create direction. If the first progression suggests setup and the second suggests payoff, the writer gets an implied arc to write into.

### Better second-step meaning

The second chord progression step should not feel like "pick another one." It should feel like "shape the next section." That makes the optional second progression more valuable.

## Recommended Model

Use role or suitability tags on chord progressions.

Each `ChordProgression` should be able to carry one or more suitability tags. A progression may belong to multiple roles, because harmonic function is contextual.

Possible tags:

- `OPEN`
- `RESOLVED`
- `LIFT`
- `COLOR`
- `LOOP`
- `CADENTIAL`
- `BRIDGE`

The exact enum names can be chosen during implementation. The important part is that these tags describe harmonic behavior rather than only song-section names.

Keep the existing progression category field. Category and suitability answer different questions:

- Category: what style world is this progression from?
- Suitability: what job can this progression do in the song?

## Default Step Behavior

### First chord progression step

Prefer progressions tagged as open, questioning, foundational, or loop-friendly.

Good candidates include:

- Open loops
- Minor-flavored loops
- Progressions that avoid immediate full closure
- Progressions that establish a clear but flexible tonal world
- Simple foundations that are easy to write over

Examples from the current list:

- `Two Chord Open Loop`: `I - IV`
- `Two Five Loop`: `ii - V`
- `Minor Pop Loop`: `vi - IV - I - V`
- `Extended Turnaround`: `iii - vi - ii - V`
- `Suspended Loop`: `I - Vsus4`
- `Drone Loop`: `I - bVII`
- `Modern Film Loop`: `i - bVI - III - bVII`

### Second chord progression step

Prefer progressions tagged as resolved, declarative, lifting, cadential, or colorfully contrasting.

Good candidates include:

- Strong tonic arrivals
- Chorus-friendly progressions
- Cadential progressions
- Borrowed-chord progressions that return home
- Progressions that brighten, widen, or intensify the harmonic field

Examples from the current list:

- `Classic Cadence`: `I - IV - V - I`
- `Pop Axis`: `I - V - vi - IV`
- `50s Progression`: `I - vi - IV - V`
- `Two Five One`: `ii - V - I`
- `Major To Minor Four`: `I - IV - iv - I`
- `Backdoor Resolution`: `ii - bVII - I`
- `Lydian Lift`: `I - II - IV - I`
- `Epic Rise`: `vi - IV - I - V`

## Implementation Approach

### Preferred approach: role tags

Add a suitability field to `ChordProgression`, likely as a set of enum values.

Conceptually:

```kotlin
enum class ChordProgressionSuitability {
    OPEN,
    RESOLVED,
    LIFT,
    COLOR,
    LOOP,
    CADENTIAL,
    BRIDGE
}
```

Then `ChordProgression` can support multiple tags:

```kotlin
data class ChordProgression(
    val name: String,
    val category: String,
    val romanNumerals: List<String>,
    val suitability: Set<ChordProgressionSuitability> = emptySet()
)
```

Repository accessors can expose filtered lists:

- `availableFirstChordProgressions(disableTwoChordProgressions: Boolean)`
- `availableSecondChordProgressions(disableTwoChordProgressions: Boolean)`
- Existing combined access can remain for compatibility.

### Simpler first step: two curated lists

If tagging every progression feels too large for the first implementation, create two curated lists:

- `firstChordProgressions`
- `secondChordProgressions`

Some progressions may appear in both lists.

This is less flexible, but it is easy to implement and can later be refactored into tags without changing the UI concept.

## Randomization Rules

Random selection should use the same musical distinction as manual selection.

- First progression randomizes from the first/open pool.
- Second progression randomizes from the second/resolved or contrast pool.
- If the second pool is empty after filtering, fall back to the combined available pool.
- Avoid selecting the exact same progression twice when possible.
- Apply the existing "Disable 2-chord chord progressions" setting after role filtering or list selection.

## UI Guidance

Avoid overexplaining the theory in the app.

The first progression step can remain "Chord Progression."

The second progression step could become slightly more intentional:

- Current: "Pick a second chord progression (e.g. for the chorus)."
- Possible: "Pick a contrasting progression for the next section."

Avoid making "chorus" the only default framing. Verse/chorus is useful, but SongScaffold should also work for bridges, refrains, instrumental sections, theatre forms, and improvisational prompts.

## Settings

Do not add a user-facing setting for this immediately.

The first version should simply improve the defaults. If users later need more control, possible settings could include:

- Use separate chord progression pools
- Let both progression steps draw from all progressions
- Prefer open/resolved contrast
- Prefer same-category or different-category contrast

These are likely premature for the first pass.

## Open Questions

- Which suitability tags should be used in the first version?
- Should suitability tags be visible anywhere, or remain internal?
- Should a progression with no suitability tags appear in both pools or neither?
- Should the second progression avoid the same category as the first, or only avoid the exact same progression?
- Should the app eventually support named sections like verse, chorus, bridge, pre-chorus, or refrain?
- Should the existing category labels eventually be revised so style and function are not mixed together?

## Recommendation

Implement the concept as role-aware progression selection.

The strongest long-term version is suitability tags, because it captures the musical truth that progressions can serve more than one role. If implementation needs to stay small, start with two curated lists and migrate to tags later.

The important product change is that the second progression should be selected for relationship, not merely variety. SongScaffold should help the writer move from setup to payoff, question to answer, or stable frame to contrasting color.

---

## Claude's Response

This is the strongest of the three documents. It does what a synthesis plan should do: takes the best idea from the investigative phase — the OPEN/RESOLVED framing — and builds the whole document around it rather than listing it as one option among three. "Songwriting bias, not a hard rule" is exactly the right qualifier. The Category vs. Suitability distinction is cleanly articulated. The code example is concrete enough to actually implement from.

Three things worth resolving before implementation:

**Some "resolved" examples aren't actually resolved.** The second progression pool includes `Pop Axis` (ends on IV), `50s Progression` (ends on V), and `Epic Rise` (ends on V). None of those land on I. They are good progressions but they undercut the OPEN/RESOLVED argument the document just made. Worth reconsidering which tag each gets.

**BRIDGE is a section name, not a harmonic descriptor.** The tag set otherwise describes behavior — OPEN, RESOLVED, LIFT, COLOR, LOOP, CADENTIAL — and then BRIDGE appears and breaks the pattern. A tag like PIVOT or MODAL might be more consistent with the rest.

**The empty suitability set needs a policy decision before writing code.** The open questions section asks whether untagged progressions appear in both pools or neither, but the code shows `= emptySet()` as the default. Any progressions missed during tagging will be silently excluded or silently included depending on how the accessors are written. That behavior should be decided and documented before implementation begins.

---

## Codex Final Thoughts

Claude's response is right on all three implementation cautions.

The biggest correction is the resolved-example issue. The plan should not imply that every second-step candidate literally resolves to `I`. `Pop Axis`, `50s Progression`, and `Epic Rise` can all work beautifully as chorus or payoff progressions, but they are not resolved in the strict cadential sense because they end away from tonic. They are better described as `LIFT`, `ANTHEMIC`, `CYCLIC`, or `MOMENTUM` candidates rather than `RESOLVED` candidates. The second-step pool should probably mean "answer, lift, or payoff," not only "cadential resolution."

That suggests a small vocabulary refinement:

- `OPEN`: questioning, suspended, unresolved, or setup-oriented
- `CADENTIAL`: strongly resolves or points toward tonic resolution
- `LIFT`: brightens, widens, or intensifies the section
- `LOOP`: works as a repeating harmonic bed
- `COLOR`: uses borrowed, modal, chromatic, or otherwise distinctive harmonic color
- `PIVOT`: useful for bridge-like contrast, redirection, or sectional turn

I agree that `BRIDGE` should not be a tag. It names a song section, while the rest of the tags name harmonic behavior. `PIVOT` is a better fit because it describes what a bridge-like progression often does without requiring the app to know the section name.

The empty-set issue should be resolved before implementation. My preference is: do not default to `emptySet()` in finished data. Every progression should be intentionally tagged. During implementation, an empty set should be treated as "uncategorized" and should fall back into both pools only if needed to avoid empty results. That prevents accidental disappearance while still making missing tags visible during review.

So the final implementation guidance should be:

- Use suitability tags, not hard-coded verse/chorus labels.
- Do not equate second progression with strict resolution only.
- Split selection around relationship: setup vs payoff, or frame vs contrast.
- Replace `BRIDGE` with a behavior tag such as `PIVOT`.
- Require every progression to receive at least one suitability tag.
- Keep fallback behavior defensive, but do not let fallback hide incomplete tagging.

With those adjustments, the plan is ready to become an implementation spec. The concept is strong; the main remaining work is curation: deciding what each existing progression is best suited to do.
