package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import com.example.model.ContentType
import com.example.model.DrilldownContentItem
import com.example.model.MetricCardData
import com.example.model.VideoItem
import com.example.ui.components.*
import com.example.ui.theme.*

enum class AnalyticsSubTab(val label: String) {
    OVERVIEW("Overview"),
    CONTENT("Content"),
    AUDIENCE("Audience"),
    TRENDS("Trends")
}

@Composable
fun AnalyticsScreen(
    onVideoClick: (VideoItem) -> Unit
) {
    var selectedSubTab by remember { mutableStateOf(AnalyticsSubTab.OVERVIEW) }
    var selectedContentFilter by remember { mutableStateOf("Shorts") }
    var activeDrilldownMetric by remember { mutableStateOf<MetricCardData?>(null) }

    val colors = StudioTheme.colors

    if (activeDrilldownMetric != null) {
        MetricDrilldownScreen(
            metric = activeDrilldownMetric!!,
            onBackClick = { activeDrilldownMetric = null },
            onVideoClick = onVideoClick
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .testTag("analytics_screen")
    ) {
        // Sub-tabs Row (Overview, Content, Audience, Trends)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AnalyticsSubTab.values().forEach { subTab ->
                val isSelected = subTab == selectedSubTab
                Column(
                    modifier = Modifier
                        .clickable { selectedSubTab = subTab }
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = subTab.label,
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) colors.textPrimary else colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(52.dp)
                            .height(2.5.dp)
                            .background(if (isSelected) colors.textPrimary else Color.Transparent)
                    )
                }
            }
        }

        HorizontalDivider(thickness = 0.8.dp, color = colors.divider)

        // SubTab content
        when (selectedSubTab) {
            AnalyticsSubTab.OVERVIEW -> OverviewTabContent(
                onMetricClick = { activeDrilldownMetric = it },
                onVideoClick = onVideoClick
            )
            AnalyticsSubTab.CONTENT -> ContentTabAnalytics(
                selectedFilter = selectedContentFilter,
                onFilterSelected = { selectedContentFilter = it },
                onVideoClick = onVideoClick
            )
            AnalyticsSubTab.AUDIENCE -> AudienceTabContent(onVideoClick)
            AnalyticsSubTab.TRENDS -> TrendsTabContent()
        }
    }
}

