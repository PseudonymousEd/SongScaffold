package com.songscaffold.app.data

import com.songscaffold.app.model.ChordProgression
import com.songscaffold.app.model.ChordProgressionSuitability
import com.songscaffold.app.model.ChordProgressionSuitability.CADENTIAL
import com.songscaffold.app.model.ChordProgressionSuitability.COLOR
import com.songscaffold.app.model.ChordProgressionSuitability.LIFT
import com.songscaffold.app.model.ChordProgressionSuitability.LOOP
import com.songscaffold.app.model.ChordProgressionSuitability.OPEN
import com.songscaffold.app.model.ChordProgressionSuitability.PIVOT
import com.songscaffold.app.model.TopicCategory
import com.songscaffold.app.model.TopicPrompt

object PromptRepository {

    val topics: List<TopicPrompt> = buildList {
        // Themes
        listOf(
            "love", "loss", "freedom", "home", "time", "change", "hope", "fear", "joy", "regret",
            "betrayal", "longing", "pride", "shame", "revenge", "forgiveness", "survival", "identity",
            "solitude", "connection", "memory", "ambition", "redemption", "sacrifice", "loyalty",
            "heartbreak", "justice", "wonder", "courage", "silence", "distance", "belonging",
            "legacy", "failure", "patience", "jealousy", "faith", "obsession", "desire", "grief"
        ).forEach { add(TopicPrompt(it, TopicCategory.THEME)) }

        // Images
        listOf(
            "a broken window", "the open sea", "a burning candle", "an empty chair",
            "a crumbling wall", "the rising sun", "a fading photograph", "a locked gate",
            "falling leaves", "a distant storm", "a crowded room", "a single road",
            "a rusted key", "a silent phone", "a torn letter", "a broken compass",
            "a half-filled glass", "a fire burning low", "an overgrown path", "a bridge in fog",
            "a rusted anchor", "an empty birdcage", "a burning bridge", "footprints in snow",
            "a ticking clock", "a last bus home", "a lighthouse in fog", "a wilting flower",
            "an old jukebox", "a cracked mirror", "a half-open door", "an unmailed letter",
            "a candle in the wind", "a road that ends at water"
        ).forEach { add(TopicPrompt(it, TopicCategory.IMAGE)) }

        // Characters
        listOf(
            "a wandering stranger", "an old sailor", "a taxi driver at midnight", "a lost child",
            "a returning soldier", "a heartbroken lover", "a midnight thief", "a weary traveler",
            "a forgotten poet", "a restless ghost", "a street musician", "an old lighthouse keeper",
            "a runaway bride", "a retired boxer", "a dreaming prisoner", "a jilted groom",
            "a night-shift nurse", "a small-town preacher", "a con artist with a conscience",
            "a soldier who forgot how to come home", "a child waiting at a window",
            "a washed-up rockstar", "a widow at the sea", "a teacher no one remembers",
            "an ex-convict on their first morning out", "a carnival barker with regrets",
            "a detective who knows too much"
        ).forEach { add(TopicPrompt(it, TopicCategory.CHARACTER)) }

        // Situations
        listOf(
            "leaving home for the last time", "waiting at a bus stop in the rain",
            "standing at a crossroads", "saying goodbye at a train station",
            "waking up in an unfamiliar place", "watching the last sunset of summer",
            "finding an old letter", "hearing a song from the past",
            "walking through an empty house", "driving through the night alone",
            "packing up the last box", "staring at a blank page", "missing a flight",
            "running out of time", "getting caught in a lie",
            "receiving a letter with no return address",
            "watching someone else live your old life",
            "looking in the mirror and not recognizing yourself",
            "arriving too late to say goodbye",
            "sitting with someone who is slowly forgetting you",
            "burning something that used to matter",
            "realizing you've been lying to yourself for years",
            "deciding not to go back",
            "finding out you were wrong about everything",
            "watching the last light go out"
        ).forEach { add(TopicPrompt(it, TopicCategory.SITUATION)) }

        // Emotions
        listOf(
            "jealousy", "longing", "relief", "pride", "shame", "euphoria", "dread", "nostalgia",
            "rage", "contentment", "despair", "wonder", "confusion", "gratitude", "resentment",
            "bittersweet joy", "quiet sadness", "reckless hope", "hollow victory",
            "overwhelming awe", "hollow grief", "desperate joy", "quiet fury", "reluctant love",
            "cold resolve", "tender regret", "stunned disbelief", "reckless abandon",
            "melancholy wonder", "exhausted hope"
        ).forEach { add(TopicPrompt(it, TopicCategory.EMOTION)) }

        // Places
        listOf(
            "a grocery store parking lot", "a highway at midnight", "an empty beach",
            "a rooftop at dawn", "a small town in winter", "a crowded subway",
            "a mountain overlook", "a quiet library", "a neon-lit bar", "an overgrown garden",
            "a hospital waiting room", "an abandoned church", "a fire escape in the city",
            "a foggy harbor", "a motel on the edge of town", "a late-night laundromat",
            "an empty stadium", "the back seat of a cab", "a diner at 3am",
            "a childhood bedroom left unchanged", "a harbor at low tide",
            "an airport departure lounge", "a field after the last concert",
            "a campfire dying out", "a payphone on a street corner"
        ).forEach { add(TopicPrompt(it, TopicCategory.PLACE)) }

        // Goals
        listOf(
            "leave town", "find love", "start over", "be heard", "reach the coast",
            "find peace", "go back home", "make things right", "disappear for a while",
            "be understood", "break free", "win someone back", "see the world",
            "forget the past", "build something new", "hold on to what's left",
            "outrun your own past", "say what you should have said years ago",
            "convince yourself it was worth it", "find someone who still remembers",
            "stop pretending everything is fine", "get one more chance",
            "finish what you started", "walk away and not look back",
            "be the person you promised you'd be"
        ).forEach { add(TopicPrompt(it, TopicCategory.GOAL)) }

        // Obstacles
        listOf(
            "your car won't start", "the door is locked", "a broken promise stands in the way",
            "a missed call changed everything", "a fading memory blocks the path",
            "there's no money left", "fear is holding you back", "you took a wrong turn",
            "the bridge is out", "a storm is rolling in", "the letter came too late",
            "trust was broken", "the road ends here", "you lost the only key",
            "time has run out", "the wound never healed",
            "you don't know how to ask for help", "pride is standing in the way",
            "the words keep coming out wrong", "someone else got there first",
            "the one you needed is already gone", "the memory is all you have left",
            "you made a promise you can't keep", "the door only opens from the other side",
            "you're still holding on to the wrong thing"
        ).forEach { add(TopicPrompt(it, TopicCategory.OBSTACLE)) }

        // Twists
        listOf(
            "the letter never arrived", "it was all a dream", "the stranger knew your name",
            "the key didn't fit", "there was no signal", "they were already gone",
            "the map was wrong", "you were the last one left", "the door was open all along",
            "it turned out to be a test", "the voice on the phone was familiar",
            "the photo was of you", "the ending was just a beginning",
            "what you feared most saved you", "the person you were waiting for was you",
            "the goodbye was actually a hello", "they came back but nothing was the same",
            "the thing you lost was never really yours",
            "the destination turned out to be the starting point",
            "you were the villain in their story",
            "the love was real but the timing wasn't",
            "what looked like the end was a beginning in disguise",
            "the person you were running from was yourself",
            "the rescue turned into a surrender",
            "the answer you wanted would have broken you"
        ).forEach { add(TopicPrompt(it, TopicCategory.TWIST)) }

        // Elements
        listOf(
            "a clock ticking", "distant thunder", "a single candle", "an old photograph",
            "a closing door", "a song on the radio", "a glass of water nearly empty",
            "a flickering light", "a train whistle in the dark", "smoke rising slowly",
            "a ringing phone", "an open window", "rain on glass", "a crowded silence",
            "a match being struck", "the last note of a song fading out",
            "an empty glass on a bar", "a broken wristwatch",
            "footsteps on a gravel path", "a crumpled note in a pocket",
            "the sound of a door closing down the hall",
            "a half-finished letter on a desk", "smoke from a snuffed candle",
            "a light left on in an empty room", "the sound of rain starting"
        ).forEach { add(TopicPrompt(it, TopicCategory.ELEMENT)) }
    }

