package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudioRepository
import com.example.model.VideoItem
import com.example.ui.components.MetricIndicatorBadge
import com.example.ui.components.VideoThumbnailView
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    onVideoClick: (VideoItem) -> Unit,
    onAnalyticsClick: () -> Unit
) {
    val profile = StudioRepository.danielProfile
    var isContentExpanded by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(StudioPureBlack)
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
    ) {
        // Channel Summary
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large circular channel avatar (Vibrant orange circular avatar)
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFFFF7043), StudioAvatarOrange, Color(0xFFE64A19))
                            )
                        )
                        .testTag("channel_summary_avatar")
                )

                Spacer(modifier = Modifier.width(18.dp))

                Column {
                    Text(
                        text = profile.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = profile.subscribersCount,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextWhite,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Total subscribers",
                        fontSize = 13.sp,
                        color = StudioTextMuted
                    )
                }
            }
        }

        // Channel Analytics Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAnalyticsClick() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Channel analytics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextWhite
                    )
                    Text(
                        text = "Last 28 days",
                        fontSize = 13.sp,
                        color = StudioTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2-Column, 2-Row Grid
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Row 1: Views & Watch time (hours)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 1: Views
                        AnalyticsMetricTile(
                            modifier = Modifier.weight(1f),
                            label = "Views",
                            value = StudioRepository.dashboardViews,
                            showGreenUp = true
                        )

                        // Card 2: Watch time (hours)
                        AnalyticsMetricTile(
                            modifier = Modifier.weight(1f),
                            label = "Watch time (hours)",
                            value = StudioRepository.dashboardWatchTime,
                            showGreenUp = true
                        )
                    }

                    // Row 2: Subscribers & Estimated revenue
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Card 3: Subscribers
                        AnalyticsMetricTile(
                            modifier = Modifier.weight(1f),
                            label = "Subscribers",
                            value = StudioRepository.dashboardSubscribers,
                            showGreenUp = true
                        )

                        // Card 4: Estimated revenue
                        AnalyticsMetricTile(
                            modifier = Modifier.weight(1f),
                            label = "Estimated revenue",
                            value = StudioRepository.dashboardRevenue,
                            showGreenUp = false
                        )
                    }
                }
            }
        }

        // Latest published content Section
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Latest published content",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = StudioTextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable Content Card
            val video = StudioRepository.dashboardLatestVideo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("latest_content_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = StudioCardDark)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Top row: Thumbnail & Title/Status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVideoClick(video) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VideoThumbnailView(
                            title = "POV",
                            isShort = false,
                            width = 78.dp,
                            height = 50.dp
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = StudioTextWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Copyright",
                                fontSize = 12.sp,
                                color = StudioTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 0.8.dp, color = StudioDividerDark)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Stats row: Views, Likes, Comments, Expand toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 18.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null,
                                tint = StudioTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "39K",
                                fontSize = 13.sp,
                                color = StudioTextWhite
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 18.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                tint = StudioTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "2.7K",
                                fontSize = 13.sp,
                                color = StudioTextWhite
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Comment,
                                contentDescription = null,
                                tint = StudioTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "220",
                                fontSize = 13.sp,
                                color = StudioTextWhite
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = { isContentExpanded = !isContentExpanded },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isContentExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isContentExpanded) "Collapse" else "Expand",
                                tint = StudioTextMuted,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Expanded Section
                    AnimatedVisibility(
                        visible = isContentExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(thickness = 0.8.dp, color = StudioDividerDark)
                            Spacer(modifier = Modifier.height(12.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                // Row 1: Ranking by views -> 1 of 10 >
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ranking by views",
                                        fontSize = 14.sp,
                                        color = StudioTextWhite
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "1 of 10",
                                            fontSize = 14.sp,
                                            color = StudioTextWhite
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            tint = StudioTextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                // Row 2: Views -> 39.5K ↑
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Views",
                                        fontSize = 14.sp,
                                        color = StudioTextWhite
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "39.5K",
                                            fontSize = 14.sp,
                                            color = StudioTextWhite
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        MetricIndicatorBadge(isPositive = true, showArrow = true)
                                    }
                                }

                                // Row 3: Impressions click-through rate (i) -> 8.3% ✓
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Impressions click-through rate",
                                            fontSize = 14.sp,
                                            color = StudioTextWhite
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Outlined.Info,
                                            contentDescription = "Info",
                                            tint = StudioTextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "8.3%",
                                            fontSize = 14.sp,
                                            color = StudioTextWhite
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        MetricIndicatorBadge(isPositive = true, isCheckmark = true)
                                    }
                                }

                                // Row 4: Average view duration -> 5:50 ↑
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Average view duration",
                                        fontSize = 14.sp,
                                        color = StudioTextWhite
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "5:50",
                                            fontSize = 14.sp,
                                            color = StudioTextWhite
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        MetricIndicatorBadge(isPositive = true, showArrow = true)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsMetricTile(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    showGreenUp: Boolean
) {
    Card(
        modifier = modifier.height(84.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 12.5.sp,
                color = StudioTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextWhite
                )
                if (showGreenUp) {
                    Spacer(modifier = Modifier.width(6.dp))
                    MetricIndicatorBadge(isPositive = true, showArrow = true, size = 18.dp)
                }
            }
        }
    }
}
