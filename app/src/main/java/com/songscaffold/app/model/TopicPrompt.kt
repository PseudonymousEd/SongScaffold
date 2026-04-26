package com.songscaffold.app.model

data class TopicPrompt(
    val text: String,
    val category: TopicCategory
)

enum class TopicCategory(val displayName: String) {
    THEME("Theme"),
    IMAGE("Image"),
    CHARACTER("Character"),
    SITUATION("Situation"),
    EMOTION("Emotion"),
    PLACE("Place"),
    GOAL("Goal"),
    OBSTACLE("Obstacle"),
    TWIST("Twist"),
    ELEMENT("Element")
}