@Composable
private fun OverviewTabContent(
    onMetricClick: (MetricCardData) -> Unit,
    onVideoClick: (VideoItem) -> Unit
) {
    val colors = StudioTheme.colors
    val overviewCards = listOf(
        StudioRepository.overviewViewsMetric,
        StudioRepository.overviewWatchTimeMetric,
        StudioRepository.overviewSubscribersMetric,
        StudioRepository.overviewEstimatedRevenueMetric // Explicit addition: Estimated revenue card
    )
    val pagerState = rememberPagerState(pageCount = { overviewCards.size })

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Swipeable Metric Cards Pager
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val metric = overviewCards[page]
                        Box(modifier = Modifier.clickable { onMetricClick(metric) }) {
                            MetricCarouselCard(metric = metric)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Pager Dots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(overviewCards.size) { i ->
                            val isCurrent = pagerState.currentPage == i
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(if (isCurrent) 6.dp else 5.dp)
                                    .clip(CircleShape)
                                    .background(if (isCurrent) colors.textPrimary else colors.textTertiary)
                            )
                        }
                    }
                }
            }
        }

        // Top content section (Views · Last 28 days)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Top content",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Views · Last 28 days",
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    StudioRepository.topContentList.forEach { video ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVideoClick(video) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VideoThumbnailView(
                                title = video.title,
                                isShort = video.type == ContentType.SHORT,
                                width = 38.dp,
                                height = if (video.type == ContentType.SHORT) 52.dp else 30.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = video.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.textPrimary,
                                modifier = Modifier.weight(1f),
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${video.views}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.textPrimary
                            )
                        }
                    }
                }
            }
        }

        // Real time section (Views · 48 hours)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Real time",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "49",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Views · 48 hours",
                                fontSize = 13.sp,
                                color = colors.textSecondary
                            )
                        }

                        RealtimeBarChart(
                            viewsCount = 49,
                            modifier = Modifier.width(140.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 0.8.dp, color = colors.divider)
                    Spacer(modifier = Modifier.height(8.dp))

                    StudioRepository.realtimeVideos.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VideoThumbnailView(
                                title = item.title,
                                isShort = true,
                                width = 36.dp,
                                height = 48.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.textPrimary,
                                modifier = Modifier.weight(1f),
                                maxLines = 1
                            )
                            Text(
                                text = "${item.views}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(14.dp)
                            ) {
                                RealtimeBarChart(viewsCount = item.views)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricDrilldownScreen(
    metric: MetricCardData,
    onBackClick: () -> Unit,
    onVideoClick: (VideoItem) -> Unit
) {
    val colors = StudioTheme.colors
    val dateChips = listOf("7D", "28D", "90D", "365D", "Oct", "Sept", "Aug", "2026", "2025", "Lifetime")
    var selectedDateRange by remember { mutableStateOf("28D") }

    val headlineText = when (metric.title) {
        "Views" -> "Your channel got ${if (selectedDateRange == "7D") "195" else if (selectedDateRange == "90D") "1,840" else "812"} views in the last ${selectedDateRange}"
        "Watch time (hours)" -> "Your channel got ${metric.value} hours in the last ${selectedDateRange}"
        "Subscribers" -> "Your channel was ${metric.value} in the last ${selectedDateRange}"
        "Estimated revenue" -> "Your estimated revenue was ${metric.value} in the last ${selectedDateRange} (Simulated)"
        else -> "${metric.title} in the last ${selectedDateRange}"
    }

    val topItems: List<DrilldownContentItem> = when (metric.title) {
        "Views" -> StudioRepository.drilldownTopContentViews
        "Watch time (hours)" -> StudioRepository.drilldownTopContentWatchTime
        "Subscribers" -> StudioRepository.drilldownTopContentSubscribers
        "Estimated revenue" -> StudioRepository.drilldownTopContentRevenue
        else -> emptyList()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.background)
                    .statusBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colors.textPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = metric.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }
        },
        containerColor = colors.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Horizontal Date Range Selector Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dateChips.forEach { chip ->
                        val isSelected = chip == selectedDateRange
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) (if (colors.isDark) Color.White else Color.Black) else colors.chipBackground)
                                .border(1.dp, if (isSelected) Color.Transparent else colors.chipBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedDateRange = chip }
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chip,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) (if (colors.isDark) Color.Black else Color.White) else colors.textPrimary
                            )
                        }
                    }
                }
            }

            // Headline
            item {
                Text(
                    text = headlineText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // Big Drilldown Chart
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TrendLineChart(
                            data = metric.trendData,
                            lineColor = if (metric.title == "Estimated revenue") StudioGreen else Color(0xFF00ACC1),
                            yMinLabel = metric.yMinLabel,
                            yMidLabel = metric.yMidLabel,
                            yMaxLabel = metric.yMaxLabel,
                            startDateLabel = "24 Sept",
                            endDateLabel = "21 Oct"
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(thickness = 0.8.dp, color = colors.divider)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Metric Legend and Comparison
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (metric.title == "Estimated revenue") StudioGreen else Color(0xFF00ACC1))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = metric.title,
                                    fontSize = 14.sp,
                                    color = colors.textPrimary
                                )
                            }
                            Text(
                                text = metric.value,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }

                        if (metric.subtitle.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (metric.title == "Views") "612 more than the usual 140–200" else metric.subtitle,
                                fontSize = 13.sp,
                                color = if (metric.isPositive) StudioGreen else colors.textSecondary
                            )
                        }
                    }
                }
            }

            // Top Content Breakdown List (matching recording)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Top content",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(topItems.size) { i ->
                val item = topItems[i]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VideoThumbnailView(
                            title = item.title,
                            isShort = item.isShort,
                            width = 38.dp,
                            height = if (item.isShort) 48.dp else 28.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = item.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.textPrimary,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.valueText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                    }

                    if (item.sharePercent > 0f) {
                        Spacer(modifier = Modifier.height(4.dp))
                        StudioLinearProgress(
                            percentage = item.sharePercent * 100f,
                            barColor = Color(0xFF00ACC1),
                            modifier = Modifier.padding(start = 50.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentTabAnalytics(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    onVideoClick: (VideoItem) -> Unit
) {
    val colors = StudioTheme.colors
    val filters = listOf("All", "Videos", "Shorts", "Live", "Posts")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Filter pills row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .height(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) (if (colors.isDark) Color.White else Color.Black) else colors.chipBackground)
                            .border(1.dp, if (isSelected) Color.Transparent else colors.chipBorder, RoundedCornerShape(8.dp))
                            .clickable { onFilterSelected(filter) }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) (if (colors.isDark) Color.Black else Color.White) else colors.textPrimary
                        )
                    }
                }
            }
        }

        when (selectedFilter) {
            "Shorts" -> {
                // Shorts cards (matching screen recording 00:26 - 00:28 & 00:48 - 00:52)
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Views", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "772", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                MetricIndicatorBadge(isPositive = true, showArrow = true)
                            }
                            Text(text = "662 more than usual", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(14.dp))
                            TrendLineChart(
                                data = listOf(10f, 20f, 60f, 15f, 25f, 40f, 30f, 35f, 20f, 25f, 30f, 25f, 40f),
                                lineColor = Color(0xFF673AB7),
                                yMinLabel = "0",
                                yMaxLabel = "60"
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "How viewers engaged", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "Last 28 days", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(14.dp))
                            StudioLinearProgress(percentage = 83.2f, barColor = Color(0xFF673AB7))
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text(text = "83.2%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                    Text(text = "Stayed to watch", fontSize = 12.sp, color = colors.textSecondary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "16.8%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                    Text(text = "Swiped away", fontSize = 12.sp, color = colors.textSecondary)
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Top Shorts", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "Views · Last 28 days", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                VideoThumbnailView(title = "80 HAZAR", isShort = true, width = 36.dp, height = 48.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "80 HAZAR KE MERE SHOES 👟 MEKO...",
                                    fontSize = 13.sp,
                                    color = colors.textPrimary,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )
                                Text(text = "735", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "How viewers find your Shorts", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "Views · Last 28 days", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(14.dp))
                            listOf(
                                "YouTube search" to "95.0%",
                                "Shorts feed" to "1.7%",
                                "Sound pages" to "1.3%"
                            ).forEach { (src, pct) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = src, fontSize = 14.sp, color = colors.textPrimary)
                                    Text(text = pct, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                }
                            }
                        }
                    }
                }
            }

            "Videos" -> {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Views", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "39", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                MetricIndicatorBadge(isPositive = false, showArrow = true)
                            }
                            Text(text = "11 fewer than usual", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(14.dp))
                            TrendLineChart(
                                data = listOf(1f, 0.5f, 2f, 1f, 1f, 3f, 1f, 0f, 2f, 1f, 4f, 1f),
                                lineColor = Color(0xFF673AB7),
                                yMinLabel = "0",
                                yMaxLabel = "6"
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "How viewers find your videos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "Views · Last 28 days", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(14.dp))
                            listOf(
                                "YouTube search" to "56.4%",
                                "Browse features" to "20.5%",
                                "Other YouTube features" to "12.8%"
                            ).forEach { (src, pct) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = src, fontSize = 14.sp, color = colors.textPrimary)
                                    Text(text = pct, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                // All, Live, Posts
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Views", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "Last 28 days", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Shorts", fontSize = 14.sp, color = colors.textPrimary)
                                Text(text = "772", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            StudioLinearProgress(percentage = 95f, barColor = Color(0xFF673AB7))

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Videos", fontSize = 14.sp, color = colors.textPrimary)
                                Text(text = "39", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            StudioLinearProgress(percentage = 5f, barColor = Color(0xFF673AB7))
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Published content", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                            Text(text = "Last 28 days", fontSize = 13.sp, color = colors.textSecondary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(colors.chipBackground)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = colors.textSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Nothing to show for these dates",
                                        fontSize = 13.sp,
                                        color = colors.textSecondary
                                    )
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
private fun AudienceTabContent(onVideoClick: (VideoItem) -> Unit) {
    val colors = StudioTheme.colors

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Monthly audience Card (Recording 00:56)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Monthly audience",
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "368",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TrendLineChart(
                        data = StudioRepository.audienceMonthlyMetric.trendData,
                        lineColor = Color(0xFF673AB7),
                        yMinLabel = "0",
                        yMidLabel = "170",
                        yMaxLabel = "510",
                        startDateLabel = "24 Sept",
                        endDateLabel = "21 Oct"
                    )
                }
            }
        }

        // Audience by watch behaviour
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Audience by watch behaviour",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Monthly audience · 21 Oct 2026",
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    AudienceSegmentBar(newPct = 97.6f, casualPct = 2.4f, regularPct = 0.1f)
                    Spacer(modifier = Modifier.height(14.dp))

                    listOf(
                        Triple("New viewers", "97.6%", Color(0xFF311B92)),
                        Triple("Casual viewers", "2.4%", Color(0xFF7E57C2)),
                        Triple("Regular viewers", "< 0.1%", Color(0xFFBA68C8))
                    ).forEach { (label, pct, col) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(col))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = label, fontSize = 14.sp, color = colors.textPrimary)
                            }
                            Text(text = pct, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
                        }
                    }
                }
            }
        }

        // Device type (Recording 01:01)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Device type", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                    Text(text = "Watch time (hours) · Last 28 days", fontSize = 13.sp, color = colors.textSecondary)
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                    ) {
                        Box(modifier = Modifier.weight(91.7f).fillMaxHeight().background(Color(0xFF311B92)))
                        Box(modifier = Modifier.weight(5.1f).fillMaxHeight().background(Color(0xFF7E57C2)))
                        Box(modifier = Modifier.weight(1.6f).fillMaxHeight().background(Color(0xFFBA68C8)))
                        Box(modifier = Modifier.weight(1.6f).fillMaxHeight().background(Color(0xFFE91E63)))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    listOf(
                        "Mobile" to "91.7%",
                        "TV" to "5.1%",
                        "Computer" to "1.6%",
                        "Tablet" to "1.6%"
                    ).forEach { (dev, pct) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = dev, fontSize = 14.sp, color = colors.textPrimary)
                            Text(text = pct, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
                        }
                    }
                }
            }
        }

        // Geography (Recording 00:55)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Geography", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                    Text(text = "Views · Last 28 days", fontSize = 13.sp, color = colors.textSecondary)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "India", fontSize = 14.sp, color = colors.textPrimary)
                        Text(text = "86.1%", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    StudioLinearProgress(percentage = 86.1f, barColor = Color(0xFF673AB7))
                }
            }
        }
    }
}

