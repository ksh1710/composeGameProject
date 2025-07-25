package com.example.gameapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gameapp.ui.theme.bottomBarDark
import com.example.gameapp.ui.theme.bottomBarLight
import com.example.gameapp.utils.BottomNavItem
import com.example.gameapp.utils.navigateTO


@Composable
fun BottomBar(navController: NavController, selectedItem: BottomNavItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(bottomBarLight),

        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (item in BottomNavItem.entries) {
            Column(
                modifier = Modifier
                    .clickable {
                        navigateTO(
                            navController = navController,
                            route = item.navDestination.route
                        )
                    }
                    .clip(CircleShape)
                    .background(if (item == selectedItem) bottomBarDark else Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(30.dp),
                    colorFilter = ColorFilter . tint (Color.White)

                )
//                Text(
//                    text = item.title, color = if (item == selectedItem) {
//                        Color.Black
//                    } else {
//                        Color.Gray
//                    }
//                )
            }
        }
    }

}