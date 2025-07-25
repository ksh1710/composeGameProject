package com.example.gameapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddPlayerViewModel @Inject constructor(): ViewModel() {

    private val _players = mutableStateListOf<String>()
    val players: List<String> = _players

    private val _newPlayerName = MutableStateFlow("")
    val newPlayerName: StateFlow<String> = _newPlayerName.asStateFlow()


    // Channel for one-shot UI events like showing a toast/snackbar
    private val _events = Channel<AddPlayerEvent>()
    val events = _events.receiveAsFlow() // Expose as a Flow

    sealed class AddPlayerEvent {
        data class ShowToast(val message: String) : AddPlayerEvent()
        // You could add other events like Navigate, ShowDialog, etc.
    }



    fun addPlayer(name: String) {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) {
            viewModelScope.launch {
                _events.send(AddPlayerEvent.ShowToast("Player name cannot be empty."))
            }
            return
        }
        if (_players.contains(trimmedName)) {
            viewModelScope.launch {
                _events.send(AddPlayerEvent.ShowToast("Player '$trimmedName' already exists."))
            }
        } else {
            _players.add(trimmedName)
            _newPlayerName.value = "" // Clear the input field after adding
        }
    }

    fun removePlayer(name: String) {
        _players.remove(name)
    }

    fun updateNewPlayerName(name: String) {
        _newPlayerName.value = name
    }

    fun canProceedToNextScreenAndNotify(): Boolean {
        val canProceed = _players.size >= 2 // Or your desired minimum number of players
        if (!canProceed) {
            viewModelScope.launch {
                _events.send(AddPlayerEvent.ShowToast("Please add at least 2 players to proceed."))
            }
        }
        return canProceed
    }
}