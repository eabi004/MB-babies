package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AuthRepository
import com.example.data.repository.DailyChallengeRepository
import com.example.data.repository.WordPuzzleRepository
import com.example.model.CbseCategory
import com.example.model.Language
import com.example.model.PuzzleType
import com.example.model.WordPuzzle
import com.example.util.SoundFxManager
import com.example.util.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PuzzleUiState(
    val currentPuzzle: WordPuzzle? = null,
    val selectedLetters: List<Char> = emptyList(), // For UNSCRAMBLE
    val availableLetters: List<Char> = emptyList(), // For UNSCRAMBLE
    val selectedMissingChar: Char? = null, // For MISSING_LETTER
    val missingLetterOptions: List<Char> = emptyList(), // Language-aware options for MISSING_LETTER
    val selectedOption: String? = null, // For PICTURE_MATCH
    val isHintRevealed: Boolean = false,
    val isSolved: Boolean = false,
    val scoreEarned: Int = 0,
    val starsEarned: Int = 0,
    val showCelebrationDialog: Boolean = false,
    val errorMessage: String? = null
)

class PuzzleViewModel(
    private val puzzleRepository: WordPuzzleRepository,
    private val authRepository: AuthRepository,
    private val challengeRepository: DailyChallengeRepository,
    private val ttsManager: TtsManager,
    private val soundFxManager: SoundFxManager = SoundFxManager()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PuzzleUiState())
    val uiState: StateFlow<PuzzleUiState> = _uiState.asStateFlow()

    private var puzzleList: List<WordPuzzle> = emptyList()
    private var currentPuzzleIndex = 0
    private var currentLanguage = Language.ENGLISH

    fun startCategorySession(language: Language, category: CbseCategory?) {
        currentLanguage = language
        puzzleList = puzzleRepository.getPuzzlesForLanguageAndCategory(language, category).shuffled()
        currentPuzzleIndex = 0
        if (puzzleList.isNotEmpty()) {
            loadPuzzle(puzzleList[0])
        } else {
            _uiState.value = PuzzleUiState(errorMessage = "No puzzles found for this category.")
        }
    }

    private fun loadPuzzle(puzzle: WordPuzzle) {
        val available = when (puzzle.puzzleType) {
            PuzzleType.UNSCRAMBLE -> puzzle.word.toCharArray().toList().shuffled()
            else -> emptyList()
        }

        val missingOptions = if (puzzle.puzzleType == PuzzleType.MISSING_LETTER) {
            generateMissingLetterOptions(puzzle)
        } else {
            emptyList()
        }

        _uiState.value = PuzzleUiState(
            currentPuzzle = puzzle,
            availableLetters = available,
            missingLetterOptions = missingOptions,
            selectedLetters = emptyList(),
            selectedMissingChar = null,
            selectedOption = null,
            isHintRevealed = false,
            isSolved = false,
            showCelebrationDialog = false
        )

        // Pronounce word when puzzle opens
        ttsManager.speak(puzzle.word, currentLanguage)
    }

    private fun generateMissingLetterOptions(puzzle: WordPuzzle): List<Char> {
        val targetChar = puzzle.word.getOrNull(puzzle.missingIndex)
        val set = mutableSetOf<Char>()
        if (targetChar != null) {
            set.add(targetChar)
        }

        val languagePool = when (currentLanguage) {
            Language.ENGLISH -> listOf('A', 'E', 'I', 'O', 'U', 'B', 'C', 'D', 'M', 'N', 'P', 'R', 'S', 'T', 'G', 'K', 'L')
            Language.TELUGU -> listOf('క', 'గ', 'చ', 'జ', 'త', 'ద', 'న', 'ప', 'మ', 'య', 'ర', 'ల', 'వ', 'స', 'ు', 'ి', 'ే', 'ా', 'ో', 'ం', 'అ', 'ఇ', 'ఉ', 'ట', 'డ')
            Language.HINDI -> listOf('क', 'ख', 'ग', 'घ', 'च', 'छ', 'ज', 'झ', 'त', 'द', 'न', 'प', 'ब', 'म', 'य', 'र', 'ल', 'व', 'स', 'ु', 'ी', 'ा', 'े', 'ो', 'ं', 'अ', 'आ', 'इ')
        }

        // Add 6 random language distractors
        languagePool.filter { it != targetChar }.shuffled().take(6).forEach { set.add(it) }

        // Also add characters from word as additional context if set size is small
        puzzle.word.forEach { c ->
            if (set.size < 8 && c != targetChar) {
                set.add(c)
            }
        }

        return set.toList().shuffled()
    }

    fun selectUnscrambleLetter(char: Char, index: Int) {
        val currentState = _uiState.value
        val puzzle = currentState.currentPuzzle ?: return
        if (currentState.isSolved) return

        soundFxManager.playClickSound()
        ttsManager.speak(char.toString(), currentLanguage)

        val newAvailable = currentState.availableLetters.toMutableList()
        if (index in newAvailable.indices) {
            newAvailable.removeAt(index)
        }
        val newSelected = currentState.selectedLetters + char

        _uiState.value = currentState.copy(
            selectedLetters = newSelected,
            availableLetters = newAvailable
        )

        // Check if user spelled full word
        val formedWord = newSelected.joinToString("")
        if (formedWord.length == puzzle.word.length) {
            if (formedWord.equals(puzzle.word, ignoreCase = true)) {
                onCorrectAnswer()
            } else {
                soundFxManager.playIncorrectSound()
                _uiState.value = _uiState.value.copy(errorMessage = "Not quite right! Tap letters to clear.")
            }
        }
    }

    fun removeUnscrambleLetter(index: Int) {
        val currentState = _uiState.value
        if (currentState.isSolved) return
        val selected = currentState.selectedLetters.toMutableList()
        if (index in selected.indices) {
            soundFxManager.playClickSound()
            val removedChar = selected.removeAt(index)
            ttsManager.speak(removedChar.toString(), currentLanguage)
            val newAvailable = currentState.availableLetters + removedChar
            _uiState.value = currentState.copy(
                selectedLetters = selected,
                availableLetters = newAvailable,
                errorMessage = null
            )
        }
    }

    fun selectMissingLetterOption(char: Char) {
        val currentState = _uiState.value
        val puzzle = currentState.currentPuzzle ?: return
        if (currentState.isSolved) return

        soundFxManager.playClickSound()
        ttsManager.speak(char.toString(), currentLanguage)
        _uiState.value = currentState.copy(selectedMissingChar = char)

        val targetChar = puzzle.word.getOrNull(puzzle.missingIndex)
        if (targetChar != null && char.equals(targetChar, ignoreCase = true)) {
            onCorrectAnswer()
        } else {
            soundFxManager.playIncorrectSound()
            _uiState.value = _uiState.value.copy(errorMessage = "Incorrect letter, try again!")
        }
    }

    fun selectPictureMatchOption(option: String) {
        val currentState = _uiState.value
        val puzzle = currentState.currentPuzzle ?: return
        if (currentState.isSolved) return

        soundFxManager.playClickSound()
        ttsManager.speak(option, currentLanguage)
        _uiState.value = currentState.copy(selectedOption = option)

        if (option.equals(puzzle.word, ignoreCase = true)) {
            onCorrectAnswer()
        } else {
            soundFxManager.playIncorrectSound()
            _uiState.value = _uiState.value.copy(errorMessage = "Try again!")
        }
    }

    fun revealHint() {
        val currentState = _uiState.value
        if (currentState.isHintRevealed) return
        soundFxManager.playHintSound()
        _uiState.value = currentState.copy(isHintRevealed = true)
    }

    fun speakWord() {
        val puzzle = _uiState.value.currentPuzzle ?: return
        soundFxManager.playClickSound()
        ttsManager.speak(puzzle.word, currentLanguage)
    }

    private fun onCorrectAnswer() {
        val currentState = _uiState.value
        val puzzle = currentState.currentPuzzle ?: return

        soundFxManager.playCorrectSound()

        val stars = if (currentState.isHintRevealed) 2 else 3
        val xp = if (stars == 3) 150 else 100
        val coins = 25

        _uiState.value = currentState.copy(
            isSolved = true,
            scoreEarned = xp,
            starsEarned = stars,
            showCelebrationDialog = true,
            errorMessage = null
        )

        // Pronounce victory speech
        ttsManager.speak("Great job! ${puzzle.word}", currentLanguage)

        viewModelScope.launch {
            puzzleRepository.savePuzzleSolved(
                puzzleId = puzzle.id,
                language = currentLanguage,
                category = puzzle.category,
                score = xp,
                stars = stars
            )
            authRepository.addReward(xp = xp, stars = stars, coins = coins)
            challengeRepository.recordPuzzleSolved(currentLanguage)
        }
    }

    fun nextPuzzle() {
        soundFxManager.playClickSound()
        if (currentPuzzleIndex < puzzleList.size - 1) {
            currentPuzzleIndex++
            loadPuzzle(puzzleList[currentPuzzleIndex])
        } else {
            // Restart or reshuffle
            puzzleList = puzzleList.shuffled()
            currentPuzzleIndex = 0
            if (puzzleList.isNotEmpty()) {
                loadPuzzle(puzzleList[0])
            }
        }
    }

    fun dismissCelebration() {
        _uiState.value = _uiState.value.copy(showCelebrationDialog = false)
        nextPuzzle()
    }

    override fun onCleared() {
        super.onCleared()
        soundFxManager.release()
    }
}
