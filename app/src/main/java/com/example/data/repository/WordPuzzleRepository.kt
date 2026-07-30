package com.example.data.repository

import com.example.data.local.PuzzleProgressDao
import com.example.data.local.PuzzleProgressEntity
import com.example.model.CbseCategory
import com.example.model.Language
import com.example.model.PuzzleType
import com.example.model.WordPuzzle
import kotlinx.coroutines.flow.Flow

class WordPuzzleRepository(private val progressDao: PuzzleProgressDao) {

    val totalSolvedCountFlow: Flow<Int> = progressDao.getTotalSolvedCountFlow()

    fun getProgressFlow(language: Language): Flow<List<PuzzleProgressEntity>> {
        return progressDao.getProgressByLanguageFlow(language.name)
    }

    suspend fun savePuzzleSolved(puzzleId: String, language: Language, category: CbseCategory, score: Int, stars: Int) {
        val existing = progressDao.getProgressForPuzzle(puzzleId)
        val bestScore = maxOf(score, existing?.bestScore ?: 0)
        val bestStars = maxOf(stars, existing?.starsEarned ?: 0)

        val progress = PuzzleProgressEntity(
            puzzleId = puzzleId,
            language = language.name,
            category = category.name,
            isSolved = true,
            bestScore = bestScore,
            starsEarned = bestStars,
            solvedTimestamp = System.currentTimeMillis()
        )
        progressDao.saveProgress(progress)
    }

    fun getPuzzlesForLanguageAndCategory(language: Language, category: CbseCategory?): List<WordPuzzle> {
        val all = getCbseCurriculumBank(language)
        return if (category == null) all else all.filter { it.category == category }
    }

