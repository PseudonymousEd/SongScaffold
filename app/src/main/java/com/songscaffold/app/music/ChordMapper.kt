package com.songscaffold.app.music

import com.songscaffold.app.model.ChordProgression

object ChordMapper {

    private val keyMap: Map<String, Map<String, String>> = mapOf(
        "C"  to mapOf("I" to "C",   "ii" to "Dm",  "iii" to "Em",  "IV" to "F",   "V" to "G",   "vi" to "Am",  "I7" to "Cmaj7", "V/vi" to "E",  "iv" to "Fm",  "♭VII" to "Bb"),
        "Db" to mapOf("I" to "Db",  "ii" to "Ebm", "iii" to "Fm",  "IV" to "Gb",  "V" to "Ab",  "vi" to "Bbm", "I7" to "Dbmaj7","V/vi" to "F",  "iv" to "Gbm", "♭VII" to "B"),
        "D"  to mapOf("I" to "D",   "ii" to "Em",  "iii" to "F#m", "IV" to "G",   "V" to "A",   "vi" to "Bm",  "I7" to "Dmaj7", "V/vi" to "F#", "iv" to "Gm",  "♭VII" to "C"),
        "Eb" to mapOf("I" to "Eb",  "ii" to "Fm",  "iii" to "Gm",  "IV" to "Ab",  "V" to "Bb",  "vi" to "Cm",  "I7" to "Ebmaj7","V/vi" to "G",  "iv" to "Abm", "♭VII" to "Db"),
        "E"  to mapOf("I" to "E",   "ii" to "F#m", "iii" to "G#m", "IV" to "A",   "V" to "B",   "vi" to "C#m", "I7" to "Emaj7", "V/vi" to "G#", "iv" to "Am",  "♭VII" to "D"),
        "F"  to mapOf("I" to "F",   "ii" to "Gm",  "iii" to "Am",  "IV" to "Bb",  "V" to "C",   "vi" to "Dm",  "I7" to "Fmaj7", "V/vi" to "A",  "iv" to "Bbm", "♭VII" to "Eb"),
        "Gb" to mapOf("I" to "Gb",  "ii" to "Abm", "iii" to "Bbm", "IV" to "B",   "V" to "Db",  "vi" to "Ebm", "I7" to "Gbmaj7","V/vi" to "Bb", "iv" to "Bm",  "♭VII" to "E"),
        "G"  to mapOf("I" to "G",   "ii" to "Am",  "iii" to "Bm",  "IV" to "C",   "V" to "D",   "vi" to "Em",  "I7" to "Gmaj7", "V/vi" to "B",  "iv" to "Cm",  "♭VII" to "F"),
        "Ab" to mapOf("I" to "Ab",  "ii" to "Bbm", "iii" to "Cm",  "IV" to "Db",  "V" to "Eb",  "vi" to "Fm",  "I7" to "Abmaj7","V/vi" to "C",  "iv" to "Dbm", "♭VII" to "Gb"),
        "A"  to mapOf("I" to "A",   "ii" to "Bm",  "iii" to "C#m", "IV" to "D",   "V" to "E",   "vi" to "F#m", "I7" to "Amaj7", "V/vi" to "C#", "iv" to "Dm",  "♭VII" to "G"),
        "Bb" to mapOf("I" to "Bb",  "ii" to "Cm",  "iii" to "Dm",  "IV" to "Eb",  "V" to "F",   "vi" to "Gm",  "I7" to "Bbmaj7","V/vi" to "D",  "iv" to "Ebm", "♭VII" to "Ab"),
        "B"  to mapOf("I" to "B",   "ii" to "C#m", "iii" to "D#m", "IV" to "E",   "V" to "F#",  "vi" to "G#m", "I7" to "Bmaj7", "V/vi" to "D#", "iv" to "Em",  "♭VII" to "A")
    )

    fun renderProgression(key: String, progression: ChordProgression): List<String> {
        val chords = keyMap[key] ?: return emptyList()
        return progression.romanNumerals.map { numeral ->
            chords[numeral] ?: numeral
        }
    }
}
