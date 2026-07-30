package com.example.ui.puzzle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.PuzzleType
import com.example.model.WordPuzzle
import com.example.ui.components.LanguageSelectorTabs
import com.example.ui.components.ParticleOverlay
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsPrimary
import com.example.ui.theme.KidsPurple
import com.example.ui.theme.KidsSecondary
import com.example.ui.theme.KidsYellow
import com.example.util.performSubtleSuccessHaptic
import com.example.viewmodel.PuzzleUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PuzzleScreen(
    state: PuzzleUiState,
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onSelectUnscrambleLetter: (Char, Int) -> Unit,
    onRemoveUnscrambleLetter: (Int) -> Unit,
    onSelectMissingLetter: (Char) -> Unit,
    onSelectOption: (String) -> Unit,
    onRevealHint: () -> Unit,
    onSpeakWord: () -> Unit,
    onDismissCelebration: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val puzzle = state.currentPuzzle

    LaunchedEffect(state.showCelebrationDialog) {
        if (state.showCelebrationDialog) {
            performSubtleSuccessHaptic(context, haptic)
        }
    }

    if (puzzle == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading CBSE Word Puzzles...", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("puzzle_screen"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // 3 Language Tabs Row
        LanguageSelectorTabs(
            currentLanguage = currentLanguage,
            onLanguageSelected = onLanguageSelected,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Puzzle Category Header & TTS Pronounce Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("puzzle_header_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CBSE Grade 1 • ${puzzle.category.title}",
                        fontSize = 12.sp,
                        color = KidsPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = puzzle.puzzleType.label,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Audio Speaker Button
                IconButton(
                    onClick = onSpeakWord,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(KidsPrimary)
                        .testTag("speak_audio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Pronounce Word",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visual Illustration Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .testTag("puzzle_image_card"),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                val imageResId = puzzle.imageDrawableName?.let {
                    val actualName = if (it == "img_fruits_category") "img_fruits_category_1785400644132"
                    else if (it == "img_animals_category") "img_animals_category_1785400661784"
                    else if (it == "img_hero_banner") "img_hero_banner_1785400631074"
                    else "img_app_icon_1785400617665"

                    context.resources.getIdentifier(actualName, "drawable", context.packageName)
                } ?: 0

                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = puzzle.englishMeaning,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(text = "🎨", fontSize = 60.sp)
                }

                // Meaning Pill
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    color = Color.Black.copy(alpha = 0.75f)
                ) {
                    Text(
                        text = "💡 ${puzzle.englishMeaning} (${puzzle.phonetics})",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Puzzle Interaction Area based on PuzzleType
        when (puzzle.puzzleType) {
            PuzzleType.UNSCRAMBLE -> {
                // Answer Slots
                Text(
                    text = "Tap tiles to build the word:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("answer_slots_row")
                ) {
                    puzzle.word.forEachIndexed { index, _ ->
                        val char = state.selectedLetters.getOrNull(index)
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (char != null) KidsPrimary else MaterialTheme.colorScheme.surfaceContainerHigh
                                )
                                .border(
                                    width = 2.dp,
                                    color = if (char != null) KidsPrimary else MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    if (char != null && index in state.selectedLetters.indices) {
                                        onRemoveUnscrambleLetter(index)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char?.toString() ?: "_",
                                color = if (char != null) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Available Letter Tiles
                Text(
                    text = "Available Letter Tiles:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("available_tiles_row")
                ) {
                    state.availableLetters.forEachIndexed { idx, char ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(KidsSecondary)
                                .clickable { onSelectUnscrambleLetter(char, idx) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }

            PuzzleType.MISSING_LETTER -> {
                // Word with Blank Slot
                Text(
                    text = "Fill in the missing letter:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    puzzle.word.forEachIndexed { index, char ->
                        val isBlank = index == puzzle.missingIndex
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isBlank) KidsPurple else MaterialTheme.colorScheme.surfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isBlank) (state.selectedMissingChar?.toString() ?: "?") else char.toString(),
                                color = if (isBlank) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Options Choices
                val optionsList = if (state.missingLetterOptions.isNotEmpty()) {
                    state.missingLetterOptions
                } else {
                    val targetChar = puzzle.word.getOrNull(puzzle.missingIndex)
                    val pool = when (currentLanguage) {
                        Language.ENGLISH -> listOf('A', 'E', 'I', 'O', 'U', 'B', 'C', 'D', 'M', 'N', 'P', 'R', 'S', 'T')
                        Language.TELUGU -> listOf('క', 'గ', 'చ', 'జ', 'త', 'ద', 'న', 'ప', 'మ', 'య', 'ర', 'ల', 'వ', 'స', 'ు', 'ి', 'ే', 'ా')
                        Language.HINDI -> listOf('क', 'ख', 'ग', 'घ', 'च', 'छ', 'ज', 'झ', 'त', 'द', 'न', 'प', 'ब', 'म', 'य', 'र', 'ल', 'व', 'स', 'ु', 'ी', 'ा', 'े')
                    }
                    val s = mutableSetOf<Char>()
                    if (targetChar != null) s.add(targetChar)
                    pool.filter { it != targetChar }.shuffled().take(6).forEach { s.add(it) }
                    s.toList().shuffled()
                }

                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    optionsList.forEach { char ->
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(KidsPrimary)
                                .clickable { onSelectMissingLetter(char) }
                                .testTag("missing_letter_option_$char"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }

            PuzzleType.PICTURE_MATCH -> {
                Text(
                    text = "Select the matching word:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    puzzle.options.forEach { option ->
                        val isSelected = state.selectedOption == option
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clickable { onSelectOption(option) }
                                .testTag("option_card_$option"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) KidsPrimary else MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Error message feedback
        AnimatedVisibility(visible = state.errorMessage != null) {
            Text(
                text = state.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Hint Button & Hint Reveal Box
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onRevealHint,
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isHintRevealed
            ) {
                Icon(Icons.Default.Lightbulb, contentDescription = "Hint", tint = KidsYellow)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (state.isHintRevealed) "Hint Revealed" else "Get Hint (10 🪙)")
            }

            IconButton(
                onClick = onSpeakWord,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(KidsSecondary)
            ) {
                Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = Color.White)
            }
        }

        if (state.isHintRevealed) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = KidsYellow.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "💡 Hint: ${puzzle.hintText}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Victory Dialog / Celebration Overlay
        if (state.showCelebrationDialog) {
            AlertDialog(
                onDismissRequest = onDismissCelebration,
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🎉 Awesome Job!", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            repeat(state.starsEarned) {
                                Text(text = "⭐", fontSize = 28.sp)
                            }
                        }
                    }
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "You mastered '${puzzle.displayScript}'!",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Awarded +${state.scoreEarned} XP and 25 Coins 🪙")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = onDismissCelebration,
                        colors = ButtonDefaults.buttonColors(containerColor = KidsGreen)
                    ) {
                        Text("Next Word", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.NavigateNext, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    }

    ParticleOverlay(
        trigger = state.showCelebrationDialog,
        modifier = Modifier.fillMaxSize()
    )
}
}
