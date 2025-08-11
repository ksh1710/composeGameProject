package com.example.gameapp.ui.presentation.truthAndDareGame.truthOrDareGameScreen

import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gameapp.data.values.bottleDesigns
import com.example.gameapp.ui.presentation.truthAndDareGame.truthOrDareGameScreen.components.BottleCanvas
import com.example.gameapp.utils.GameMode
import com.example.gameapp.utils.determinePlayersFromRotation
import com.example.gameapp.utils.spinBottle
import com.example.gameapp.viewmodel.SharedTruthOrDareViewModel
import kotlinx.coroutines.launch
import com.example.gameapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TruthOrDareGameScreen(
    viewmodel: SharedTruthOrDareViewModel,
    onGameEnd: () -> Unit
) {
    val players by viewmodel.players.collectAsState()
    val selectedMode by viewmodel.selectedMode.collectAsState()
    val askingPlayer = remember { mutableStateOf<String?>(null) }
    val answeringPlayer = remember { mutableStateOf<String?>(null) }
    val taskPrompt = remember { mutableStateOf<String?>(null) }
    val truth = viewmodel.getListOfTruths(selectedMode ?: GameMode.FRIENDS).random()
    val dare = viewmodel.getListOfDares(selectedMode ?: GameMode.FRIENDS).random()
    val scores by viewmodel.playerScores.collectAsState()

    var showWinnerDialog by remember { mutableStateOf(false) }
    var winnerName by remember { mutableStateOf("") }


    var selectedPoints by remember { mutableIntStateOf(0) }


    val ctx = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(ctx, R.raw.bottle_sound)
    }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    // Bottle selection state
    var selectedBottleIndex by remember { mutableIntStateOf(0) }
    var showBottleSelector by remember { mutableStateOf(false) }


    // Animation states
    var spinning by remember { mutableStateOf(false) }
    var shouldSpin by remember { mutableStateOf(false) }
    var targetRotation by remember { mutableFloatStateOf(0f) }
    val bottleRotation = remember { Animatable(0f) }

    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }


    // Animation effect - This is the core of the rotation system
    LaunchedEffect(shouldSpin) {
        if (shouldSpin) {
            spinning = true
            mediaPlayer.start()

            // The animation interpolates from current rotation to target rotation
            // over 3 seconds with easing that mimics real physics
            bottleRotation.animateTo(
                targetValue = targetRotation,
                animationSpec = tween(
                    durationMillis = 3000,
                    easing = FastOutSlowInEasing // Starts fast, slows down naturally
                )
            )
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.prepare() // Prepare the player for the next spin
            }

            // After animation completes, determine the players
            val (asking, answering) = determinePlayersFromRotation(
                targetRotation,
                players
            )
            askingPlayer.value = asking
            answeringPlayer.value = answering
            spinning = false
            shouldSpin = false
        }
    }
    Scaffold { paddingVal ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingVal)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                // Header with bottle selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Truth or Dare",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Spin the Bottle",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    // Bottle selector button
                    IconButton(
                        onClick = { showBottleSelector = true },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Change bottle design",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
//                Spacer(modifier = Modifier.height(32.dp))
            item {
//            // Bottle in the center with rotation animation
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .graphicsLayer {
                            // This applies the rotation transformation
                            rotationZ = bottleRotation.value
                            transformOrigin = TransformOrigin(0.5f, 0.5f) // Rotate around center
                        }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = !spinning && players.size >= 2
                        ) {
                            spinBottle(
                                spinning = spinning,
                                playersSize = players.size,
                                askingPlayer = askingPlayer,
                                answeringPlayer = answeringPlayer,
                                taskPrompt = taskPrompt,
                                setTargetRotation = { targetRotation = it },
                                bottleRotation = bottleRotation,
                                setShouldSpin = { shouldSpin = it }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Draw the selected bottle design
                    BottleCanvas(
                        design = bottleDesigns[selectedBottleIndex],
                        modifier = Modifier.size(120.dp)
                    )

//                    BottleDesign(
//                        modifier = Modifier.size(300.dp)
//                    )
                }
//    }\
                Spacer(modifier = Modifier.height(16.dp))

                // Spin instruction or status
                if (spinning) {
                    Text(
                        "🌀 Spinning...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else if (askingPlayer.value == null) {
                    Text(
                        "Tap the bottle to spin!",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            item {
                // Game interaction
                if (!spinning && askingPlayer.value != null && answeringPlayer.value != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "🗣️ ${askingPlayer.value} asks ${answeringPlayer.value}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (taskPrompt.value == null) {
                                Row {
                                    // Truth button
                                    Button(
                                        onClick = {
                                            taskPrompt.value = truth
                                            selectedPoints = 1 // Truth worth 1 point
                                        },
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(100.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent
                                        ),
                                        contentPadding = PaddingValues()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    brush = Brush.horizontalGradient(
                                                        colors = listOf(
                                                            Color(0xFF6A11CB),
                                                            Color(0xFF2575FC)
                                                        )
                                                    ),
                                                    shape = RoundedCornerShape(25.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Truth",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(Modifier.width(16.dp))

                                    // Dare button
                                    Button(
                                        onClick = {
                                            taskPrompt.value = dare
                                            selectedPoints = 2 // Dare worth 2 points

                                        },
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(100.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent
                                        ),
                                        contentPadding = PaddingValues()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    brush = Brush.horizontalGradient(
                                                        colors = listOf(
                                                            Color(0xFFFF6B6B),
                                                            Color(0xFFFF8E53)
                                                        )
                                                    ),
                                                    shape = RoundedCornerShape(25.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Dare",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            } else {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.1f
                                        )
                                    )
                                ) {
                                    Text(
                                        taskPrompt.value ?: "",
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(16.dp),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
            item {
                if (taskPrompt.value != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                answeringPlayer.let {
                                    viewmodel.addPoints(it.value!!, selectedPoints)
                                }
                                taskPrompt.value = null // Clear for next turn
                                askingPlayer.value = null
                                answeringPlayer.value = null

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Completed ✅")
                        }
                        Button(
                            onClick = {
                                taskPrompt.value = null // No points awarded
                                askingPlayer.value = null
                                answeringPlayer.value = null

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                        ) {
                            Text("Skipped ❌")
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Scores", fontWeight = FontWeight.Bold)
                    scores.forEach { (player, score) ->
                        Text("$player: $score points")
                    }
                }

            }
            // Reset game button
            item {
                Row {
                    Text(
                        modifier = Modifier.clickable {

                            val maxScore = scores.maxByOrNull { it.value }
                            if (maxScore != null) {
                                winnerName = maxScore.key
                                showWinnerDialog = true
                            }

                            coroutineScope.launch {
                                askingPlayer.value = null
                                answeringPlayer.value = null
                                taskPrompt.value = null
                                spinning = false
                                shouldSpin = false
                                bottleRotation.snapTo(0f)
                            }
                        },
                        text = "End Game",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Bottle Selector Dialog
            item {
                if (showBottleSelector) {
                    Dialog(onDismissRequest = { showBottleSelector = false }) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    "Choose Bottle Design",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.height(300.dp)
                                ) {
                                    itemsIndexed(bottleDesigns) { index, design ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(1f)
                                                .clickable {
                                                    selectedBottleIndex = index
                                                    showBottleSelector = false
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (index == selectedBottleIndex) {
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                } else {
                                                    MaterialTheme.colorScheme.surface
                                                }
                                            ),
                                            border = if (index == selectedBottleIndex) {
                                                BorderStroke(
                                                    2.dp,
                                                    MaterialTheme.colorScheme.primary
                                                )
                                            } else null
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(8.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                BottleCanvas(
                                                    design = design,
                                                    modifier = Modifier.size(60.dp)
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    design.name,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = { showBottleSelector = false }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showWinnerDialog) {
            AlertDialog(
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                ),
                onDismissRequest = { showWinnerDialog = false },
                title = {
                    Text(
                        text = "🏆 Winner! 🏆",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Animated Emoji Celebration
                        val infiniteTransition = rememberInfiniteTransition(label = "")
                        val offset by infiniteTransition.animateFloat(
                            initialValue = -10f,
                            targetValue = 10f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(500, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = ""
                        )

                        Text(
                            text = "🎉 $winnerName 🎉",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(y = offset.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Congratulations! You conquered Truth or Dare 😄")
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        showWinnerDialog = false
//                            onGameEnd()
                        viewmodel.resetScores()
                    }) {
                        Text("Play Again 🔄")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showWinnerDialog = false
                        onGameEnd()
                    }) {
                        Text("Exit 🏠")
                    }
                }
            )
        }
    }
}
