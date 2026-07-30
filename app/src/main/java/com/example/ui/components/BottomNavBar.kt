package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsPrimary
import com.example.ui.theme.KidsPurple
import com.example.ui.theme.KidsSecondary
import com.example.ui.theme.KidsYellow

enum class NavScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val emoji: String,
    val activeColor: Color
) {
    HOME("home", "Home", Icons.Default.Home, "🏠", KidsPrimary),
    MATH("math", "Math", Icons.Default.Extension, "🔢", KidsOrange),
    QUIZ("quiz", "Quiz", Icons.Default.TaskAlt, "🔬", KidsPurple),
    PUZZLES("puzzles", "Puzzles", Icons.Default.Extension, "🧩", KidsSecondary),
    DAILY("daily", "Quests", Icons.Default.TaskAlt, "🎯", KidsGreen),
    VIDEOS("videos", "Videos", Icons.Default.OndemandVideo, "📺", KidsPurple),
    PROFILE("profile", "Profile", Icons.Default.Person, "👤", KidsYellow)
}

@Composable
fun AppBottomNavBar(
    currentRoute: String,
    onNavigate: (NavScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        // Exclude PROFILE from bottom navbar as it is positioned in the top header bar
        NavScreen.entries.filter { it != NavScreen.PROFILE }.forEach { screen ->
            val isSelected = currentRoute == screen.route

            val itemColor by animateColorAsState(
                targetValue = if (isSelected) screen.activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = tween(300),
                label = "NavColor"
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(screen.emoji, fontSize = if (isSelected) 18.sp else 16.sp)
                    }
                },
                label = {
                    Text(
                        text = screen.title,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp,
                        color = itemColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = screen.activeColor,
                    indicatorColor = screen.activeColor.copy(alpha = 0.2f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_${screen.route}")
            )
        }
    }
}