    private fun chordProgression(
        name: String,
        category: String,
        romanNumerals: List<String>,
        vararg suitability: ChordProgressionSuitability
    ): ChordProgression =
        ChordProgression(name, category, romanNumerals, suitability.toSet())

    val chordProgressions: List<ChordProgression> = listOf(
        chordProgression("Classic Cadence",        "Classic / Standard",      listOf("I", "IV", "V", "I"), CADENTIAL),
        chordProgression("Pop Axis",               "Classic / Standard",      listOf("I", "V", "vi", "IV"), LIFT, LOOP),
        chordProgression("Minor Pop Loop",         "Classic / Standard",      listOf("vi", "IV", "I", "V"), OPEN, LIFT, LOOP),

        chordProgression("Two Five One",           "Musical Theatre / Jazz",  listOf("ii", "V", "I"), CADENTIAL),
        chordProgression("Circle Turnaround",      "Musical Theatre / Jazz",  listOf("I", "vi", "ii", "V"), OPEN, CADENTIAL),
        chordProgression("Extended Turnaround",    "Musical Theatre / Jazz",  listOf("iii", "vi", "ii", "V"), OPEN, PIVOT),
        chordProgression("Borrowed Minor Four",    "Musical Theatre / Jazz",  listOf("I", "Imaj7", "IV", "iv"), COLOR, PIVOT),
        chordProgression("Dominant Lift Setup",    "Musical Theatre / Jazz",  listOf("IV", "I", "II7", "V"), OPEN, LIFT, PIVOT, COLOR),

        chordProgression("Secondary Dominant Lift","Expressive / Color",      listOf("I", "V/vi", "vi", "IV"), LIFT, COLOR),
        chordProgression("Major To Minor Four",    "Expressive / Color",      listOf("I", "IV", "iv", "I"), CADENTIAL, COLOR),
        chordProgression("Minor To Resolution",    "Expressive / Color",      listOf("vi", "ii", "V", "I"), CADENTIAL, LIFT),
        chordProgression("Flat Seven Color",       "Expressive / Color",      listOf("I", "♭VII", "IV", "I"), COLOR, CADENTIAL),

        chordProgression("Two Chord Open Loop",    "Loops",                   listOf("I", "IV"), OPEN, LOOP),
        chordProgression("Two Five Loop",          "Loops",                   listOf("ii", "V"), OPEN, LOOP),

        chordProgression("50s Progression",          "Classic / Standard",      listOf("I", "vi", "IV", "V"), LIFT, LOOP),
        chordProgression("Plagal Loop",              "Classic / Standard",      listOf("IV", "I", "V", "I"), CADENTIAL, LIFT),
        chordProgression("Descending Bass Line",     "Classic / Standard",      listOf("I", "V/7", "vi", "V"), OPEN, COLOR),
        chordProgression("Axis Variant",             "Classic / Standard",      listOf("vi", "V", "IV", "I"), CADENTIAL, LIFT),

        chordProgression("Backdoor Resolution",      "Musical Theatre / Jazz",  listOf("ii", "♭VII", "I"), CADENTIAL, COLOR),
        chordProgression("Rhythm Changes",           "Musical Theatre / Jazz",  listOf("I", "vi", "ii", "V"), OPEN, CADENTIAL),
        chordProgression("Minor Two Five One",       "Musical Theatre / Jazz",  listOf("iiø", "V7", "i"), CADENTIAL, COLOR),
        chordProgression("Chromatic Walk-Up",        "Musical Theatre / Jazz",  listOf("I", "I#dim", "ii", "V"), PIVOT, COLOR),
        chordProgression("Major Six Turnaround",     "Musical Theatre / Jazz",  listOf("I", "VI", "ii", "I"), OPEN, LOOP, COLOR, PIVOT),

        chordProgression("Line Cliche Major",        "Expressive / Color",      listOf("I", "Imaj7", "I7", "IV"), OPEN, COLOR, PIVOT),
        chordProgression("Mixolydian Variant",       "Expressive / Color",      listOf("I", "♭VII", "I", "IV"), COLOR, LOOP),
        chordProgression("Chromatic Mediants",       "Expressive / Color",      listOf("I", "♭III", "IV", "I"), COLOR, LIFT),
        chordProgression("Deceptive Cycle",          "Expressive / Color",      listOf("V", "vi", "IV", "I"), CADENTIAL, PIVOT),

        chordProgression("Drone Loop",               "Loops",                   listOf("I", "♭VII"), OPEN, LOOP, COLOR),
        chordProgression("Suspended Loop",           "Loops",                   listOf("I", "Vsus4"), OPEN, LOOP),
        chordProgression("Minor Oscillation",        "Loops",                   listOf("i", "♭VI"), OPEN, LOOP, COLOR),
        chordProgression("Pedal Loop",               "Loops",                   listOf("I", "ii/I", "IV/I", "V/I"), OPEN, LOOP, COLOR),

        chordProgression("Epic Rise",                "Cinematic / Modern",      listOf("vi", "IV", "I", "V"), LIFT, LOOP),
        chordProgression("Modern Film Loop",         "Cinematic / Modern",      listOf("i", "♭VI", "III", "♭VII"), OPEN, LOOP, COLOR),
        chordProgression("Lydian Lift",              "Cinematic / Modern",      listOf("I", "II", "IV", "I"), LIFT, COLOR),
        chordProgression("Ambiguous Loop",           "Cinematic / Modern",      listOf("I", "V", "ii", "IV"), OPEN, LOOP, PIVOT)


    )