@Composable
private fun TrendsTabContent() {
    val colors = StudioTheme.colors
    var searchQuery by remember { mutableStateOf("") }
    var topicSaves by remember { mutableStateOf(mapOf("bengali bangladeshi short film" to true, "bengali reels" to false)) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Explore topics header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Explore topics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Help",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = colors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary
                    )
                }
            }
        }

        // Search Input
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(colors.searchBackground)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (searchQuery.isEmpty()) "Search" else searchQuery,
                        fontSize = 15.sp,
                        color = if (searchQuery.isEmpty()) colors.textSecondary else colors.textPrimary
                    )
                }
            }
        }

        // Top searches header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Top searches",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colors.textPrimary
                )
            }
        }

        // Top search cards
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(StudioRepository.topSearches.size) { i ->
                    val topic = StudioRepository.topSearches[i]
                    val isSaved = topicSaves[topic.title] ?: false
                    Card(
                        modifier = Modifier
                            .width(220.dp)
                            .border(1.dp, colors.cardBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = topic.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary,
                                minLines = 2,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        topicSaves = topicSaves.toMutableMap().apply {
                                            put(topic.title, !isSaved)
                                        }
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Save",
                                        tint = if (isSaved) StudioRed else colors.textPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Options",
                                    tint = colors.textPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recent videos
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent videos",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colors.textPrimary
                )
            }
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(StudioRepository.trendRecentVideos.size) { i ->
                    val v = StudioRepository.trendRecentVideos[i]
                    Column(modifier = Modifier.width(220.dp)) {
                        VideoThumbnailView(
                            title = v.title,
                            duration = v.duration,
                            width = 220.dp,
                            height = 124.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = v.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary,
                                modifier = Modifier.weight(1f),
                                maxLines = 2
                            )
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = colors.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${v.channelName} · ${v.viewsText} · ${v.timeAgo}",
                            fontSize = 11.sp,
                            color = colors.textSecondary,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCarouselCard(
    metric: MetricCardData,
    lineColor: Color = Color(0xFF00ACC1)
) {
    val colors = StudioTheme.colors
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = metric.title,
            fontSize = 13.sp,
            color = colors.textSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = metric.value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            MetricIndicatorBadge(
                isPositive = metric.isPositive,
                isCheckmark = metric.isCheckmark,
                showArrow = metric.showArrow
            )
        }
        if (metric.subtitle.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = metric.subtitle,
                fontSize = 13.sp,
                color = if (metric.isSimulatedDemo) StudioGreen else colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        TrendLineChart(
            data = metric.trendData,
            lineColor = if (metric.title == "Estimated revenue") StudioGreen else lineColor,
            yMinLabel = metric.yMinLabel,
            yMidLabel = metric.yMidLabel,
            yMaxLabel = metric.yMaxLabel,
            startDateLabel = metric.startDateLabel,
            endDateLabel = metric.endDateLabel
        )
    }
}
