package com.example.gameapp.ui.presentation.truthAndDareGame.truthOrDareModeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.example.gameapp.utils.GameMode
import com.example.gameapp.viewmodel.TruthOrDareModeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TruthOrDareModeScreen(
    navigateToAddPlayerScreen: (GameMode) -> Unit, // Pass selected mode
    viewModel: TruthOrDareModeViewModel = hiltViewModel()
) {
    val selectedMode by viewModel.selectedMode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select Game Mode") })
        },
//        bottomBar = {
//            Button(
//                onClick = { selectedMode?.let { navigateToAddPlayerScreen(it) } },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                enabled = selectedMode != null
//            ) {
//                Text("Start Game")
//            }
//        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Choose a game mode:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            GameMode.entries.forEach { mode ->
                ModeSelectionCard(
                    mode = mode,
                    isSelected = mode == selectedMode,
                    onModeSelected = { mode ->
                        viewModel.selectMode(mode).let {
                            navigateToAddPlayerScreen(mode)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ModeSelectionCard(
    mode: GameMode,
    isSelected: Boolean,
    onModeSelected: (GameMode) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onModeSelected(mode) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mode.displayName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = when (mode) {
                    GameMode.FRIENDS -> "Perfect for casual fun with friends."
                    GameMode.COUPLE -> "Designed for couples to spice things up."
                    GameMode.ADULT -> "For mature players, expect some edgy truths and dares!"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