    private val firstProgressionSuitability = setOf(OPEN, LOOP)
    private val secondProgressionSuitability = setOf(CADENTIAL, LIFT, PIVOT)

    fun availableChordProgressions(disableTwoChordProgressions: Boolean): List<ChordProgression> =
        applyChordProgressionSettings(chordProgressions, disableTwoChordProgressions)

    fun availableFirstChordProgressions(disableTwoChordProgressions: Boolean): List<ChordProgression> =
        applyChordProgressionSettings(
            chordProgressions.filter { it.isSuitedForFirstProgression() },
            disableTwoChordProgressions
        ).ifEmpty { availableChordProgressions(disableTwoChordProgressions) }

    fun availableSecondChordProgressions(disableTwoChordProgressions: Boolean): List<ChordProgression> =
        applyChordProgressionSettings(
            chordProgressions.filter { it.isSuitedForSecondProgression() },
            disableTwoChordProgressions
        ).ifEmpty { availableChordProgressions(disableTwoChordProgressions) }

    fun secondChordProgressionCandidates(
        disableTwoChordProgressions: Boolean,
        firstProgression: ChordProgression?
    ): List<ChordProgression> {
        val secondPool = availableSecondChordProgressions(disableTwoChordProgressions)
        val combinedPool = availableChordProgressions(disableTwoChordProgressions)

        return secondPool.filterNotFirst(firstProgression)
            .ifEmpty { secondPool }
            .ifEmpty { combinedPool.filterNotFirst(firstProgression) }
            .ifEmpty { combinedPool }
    }

