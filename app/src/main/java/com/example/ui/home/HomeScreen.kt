package com.example.ui.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.model.CbseCategory
import com.example.model.Language
import com.example.ui.components.LanguageSelectorTabs
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsPrimary
import com.example.ui.theme.KidsPurple
import com.example.ui.theme.KidsSecondary
import com.example.ui.theme.KidsTertiary
import com.example.ui.theme.KidsYellow
import com.example.util.SoundFxManager
import com.example.util.TtsManager
import com.example.util.performSubtleSuccessHaptic

data class AlphabetPronounceItem(val letter: String, val word: String, val emoji: String)

val englishAlphabets = listOf(
    AlphabetPronounceItem("A", "Apple", "🍎"),
    AlphabetPronounceItem("B", "Ball", "⚽"),
    AlphabetPronounceItem("C", "Cat", "🐱"),
    AlphabetPronounceItem("D", "Dog", "🐶"),
    AlphabetPronounceItem("E", "Elephant", "🐘"),
    AlphabetPronounceItem("F", "Fish", "🐟"),
    AlphabetPronounceItem("G", "Giraffe", "🦒"),
    AlphabetPronounceItem("H", "House", "🏠"),
    AlphabetPronounceItem("I", "Ice Cream", "🍦"),
    AlphabetPronounceItem("J", "Juice", "🧃"),
    AlphabetPronounceItem("K", "Kite", "🪁"),
    AlphabetPronounceItem("L", "Lion", "🦁"),
    AlphabetPronounceItem("M", "Monkey", "🐒"),
    AlphabetPronounceItem("N", "Nest", "🪹"),
    AlphabetPronounceItem("O", "Orange", "🍊"),
    AlphabetPronounceItem("P", "Parrot", "🦜"),
    AlphabetPronounceItem("Q", "Queen", "👑"),
    AlphabetPronounceItem("R", "Rabbit", "🐰"),
    AlphabetPronounceItem("S", "Sun", "☀️"),
    AlphabetPronounceItem("T", "Tiger", "🐯"),
    AlphabetPronounceItem("U", "Umbrella", "☂️"),
    AlphabetPronounceItem("V", "Van", "🚐"),
    AlphabetPronounceItem("W", "Watermelon", "🍉"),
    AlphabetPronounceItem("X", "Xylophone", "🎼"),
    AlphabetPronounceItem("Y", "Yak", "🐂"),
    AlphabetPronounceItem("Z", "Zebra", "🦓")
)

val teluguAlphabets = listOf(
    AlphabetPronounceItem("అ", "అమ్మ", "👩"),
    AlphabetPronounceItem("ఆ", "ఆవు", "🐄"),
    AlphabetPronounceItem("ఇ", "ఇల్లు", "🏠"),
    AlphabetPronounceItem("ఈ", "ఈగ", "🪰"),
    AlphabetPronounceItem("ఉ", "ఉడుత", "🐿️"),
    AlphabetPronounceItem("ఊ", "ఊయల", "🛝"),
    AlphabetPronounceItem("ఎ", "ఎలుక", "🐀"),
    AlphabetPronounceItem("ఏ", "ఏనుగు", "🐘"),
    AlphabetPronounceItem("ఐ", "ఐస్క్రీమ్", "🍦"),
    AlphabetPronounceItem("ఒ", "ఒంటె", "🐪"),
    AlphabetPronounceItem("ఓ", "ఓడ", "🚢"),
    AlphabetPronounceItem("ఔ", "ఔషధం", "💊"),
    AlphabetPronounceItem("క", "కమలం", "🪷"),
    AlphabetPronounceItem("గ", "గంట", "🔔"),
    AlphabetPronounceItem("చ", "చదరంగం", "♟️"),
    AlphabetPronounceItem("జ", "జల్లెడ", "🫧")
)

