package com.example.gameapp.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlin.random.Random

fun spinBottle(
    spinning: Boolean,
    playersSize: Int,
    askingPlayer: MutableState<String?>,
    answeringPlayer: MutableState<String?>,
    taskPrompt: MutableState<String?>,
    bottleRotation: Animatable<Float, AnimationVector1D>,
    setTargetRotation: (Float) -> Unit,
    setShouldSpin: (Boolean) -> Unit
) {
    if (spinning || playersSize < 2) return

    // Clear previous results
    askingPlayer.value = null
    answeringPlayer.value = null
    taskPrompt.value = null

    // Calculate the target rotation
    // This creates a realistic spinning effect by:
    // 1. Adding multiple full rotations for visual effect
    // 2. Adding a random final position
    // 3. Building on the current rotation to maintain continuity
    val spins = Random.nextInt(3, 8) // 3-7 full rotations (1080° to 2520°)
    val finalRotation = Random.nextFloat() * 360f // Random final position (0-360°)
    val newTargetRotation = bottleRotation.value + (spins * 360f) + finalRotation

    setTargetRotation(newTargetRotation)
    setShouldSpin(true) // This triggers the LaunchedEffect
}

fun determinePlayersFromRotation(
    rotation: Float,
    players: List<String>
): Pair<String, String> {
    // Normalize rotation to 0-360 range
    val normalizedRotation = (rotation % 360f + 360f) % 360f
    val playerCount = players.size
    val degreesPerPlayer = 360f / playerCount

    // Calculate which player the bottle is pointing to
    // We add half a segment to ensure proper rounding
    val askingIndex = ((normalizedRotation + degreesPerPlayer / 2) / degreesPerPlayer).toInt() % playerCount

    // Calculate the opposite player (bottle tail points to answering player)
    val answeringIndex = (askingIndex + playerCount / 2) % playerCount

    return Pair(players[askingIndex], players[answeringIndex])
}