    private fun applyChordProgressionSettings(
        progressions: List<ChordProgression>,
        disableTwoChordProgressions: Boolean
    ): List<ChordProgression> =
        if (disableTwoChordProgressions) {
            progressions.filterNot(::isTwoChordProgression)
        } else {
            progressions
        }

    private fun ChordProgression.isSuitedForFirstProgression(): Boolean =
        suitability.any { it in firstProgressionSuitability }

    private fun ChordProgression.isSuitedForSecondProgression(): Boolean =
        suitability.any { it in secondProgressionSuitability }

    private fun List<ChordProgression>.filterNotFirst(
        firstProgression: ChordProgression?
    ): List<ChordProgression> =
        firstProgression?.let { first -> filter { it != first } } ?: this

    fun isTwoChordProgression(progression: ChordProgression): Boolean =
        progression.romanNumerals.size == 2

    val pointOfViewOptions = listOf(
        "First person — I / me / my",
        "Second person — you",
        "Third person — he / she / they",
        "Direct address — singing to someone present",
        "Memory voice — singing from the past",
        "Future self — singing to who you might become",
        "Observer — describing someone else without entering their mind"
    )

    val deliveryModes = listOf(
        "Performing — outward, shaped, intentional",
        "Thinking — inward, discovering, conversational"
    )

