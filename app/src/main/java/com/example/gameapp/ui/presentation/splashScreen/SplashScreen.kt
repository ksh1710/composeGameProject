package com.example.gameapp.ui.presentation.splashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.gameapp.ui.theme.GameAppTheme
import kotlinx.coroutines.delay
import com.example.gameapp.R
import com.example.gameapp.ui.theme.SplashDark
import com.example.gameapp.ui.theme.SplashLight

@Composable
fun SplashScreen(
    navigateAhead: () -> Unit
) {

    LaunchedEffect(key1 = true) {
        delay(1000L)
        navigateAhead()
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        SplashLight, SplashDark
                    ,

            )
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.splash_bg),
            contentDescription = "Logo"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    GameAppTheme {
        SplashScreen(navigateAhead = {})
    }
}