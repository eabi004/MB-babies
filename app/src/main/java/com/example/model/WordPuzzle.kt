package com.example.model

enum class Language(val displayName: String, val code: String, val flagEmoji: String) {
    ENGLISH("English", "en", "🇬🇧"),
    TELUGU("తెలుగు (Telugu)", "te", "🇮🇳"),
    HINDI("हिंदी (Hindi)", "hi", "🇮🇳")
}

enum class PuzzleType(val label: String) {
    UNSCRAMBLE("Letter Unscramble"),
    MISSING_LETTER("Missing Letter"),
    PICTURE_MATCH("Picture Word Match")
}

enum class CbseCategory(val title: String, val iconRes: String) {
    FRUITS("Fruits & Food", "img_fruits_category"),
    ANIMALS("Animals & Birds", "img_animals_category"),
    NATURE("Nature & Sky", "img_hero_banner"),
    SCHOOL("School & Home", "img_app_icon"),
    COLORS_NUMBERS("Colors & Numbers", "img_hero_banner")
}

data class WordPuzzle(
    val id: String,
    val word: String, // Target word in chosen language (e.g., "APPLE", "మామిడి", "सूरज")
    val displayScript: String, // Native script (e.g., "Apple", "మామిడి", "सूरज")
    val phonetics: String, // Phonetic guide (e.g., "ap-puhl", "maa-mi-di", "soo-raj")
    val englishMeaning: String,
    val category: CbseCategory,
    val puzzleType: PuzzleType,
    val hintText: String,
    val options: List<String> = emptyList(), // For PICTURE_MATCH type
    val missingIndex: Int = -1, // For MISSING_LETTER type
    val imageDrawableName: String? = null
)