    val phrasingStyles = listOf(
        "Hold — sustain vowels, connect phrases",
        "Break — pause, fragment, interrupt"
    )

    val emotionalIntensityOptions = listOf(
        "Low — internal, contained",
        "High — projected, expressive"
    )

    val majorKeys = listOf(
        "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"
    )

    val startingNoteOptions = listOf(
        "Root (1) — grounded, direct",
        "3rd — emotional, expressive",
        "5th — open, searching"
    )

    val secondNoteDirectionOptions = listOf(
        "Step up",
        "Step down",
        "Leap up",
        "Leap down",
        "Repeat the first note"
    )

    val rhymeWords: List<String> = listOf(
        "light", "rain", "blue", "time", "day", "night", "song", "heart",
        "face", "hand", "star", "door", "tree", "fire", "road", "wave",
        "moon", "sun", "sky", "wind", "fall", "love", "dream", "high",
        "cry", "rise", "shine", "fine", "mine", "line", "stay", "play",
        "pray", "way", "bright", "might", "fight", "right", "sight", "white",
        "game", "flame", "name", "came", "pain", "gain", "chain", "train",
        "break", "shake", "wake", "make", "take", "fly", "die", "try",
        "lie", "buy", "ride", "hide", "wide", "side", "pride", "stone",
        "bone", "lone", "moan", "phone", "tone", "zone", "call", "hall",
        "tall", "wall", "ball", "cold", "bold", "hold", "gold", "told",
        "old", "ground", "sound", "found", "round", "bound", "word", "heard",
        "burn", "turn", "learn", "yearn", "gone", "on", "run", "done",
        "one", "fun", "gun", "sing", "ring", "bring", "thing", "king",
        "spring", "sting", "swing", "back", "track", "crack", "black",
        "red", "bed", "head", "dead", "bread", "led", "said", "thread",
        "grace", "chase", "race", "pace", "trust", "dust", "rust", "must",
        "glow", "flow", "show", "know", "grow", "slow", "blow", "throw",
        "sweet", "meet", "feet", "beat", "heat", "seat", "street", "deep",
        "keep", "sleep", "weep", "creep", "well", "bell", "sell", "tell",
        "fell", "spell", "shell", "yell", "still", "fill", "hill", "will",
        "chill", "thrill", "spill", "kill", "sail", "tail", "trail", "fail",
        "jail", "pale", "tale", "sale", "free", "sea", "me", "be",
        "key", "see", "flee", "knee", "raw", "draw", "saw", "claw",
        "thaw", "jaw", "law", "flaw", "more", "floor", "grease", "cease",
        "share", "wear", "yay", "scare", "rest", "ed", "cool", "rule",
        "earn", "meow", "now", "how", "stop", "top", "fee", "wealth",
        "health", "guy", "born", "shorn", "eye", "kid", "did", "war",
        "hour", "here", "dear", "brain", "spine", "voice", "choice"
    )

    val rhymeSchemes = listOf(
        "AABB — pairs of matching rhymes",
        "ABAB — alternating rhymes",
        "ABAC — partial repetition with variation",
        "AAAA — same rhyme throughout",
        "Free — no planned rhyme scheme"
    )
}
