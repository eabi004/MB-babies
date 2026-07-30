package com.example.ui.math

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.ui.components.ParticleOverlay
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsPrimary
import com.example.ui.theme.KidsPurple
import com.example.ui.theme.KidsSecondary
import com.example.ui.theme.KidsYellow
import com.example.util.SoundFxManager
import com.example.util.TtsManager
import com.example.util.performSubtleSuccessHaptic

enum class MathType(val displayName: String, val emoji: String, val color: Color) {
    ADDITION("Addition", "➕", KidsPrimary),
    SUBTRACTION("Subtraction", "➖", KidsOrange),
    COUNTING("Counting", "🍎", KidsGreen)
}

data class MathPuzzle(
    val id: String,
    val type: MathType,
    val num1: Int,
    val num2: Int,
    val answer: Int,
    val emojiItem: String,
    val options: List<Int>
)

val sampleMathPuzzles = listOf(
    // Addition
    MathPuzzle("add_1", MathType.ADDITION, 3, 2, 5, "🍎", listOf(4, 5, 6, 3)),
    MathPuzzle("add_2", MathType.ADDITION, 4, 3, 7, "🎈", listOf(6, 7, 8, 5)),
    MathPuzzle("add_3", MathType.ADDITION, 2, 2, 4, "⭐", listOf(3, 4, 5, 2)),
    MathPuzzle("add_4", MathType.ADDITION, 5, 1, 6, "⚽", listOf(5, 6, 7, 4)),
    MathPuzzle("add_5", MathType.ADDITION, 6, 3, 9, "🍪", listOf(8, 9, 10, 7)),

    // Subtraction
    MathPuzzle("sub_1", MathType.SUBTRACTION, 5, 2, 3, "🎈", listOf(2, 3, 4, 1)),
    MathPuzzle("sub_2", MathType.SUBTRACTION, 6, 3, 3, "🍎", listOf(2, 3, 4, 5)),
    MathPuzzle("sub_3", MathType.SUBTRACTION, 4, 1, 3, "⭐", listOf(1, 2, 3, 4)),
    MathPuzzle("sub_4", MathType.SUBTRACTION, 7, 4, 3, "⚽", listOf(2, 3, 4, 5)),
    MathPuzzle("sub_5", MathType.SUBTRACTION, 8, 3, 5, "🍪", listOf(4, 5, 6, 7)),

    // Counting
    MathPuzzle("cnt_1", MathType.COUNTING, 6, 0, 6, "🍎", listOf(4, 5, 6, 7)),
    MathPuzzle("cnt_2", MathType.COUNTING, 8, 0, 8, "⭐", listOf(6, 7, 8, 9)),
    MathPuzzle("cnt_3", MathType.COUNTING, 5, 0, 5, "⚽", listOf(3, 4, 5, 6))
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MathScreen(
    currentLanguage: Language,
    onAddReward: (xp: Int, stars: Int, coins: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val soundFx = remember { SoundFxManager() }
    val ttsManager = remember(context) { TtsManager(context) }

    DisposableEffect(Unit) {
        onDispose {
            soundFx.release()
            ttsManager.shutdown()
        }
    }

    var selectedType by remember { mutableStateOf(MathType.ADDITION) }
    var currentPuzzleIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var isSolved by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showCelebration by remember { mutableStateOf(false) }
    var countedItems by remember { mutableStateOf(setOf<Int>()) }

    val filteredPuzzles = remember(selectedType) {
        sampleMathPuzzles.filter { it.type == selectedType }
    }

    val puzzle = filteredPuzzles.getOrElse(currentPuzzleIndex % filteredPuzzles.size) {
        sampleMathPuzzles.first()
    }

    fun speakEquation() {
        val textToSpeak = when (puzzle.type) {
            MathType.ADDITION -> "${puzzle.num1} plus ${puzzle.num2} equals what?"
            MathType.SUBTRACTION -> "${puzzle.num1} minus ${puzzle.num2} equals what?"
            MathType.COUNTING -> "Count the items: how many ${puzzle.emojiItem} are there?"
        }
        ttsManager.speak(textToSpeak, Language.ENGLISH)
    }

    fun handleAnswerSelect(num: Int) {
        if (isSolved) return
        selectedAnswer = num
        soundFx.playClickSound()

        if (num == puzzle.answer) {
            isSolved = true
            errorMessage = null
            soundFx.playCorrectSound()
            performSubtleSuccessHaptic(context, haptic)
            onAddReward(100, 2, 20)
            showCelebration = true

            val text = when (puzzle.type) {
                MathType.ADDITION -> "Awesome! ${puzzle.num1} plus ${puzzle.num2} equals ${puzzle.answer}!"
                MathType.SUBTRACTION -> "Great job! ${puzzle.num1} minus ${puzzle.num2} equals ${puzzle.answer}!"
                MathType.COUNTING -> "Correct! There are ${puzzle.answer} items!"
            }
            ttsManager.speak(text, Language.ENGLISH)
        } else {
            soundFx.playIncorrectSound()
            errorMessage = "Not quite! Try counting the visual items."
            ttsManager.speak("Try again!", Language.ENGLISH)
        }
    }

    fun nextPuzzle() {
        soundFx.playClickSound()
        selectedAnswer = null
        isSolved = false
        errorMessage = null
        showCelebration = false
        countedItems = emptySet()
        currentPuzzleIndex = (currentPuzzleIndex + 1) % filteredPuzzles.size
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("math_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Mode Selector Tabs
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MathType.entries.forEach { type ->
                        val isSelected = selectedType == type
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) type.color else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedType = type
                                    currentPuzzleIndex = 0
                                    selectedAnswer = null
                                    isSolved = false
                                    errorMessage = null
                                    countedItems = emptySet()
                                }
                                .testTag("math_type_tab_${type.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(type.emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = type.displayName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Main Puzzle Area Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("math_puzzle_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title & Audio Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = puzzle.type.color.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${puzzle.type.emoji} ${puzzle.type.displayName} Puzzle",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontWeight = FontWeight.Bold,
                                color = puzzle.type.color,
                                fontSize = 13.sp
                            )
                        }

                        IconButton(
                            onClick = { speakEquation() },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(KidsPrimary)
                                .testTag("math_audio_speak_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen",
                                tint = Color.White
                            )
                        }
                    }

                    // Equation Display Banner
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (puzzle.type == MathType.COUNTING) {
                            Text(
                                text = "How many ${puzzle.emojiItem} ?",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            // Number 1
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = KidsPrimary.copy(alpha = 0.15f),
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${puzzle.num1}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 28.sp,
                                        color = KidsPrimary
                                    )
                                }
                            }

                            // Operator (+ or -)
                            Text(
                                text = if (puzzle.type == MathType.ADDITION) "➕" else "➖",
                                fontSize = 22.sp
                            )

                            // Number 2
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = KidsOrange.copy(alpha = 0.15f),
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${puzzle.num2}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 28.sp,
                                        color = KidsOrange
                                    )
                                }
                            }

                            // Equals
                            Text("=", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)

                            // Answer Slot Tile
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSolved) KidsGreen else KidsPurple.copy(alpha = 0.2f),
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = selectedAnswer?.toString() ?: "?",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 28.sp,
                                        color = if (isSolved) Color.White else KidsPurple
                                    )
                                }
                            }
                        }
                    }

                    // Visual Counting Aids Section
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "👇 Tap visual items to count!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (puzzle.type == MathType.ADDITION) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Group 1
                                    FlowRow(
                                        maxItemsInEachRow = 3,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        repeat(puzzle.num1) { idx ->
                                            val itemId = idx
                                            val isTapped = countedItems.contains(itemId)
                                            Box(
                                                modifier = Modifier
                                                    .padding(4.dp)
                                                    .size(42.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isTapped) KidsGreen.copy(alpha = 0.3f) else Color.Transparent)
                                                    .clickable {
                                                        soundFx.playClickSound()
                                                        countedItems = countedItems + itemId
                                                        ttsManager.speak("${itemId + 1}", Language.ENGLISH)
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(puzzle.emojiItem, fontSize = 28.sp)
                                            }
                                        }
                                    }

                                    Text("➕", fontSize = 20.sp)

                                    // Group 2
                                    FlowRow(
                                        maxItemsInEachRow = 3,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        repeat(puzzle.num2) { idx ->
                                            val itemId = puzzle.num1 + idx
                                            val isTapped = countedItems.contains(itemId)
                                            Box(
                                                modifier = Modifier
                                                    .padding(4.dp)
                                                    .size(42.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isTapped) KidsGreen.copy(alpha = 0.3f) else Color.Transparent)
                                                    .clickable {
                                                        soundFx.playClickSound()
                                                        countedItems = countedItems + itemId
                                                        ttsManager.speak("${itemId + 1}", Language.ENGLISH)
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(puzzle.emojiItem, fontSize = 28.sp)
                                            }
                                        }
                                    }
                                }
                            } else if (puzzle.type == MathType.SUBTRACTION) {
                                FlowRow(
                                    maxItemsInEachRow = 5,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(puzzle.num1) { idx ->
                                        val isSubtracted = idx >= (puzzle.num1 - puzzle.num2)
                                        Box(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(if (isSubtracted) Color.LightGray.copy(alpha = 0.3f) else KidsOrange.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = puzzle.emojiItem,
                                                fontSize = 28.sp,
                                                modifier = Modifier.scale(if (isSubtracted) 0.7f else 1f)
                                            )
                                            if (isSubtracted) {
                                                Text("❌", fontSize = 20.sp)
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Counting
                                FlowRow(
                                    maxItemsInEachRow = 4,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(puzzle.num1) { idx ->
                                        val itemId = idx
                                        val isTapped = countedItems.contains(itemId)
                                        Box(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .size(44.dp)
                                                .clip(CircleShape)
                                                .background(if (isTapped) KidsGreen.copy(alpha = 0.3f) else Color.Transparent)
                                                .clickable {
                                                    soundFx.playClickSound()
                                                    countedItems = countedItems + itemId
                                                    ttsManager.speak("${itemId + 1}", Language.ENGLISH)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(puzzle.emojiItem, fontSize = 30.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Error message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Option Answers Tiles (Tap / Drag to Place)
                    Text(
                        text = "Choose the correct number:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        puzzle.options.shuffled().forEach { option ->
                            val isSelectedOption = selectedAnswer == option
                            val scaleAnim by animateFloatAsState(
                                targetValue = if (isSelectedOption) 1.1f else 1f,
                                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                                label = "OptionScale"
                            )

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelectedOption && isSolved) KidsGreen else KidsPrimary,
                                shadowElevation = 4.dp,
                                modifier = Modifier
                                    .weight(1f)
                                    .scale(scaleAnim)
                                    .clickable { handleAnswerSelect(option) }
                                    .testTag("math_option_$option")
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$option",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    // Solved Next Puzzle Button
                    if (isSolved) {
                        Button(
                            onClick = { nextPuzzle() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("next_math_puzzle_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KidsGreen)
                        ) {
                            Text("Next Math Puzzle 🚀", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

        ParticleOverlay(
            trigger = showCelebration,
            modifier = Modifier.fillMaxSize()
        )
    }

    // Celebration Dialog
    if (showCelebration) {
        AlertDialog(
            onDismissRequest = { showCelebration = false },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎉 MATH SUPERSTAR! 🎉", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = KidsGreen)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("You solved the puzzle!", fontSize = 14.sp)
                }
            },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("⭐ +2 Stars", fontWeight = FontWeight.Bold, color = KidsOrange)
                    Text("🪙 +20 Coins", fontWeight = FontWeight.Bold, color = KidsYellow)
                    Text("⚡ +100 XP", fontWeight = FontWeight.Bold, color = KidsGreen)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCelebration = false
                        nextPuzzle()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KidsPrimary)
                ) {
                    Text("Play Next Math Puzzle!", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
