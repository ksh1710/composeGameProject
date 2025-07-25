package com.example.gameapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gameapp.utils.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TruthOrDareModeViewModel @Inject constructor() : ViewModel() {

    private val _selectedMode = MutableStateFlow<GameMode?>(null)
    val selectedMode: StateFlow<GameMode?> = _selectedMode.asStateFlow()

    fun selectMode(mode: GameMode) {
        _selectedMode.value = mode
    }
}