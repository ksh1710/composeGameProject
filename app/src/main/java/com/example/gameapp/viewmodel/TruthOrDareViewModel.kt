package com.example.gameapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gameapp.utils.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SharedTruthOrDareViewModel @Inject constructor() : ViewModel() {


    private val _players = MutableStateFlow<List<String>>(emptyList())
    val players: StateFlow<List<String>> = _players.asStateFlow()

    private val _selectedMode = MutableStateFlow<GameMode?>(null)
    val selectedMode: StateFlow<GameMode?> = _selectedMode.asStateFlow()

    // You can also add game-specific state here if the GameScreen is also controlled by this ViewModel
    private val _currentTruthOrDare = MutableStateFlow("")
    val currentTruthOrDare: StateFlow<String> = _currentTruthOrDare.asStateFlow()

    private val _currentPlayerIndex = MutableStateFlow(0)
    val currentPlayerIndex: StateFlow<Int> = _currentPlayerIndex.asStateFlow()

    private val _playerScores = MutableStateFlow<Map<String, Int>>(emptyMap())
    val playerScores: StateFlow<Map<String, Int>> = _playerScores

    fun initializeScores(players: List<String>) {
        _playerScores.value = players.associateWith { 0 }
    }

    fun addPoints(player: String, points: Int) {
        _playerScores.value = _playerScores.value.toMutableMap().apply {
            this[player] = (this[player] ?: 0) + points
        }
    }

    fun resetScores() {
        _playerScores.value = _playerScores.value.mapValues { 0 }
    }


    // Example data for truths and dares (you'd load this from resources, database, or network)
    private val friendsTruths = listOf(
        "What's your most embarrassing moment?",
        "What's one secret you've never told anyone?",
        "What's your biggest fear?"
    )
    private val friendsDares = listOf(
        "Do 10 push-ups.",
        "Sing your favorite song loudly.",
        "Call a friend and tell them you love them."
    )

    private val coupleTruths = listOf(
        "What's your favorite memory of us?",
        "What's one thing you wish we did more often?",
        "What's something new you want to try together?"
    )
    private val coupleDares = listOf(
        "Give your partner a 2-minute massage.",
        "Write a short love poem for your partner.",
        "Dance together to a romantic song."
    )

    private val adultTruths = listOf(
        "What's the naughtiest thing you've ever done?",
        "What's your biggest turn-on?",
        "What's a fantasy you've never told anyone?"
    )
    private val adultDares = listOf(
        "Take off an item of clothing.",
        "Kiss someone on the neck for 10 seconds.",
        "Send a flirty text to someone."
    )

    fun getListOfTruths(gameMode: GameMode): List<String> {
        if (gameMode == GameMode.FRIENDS) {
            return friendsTruths
        } else if (gameMode == GameMode.COUPLE) {
            return coupleTruths
        } else {
            return adultTruths
        }
    }

    fun getListOfDares(gameMode: GameMode): List<String> {
        if (gameMode == GameMode.FRIENDS) {
            return friendsDares
        } else if (gameMode == GameMode.COUPLE) {
            return coupleDares
        } else {
            return adultDares
        }
    }

    fun setPlayers(playerList: List<String>) {
        _players.value = playerList
    }

    fun setSelectedMode(mode: GameMode) {
        _selectedMode.value = mode
    }

    // Logic to get the next truth or dare based on mode and player
    fun getNextTruthOrDare() {
        val currentPlayers = _players.value
        if (currentPlayers.isEmpty()) {
            _currentTruthOrDare.value = "Please add players to start the game!"
            return
        }

        val currentMode = _selectedMode.value ?: run {
            _currentTruthOrDare.value = "Please select a game mode!"
            return
        }

        val truths = when (currentMode) {
            GameMode.FRIENDS -> friendsTruths
            GameMode.COUPLE -> coupleTruths
            GameMode.ADULT -> adultTruths
            // Add other modes here
        }

        val dares = when (currentMode) {
            GameMode.FRIENDS -> friendsDares
            GameMode.COUPLE -> coupleDares
            GameMode.ADULT -> adultDares
            // Add other modes here
        }

        val isTruth = kotlin.random.Random.nextBoolean()
        val content = if (isTruth) {
            truths.random()
        } else {
            dares.random()
        }
        _currentTruthOrDare.value = content

        // Move to next player
        _currentPlayerIndex.value = (_currentPlayerIndex.value + 1) % currentPlayers.size
    }

    fun getCurrentPlayerName(): String? {
        return _players.value.getOrNull(_currentPlayerIndex.value)
    }

    fun resetGameState() {
        _players.value = emptyList()
        _selectedMode.value = null
        _currentTruthOrDare.value = ""
        _currentPlayerIndex.value = 0
    }
}
