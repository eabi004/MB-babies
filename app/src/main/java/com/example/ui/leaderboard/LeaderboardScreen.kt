package com.example.ui.leaderboard

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LeaderboardEntity
import com.example.data.local.UserEntity
import com.example.ui.theme.KidsPrimary
import com.example.ui.theme.KidsPurple
import com.example.ui.theme.KidsSecondary
import com.example.ui.theme.KidsYellow

@Composable
fun LeaderboardScreen(
    entries: List<LeaderboardEntity>,
    user: UserEntity?,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        "GLOBAL" to "🌍 Global",
        "ENGLISH" to "🇬🇧 English",
        "TELUGU" to "🇮🇳 తెలుగు",
        "HINDI" to "🇮🇳 हिंदी"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("leaderboard_screen")
    ) {
        // Filter Tabs
        TabRow(
            selectedTabIndex = filters.indexOfFirst { it.first == selectedFilter }.coerceAtLeast(0),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = KidsPrimary
        ) {
            filters.forEach { (code, label) ->
                Tab(
                    selected = selectedFilter == code,
                    onClick = { onFilterSelected(code) },
                    text = { Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    modifier = Modifier.testTag("filter_tab_$code")
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = KidsPurple.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = KidsYellow,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Global Leaderboard",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Top CBSE Grade 1 Word Explorers worldwide",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Ranked Users
            items(entries) { entry ->
                val isCurrentUser = entry.username.equals(user?.username, ignoreCase = true)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("leaderboard_card_${entry.rank}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrentUser) KidsPrimary.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (entry.rank <= 3) 4.dp else 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Rank Medal
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (entry.rank) {
                                            1 -> KidsYellow
                                            2 -> Color(0xFFC0C0C0)
                                            3 -> Color(0xFFCD7F32)
                                            else -> MaterialTheme.colorScheme.surfaceContainerHighest
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (entry.rank) {
                                        1 -> "🥇"
                                        2 -> "🥈"
                                        3 -> "🥉"
                                        else -> "#${entry.rank}"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (entry.rank <= 3) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = if (entry.avatarId.contains("cat")) "🐱" else if (entry.avatarId.contains("lion")) "🦁" else "🦉",
                                fontSize = 24.sp
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = entry.username,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = entry.badgeTitle,
                                    fontSize = 11.sp,
                                    color = KidsSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Score
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "⚡ ${entry.totalXp} XP",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = KidsPrimary
                            )
                            Text(
                                text = "⭐ ${entry.stars} Stars",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Current User Footer Card
        if (user != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = KidsPrimary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🦉", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Your Current Rank: #4",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = user.username,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        text = "⚡ ${user.totalXp} XP",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
