package com.example.gameapp.ui.presentation.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gameapp.ui.common.BottomBar
import com.example.gameapp.utils.BottomNavItem
import com.example.gameapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAddPlayerScreen: () -> Unit,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
//    val selectedItem =
//        BottomNavItem.entries.find { it.navDestination.route == currentRoute } ?: BottomNavItem.HOME

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Ice Breaker Games")
            })
        },
//        bottomBar = {
//            BottomBar(navController = navController, selectedItem = selectedItem)
//        }
    ) { it ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = it

        ) {
            item {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            navigateToAddPlayerScreen()
                        },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6C6C6C)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .padding(16.dp)
                            .wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp)), // Optional rounded image
                            painter = painterResource(R.drawable.truth_dare_logo),
                            contentDescription = "Logo"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Truth or Dare",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}