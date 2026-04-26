package com.songscaffold.app.model

data class ChordProgression(
    val name: String,
    val category: String,
    val romanNumerals: List<String>
) {
    val romanDisplay: String get() = romanNumerals.joinToString(" – ")
}
