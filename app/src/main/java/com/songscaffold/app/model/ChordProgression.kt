package com.songscaffold.app.model

enum class ChordProgressionSuitability {
    OPEN,
    CADENTIAL,
    LIFT,
    LOOP,
    COLOR,
    PIVOT
}

data class ChordProgression(
    val name: String,
    val category: String,
    val romanNumerals: List<String>,
    val suitability: Set<ChordProgressionSuitability>
) {
    val romanDisplay: String get() = romanNumerals.joinToString(" - ")
}