val hindiAlphabets = listOf(
    AlphabetPronounceItem("अ", "अनार", "🍎"),
    AlphabetPronounceItem("आ", "आम", "🥭"),
    AlphabetPronounceItem("इ", "इमली", "🫘"),
    AlphabetPronounceItem("ई", "ईख", "🌾"),
    AlphabetPronounceItem("उ", "उल्लू", "🦉"),
    AlphabetPronounceItem("ऊ", "ऊन", "🧶"),
    AlphabetPronounceItem("ऋ", "ऋषि", "🧘"),
    AlphabetPronounceItem("ए", "एड़ी", "🦶"),
    AlphabetPronounceItem("ऐ", "ऐनक", "👓"),
    AlphabetPronounceItem("ओ", "ओखली", "🥣"),
    AlphabetPronounceItem("औ", "औरत", "👩"),
    AlphabetPronounceItem("क", "कमल", "🪷"),
    AlphabetPronounceItem("ख", "खरगोश", "🐰"),
    AlphabetPronounceItem("ग", "गमला", "🪴"),
    AlphabetPronounceItem("घ", "घर", "🏠")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    user: UserEntity?,
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onSelectCategory: (CbseCategory?) -> Unit,
    onStartDailyQuests: () -> Unit,
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

    var customTextToVoice by remember { mutableStateOf("A B C D E") }
    var lastSpokenItem by remember { mutableStateOf<AlphabetPronounceItem?>(null) }

    val alphabetList = when (currentLanguage) {
        Language.ENGLISH -> englishAlphabets
        Language.TELUGU -> teluguAlphabets
        Language.HINDI -> hindiAlphabets
    }

    fun speakCustomText(slowLetters: Boolean = false) {
        soundFx.playClickSound()
        if (customTextToVoice.isBlank()) return

        if (slowLetters) {
            val spokenLetters = customTextToVoice
                .filter { it.isLetterOrDigit() || it.isWhitespace() }
                .map { "$it." }
                .joinToString(" ")
            ttsManager.speak(spokenLetters, currentLanguage)
        } else {
            ttsManager.speak(customTextToVoice, currentLanguage)
        }
    }

    fun pronounceAlphabetItem(item: AlphabetPronounceItem) {
        soundFx.playClickSound()
        performSubtleSuccessHaptic(context, haptic)
        lastSpokenItem = item
        val textToSpeak = when (currentLanguage) {
            Language.ENGLISH -> "Letter ${item.letter}. ${item.letter} for ${item.word}!"
            Language.TELUGU -> "${item.letter} - ${item.word}"
            Language.HINDI -> "${item.letter} - ${item.word}"
        }
        ttsManager.speak(textToSpeak, currentLanguage)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 3-Tab Language Switcher (1. English, 2. Hindi, 3. Telugu)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Select Language / భాష / भाषा:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LanguageSelectorTabs(
                    currentLanguage = currentLanguage,
                    onLanguageSelected = onLanguageSelected
                )
            }
        }

        // Hero Adventure Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val heroDrawableId = context.resources.getIdentifier(
                        "img_hero_banner_1785400631074",
                        "drawable",
                        context.packageName
                    )
                    if (heroDrawableId != 0) {
                        Image(
                            painter = painterResource(id = heroDrawableId),
                            contentDescription = "CBSE Word Adventure Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(KidsPrimary, KidsPurple)
                                    )
                                )
                        )
                    }

                    // Scrim gradient overlay for readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${currentLanguage.flagEmoji} ${currentLanguage.displayName}",
                            color = KidsYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "MB Babies Learning World",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "ABCs, Math, Science, Social Studies & Word Puzzles!",
                            color = Color.White.copy(alpha = 0.95f),
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onSelectCategory(null) },
                            colors = ButtonDefaults.buttonColors(containerColor = KidsSecondary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("quick_start_button")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Play All Puzzles", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // TEXT TO VOICE / LETTER PRONOUNCE SECTION
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("text_to_voice_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = KidsPurple,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.RecordVoiceOver,
                                        contentDescription = "Text to Voice",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "🗣️ Text to Voice Pronouncer",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Type letters, words, or sentences to speak aloud!",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Input Text Field
                    OutlinedTextField(
                        value = customTextToVoice,
                        onValueChange = { customTextToVoice = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("text_to_voice_input_field"),
                        placeholder = { Text("Type letters e.g. A B C D or Cat") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KidsPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        trailingIcon = {
                            if (customTextToVoice.isNotEmpty()) {
                                IconButton(onClick = { customTextToVoice = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        }
                    )

                    // Speak Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { speakCustomText(slowLetters = false) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("speak_text_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KidsPrimary),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("🔊 Speak Word", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { speakCustomText(slowLetters = true) },
                            modifier = Modifier
                                .weight(1.1f)
                                .testTag("pronounce_letters_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = KidsOrange),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp)
                        ) {
                            Text("🔤 Spell Letter-by-Letter", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                        }
                    }

                    // Quick Preset Buttons for Kids
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Presets:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        listOf("A B C", "1 2 3", "Cat", "Sun", "Apple").forEach { preset ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = KidsPurple.copy(alpha = 0.12f),
                                modifier = Modifier.clickable {
                                    customTextToVoice = preset
                                    speakCustomText(slowLetters = false)
                                }
                            ) {
                                Text(
                                    text = preset,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidsPurple,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // INTERACTIVE A-Z ALPHABET & LETTER SOUND BOARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("alphabet_sound_board_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "🔤 Tap & Pronounce Alphabet Board",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Tap any letter tile to hear clear voice pronunciation!",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = KidsGreen.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = currentLanguage.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = KidsGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Active Spoken Preview Banner
                    if (lastSpokenItem != null) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = KidsYellow.copy(alpha = 0.25f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, KidsOrange.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(lastSpokenItem!!.emoji, fontSize = 32.sp)
                                Column {
                                    Text(
                                        text = "${lastSpokenItem!!.letter} for ${lastSpokenItem!!.word}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Listening to voice audio...",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // FlowRow of Letter Tiles
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 5
                    ) {
                        alphabetList.forEach { item ->
                            val isSelected = lastSpokenItem?.letter == item.letter
                            val scaleAnim by animateFloatAsState(
                                targetValue = if (isSelected) 1.15f else 1f,
                                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                                label = "LetterTileScale"
                            )

                            val tileBg = if (isSelected) KidsPrimary else MaterialTheme.colorScheme.surfaceVariant
                            val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = tileBg,
                                border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, KidsPrimary.copy(alpha = 0.2f)) else null,
                                shadowElevation = if (isSelected) 6.dp else 1.dp,
                                modifier = Modifier
                                    .size(58.dp)
                                    .scale(scaleAnim)
                                    .clickable { pronounceAlphabetItem(item) }
                                    .testTag("letter_tile_${item.letter}")
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = item.letter,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        color = textColor
                                    )
                                    Text(
                                        text = item.emoji,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Streak & Daily Quest Prompt Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartDailyQuests() }
                    .testTag("streak_card"),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(KidsSecondary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Whatshot,
                                contentDescription = null,
                                tint = KidsSecondary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "${user?.streakDays ?: 1} Day Learning Streak 🔥",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Complete daily quests for +150 XP bonus",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = KidsGreen
                    )
                }
            }
        }

        // Category Selection Title
        item {
            Text(
                text = "Explore CBSE Curriculum Categories",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Horizontal Category Cards
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(CbseCategory.entries.toTypedArray()) { cat ->
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .height(180.dp)
                            .clickable { onSelectCategory(cat) }
                            .testTag("category_card_${cat.name}"),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start
                        ) {
                            val catDrawableId = when (cat) {
                                CbseCategory.FRUITS -> context.resources.getIdentifier("img_fruits_category_1785400644132", "drawable", context.packageName)
                                CbseCategory.ANIMALS -> context.resources.getIdentifier("img_animals_category_1785400661784", "drawable", context.packageName)
                                else -> 0
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(KidsTertiary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (catDrawableId != 0) {
                                    Image(
                                        painter = painterResource(id = catDrawableId),
                                        contentDescription = cat.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(
                                        text = when (cat) {
                                            CbseCategory.FRUITS -> "🍎"
                                            CbseCategory.ANIMALS -> "🦁"
                                            CbseCategory.NATURE -> "☀️"
                                            CbseCategory.SCHOOL -> "🎒"
                                            CbseCategory.COLORS_NUMBERS -> "🎨"
                                        },
                                        fontSize = 40.sp
                                    )
                                }
                            }

                            Text(
                                text = cat.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Start",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = KidsPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = KidsPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Learning Standards Info Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📖 CBSE Grade 1 Learning Objectives",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Aligned with NCERT / CBSE curriculum for phonics, sight word recognition, letter tile unscramble, and bilingual vocabulary development in English, Telugu, and Hindi.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

