package com.example.gameapp.ui.presentation.truthAndDareGame.addPlayerScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.example.gameapp.ui.presentation.truthAndDareGame.addPlayerScreen.components.PlayerItem
import com.example.gameapp.viewmodel.AddPlayerViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlayerScreen(
    navigateToGameModeSelectionScreen: (List<String>) -> Unit, // Pass players to the next screen
    viewModel: AddPlayerViewModel = hiltViewModel()
) {
    val players = viewModel.players // Observe the players list
    val newPlayerName by viewModel.newPlayerName.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current // Get keyboard controller
    val ctx = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AddPlayerViewModel.AddPlayerEvent.ShowToast -> {
                    Toast.makeText(ctx, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Players") })
        },
        bottomBar = {
            Button(
                onClick = {
                    if (viewModel.canProceedToNextScreenAndNotify()) {
                        navigateToGameModeSelectionScreen(players)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("Proceed to Game Mode")
            }
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = newPlayerName,
                onValueChange = { viewModel.updateNewPlayerName(it) },
                label = { Text("Player Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // Set the IME action to "Done"
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.addPlayer(newPlayerName)
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = { viewModel.addPlayer(newPlayerName) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Player")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Current Players:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (players.isEmpty()) {
                Text("No players added yet. Add at least two players to proceed.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(players) { player ->
                        PlayerItem(
                            playerName = player,
                            onDeleteClick = { viewModel.removePlayer(player) }
                        )
                    }
                }
            }
        }
    }
}
