package com.example.gameapp.ui.navigation

import androidx.navigation.NamedNavArgument
import kotlinx.serialization.Serializable


object AppRoutes {
    const val SPLASH_SCREEN = "splashScreen"
    const val HOME_SCREEN = "homeScreen"
    const val SETTINGS_SCREEN = "settingsScreen"

    // Base route for the Truth or Dare Game
    const val TRUTH_OR_DARE_GAME_GRAPH = "truthOrDareGameGraph"

    // Routes *within* the Truth or Dare Game Graph
    const val TRUTH_OR_DARE_ADD_PLAYER_SCREEN = "truthOrDareAddPlayerScreen"
    const val TRUTH_OR_DARE_MODE_SCREEN = "truthOrDareModeScreen"
    const val TRUTH_OR_DARE_GAME_SCREEN = "truthOrDareActualGameScreen"
}

@Serializable
sealed class Destinations(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object SplashScreen : Destinations(AppRoutes.SPLASH_SCREEN)
    data object HomeScreen : Destinations(AppRoutes.HOME_SCREEN)
    data object SettingsScreen : Destinations(AppRoutes.SETTINGS_SCREEN)

    // Represents the entry point to the Truth or Dare game flow
    data object TruthOrDareGameGraph : Destinations(AppRoutes.TRUTH_OR_DARE_GAME_GRAPH)

    data object TruthOrDareAddPlayerScreen : Destinations(AppRoutes.TRUTH_OR_DARE_ADD_PLAYER_SCREEN)
    data object TruthOrDareModeScreen : Destinations(AppRoutes.TRUTH_OR_DARE_MODE_SCREEN)
    data object TruthOrDareGameScreen : Destinations(AppRoutes.TRUTH_OR_DARE_GAME_SCREEN)

}