    private fun getCbseCurriculumBank(language: Language): List<WordPuzzle> {
        return when (language) {
            Language.ENGLISH -> listOf(
                // ANIMALS (Extensive CBSE Grade 1 Animals)
                WordPuzzle(
                    id = "en_cat",
                    word = "CAT",
                    displayScript = "Cat",
                    phonetics = "K-a-t",
                    englishMeaning = "A cute pet that says meow",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "A small pet animal that purrs and catches mice 🐱",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_lion",
                    word = "LION",
                    displayScript = "Lion",
                    phonetics = "Li-on",
                    englishMeaning = "King of the jungle",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "Wild big cat with a roaring mane 🦁",
                    missingIndex = 2,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_tiger",
                    word = "TIGER",
                    displayScript = "Tiger",
                    phonetics = "Ti-ger",
                    englishMeaning = "National animal of India",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "Big wild cat with orange and black stripes 🐅",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_elephant",
                    word = "ELEPHANT",
                    displayScript = "Elephant",
                    phonetics = "El-e-phant",
                    englishMeaning = "Largest land mammal",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "Big grey animal with a long trunk and big ears 🐘",
                    options = listOf("ELEPHANT", "LION", "TIGER", "BEAR"),
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_monkey",
                    word = "MONKEY",
                    displayScript = "Monkey",
                    phonetics = "Mon-key",
                    englishMeaning = "Playful animal that loves bananas",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "Swings on tree branches and loves eating bananas 🐒",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_rabbit",
                    word = "RABBIT",
                    displayScript = "Rabbit",
                    phonetics = "Rab-bit",
                    englishMeaning = "Cute animal that eats carrots",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "Hops around with long ears and loves carrots 🐇",
                    missingIndex = 2,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_duck",
                    word = "DUCK",
                    displayScript = "Duck",
                    phonetics = "D-u-c-k",
                    englishMeaning = "Water bird that quacks",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "Swims in the pond and says quack quack 🦆",
                    options = listOf("DUCK", "RABBIT", "CAT", "DOG"),
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_dog",
                    word = "DOG",
                    displayScript = "Dog",
                    phonetics = "D-o-g",
                    englishMeaning = "Loyal pet that barks",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "Man's best friend that barks woof woof 🐕",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_bear",
                    word = "BEAR",
                    displayScript = "Bear",
                    phonetics = "B-e-a-r",
                    englishMeaning = "Large furry wild animal",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "Furry forest animal that loves honey 🐻",
                    missingIndex = 1,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "en_zebra",
                    word = "ZEBRA",
                    displayScript = "Zebra",
                    phonetics = "Ze-bra",
                    englishMeaning = "Striped horse-like animal",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "Animal with black and white stripes 🦓",
                    options = listOf("ZEBRA", "HORSE", "COW", "LION"),
                    imageDrawableName = "img_animals_category"
                ),

                // FRUITS
                WordPuzzle(
                    id = "en_apple",
                    word = "APPLE",
                    displayScript = "Apple",
                    phonetics = "A-p-p-l-e",
                    englishMeaning = "A crunchy red or green fruit",
                    category = CbseCategory.FRUITS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "A popular sweet red fruit. An apple a day keeps doctor away! 🍎",
                    imageDrawableName = "img_fruits_category"
                ),
                WordPuzzle(
                    id = "en_mango",
                    word = "MANGO",
                    displayScript = "Mango",
                    phonetics = "Man-go",
                    englishMeaning = "King of fruits",
                    category = CbseCategory.FRUITS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "National fruit of India, juicy yellow fruit 🥭",
                    missingIndex = 1,
                    imageDrawableName = "img_fruits_category"
                ),
                WordPuzzle(
                    id = "en_banana",
                    word = "BANANA",
                    displayScript = "Banana",
                    phonetics = "Ba-na-na",
                    englishMeaning = "Yellow curved fruit",
                    category = CbseCategory.FRUITS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "Monkeys love to eat this yellow fruit! 🍌",
                    options = listOf("BANANA", "APPLE", "ORANGE", "GRAPES"),
                    imageDrawableName = "img_fruits_category"
                ),

                // NATURE
                WordPuzzle(
                    id = "en_sun",
                    word = "SUN",
                    displayScript = "Sun",
                    phonetics = "S-u-n",
                    englishMeaning = "Bright star in the day sky",
                    category = CbseCategory.NATURE,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "Shines brightly in the sky during the day ☀️",
                    imageDrawableName = "img_hero_banner"
                ),
                WordPuzzle(
                    id = "en_tree",
                    word = "TREE",
                    displayScript = "Tree",
                    phonetics = "T-r-e-e",
                    englishMeaning = "Tall plant with leaves and trunk",
                    category = CbseCategory.NATURE,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "Gives us fresh air, fruits, and shade 🌳",
                    missingIndex = 2,
                    imageDrawableName = "img_hero_banner"
                ),

                // SCHOOL
                WordPuzzle(
                    id = "en_school",
                    word = "SCHOOL",
                    displayScript = "School",
                    phonetics = "S-c-h-o-o-l",
                    englishMeaning = "Place where kids learn",
                    category = CbseCategory.SCHOOL,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "Where we go every day to learn with teachers and friends 🎒",
                    imageDrawableName = "img_app_icon"
                ),
                WordPuzzle(
                    id = "en_book",
                    word = "BOOK",
                    displayScript = "Book",
                    phonetics = "B-o-o-k",
                    englishMeaning = "Has pages to read stories",
                    category = CbseCategory.SCHOOL,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "Contains stories and knowledge to read 📖",
                    missingIndex = 1,
                    imageDrawableName = "img_app_icon"
                )
            )

            Language.TELUGU -> listOf(
                // ANIMALS TELUGU (తెలుగు జంతువులు)
                WordPuzzle(
                    id = "te_pilli",
                    word = "పిల్లి",
                    displayScript = "పిల్లి (Pilli)",
                    phonetics = "Pil-li",
                    englishMeaning = "Cat",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "మ్యావ్ మ్యావ్ అనే చిన్న పెంపుడు జంతువు (Cat) 🐱",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_kukka",
                    word = "కుక్క",
                    displayScript = "కుక్క (Kukka)",
                    phonetics = "Kuk-ka",
                    englishMeaning = "Dog",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "నమ్మకమైన పెంపుడు జంతువు (Dog) 🐕",
                    missingIndex = 1,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_eenugu",
                    word = "ఏనుగు",
                    displayScript = "ఏనుగు (Eenugu)",
                    phonetics = "Ee-nu-gu",
                    englishMeaning = "Elephant",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "పెద్ద తొండము ఉన్న జంతువు (Elephant) 🐘",
                    options = listOf("ఏనుగు", "సింహం", "కుక్క", "పిల్లి"),
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_simham",
                    word = "సింహం",
                    displayScript = "సింహం (Simham)",
                    phonetics = "Sim-ham",
                    englishMeaning = "Lion",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "అడవికి రాజు అయిన జంతువు (Lion) 🦁",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_puli",
                    word = "పులి",
                    displayScript = "పులి (Puli)",
                    phonetics = "Pu-li",
                    englishMeaning = "Tiger",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "భారత జాతీయ జంతువు (Tiger) 🐅",
                    missingIndex = 1,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_koti",
                    word = "కోతి",
                    displayScript = "కోతి (Koti)",
                    phonetics = "Ko-ti",
                    englishMeaning = "Monkey",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "చెట్లపై ఎగిరే చురుకైన జంతువు (Monkey) 🐒",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_jinka",
                    word = "జింక",
                    displayScript = "జింక (Jinka)",
                    phonetics = "Jin-ka",
                    englishMeaning = "Deer",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "అందమైన కొమ్ములున్న సాదు జంతువు (Deer) 🦌",
                    options = listOf("జింక", "పులి", "కోతి", "మేక"),
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_baatu",
                    word = "బాతు",
                    displayScript = "బాతు (Baatu)",
                    phonetics = "Baa-tu",
                    englishMeaning = "Duck",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "నీటిలో ఈదే పక్షి (Duck) 🦆",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_meka",
                    word = "మేక",
                    displayScript = "మేక (Meka)",
                    phonetics = "Me-ka",
                    englishMeaning = "Goat",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "మే మే అనే గడ్డి మేసే జంతువు (Goat) 🐐",
                    missingIndex = 1,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "te_chepa",
                    word = "చేప",
                    displayScript = "చేప (Chepa)",
                    phonetics = "Che-pa",
                    englishMeaning = "Fish",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "నీటిలో మాత్రమే జీవించే జీవి (Fish) 🐟",
                    options = listOf("చేప", "బాతు", "కప్ప", "పిల్లి"),
                    imageDrawableName = "img_animals_category"
                ),

                // FRUITS TELUGU
                WordPuzzle(
                    id = "te_pandu",
                    word = "పండు",
                    displayScript = "పండు (Pandu)",
                    phonetics = "Pan-du",
                    englishMeaning = "Fruit",
                    category = CbseCategory.FRUITS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "తీయ్యనైన ప్రకృతి తీపి పదార్థం (Fruit) 🍎",
                    options = listOf("పండు", "నీరు", "చెట్టు", "పువ్వు"),
                    imageDrawableName = "img_fruits_category"
                ),
                WordPuzzle(
                    id = "te_mamidi",
                    word = "మామిడి",
                    displayScript = "మామిడి (Mamidi)",
                    phonetics = "Maa-mi-di",
                    englishMeaning = "Mango",
                    category = CbseCategory.FRUITS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "ఫలరాజు, పసుపు రంగు తీపి పండు (Mango) 🥭",
                    imageDrawableName = "img_fruits_category"
                ),

                // NATURE TELUGU
                WordPuzzle(
                    id = "te_sooryudu",
                    word = "సూర్యుడు",
                    displayScript = "సూర్యుడు (Sooryudu)",
                    phonetics = "Soor-yu-du",
                    englishMeaning = "Sun",
                    category = CbseCategory.NATURE,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "పగలు ఆకాశంలో వెలుగునిచ్చే భాస్కరుడు (Sun) ☀️",
                    imageDrawableName = "img_hero_banner"
                ),
                WordPuzzle(
                    id = "te_neeru",
                    word = "నీరు",
                    displayScript = "నీరు (Neeru)",
                    phonetics = "Nee-ru",
                    englishMeaning = "Water",
                    category = CbseCategory.NATURE,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "దాహం తీర్చే అమృతం (Water) 💧",
                    options = listOf("నీరు", "గాలి", "నిప్పు", "మన్ను"),
                    imageDrawableName = "img_hero_banner"
                ),

                // SCHOOL TELUGU
                WordPuzzle(
                    id = "te_badi",
                    word = "బడి",
                    displayScript = "బడి (Badi)",
                    phonetics = "Ba-di",
                    englishMeaning = "School",
                    category = CbseCategory.SCHOOL,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "విద్య నేర్చుకునే దేవాలయం (School) 🎒",
                    imageDrawableName = "img_app_icon"
                ),
                WordPuzzle(
                    id = "te_pustakam",
                    word = "పుస్తకం",
                    displayScript = "పుస్తకం (Pustakam)",
                    phonetics = "Pus-ta-kam",
                    englishMeaning = "Book",
                    category = CbseCategory.SCHOOL,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "జ్ఞానాన్ని అందించే గ్రంథం (Book) 📖",
                    missingIndex = 1,
                    imageDrawableName = "img_app_icon"
                )
            )

            Language.HINDI -> listOf(
                // ANIMALS HINDI (हिंदी जानवर)
                WordPuzzle(
                    id = "hi_billi",
                    word = "बिल्ली",
                    displayScript = "बिल्ली (Billi)",
                    phonetics = "Bil-li",
                    englishMeaning = "Cat",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "म्याऊँ म्याऊँ बोलने वाला पालतू जानवर (Cat) 🐱",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_kutta",
                    word = "कुत्ता",
                    displayScript = "कुत्ता (Kutta)",
                    phonetics = "Kut-ta",
                    englishMeaning = "Dog",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "वफादार पालतू जानवर जो भौंकता है (Dog) 🐕",
                    missingIndex = 1,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_sher",
                    word = "शेर",
                    displayScript = "शेर (Sher)",
                    phonetics = "Sher",
                    englishMeaning = "Lion",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "जंगल का राजा (Lion) 🦁",
                    options = listOf("शेर", "हाथी", "कुत्ता", "गाय"),
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_hathi",
                    word = "हाथी",
                    displayScript = "हाथी (Hathi)",
                    phonetics = "Haa-thi",
                    englishMeaning = "Elephant",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "लंबी सूंड वाला विशाल जानवर (Elephant) 🐘",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_bagh",
                    word = "बाघ",
                    displayScript = "बाघ (Bagh)",
                    phonetics = "Bagh",
                    englishMeaning = "Tiger",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "भारत का राष्ट्रीय पशु (Tiger) 🐅",
                    missingIndex = 1,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_bandar",
                    word = "बंदर",
                    displayScript = "बंदर (Bandar)",
                    phonetics = "Ban-dar",
                    englishMeaning = "Monkey",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "पेड़ों पर उछल-कूद करने वाला जानवर (Monkey) 🐒",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_bhalu",
                    word = "भालू",
                    displayScript = "भालू (Bhalu)",
                    phonetics = "Bhaa-loo",
                    englishMeaning = "Bear",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "शहद पसंद करने वाला जंगली जानवर (Bear) 🐻",
                    options = listOf("भालू", "शेर", "बंदर", "हिरन"),
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_hiran",
                    word = "हिरन",
                    displayScript = "हिरन (Hiran)",
                    phonetics = "Hi-ran",
                    englishMeaning = "Deer",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "तेज़ दौड़ने वाला सुंदर जानवर (Deer) 🦌",
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_machhli",
                    word = "मछली",
                    displayScript = "मछली (Machhli)",
                    phonetics = "Machh-li",
                    englishMeaning = "Fish",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "जल की रानी है, जीवन इसका पानी है (Fish) 🐟",
                    missingIndex = 1,
                    imageDrawableName = "img_animals_category"
                ),
                WordPuzzle(
                    id = "hi_mor",
                    word = "मोर",
                    displayScript = "मोर (Mor)",
                    phonetics = "Mor",
                    englishMeaning = "Peacock",
                    category = CbseCategory.ANIMALS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "भारत का राष्ट्रीय सुंदर पक्षी (Peacock) 🦚",
                    options = listOf("मोर", "बत्तख", "शेर", "भालू"),
                    imageDrawableName = "img_animals_category"
                ),

                // FRUITS HINDI
                WordPuzzle(
                    id = "hi_aam",
                    word = "आम",
                    displayScript = "आम (Aam)",
                    phonetics = "Aam",
                    englishMeaning = "Mango",
                    category = CbseCategory.FRUITS,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "फलों का राजा, मीठा पीला फल (Mango) 🥭",
                    imageDrawableName = "img_fruits_category"
                ),
                WordPuzzle(
                    id = "hi_seb",
                    word = "सेब",
                    displayScript = "सेब (Seb)",
                    phonetics = "Seb",
                    englishMeaning = "Apple",
                    category = CbseCategory.FRUITS,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "लाल मीठा और सेहतमंद फल (Apple) 🍎",
                    options = listOf("सेब", "आम", "केला", "अनार"),
                    imageDrawableName = "img_fruits_category"
                ),

                // NATURE HINDI
                WordPuzzle(
                    id = "hi_sooraj",
                    word = "सूरज",
                    displayScript = "सूरज (Sooraj)",
                    phonetics = "Soo-raj",
                    englishMeaning = "Sun",
                    category = CbseCategory.NATURE,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "दिन में आसमान में चमकने वाला सूर्य (Sun) ☀️",
                    imageDrawableName = "img_hero_banner"
                ),
                WordPuzzle(
                    id = "hi_paani",
                    word = "पानी",
                    displayScript = "पानी (Paani)",
                    phonetics = "Paa-ni",
                    englishMeaning = "Water",
                    category = CbseCategory.NATURE,
                    puzzleType = PuzzleType.MISSING_LETTER,
                    hintText = "जीवन के लिए आवश्यक जल (Water) 💧",
                    missingIndex = 1,
                    imageDrawableName = "img_hero_banner"
                ),

                // SCHOOL HINDI
                WordPuzzle(
                    id = "hi_kitaab",
                    word = "किताब",
                    displayScript = "किताब (Kitaab)",
                    phonetics = "Ki-taab",
                    englishMeaning = "Book",
                    category = CbseCategory.SCHOOL,
                    puzzleType = PuzzleType.UNSCRAMBLE,
                    hintText = "पढ़ने वाली पुस्तक (Book) 📖",
                    imageDrawableName = "img_app_icon"
                ),
                WordPuzzle(
                    id = "hi_ghar",
                    word = "घर",
                    displayScript = "घर (Ghar)",
                    phonetics = "Ghar",
                    englishMeaning = "House",
                    category = CbseCategory.SCHOOL,
                    puzzleType = PuzzleType.PICTURE_MATCH,
                    hintText = "जहाँ हम अपने परिवार के साथ रहते हैं (House) 🏠",
                    options = listOf("घर", "किताब", "स्कूल", "पेड़"),
                    imageDrawableName = "img_app_icon"
                )
            )
        }
    }
}
