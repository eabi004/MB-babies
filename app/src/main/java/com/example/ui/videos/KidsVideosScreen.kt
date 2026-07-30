package com.example.ui.videos

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsPrimary
import com.example.ui.theme.KidsPurple
import com.example.ui.theme.KidsYellow

private fun getYouTubeIframeHtml(videoId: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body, html { width: 100%; height: 100%; background-color: #000000; overflow: hidden; display: flex; align-items: center; justify-content: center; }
                iframe { width: 100%; height: 100%; border: 0; }
            </style>
        </head>
        <body>
            <iframe 
                src="https://www.youtube-nocookie.com/embed/$videoId?autoplay=1&playsinline=1&enablejsapi=1&rel=0&modestbranding=1" 
                title="Kids Video Player" 
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
                allowfullscreen>
            </iframe>
        </body>
        </html>
    """.trimIndent()
}

data class KidsVideo(
    val id: String,
    val title: String,
    val subject: String,
    val channel: String,
    val duration: String,
    val youtubeVideoId: String,
    val emoji: String,
    val color: Color
)

enum class KidsSubject(val displayName: String, val emoji: String, val color: Color) {
    ALL("All Videos", "🌟", KidsPurple),
    ABCS("ABCs & Phonics", "🔤", KidsPrimary),
    MATH("Math & Numbers", "🔢", KidsOrange),
    SCIENCE("Science & Nature", "🔬", KidsGreen),
    SOCIAL("Social & World", "🌍", KidsYellow),
    RHYMES("Nursery Rhymes", "🎵", KidsPurple)
}

val sampleKidsVideos = listOf(
    // ABCs
    KidsVideo(
        id = "abc_1",
        title = "Phonics Song with Two Words - A for Apple",
        subject = "ABCs & Phonics",
        channel = "ChuChu TV",
        duration = "3:45",
        youtubeVideoId = "BELlZKpi1Zs",
        emoji = "🔤",
        color = KidsPrimary
    ),
    KidsVideo(
        id = "abc_2",
        title = "Alphabet Song & ABC Phonics for Toddlers",
        subject = "ABCs & Phonics",
        channel = "Cocomelon",
        duration = "4:12",
        youtubeVideoId = "75p-N3yL9E0",
        emoji = "🍎",
        color = KidsPrimary
    ),
    KidsVideo(
        id = "abc_3",
        title = "Telugu Alphabet Song - Varnamala for Kids",
        subject = "ABCs & Phonics",
        channel = "Infobells Telugu",
        duration = "5:30",
        youtubeVideoId = "b92A_Cq-71M",
        emoji = "🇮🇳",
        color = KidsPrimary
    ),
    KidsVideo(
        id = "abc_4",
        title = "Hindi Varnamala Song - Swar and Vyanjan",
        subject = "ABCs & Phonics",
        channel = "Infobells Hindi",
        duration = "4:50",
        youtubeVideoId = "W2Y5XfO4R_Q",
        emoji = "🇮🇳",
        color = KidsPrimary
    ),

    // MATH
    KidsVideo(
        id = "math_1",
        title = "Numbers Song 1 to 20 | Counting for Kids",
        subject = "Math & Numbers",
        channel = "Super Simple Songs",
        duration = "3:20",
        youtubeVideoId = "D0Ajq682yrA",
        emoji = "🔢",
        color = KidsOrange
    ),
    KidsVideo(
        id = "math_2",
        title = "Shapes Song for Kids | Circle, Square, Triangle",
        subject = "Math & Numbers",
        channel = "Pinkfong",
        duration = "2:55",
        youtubeVideoId = "OEbRDtCAFdU",
        emoji = "📐",
        color = KidsOrange
    ),
    KidsVideo(
        id = "math_3",
        title = "Addition Song for Kids | Easy Math 1+1=2",
        subject = "Math & Numbers",
        channel = "Numberblocks",
        duration = "4:05",
        youtubeVideoId = "u4L1SJ3z9jA",
        emoji = "➕",
        color = KidsOrange
    ),

    // SCIENCE
    KidsVideo(
        id = "sci_1",
        title = "Eight Planets Song | Solar System for Kids",
        subject = "Science & Nature",
        channel = "Cocomelon",
        duration = "3:30",
        youtubeVideoId = "mQrlgH97v94",
        emoji = "🪐",
        color = KidsGreen
    ),
    KidsVideo(
        id = "sci_2",
        title = "Wild Animals Song & Sounds | Lion, Elephant, Tiger",
        subject = "Science & Nature",
        channel = "Nat Geo Kids",
        duration = "4:15",
        youtubeVideoId = "t99ULJjCsaM",
        emoji = "🦁",
        color = KidsGreen
    ),
    KidsVideo(
        id = "sci_3",
        title = "Human Body Parts Song for Kids",
        subject = "Science & Nature",
        channel = "Peekaboo Kidz",
        duration = "3:10",
        youtubeVideoId = "qylGI-Myl-Y",
        emoji = "🫀",
        color = KidsGreen
    ),

    // SOCIAL
    KidsVideo(
        id = "soc_1",
        title = "Community Helpers Song | Doctor, Teacher, Police",
        subject = "Social & World",
        channel = "ChuChu TV",
        duration = "3:50",
        youtubeVideoId = "M7G2bU3wW48",
        emoji = "👨‍🚒",
        color = KidsYellow
    ),
    KidsVideo(
        id = "soc_2",
        title = "Good Manners & Healthy Habits Song",
        subject = "Social & World",
        channel = "Infobells",
        duration = "4:20",
        youtubeVideoId = "7K2Z5M3j9N0",
        emoji = "🧼",
        color = KidsYellow
    ),

    // RHYMES
    KidsVideo(
        id = "rhy_1",
        title = "Johny Johny Yes Papa & More Rhymes",
        subject = "Nursery Rhymes",
        channel = "LooLoo Kids",
        duration = "6:10",
        youtubeVideoId = "F4tHL8reNCs",
        emoji = "👶",
        color = KidsPurple
    ),
    KidsVideo(
        id = "rhy_2",
        title = "Wheels on the Bus Go Round and Round",
        subject = "Nursery Rhymes",
        channel = "Cocomelon",
        duration = "3:15",
        youtubeVideoId = "e_04ZrNroTo",
        emoji = "🚌",
        color = KidsPurple
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun KidsVideosScreen(
    modifier: Modifier = Modifier
) {
    var selectedSubject by remember { mutableStateOf(KidsSubject.ALL) }
    var activeVideo by remember { mutableStateOf(sampleKidsVideos.first()) }
    var searchQuery by remember { mutableStateOf("") }
    var activeSearchEmbedUrl by remember { mutableStateOf<String?>(null) }

    val filteredVideos = remember(selectedSubject) {
        if (selectedSubject == KidsSubject.ALL) {
            sampleKidsVideos
        } else {
            sampleKidsVideos.filter { it.subject == selectedSubject.displayName }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("kids_videos_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Player Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("kids_video_player_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column {
                    // Embedded YouTube WebView Player
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .background(Color.Black)
                    ) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    @SuppressLint("SetJavaScriptEnabled")
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.databaseEnabled = true
                                    settings.mediaPlaybackRequiresUserGesture = false
                                    settings.useWideViewPort = true
                                    settings.loadWithOverviewMode = true
                                    settings.allowFileAccess = true
                                    settings.allowContentAccess = true
                                    settings.javaScriptCanOpenWindowsAutomatically = true
                                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                    settings.userAgentString = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                                    webChromeClient = WebChromeClient()
                                    webViewClient = object : WebViewClient() {
                                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                            return false
                                        }
                                    }

                                    if (activeSearchEmbedUrl != null) {
                                        loadUrl(activeSearchEmbedUrl!!)
                                    } else {
                                        val html = getYouTubeIframeHtml(activeVideo.youtubeVideoId)
                                        loadDataWithBaseURL("https://www.youtube-nocookie.com", html, "text/html", "UTF-8", null)
                                    }
                                }
                            },
                            update = { webView ->
                                if (activeSearchEmbedUrl != null) {
                                    webView.loadUrl(activeSearchEmbedUrl!!)
                                } else {
                                    val html = getYouTubeIframeHtml(activeVideo.youtubeVideoId)
                                    webView.loadDataWithBaseURL("https://www.youtube-nocookie.com", html, "text/html", "UTF-8", null)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Active Video Info & Play Buttons
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${activeVideo.emoji} ${activeVideo.title}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Channel: ${activeVideo.channel} • ${activeVideo.subject}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Action Buttons: Open in YouTube & Random
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Direct YouTube App / Web launcher fallback
                            val context = LocalContext.current
                            Button(
                                onClick = {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.youtube.com/watch?v=${activeVideo.youtubeVideoId}")
                                    )
                                    context.startActivity(intent)
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KidsPrimary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("launch_youtube_app_button")
                            ) {
                                Icon(Icons.Default.OpenInNew, contentDescription = "Launch YouTube", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Launch YouTube", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    activeSearchEmbedUrl = null
                                    val random = filteredVideos.shuffled().firstOrNull() ?: sampleKidsVideos.random()
                                    activeVideo = random
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = KidsOrange),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("play_random_video_button")
                            ) {
                                Icon(Icons.Default.Shuffle, contentDescription = "Random", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Next Random 🎲", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Kids-Safe YouTube Search Bar
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search Kids Videos (e.g., Solar System, Phonics)", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("kids_video_search_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = KidsPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (searchQuery.isNotBlank()) {
                                val queryClean = java.net.URLEncoder.encode("$searchQuery for kids education", "UTF-8")
                                activeSearchEmbedUrl = "https://m.youtube.com/results?search_query=$queryClean"
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(KidsPrimary)
                            .testTag("search_kids_video_button")
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                }
            }
        }

        // Subject Category Tabs Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "📚 Educational Subjects:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(KidsSubject.entries) { subj ->
                        val isSelected = selectedSubject == subj
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) subj.color else MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = if (isSelected) 4.dp else 0.dp,
                            modifier = Modifier
                                .clickable {
                                    selectedSubject = subj
                                    activeSearchEmbedUrl = null
                                }
                                .testTag("subject_tab_${subj.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(subj.emoji, fontSize = 16.sp)
                                Text(
                                    text = subj.displayName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Educational Video Cards List
        item {
            Text(
                text = "▶️ ${selectedSubject.displayName} (${filteredVideos.size} Videos)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(filteredVideos) { video ->
            val isPlaying = activeVideo.id == video.id && activeSearchEmbedUrl == null
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        activeSearchEmbedUrl = null
                        activeVideo = video
                    }
                    .testTag("video_item_${video.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPlaying) video.color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isPlaying) 6.dp else 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Play Button Badge
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(video.color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(video.emoji, fontSize = 24.sp)
                    }

                    // Video Meta
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            maxLines = 2,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = video.channel,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text("•", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                            Text(
                                text = "⏱️ ${video.duration}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = video.color
                            )
                        }
                    }

                    // Active Playing Indicator or Play Icon
                    IconButton(
                        onClick = {
                            activeSearchEmbedUrl = null
                            activeVideo = video
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isPlaying) video.color else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = if (isPlaying) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
