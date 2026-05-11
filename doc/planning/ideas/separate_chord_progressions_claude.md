# Separate Chord Progression Lists for Step 1 and Step 2

## The Current Situation

SongScaffold currently picks both chord progressions from the same shared pool (`chordProgressions` in `PromptRepository`). The only filtering applied is the optional "disable two-chord progressions" setting. Both steps draw from identical candidates.

This works, but it misses a significant musical opportunity: the two chord progression steps in a song don't serve the same function, and the progressions best suited to each are meaningfully different.

---

## The Musical Case for Separation

### Verse and Chorus Have Different Jobs

In almost every song form — pop, folk, musical theatre, country, Americana — the verse and chorus (or first and second section) serve opposing harmonic roles:

- The **verse** creates motion, question, and uncertainty. It keeps the listener leaning forward. Harmonically, it tends to avoid strong resolution. It may linger in minor territory, use open loops, or defer the tonic.
- The **chorus** delivers arrival, declaration, and payoff. It resolves. It plants the hook. Harmonically, it tends to confirm the tonic and feel conclusive.

When both chord progressions are drawn from the same undifferentiated pool, you can easily end up with two "resolving" progressions back to back (which deflates the chorus) or two open loops (which never lands anywhere). The constraint becomes less useful.

### The Antecedent / Consequent Principle

Classical phrasing theory describes this as **antecedent and consequent** phrases. The antecedent poses a question (often ending on V or an unstable chord); the consequent answers it (ending on I). This tension-and-resolution arc is fundamental to why music feels satisfying.

The same principle operates at the section level. A verse that ends unresolved makes the chorus resolution feel earned. A verse that already resolves fully can make the chorus feel redundant rather than climactic.

### What This Means for Each Pool

**Step 1 / Verse pool — "question" progressions:**

These are progressions that create forward momentum without fully resolving. Good candidates include:

- Open loops: `I → IV`, `ii → V`, `i → ♭VI`
- Minor-flavored progressions: `vi → IV → I → V`, `i → ♭VI → III → ♭VII`
- Progressions that end on V or avoid landing on I: `ii → V`, `iii → vi → ii → V`
- Suspended or ambiguous feels: `I → Vsus4`, `I → ♭VII`

**Step 2 / Chorus pool — "answer" progressions:**

These are progressions that feel conclusive, anthemic, or arrived. Good candidates include:

- Strong I-resolution progressions: `I → IV → V → I`, `IV → I → V7 → I`
- Classic anthemic shapes: `I → V → vi → IV`, `vi → IV → I → V`
- Declarative jazz resolutions: `ii → V → I`, `I → vi → ii → V`
- Color progressions with satisfying return to I: `I → IV → iv → I`, `V → vi → IV → I`

### The Contrast Principle

Even when not thinking in verse/chorus terms, harmonic contrast between sections is a core compositional value. Two progressions that sound alike flatten the song's emotional arc. Two progressions that feel like they belong to different emotional worlds — one restless, one grounded — give the improviser a clear before-and-after to perform into.

Separate lists make it structurally easier to ensure that contrast, rather than leaving it to chance.

---

## Implementation Options

### Option A: Two Separate Hardcoded Lists

The simplest approach. `PromptRepository` exposes `verseProgressions` and `chorusProgressions` as distinct lists. The step that generates Chord Progression 1 draws from `verseProgressions`; the step for Chord Progression 2 draws from `chorusProgressions`. Some progressions (like `I → V → vi → IV`) could appear in both if they genuinely work in either role.

**Pros:** Simple, predictable, easy to curate.  
**Cons:** Rigid. A user who wants two "chorus-style" progressions can't get that.

### Option B: Role Tags on Each Progression

Add a `role` field (or set of tags) to `ChordProgression` — something like `VERSE`, `CHORUS`, or `BOTH`. The default behavior uses role to filter each step, but a settings option could allow both steps to draw from the full pool.

**Pros:** More flexible. Progressions self-document their intent.  
**Cons:** More model complexity. Requires tagging the full existing list.

### Option C: "Character" Categories

Rather than verse/chorus (which implies a specific song form), tag progressions by harmonic character: `OPEN` (unresolved, looping, questioning) vs. `RESOLVED` (cadential, declarative, arriving). Step 1 defaults to `OPEN`; Step 2 defaults to `RESOLVED`. The labelling stays musical rather than structural.

**Pros:** More general — works for song forms beyond verse/chorus.  
**Cons:** Some progressions are genuinely ambiguous in character.

---

## Recommendation

**Option B** (role tags) is the most musically honest approach and sets up well for future features like playback or section labelling. The `ChordProgression` model gains a `role` or `suitability` field. The repository exposes filtered accessors (`progressionsSuitedForVerse()`, `progressionsSuitedForChorus()`), with a fallback to the full list.

If that feels like too much model change right now, **Option A** is a clean first step that delivers most of the musical benefit immediately and can be refactored into Option B later without changing the UI.

---

## Summary

The current single-pool design is functional but harmonically naive. Verses and choruses serve opposite musical purposes, and the chord progressions appropriate to each are meaningfully different. Separating the pools — even informally — would make SongScaffold's constraints more musically coherent and give improvisers a clearer emotional arc to work with.
---

## Codex's response

I think Claude's plan is directionally strong. The best part is the musical framing: first and second progression should not be treated as duplicate slots, because in practice they often carry different formal jobs. A first progression can establish the song's question, atmosphere, or tonal premise; a second progression can provide answer, lift, payoff, or a contrasting emotional space. That is a real music-theory distinction, not just a product-organization preference.

The antecedent/consequent idea is especially useful because it gives the app a compositional reason for the split. SongScaffold is not only picking chords; it is helping the writer build a small musical arc. If both progressions come from the same bag, the app can accidentally suggest two progressions with the same function. Separate pools, or role-aware filtering, would make the constraint more intentional.

I would be slightly careful about making the mapping too strictly "verse = unresolved" and "chorus = resolved." That is often true, but plenty of songs use a stable verse and a more harmonically open chorus, or use the same loop for both sections with arrangement and melody doing the contrast. The app should probably treat this as a default songwriting bias rather than a rule.

For that reason, I like Claude's Option C vocabulary almost as much as Option B. "Open," "resolved," "lift," "color," "loop," and "cadential" may be more reusable than "verse" and "chorus." Those labels describe harmonic behavior without locking the user into a specific form. A progression could have more than one tag, which would let the same progression appear in different contexts when musically appropriate.

My preferred version would be a light form of Option B:

- Add role or suitability tags to each `ChordProgression`.
- Let each progression have multiple tags.
- Use default filtered accessors for first and second progression steps.
- Keep a fallback to the full list if filtering gets too narrow.
- Continue applying the existing "disable two-chord progressions" option after role filtering.

I would avoid adding a user-facing setting for all of this at first. The app can simply make better default choices. If later users want more control, the tags could support section-specific or character-specific filters.

One implementation caution: if the model adds tags, the app should not lose the current category grouping. Category answers "what style world is this progression from?" while suitability answers "what job can this progression do in the song?" Those are different dimensions, and both are useful.

Overall, I agree with the plan. The separate-list idea is worth pursuing because it would make the second chord progression step feel less like "pick another one" and more like "shape the next section." That is a meaningful improvement for an app whose whole purpose is giving writers productive constraints.
