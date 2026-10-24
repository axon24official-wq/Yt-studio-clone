package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudioRepository
import com.example.ui.theme.*

data class EarnPerkCard(
    val title: String,
    val description: String,
    val iconEmoji: String,
    val iconBgColor: Color
)

@Composable
fun EarnScreen(
    onOpenAdminPanel: () -> Unit = {}
) {
    val colors = StudioTheme.colors
    val perkCards = listOf(
        EarnPerkCard(
            title = "Memberships",
            description = "Create a fan club of members paying monthly for access to exclusive perks",
            iconEmoji = "⭐",
            iconBgColor = if (colors.isDark) Color(0xFF1E3A24) else Color(0xFFC8E6C9)
        ),
        EarnPerkCard(
            title = "Supers",
            description = "Engage with fans who show support through one-time, interactive purchases",
            iconEmoji = "🌸",
            iconBgColor = if (colors.isDark) Color(0xFF381F40) else Color(0xFFE1BEE7)
        ),
        EarnPerkCard(
            title = "Watch page ads",
            description = "Earn from ads and YouTube Premium on the watch page",
            iconEmoji = "🧳",
            iconBgColor = if (colors.isDark) Color(0xFF3E2D1A) else Color(0xFFFFCC80)
        ),
        EarnPerkCard(
            title = "Shorts Feed ads",
            description = "Earn from ads and YouTube Premium in the Shorts Feed",
            iconEmoji = "📱",
            iconBgColor = if (colors.isDark) Color(0xFF3E3A1A) else Color(0xFFFFF59D)
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 16.dp)
            .testTag("earn_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Main Heading / Partner Status Banner
        item {
            if (StudioRepository.isMonetized) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (colors.isDark) Color(0xFF142918) else Color(0xFFE8F5E9)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (colors.isDark) Color(0xFF2E7D32) else Color(0xFFA5D6A7)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✓", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "You're a YouTube Partner",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                                Text(
                                    text = "Monetization: Active",
                                    fontSize = 13.sp,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "You're earning from Watch Page ads, Shorts Feed ads, Memberships and Supers.",
                            fontSize = 13.sp,
                            color = colors.textSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                Text(
                    text = "Become a YouTube\npartner",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    lineHeight = 34.sp
                )
            }
        }

        // Next milestone banner
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Next milestone",
                        fontSize = 14.sp,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Start your earning journey",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Earn income while connecting with your fans and gain access to support forums and creator tools",
                    fontSize = 14.sp,
                    color = colors.textSecondary,
                    lineHeight = 20.sp
                )
            }
        }

        // Benefits Horizontal Carousel (Screenshots 2, 5, 12)
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(perkCards.size) { i ->
                    val card = perkCards[i]
                    Card(
                        modifier = Modifier
                            .width(220.dp)
                            .height(240.dp)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(CircleShape)
                                    .background(card.iconBgColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = card.iconEmoji, fontSize = 38.sp)
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = card.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = card.description,
                                fontSize = 12.sp,
                                color = colors.textSecondary,
                                lineHeight = 16.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Carousel dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(colors.textPrimary))
                Spacer(modifier = Modifier.width(6.dp))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(colors.chipBorder))
                Spacer(modifier = Modifier.width(6.dp))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(colors.chipBorder))
            }
        }

        // Section: Eligibility
        item {
            Text(
                text = "Eligibility",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Showing data as of: 16 Oct 2026",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Learn more",
                        fontSize = 14.sp,
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(start = 26.dp)
                            .clickable { }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Metric 1: Uploads
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "${StudioRepository.monetizationVideoUploads} video uploads", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "last 90 days", fontSize = 13.sp, color = colors.textSecondary)
                        }
                        Text(text = "3", fontSize = 14.sp, color = colors.textPrimary)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "and one of the following",
                        fontSize = 13.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Metric 2: Watch hours
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (colors.isDark) Color(0xFF2A2A2A) else Color(0xFFEEEEEE))
                    ) {
                        Box(
                            modifier = Modifier
                                .width(8.dp)
                                .fillMaxHeight()
                                .background(Color(0xFF065FD4))
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "${StudioRepository.monetizationWatchHours} qualified watch hours", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "last 365 days", fontSize = 13.sp, color = colors.textSecondary)
                        }
                        Text(text = StudioRepository.monetizationRequiredWatchHours, fontSize = 14.sp, color = colors.textPrimary)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Metric 3: Shorts views
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (colors.isDark) Color(0xFF2A2A2A) else Color(0xFFEEEEEE))
                    ) {
                        Box(
                            modifier = Modifier
                                .width(8.dp)
                                .fillMaxHeight()
                                .background(Color(0xFF065FD4))
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "${StudioRepository.monetizationShortsViews} qualified Shorts views", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "last 90 days", fontSize = 13.sp, color = colors.textSecondary)
                        }
                        Text(text = StudioRepository.monetizationRequiredShortsViews, fontSize = 14.sp, color = colors.textPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Some benefits may have additional eligibility criteria",
                fontSize = 13.sp,
                color = colors.textSecondary
            )
        }

        // Section: Next steps
        item {
            Text(
                text = "Next steps",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "2-Step Verification",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colors.textSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Button 1: Learn more about applying
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.chipBackground,
                    contentColor = colors.textPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = "Learn more about applying",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Button 2: Apply now (Disabled state)
            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = if (colors.isDark) Color(0xFF2A2A2A) else Color(0xFFCCCCCC),
                    disabledContentColor = if (colors.isDark) Color(0xFF666666) else Color.White
                )
            ) {
                Text(
                    text = "Apply now",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Complete 2-Step Verification to apply",
                fontSize = 13.sp,
                color = colors.textSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        // Section: Future milestone -> Continue to grow
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Future milestone",
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Continue to grow",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Unlock even more ways to earn, including the option to show ads on videos",
                    fontSize = 14.sp,
                    color = colors.textSecondary
                )
            }
        }

        // Section: Creator resources and tools (Screenshot 5)
        item {
            Text(
                text = "Creator resources and tools",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = colors.textPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "YouTube Creator Support",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = colors.textSecondary)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Copyright,
                        contentDescription = null,
                        tint = colors.textPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Copyright Match Tool",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = colors.textSecondary)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartDisplay,
                        contentDescription = null,
                        tint = colors.textPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Video end screens",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = colors.textSecondary)
                }
            }
        }

        // Section: Small Admin Panel option at the bottom of the last navigation page
        item {
            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(thickness = 0.8.dp, color = colors.divider)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onOpenAdminPanel() }
                    .padding(vertical = 12.dp, horizontal = 4.dp)
                    .testTag("open_admin_panel_button"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = "Admin Panel",
                    tint = colors.textSecondary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Admin Panel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Local device customizations and demo controls",
                        fontSize = 12.sp,
                        color = colors.textSecondary
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
