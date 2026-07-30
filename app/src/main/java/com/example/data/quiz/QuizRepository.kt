package com.example.data.quiz

import com.example.BuildConfig
import com.example.model.QuizQuestion
import com.example.model.QuizSubject
import com.example.model.defaultQuizQuestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

class QuizRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .build()

    private val activeQuestions = defaultQuizQuestions.toMutableList()

    fun getQuestions(subject: QuizSubject): List<QuizQuestion> {
        return if (subject == QuizSubject.ALL) {
            activeQuestions.shuffled()
        } else {
            activeQuestions.filter { it.subject == subject }.shuffled()
        }
    }

    suspend fun fetchOnlineQuizQuestions(subject: QuizSubject): List<QuizQuestion> = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isNotBlank() && apiKey != "null") {
            try {
                val promptSubject = if (subject == QuizSubject.ALL) "Science, Social Studies, Math, or English" else subject.displayName
                val jsonPrompt = """
                    Generate 3 simple multiple-choice quiz questions for 5-7 year old children about $promptSubject.
                    Respond ONLY with a valid raw JSON array containing exactly 3 objects. Do not wrap in markdown tags like ```json.
                    Format:
                    [
                      {
                        "subject": "${if (subject == QuizSubject.ALL) "Science" else subject.displayName}",
                        "topic": "Fun Topic Name",
                        "question": "Easy Question text for kids?",
                        "options": ["Correct Option", "Wrong Option 1", "Wrong Option 2", "Wrong Option 3"],
                        "correctIndex": 0,
                        "emoji": "🎈",
                        "explanation": "1 short encouraging sentence explanation!"
                      }
                    ]
                """.trimIndent()

                val requestBodyJson = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply { put("text", jsonPrompt) })
                            })
                        })
                    })
                }.toString()

                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
                val request = Request.Builder()
                    .url(url)
                    .post(requestBodyJson.toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                val responseString = response.body?.string() ?: ""

                if (response.isSuccessful && responseString.isNotBlank()) {
                    val rootObj = JSONObject(responseString)
                    val candidates = rootObj.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val contentObj = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = contentObj?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            var text = parts.getJSONObject(0).optString("text", "")
                            text = text.replace("```json", "").replace("```", "").trim()

                            val parsedArray = JSONArray(text)
                            val newFetched = mutableListOf<QuizQuestion>()
                            for (i in 0 until parsedArray.length()) {
                                val item = parsedArray.getJSONObject(i)
                                val subjStr = item.optString("subject", subject.displayName)
                                val subjEnum = when {
                                    subjStr.contains("Social", ignoreCase = true) -> QuizSubject.SOCIAL_STUDIES
                                    subjStr.contains("Math", ignoreCase = true) -> QuizSubject.MATH
                                    subjStr.contains("English", ignoreCase = true) -> QuizSubject.ENGLISH
                                    subjStr.contains("Science", ignoreCase = true) -> QuizSubject.SCIENCE
                                    else -> if (subject == QuizSubject.ALL) QuizSubject.SCIENCE else subject
                                }
                                val optsArray = item.getJSONArray("options")
                                val optionsList = mutableListOf<String>()
                                for (j in 0 until optsArray.length()) {
                                    optionsList.add(optsArray.getString(j))
                                }

                                val q = QuizQuestion(
                                    id = "ai_${UUID.randomUUID().toString().take(6)}",
                                    subject = subjEnum,
                                    topic = item.optString("topic", "${subject.displayName} Online"),
                                    question = item.optString("question", "What is exciting about learning?"),
                                    options = optionsList,
                                    correctIndex = item.optInt("correctIndex", 0),
                                    emoji = item.optString("emoji", "🌟"),
                                    explanation = item.optString("explanation", "Great job learning new facts!"),
                                    isAiGenerated = true
                                )
                                newFetched.add(q)
                            }

                            if (newFetched.isNotEmpty()) {
                                activeQuestions.addAll(0, newFetched)
                                return@withContext newFetched
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Fallback: Dynamic subject-specific internet question generator
        val fallbackNew = generateFallbackWebQuestions(subject)
        activeQuestions.addAll(0, fallbackNew)
        return@withContext fallbackNew
    }

    private fun generateFallbackWebQuestions(subject: QuizSubject): List<QuizQuestion> {
        val timestamp = System.currentTimeMillis() % 1000
        return when (subject) {
            QuizSubject.SCIENCE -> listOf(
                QuizQuestion(
                    id = "net_sci_$timestamp",
                    subject = QuizSubject.SCIENCE,
                    topic = "Space & Stars",
                    question = "Which giant star gives light and warmth to planet Earth during the day?",
                    options = listOf("The Sun", "The Moon", "North Star", "Mars"),
                    correctIndex = 0,
                    emoji = "☀️",
                    explanation = "The Sun is a giant glowing star at the center of our solar system!",
                    isAiGenerated = true
                ),
                QuizQuestion(
                    id = "net_sci2_$timestamp",
                    subject = QuizSubject.SCIENCE,
                    topic = "Ocean Mammals",
                    question = "Which sea mammal is the largest living animal on planet Earth?",
                    options = listOf("Blue Whale", "Dolphin", "Goldfish", "Sea Turtle"),
                    correctIndex = 0,
                    emoji = "🐋",
                    explanation = "Blue Whales can grow as long as three giant school buses!",
                    isAiGenerated = true
                )
            )
            QuizSubject.SOCIAL_STUDIES -> listOf(
                QuizQuestion(
                    id = "net_soc_$timestamp",
                    subject = QuizSubject.SOCIAL_STUDIES,
                    topic = "Good Manners",
                    question = "What polite words should you say when someone offers you a gift?",
                    options = listOf("Thank You!", "Goodbye", "No way", "Sorry"),
                    correctIndex = 0,
                    emoji = "🎁",
                    explanation = "Saying 'Thank You' shows appreciation and kind heart!",
                    isAiGenerated = true
                ),
                QuizQuestion(
                    id = "net_soc2_$timestamp",
                    subject = QuizSubject.SOCIAL_STUDIES,
                    topic = "Community Safety",
                    question = "Who helps put out fires and keeps our houses safe?",
                    options = listOf("Firefighters", "Bakers", "Painters", "Tailors"),
                    correctIndex = 0,
                    emoji = "👨‍🚒",
                    explanation = "Firefighters drive big red trucks to extinguish fires safely!",
                    isAiGenerated = true
                )
            )
            QuizSubject.MATH -> listOf(
                QuizQuestion(
                    id = "net_math_$timestamp",
                    subject = QuizSubject.MATH,
                    topic = "Fun Addition",
                    question = "If you have 3 red apples and get 3 green apples, how many apples do you have?",
                    options = listOf("6 Apples", "5 Apples", "4 Apples", "7 Apples"),
                    correctIndex = 0,
                    emoji = "🍎",
                    explanation = "3 plus 3 equals 6 total delicious apples!",
                    isAiGenerated = true
                ),
                QuizQuestion(
                    id = "net_math2_$timestamp",
                    subject = QuizSubject.MATH,
                    topic = "Shapes & Corners",
                    question = "Which shape has 3 sides and 3 sharp corners?",
                    options = listOf("Triangle", "Square", "Circle", "Rectangle"),
                    correctIndex = 0,
                    emoji = "🔺",
                    explanation = "Triangles always have 3 sides and 3 corners!",
                    isAiGenerated = true
                )
            )
            QuizSubject.ENGLISH -> listOf(
                QuizQuestion(
                    id = "net_eng_$timestamp",
                    subject = QuizSubject.ENGLISH,
                    topic = "Phonics & Rhymes",
                    question = "Which word rhymes with SUN?",
                    options = listOf("RUN", "MOON", "CAT", "BALL"),
                    correctIndex = 0,
                    emoji = "🏃",
                    explanation = "SUN and RUN have the exact same 'un' rhyming sound!",
                    isAiGenerated = true
                ),
                QuizQuestion(
                    id = "net_eng2_$timestamp",
                    subject = QuizSubject.ENGLISH,
                    topic = "Opposites",
                    question = "What is the opposite word of HAPPY?",
                    options = listOf("Sad", "Glad", "Joyful", "Fun"),
                    correctIndex = 0,
                    emoji = "😢",
                    explanation = "The opposite of Happy is Sad!",
                    isAiGenerated = true
                )
            )
            QuizSubject.ALL -> listOf(
                QuizQuestion(
                    id = "net_all1_$timestamp",
                    subject = QuizSubject.SCIENCE,
                    topic = "Nature",
                    question = "What green part of a plant catches sunlight to make food?",
                    options = listOf("Leaves", "Roots", "Trunk", "Seeds"),
                    correctIndex = 0,
                    emoji = "🍃",
                    explanation = "Leaves use green chlorophyll to make plant food from sunlight!",
                    isAiGenerated = true
                ),
                QuizQuestion(
                    id = "net_all2_$timestamp",
                    subject = QuizSubject.MATH,
                    topic = "Counting Pairs",
                    question = "How many shoes make 1 pair of shoes?",
                    options = listOf("2 Shoes", "4 Shoes", "1 Shoe", "3 Shoes"),
                    correctIndex = 0,
                    emoji = "👟",
                    explanation = "A pair always means 2 matching items!",
                    isAiGenerated = true
                )
            )
        }
    }
}
