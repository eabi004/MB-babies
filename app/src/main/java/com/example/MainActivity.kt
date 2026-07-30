package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.auth.AuthRepository
import com.example.data.local.AppDatabase
import com.example.data.repository.DailyChallengeRepository
import com.example.data.repository.LeaderboardRepository
import com.example.data.repository.WordPuzzleRepository
import com.example.model.CbseCategory
import com.example.model.Language
import com.example.ui.auth.AuthScreen
import androidx.compose.runtime.rememberCoroutineScope
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.NavScreen
import com.example.ui.components.TopHeaderBar
import com.example.ui.daily.DailyChallengeScreen
import com.example.ui.home.HomeScreen
import com.example.ui.math.MathScreen
import com.example.ui.quiz.QuizScreen
import com.example.ui.videos.KidsVideosScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.puzzle.PuzzleScreen
import kotlinx.coroutines.launch
import com.example.ui.theme.AksharaWordQuestTheme
import com.example.util.TtsManager
import com.example.viewmodel.AuthViewModel
import com.example.viewmodel.DailyChallengeViewModel
import com.example.viewmodel.LeaderboardViewModel
import com.example.viewmodel.PuzzleViewModel

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Room Database & Repositories Setup
        val db = AppDatabase.getInstance(this)
        val authRepository = AuthRepository(db.userDao())
        val puzzleRepository = WordPuzzleRepository(db.puzzleProgressDao())
        val challengeRepository = DailyChallengeRepository(db.dailyChallengeDao())
        val leaderboardRepository = LeaderboardRepository(db.leaderboardDao())

        ttsManager = TtsManager(this)

        setContent {
            AksharaWordQuestTheme {
                MainAppEntry(
                    authRepository = authRepository,
                    puzzleRepository = puzzleRepository,
                    challengeRepository = challengeRepository,
                    leaderboardRepository = leaderboardRepository,
                    ttsManager = ttsManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}

@Composable
fun MainAppEntry(
    authRepository: AuthRepository,
    puzzleRepository: WordPuzzleRepository,
    challengeRepository: DailyChallengeRepository,
    leaderboardRepository: LeaderboardRepository,
    ttsManager: TtsManager
) {
    // Custom ViewModel Factories using composition/remember for clean dependency injection
    val authViewModel: AuthViewModel = remember { AuthViewModel(authRepository) }
    val puzzleViewModel: PuzzleViewModel = remember {
        PuzzleViewModel(puzzleRepository, authRepository, challengeRepository, ttsManager)
    }
    val challengeViewModel: DailyChallengeViewModel = remember {
        DailyChallengeViewModel(challengeRepository, authRepository)
    }
    val leaderboardViewModel: LeaderboardViewModel = remember {
        LeaderboardViewModel(leaderboardRepository)
    }

    val authState by authViewModel.authState.collectAsState()
    val currentUser = authState.user
    val coroutineScope = rememberCoroutineScope()

    var currentLanguage by remember { mutableStateOf(Language.ENGLISH) }
    var currentNavScreen by remember { mutableStateOf(NavScreen.HOME) }

    // Synchronize language preference with user record
    if (currentUser?.preferredLanguage != null) {
        val userLang = try {
            Language.valueOf(currentUser.preferredLanguage)
        } catch (e: Exception) {
            Language.ENGLISH
        }
        if (userLang != currentLanguage) {
            currentLanguage = userLang
        }
    }

    // Pre-seed challenges on load
    DisposableEffect(currentLanguage) {
        challengeViewModel.loadChallengesForLanguage(currentLanguage)
        onDispose {}
    }

    if (!authState.isAuthenticated || currentUser == null) {
        // Unauthenticated -> Show Scalable Auth Module
        AuthScreen(
            authState = authState,
            onRegister = { username, email, avatarId ->
                authViewModel.register(username, email, avatarId)
            },
            onLogin = { email ->
                authViewModel.login(email)
            },
            onClearError = { authViewModel.clearError() }
        )
    } else {
        // Authenticated App Scaffold
        val puzzleUiState by puzzleViewModel.uiState.collectAsState()
        val dailyChallenges by challengeViewModel.dailyChallenges.collectAsState()
        val leaderboardEntries by leaderboardViewModel.leaderboardEntries.collectAsState()
        val selectedLeaderboardFilter by leaderboardViewModel.selectedFilter.collectAsState()

        Scaffold(
            topBar = {
                TopHeaderBar(
                    user = currentUser,
                    currentLanguage = currentLanguage,
                    onLanguageSelected = { lang ->
                        currentLanguage = lang
                        authViewModel.setLanguage(lang)
                        challengeViewModel.loadChallengesForLanguage(lang)
                    },
                    onOpenProfile = {
                        currentNavScreen = NavScreen.PROFILE
                    }
                )
            },
            bottomBar = {
                AppBottomNavBar(
                    currentRoute = currentNavScreen.route,
                    onNavigate = { nav ->
                        currentNavScreen = nav
                        if (nav == NavScreen.PUZZLES && puzzleUiState.currentPuzzle == null) {
                            puzzleViewModel.startCategorySession(currentLanguage, null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentNavScreen,
                    label = "PlayfulScreenTransition",
                    transitionSpec = {
                        val targetIndex = targetState.ordinal
                        val initialIndex = initialState.ordinal
                        val direction = if (targetIndex >= initialIndex) 1 else -1

                        (slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            ),
                            initialOffsetX = { fullWidth -> direction * fullWidth }
                        ) + fadeIn(animationSpec = tween(220)) + scaleIn(
                            initialScale = 0.90f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )).togetherWith(
                            slideOutHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                targetOffsetX = { fullWidth -> -direction * fullWidth }
                            ) + fadeOut(animationSpec = tween(180)) + scaleOut(
                                targetScale = 0.95f
                            )
                        )
                    }
                ) { screen ->
                    when (screen) {
                        NavScreen.HOME -> HomeScreen(
                            user = currentUser,
                            currentLanguage = currentLanguage,
                            onLanguageSelected = { lang ->
                                currentLanguage = lang
                                authViewModel.setLanguage(lang)
                                challengeViewModel.loadChallengesForLanguage(lang)
                                puzzleViewModel.startCategorySession(lang, null)
                            },
                            onSelectCategory = { cat ->
                                puzzleViewModel.startCategorySession(currentLanguage, cat)
                                currentNavScreen = NavScreen.PUZZLES
                            },
                            onStartDailyQuests = {
                                currentNavScreen = NavScreen.DAILY
                            }
                        )

                        NavScreen.MATH -> MathScreen(
                            currentLanguage = currentLanguage,
                            onAddReward = { xp, stars, coins ->
                                coroutineScope.launch {
                                    authRepository.addReward(xp, stars, coins)
                                }
                            }
                        )

                        NavScreen.QUIZ -> QuizScreen(
                            currentLanguage = currentLanguage,
                            onAddReward = { xp, stars, coins ->
                                coroutineScope.launch {
                                    authRepository.addReward(xp, stars, coins)
                                }
                            }
                        )

                        NavScreen.PUZZLES -> PuzzleScreen(
                            state = puzzleUiState,
                            currentLanguage = currentLanguage,
                            onLanguageSelected = { lang ->
                                currentLanguage = lang
                                authViewModel.setLanguage(lang)
                                challengeViewModel.loadChallengesForLanguage(lang)
                                puzzleViewModel.startCategorySession(lang, null)
                            },
                            onSelectUnscrambleLetter = { char, idx ->
                                puzzleViewModel.selectUnscrambleLetter(char, idx)
                            },
                            onRemoveUnscrambleLetter = { idx ->
                                puzzleViewModel.removeUnscrambleLetter(idx)
                            },
                            onSelectMissingLetter = { char ->
                                puzzleViewModel.selectMissingLetterOption(char)
                            },
                            onSelectOption = { option ->
                                puzzleViewModel.selectPictureMatchOption(option)
                            },
                            onRevealHint = { puzzleViewModel.revealHint() },
                            onSpeakWord = { puzzleViewModel.speakWord() },
                            onDismissCelebration = { puzzleViewModel.dismissCelebration() }
                        )

                        NavScreen.DAILY -> DailyChallengeScreen(
                            challenges = dailyChallenges,
                            user = currentUser,
                            onClaimReward = { challenge ->
                                challengeViewModel.claimReward(challenge)
                            }
                        )

                        NavScreen.VIDEOS -> KidsVideosScreen()

                        NavScreen.PROFILE -> ProfileScreen(
                            user = currentUser,
                            currentLanguage = currentLanguage,
                            onLogout = {
                                authViewModel.logout()
                            }
                        )
                    }
                }
            }
        }
    }
}
