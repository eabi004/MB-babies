package com.example.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.quiz.QuizRepository
import com.example.model.Language
import com.example.model.QuizQuestion
import com.example.model.QuizSubject
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
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    currentLanguage: Language,
    onAddReward: (xp: Int, stars: Int, coins: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val soundFx = remember { SoundFxManager() }
    val ttsManager = remember(context) { TtsManager(context) }
    val repository = remember { QuizRepository() }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose {
            soundFx.release()
            ttsManager.shutdown()
        }
    }

    var selectedSubject by remember { mutableStateOf(QuizSubject.ALL) }
    var questionsList by remember { mutableStateOf(repository.getQuestions(QuizSubject.ALL)) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var showCelebration by remember { mutableStateOf(false) }
    var isFetchingInternet by remember { mutableStateOf(false) }
    var internetNotification by remember { mutableStateOf<String?>(null) }

    val currentQuestion = remember(questionsList, currentQuestionIndex) {
        if (questionsList.isNotEmpty()) {
            questionsList[currentQuestionIndex % questionsList.size]
        } else null
    }

    fun filterSubject(subject: QuizSubject) {
        soundFx.playClickSound()
        selectedSubject = subject
        questionsList = repository.getQuestions(subject)
        currentQuestionIndex = 0
        selectedOptionIndex = null
        isAnswered = false
    }

    fun speakCurrentQuestion() {
        if (currentQuestion != null) {
            val textToSpeak = "${currentQuestion.question}. Options are: " + currentQuestion.options.joinToString(", ")
            ttsManager.speak(textToSpeak, Language.ENGLISH)
        }
    }

    fun handleOptionClick(index: Int) {
        if (isAnswered || currentQuestion == null) return
        selectedOptionIndex = index
        isAnswered = true

        if (index == currentQuestion.correctIndex) {
            soundFx.playCorrectSound()
            performSubtleSuccessHaptic(context, haptic)
            onAddReward(100, 2, 20)
            showCelebration = true
            ttsManager.speak("Correct! ${currentQuestion.explanation}", Language.ENGLISH)
        } else {
            soundFx.playIncorrectSound()
            ttsManager.speak("Not quite. The correct answer is ${currentQuestion.options[currentQuestion.correctIndex]}", Language.ENGLISH)
        }
    }

    fun nextQuestion() {
        soundFx.playClickSound()
        selectedOptionIndex = null
        isAnswered = false
        showCelebration = false
        if (questionsList.isNotEmpty()) {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionsList.size
        }
    }

    fun fetchFromInternet() {
        coroutineScope.launch {
            isFetchingInternet = true
            soundFx.playClickSound()
            internetNotification = "Fetching fresh quizzes from internet..."
            val fetched = repository.fetchOnlineQuizQuestions(selectedSubject)
            questionsList = repository.getQuestions(selectedSubject)
            currentQuestionIndex = 0
            selectedOptionIndex = null
            isAnswered = false
            isFetchingInternet = false
            internetNotification = "Loaded ${fetched.size} new ${selectedSubject.displayName} quizzes!"
            ttsManager.speak("New quizzes ready!", Language.ENGLISH)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("science_social_quiz_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Hero Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .testTag("quiz_hero_banner"),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val heroDrawableId = context.resources.getIdentifier(
                        "img_science_social_quiz_1785407729846",
                        "drawable",
                        context.packageName
                    )
                    if (heroDrawableId != 0) {
                        Image(
                            painter = painterResource(id = heroDrawableId),
                            contentDescription = "Science & Social Studies Hero",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.horizontalGradient(listOf(KidsGreen, KidsPurple)))
                        )
                    }

                    // Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color.Black.copy(alpha = 0.75f), Color.Transparent)
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = KidsYellow
                        ) {
                            Text(
                                text = "🔬 SCIENCE & 🌍 SOCIAL STUDIES",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Little Explorer Quiz",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Discover Earth, Space, Animals & Good Habits!",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Subject Category Pills Tabs
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
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuizSubject.entries.forEach { subject ->
                        val isSelected = selectedSubject == subject
                        val activeColor = when (subject) {
                            QuizSubject.SCIENCE -> KidsPurple
                            QuizSubject.SOCIAL_STUDIES -> KidsOrange
                            QuizSubject.MATH -> KidsPrimary
                            QuizSubject.ENGLISH -> KidsSecondary
                            QuizSubject.ALL -> KidsGreen
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) activeColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, activeColor.copy(alpha = 0.3f)) else null,
                            shadowElevation = if (isSelected) 3.dp else 0.dp,
                            modifier = Modifier
                                .clickable { filterSubject(subject) }
                                .testTag("quiz_subject_tab_${subject.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(subject.emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = subject.displayName,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Internet Quiz Fetcher Action Button
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (!isFetchingInternet) fetchFromInternet() }
                    .testTag("fetch_internet_quiz_button"),
                shape = RoundedCornerShape(18.dp),
                color = KidsPrimary.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, KidsPrimary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = KidsPrimary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isFetchingInternet) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CloudDownload,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Column {
                            Text(
                                text = "🌐 Fetch New Quizzes from Internet",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = KidsPrimary
                            )
                            Text(
                                text = internetNotification ?: "Tap to load dynamic AI & online quizzes for ${selectedSubject.displayName}!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = KidsYellow
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Web", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = Color.Black)
                        }
                    }
                }
            }
        }

        // Active Quiz Card
        if (currentQuestion != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quiz_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header row with Subject badge, topic, speak button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                val subjColor = when (currentQuestion.subject) {
                                    QuizSubject.SCIENCE -> KidsPurple
                                    QuizSubject.SOCIAL_STUDIES -> KidsOrange
                                    QuizSubject.MATH -> KidsPrimary
                                    QuizSubject.ENGLISH -> KidsSecondary
                                    QuizSubject.ALL -> KidsGreen
                                }
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = subjColor.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "${currentQuestion.subject.emoji} ${currentQuestion.subject.displayName}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = subjColor,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = currentQuestion.topic,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = { speakCurrentQuestion() },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(KidsPrimary)
                                        .testTag("speak_quiz_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Read Aloud",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { nextQuestion() },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(KidsOrange)
                                        .testTag("skip_quiz_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SkipNext,
                                        contentDescription = "Skip Question",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Question Visual Hero Container
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = KidsYellow.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White,
                                    modifier = Modifier.size(64.dp),
                                    shadowElevation = 2.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(currentQuestion.emoji, fontSize = 36.sp)
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = currentQuestion.question,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 17.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Options List Header & Options
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Choose the correct answer:",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = "Question ${currentQuestionIndex + 1}/${questionsList.size}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = KidsPrimary
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            currentQuestion.options.forEachIndexed { idx, option ->
                                val isSelected = selectedOptionIndex == idx
                                val isCorrect = idx == currentQuestion.correctIndex

                                val optionBg = when {
                                    isAnswered && isCorrect -> KidsGreen
                                    isAnswered && isSelected && !isCorrect -> MaterialTheme.colorScheme.error
                                    isSelected -> KidsPrimary
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                }

                                val textColor = if (isAnswered && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurface

                                val scaleAnim by animateFloatAsState(
                                    targetValue = if (isSelected) 1.02f else 1f,
                                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                                    label = "OptionScale"
                                )

                                Surface(
                                    shape = RoundedCornerShape(18.dp),
                                    color = optionBg,
                                    border = if (!isSelected && !isAnswered) androidx.compose.foundation.BorderStroke(1.5.dp, KidsPrimary.copy(alpha = 0.25f)) else null,
                                    shadowElevation = if (isSelected) 4.dp else 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .scale(scaleAnim)
                                        .clickable { handleOptionClick(idx) }
                                        .testTag("quiz_option_$idx")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Surface(
                                                shape = CircleShape,
                                                color = if (isAnswered && (isCorrect || isSelected)) Color.White.copy(alpha = 0.3f) else KidsPrimary.copy(alpha = 0.2f),
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = listOf("A", "B", "C", "D").getOrElse(idx) { "$idx" },
                                                        fontWeight = FontWeight.ExtraBold,
                                                        fontSize = 13.sp,
                                                        color = if (isAnswered && (isCorrect || isSelected)) Color.White else KidsPrimary
                                                    )
                                                }
                                            }

                                            Text(
                                                text = option,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = textColor
                                            )
                                        }

                                        if (isAnswered && isCorrect) {
                                            Icon(Icons.Default.Check, contentDescription = "Correct", tint = Color.White)
                                        } else if (isAnswered && isSelected && !isCorrect) {
                                            Icon(Icons.Default.Close, contentDescription = "Wrong", tint = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        // Explanation Card
                        if (isAnswered) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = KidsGreen.copy(alpha = 0.15f)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text("💡 Fact & Explanation:", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = KidsGreen)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(currentQuestion.explanation, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                            }

                            Button(
                                onClick = { nextQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("next_quiz_question_button"),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KidsGreen)
                            ) {
                                Text("Next Quiz Question 🚀", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
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
                    Text("🌟 BRILLIANT DISCOVERY! 🌟", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = KidsGreen)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("You answered correctly!", fontSize = 14.sp)
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
                        nextQuestion()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KidsPrimary)
                ) {
                    Text("Continue Quiz! 🚀", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
