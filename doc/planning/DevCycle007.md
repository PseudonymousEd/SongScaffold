# DevCycle 007: Add and Adjust Chord Progressions

**Status:** Work Complete
**Start Date:** 2026-05-11
**Target Completion:** 2026-05-11
**Completion Date:** 2026-05-20
**Focus:** Add new chord progressions and make targeted corrections to existing progression categories, tags, and chord rendering support.

---

## Goal

The original goal of DevCycle 007 was to conduct a full multi-review audit of every chord progression, with Codex, ChatGPT, Claude, and the user each reviewing the entire list.

That original goal is now abandoned. The review table remains useful as a reference artifact, but DevCycle 007 is no longer a comprehensive categorization-review cycle.

The new goal is to use the chord progression system created in DevCycle 006 to add and adjust chord progressions as musical ideas emerge. This includes adding new progressions, changing suitability tags, updating style categories when needed, and extending `ChordMapper` whenever a new roman numeral appears.

## Desired Outcome

- New chord progressions can be added safely with category, roman numerals, and suitability tags.
- Existing progressions can be adjusted when review or user discussion reveals a better musical interpretation.
- Any new roman numerals introduced by new progressions are supported by `ChordMapper`.
- The review table stays available as a lightweight record, but it is no longer treated as a required multi-review workflow.
- Each app change builds successfully with `.\gradlew.bat assembleDebug`.

---

## Original Goal Status

**Status:** Abandoned

The original full-review process is not continuing as planned.

Completed before abandonment:

- [x] Created `doc/planning/ideas/chord_progression_tag_review.md`.
- [x] Added an initial Codex review column.
- [x] Captured Claude review notes in the same artifact.

Abandoned:

- [ ] Full ChatGPT review of every progression.
- [ ] Full Claude/user reconciliation workflow.
- [ ] Applying a single final categorization pass across the entire library.

**Reason:**
The work shifted from broad audit to active curation: adding and adjusting specific chord progressions based on musical discussion.

---

## Tasks

### Phase 1: Maintain Review Artifact

**Status:** Work Complete

- [x] Keep `doc/planning/ideas/chord_progression_tag_review.md` as a reference artifact.
- [x] Use the review table to record notable discussion-driven changes.
- [x] Do not treat the table as a mandatory complete-review gate.

**Technical Notes:**
The review artifact is still useful for documenting why specific progressions changed, especially when user judgment differs from Codex or Claude recommendations.

### Phase 2: Adjust Existing Progressions

**Status:** In Progress

- [x] Update `Major Six Turnaround` from `CADENTIAL`, `COLOR` to `OPEN`, `LOOP`, `COLOR`, `PIVOT`.
- [ ] Apply future targeted tag/category changes as they are decided.
- [ ] Record meaningful changes in the review artifact when useful.
- [ ] Build after changes that affect app code.

**Technical Notes:**
Current pool qualification:

- First progression pool: `OPEN`, `LOOP`
- Second progression pool: `CADENTIAL`, `LIFT`, `PIVOT`

`COLOR` remains descriptive and does not qualify a progression for either pool by itself.

### Phase 3: Add New Progressions

**Status:** In Progress

- [x] Add `Dominant Lift Setup`: `IV - I - II7 - V`.
- [x] Categorize `Dominant Lift Setup` as `Musical Theatre / Jazz`.
- [x] Tag `Dominant Lift Setup` as `OPEN`, `LIFT`, `PIVOT`, `COLOR`.
- [x] Add `II7` support to `ChordMapper` for all supported keys.
- [x] Add `Dominant Lift Setup` to the review artifact.
- [ ] Add future progressions as requested.
- [ ] Add chord-mapper support for any new roman numerals introduced.
- [ ] Build after each app-code change.

**Technical Notes:**
When adding a progression:

1. Add it to `PromptRepository.kt`.
2. Choose a style category.
3. Choose suitability tags.
4. Check whether every roman numeral renders in `ChordMapper`.
5. Add mapper support for any new roman numeral.
6. Optionally add a row to `chord_progression_tag_review.md`.
7. Run `.\gradlew.bat assembleDebug`.

### Phase 4: Verification

**Status:** In Progress

- [x] Build after changing `Major Six Turnaround`.
- [x] Build after adding `Dominant Lift Setup` and `II7`.
- [ ] Manually verify new/adjusted progressions appear in the expected progression steps.
- [ ] Manually verify new roman numerals render correctly in summary output.

**Technical Notes:**
Automated build verification catches Kotlin and resource issues. Manual verification is still needed for user-visible list placement and rendered-chord inspection.

---

## Open Questions

1. **Should every new progression be added to the review table?**
   Recommendation: Yes, when the progression involves new tags, new roman numerals, or meaningful musical judgment. Very small mechanical fixes may not need a row.

2. **Should DevCycle 007 still close with a full category/tag sweep?**
   Recommendation: No. Close it as an add/adjust cycle once current additions and targeted edits are complete. A future cycle can revive the full audit if needed.

3. **Should new roman numerals be added one by one or through a generalized mapper refactor?**
   Recommendation: Add them one by one for now. A generalized mapper may be worthwhile later, but the current map is explicit and easy to verify.

---

## Notes and Risks

- The abandoned review goal should not block useful progression additions.
- Adding roman numerals without updating `ChordMapper` will cause raw roman text to appear in rendered chords.
- Over-tagging still risks making first and second progression pools too similar.
- Under-tagging can hide useful progressions from role-specific steps.
- The review artifact may become a mixed historical record; that is acceptable as long as final app behavior remains clear.

---

## Completion Summary

**Completion Date:** 2026-05-20
**Phases Completed:** 1, 2, 3, 4 (partial — manual progression verification not performed)
**Work Deferred:** Full multi-reviewer categorization audit (intentionally abandoned); manual verification of rendered chords in the running app.

**Accomplishments:**
- Updated `Major Six Turnaround` suitability tags from `CADENTIAL, COLOR` to `OPEN, LOOP, COLOR, PIVOT`.
- Added new progression `Dominant Lift Setup` (IV – I – II7 – V) in the Musical Theatre / Jazz category, tagged `OPEN, LIFT, PIVOT, COLOR`.
- Added `II7` roman numeral support to `ChordMapper` for all supported keys.
- Maintained `chord_progression_tag_review.md` as a lightweight reference artifact.

**Metrics:**
- Progressions added: 1
- Progressions adjusted: 1
- Files modified: 3 (`PromptRepository.kt`, `ChordMapper.kt`, `chord_progression_tag_review.md`)
- Build/test status: `.\gradlew.bat assembleDebug` — passed after each change

**Lessons / Notes:**
Active curation (add/adjust as musical ideas emerge) is more productive than a mandatory multi-reviewer audit sweep. The review table is still a useful lightweight record, but not a gate.
