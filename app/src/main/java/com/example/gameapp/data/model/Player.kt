package com.example.gameapp.data.model

data class Player(
    val id: String = java.util.UUID.randomUUID().toString(), // Unique ID for each player
    var name: String,
    var score: Int = 0
)