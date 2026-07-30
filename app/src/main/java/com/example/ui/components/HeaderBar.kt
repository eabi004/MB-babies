package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.data.local.UserEntity
import com.example.model.Language
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsPrimary
import com.example.ui.theme.KidsPurple
import com.example.ui.theme.KidsYellow

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopHeaderBar(
    user: UserEntity?,
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onOpenProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_header_bar"),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Branding & Child Profile Avatar (Clickable to open profile)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onOpenProfile() }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(KidsPurple.copy(alpha = 0.2f))
                        .padding(6.dp)
                ) {
                    Text(
                        text = if (user?.avatarId?.contains("cat") == true) "🐱" else if (user?.avatarId?.contains("lion") == true) "🦁" else "👶",
                        fontSize = 22.sp
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MB Babies",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = KidsPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("✨", fontSize = 12.sp)
                    }

                    Text(
                        text = user?.username ?: "Little Explorer",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Interactive Profile Button on Top Right
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = KidsPurple.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, KidsPurple.copy(alpha = 0.3f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onOpenProfile() }
                    .testTag("top_profile_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("👤", fontSize = 14.sp)
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = KidsPurple
                    )
                }
            }
        }
    }
}

