package com.example.gameapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gameapp.ui.presentation.homeScreen.HomeScreen
import com.example.gameapp.ui.presentation.splashScreen.SplashScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SplashScreen.route
    ) {

        composable(Destinations.SplashScreen.route) {
            SplashScreen(navigateAhead = {
                navController.navigate(Destinations.HomeScreen.route) {
                    popUpTo(Destinations.SplashScreen.route) {
                        inclusive = true
                    }
                }
            })
        }
        composable(Destinations.HomeScreen.route) {
            HomeScreen(
                navigateToAddPlayerScreen = {
                    navController.navigate(Destinations.TruthOrDareGameGraph.route)
                },
                navController = navController
            )
        }

        truthOrDareGameGraph(navController)

        composable(Destinations.SettingsScreen.route) {
//            SettingsScreen(
//                navController = navController
//            )
        }
    }
}

