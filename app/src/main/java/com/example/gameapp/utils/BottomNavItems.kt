package com.example.gameapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.gameapp.ui.navigation.Destinations

enum class BottomNavItem(
    val icon: ImageVector,
    val navDestination: Destinations
) {
    HOME(Icons.Default.Home, Destinations.HomeScreen),
    SETTINGS(Icons.Default.Settings, Destinations.SettingsScreen),
//    LEARN(Icons.Default.Build, Destinations.LearnScreen),
//    PROFILE(Icons.Default.Person, Destinations.ProfileScreen)
}

fun navigateTO(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
    }
}
