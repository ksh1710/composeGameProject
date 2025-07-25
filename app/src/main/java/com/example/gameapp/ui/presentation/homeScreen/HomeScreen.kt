package com.example.gameapp.ui.presentation.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gameapp.ui.common.BottomBar
import com.example.gameapp.utils.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAddPlayerScreen: () -> Unit,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItem =
        BottomNavItem.entries.find { it.navDestination.route == currentRoute } ?: BottomNavItem.HOME

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Home Screen")
            })
        },
        bottomBar = {
            BottomBar(navController = navController, selectedItem = selectedItem)
        }
    ) { it ->
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                Card(
                    modifier = Modifier.size(100.dp),
                    onClick = navigateToAddPlayerScreen,
                    colors = CardDefaults.cardColors(containerColor = Color.Cyan)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Logo"
                        )
                        Text(text = "Truth or Dare", color = Color.Black)
                    }
                }
            }
        }
    }
}