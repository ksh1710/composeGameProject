package com.example.gameapp.ui.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.gameapp.ui.presentation.truthAndDareGame.truthOrDareGameScreen.TruthOrDareGameScreen
import com.example.gameapp.ui.presentation.truthAndDareGame.addPlayerScreen.AddPlayerScreen
import com.example.gameapp.ui.presentation.truthAndDareGame.truthOrDareModeScreen.TruthOrDareModeScreen
import com.example.gameapp.viewmodel.SharedTruthOrDareViewModel


fun NavGraphBuilder.truthOrDareGameGraph(navController: NavController) {
    navigation(
        startDestination = Destinations.TruthOrDareModeScreen.route, // First screen in this flow
        route = Destinations.TruthOrDareGameGraph.route // Route for the entire nested graph
    ) {

        composable(Destinations.TruthOrDareAddPlayerScreen.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Destinations.TruthOrDareGameGraph.route)
            }
            val sharedViewModel: SharedTruthOrDareViewModel = hiltViewModel(parentEntry)

            AddPlayerScreen(
                navigateToGameScreen = { players ->
                    sharedViewModel.setPlayers(players)
                    navController.navigate(Destinations.TruthOrDareGameScreen.route)
                },
                initializeScores = { players ->
                    sharedViewModel.initializeScores(players)
                },
            )
        }

        composable(Destinations.TruthOrDareModeScreen.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Destinations.TruthOrDareGameGraph.route)
            }
            val sharedViewModel: SharedTruthOrDareViewModel = hiltViewModel(parentEntry)

            TruthOrDareModeScreen(
                navigateToAddPlayerScreen = { mode ->
                    sharedViewModel.setSelectedMode(mode)
                    navController.navigate(Destinations.TruthOrDareAddPlayerScreen.route)
                }
            )

        }
        composable(Destinations.TruthOrDareGameScreen.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Destinations.TruthOrDareGameGraph.route)
            }
            val sharedViewModel: SharedTruthOrDareViewModel = hiltViewModel(parentEntry)

            TruthOrDareGameScreen(
                viewmodel = sharedViewModel,
                onGameEnd = {
                    navController.navigate(Destinations.HomeScreen.route) {
                        popUpTo(Destinations.TruthOrDareGameGraph.route) {
                            inclusive = true // Clear the back stack
                        }
                    }
                }
            )
        }
    }
}
