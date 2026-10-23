package com.example.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.model.*

object StudioRepository {
    private const val TAG = "StudioRepository"

    // Default Profile
    private val defaultDanielProfile = ChannelProfile(
        id = "channel_daniel",
        name = "Daniel Dalen",
        handle = "@danieldalen",
        subscribersCount = "48,237",
        isMonetized = true
    )

    // Reactive Profile (updates automatically when synced)
    var danielProfile by mutableStateOf(defaultDanielProfile)
    var currentProfile by mutableStateOf(defaultDanielProfile)

    val demoChannels = listOf(
        defaultDanielProfile,
        ChannelProfile(
            id = "channel_angel",
            name = "Angel",
            handle = "@angel-664",
            subscribersCount = "2,460"
        ),
        ChannelProfile(
            id = "channel_music",
            name = "Angel Mashups",
            handle = "@angel_mashups",
            subscribersCount = "12.4K"
        )
    )

    // Dashboard analytics 2x2 grid data (reactive Compose state)
    var dashboardViews by mutableStateOf("887.7K")
    var dashboardWatchTime by mutableStateOf("66.2K")
    var dashboardSubscribers by mutableStateOf("+39.0K")
    var dashboardRevenue by mutableStateOf("$0")
    var dashboardPeriodLabel by mutableStateOf("Last 28 days")

    // Sync State
    var isSyncing by mutableStateOf(false)
    var isConnectedToSupabase by mutableStateOf(false)
    var lastSyncError by mutableStateOf<String?>(null)

    // Dashboard latest published video
    var dashboardLatestVideo by mutableStateOf(
        VideoItem(
            id = "vid_daniel_pov",
            title = "pov: a 26 year old entrepreneur at a bus...",
            type = ContentType.VIDEO,
            duration = "8:42",
            publishedTimeAgo = "First 1 day, 6 hours",
            views = 39500,
            likes = 2700,
            comments = 220,
            rankingText = "1 of 10",
            avgPercentageViewed = "5:50",
            isExpanded = true,
            thumbnailTitle = "POV"
        )
    )

    // Dashboard latest content list
    var latestPublishedContent by mutableStateOf(
        listOf(
            VideoItem(
                id = "vid_unfreeze",
                title = "#unfrezzmyaccount #lifeisbutadrea...",
                type = ContentType.SHORT,
                duration = "0:15",
                publishedTimeAgo = "First 177 days, 14 hours",
                views = 2,
                likes = 0,
                comments = 0,
                rankingText = "8 of 10",
                avgPercentageViewed = "95.0%",
                isExpanded = true,
                thumbnailTitle = "UNFREEZE"
            ),
            VideoItem(
                id = "vid_26march",
                title = "26 March 2026",
                type = ContentType.SHORT,
                duration = "0:12",
                publishedTimeAgo = "First 209 days, 21 hours",
                views = 6,
                likes = 0,
                comments = 0,
                rankingText = "3 of 10",
                avgPercentageViewed = "84.2%",
                isExpanded = false,
                thumbnailTitle = "26 MARCH"
            ),
            VideoItem(
                id = "vid_eid",
                title = "#eidkobhoga2026 #eid #eidmubarak",
                type = ContentType.VIDEO,
                duration = "0:27",
                publishedTimeAgo = "First 217 days, 12 hours",
                views = 1,
                likes = 0,
                comments = 0,
                rankingText = "9 of 10",
                avgPercentageViewed = "62.0%",
                isExpanded = false,
                thumbnailTitle = "सऊदी से आ गई बड़ी खबर चाँद हुआ या नहीं? 2026"
            )
        )
    )

    val dashboardComment = CommentItem(
        id = "c1",
        authorHandle = "@gautamm24",
        timeAgo = "1 month ago",
        content = "Ye kosa gana hai bhai ??☠️🧐",
        videoTitle = "80 HAZAR KE MERE SHOES 👟 MEKO MUT TU KUDH KO PUCH#mcstan #f...",
        likesCount = 0
    )

    // Content tab lists (reactive)
    var contentVideos by mutableStateOf(
        listOf(
            VideoItem(
                id = "c_vid_1",
                title = "#eidkobhoga2026 #eid #...",
                type = ContentType.VIDEO,
                duration = "0:27",
                publishedTimeAgo = "217 days ago",
                views = 1,
                likes = 0,
                thumbnailTitle = "सऊदी से आ गई बड़ी खबर\nचाँद हुआ या नहीं?\n2026"
            ),
            VideoItem(
                id = "c_vid_2",
                title = "Waz ওয়াজ Mizanur Rahm...",
                type = ContentType.VIDEO,
                duration = "12:15",
                publishedTimeAgo = "230 days ago",
                views = 2,
                likes = 0,
                thumbnailTitle = "ওয়াজ মিজানুর রহমান"
            ),
            VideoItem(
                id = "c_vid_3",
                title = "UNOWAY - Swervin' [No C...",
                type = ContentType.VIDEO,
                duration = "3:03",
                publishedTimeAgo = "240 days ago",
                views = 51,
                likes = 2,
                thumbnailTitle = "SWERVIN"
            ),
            VideoItem(
                id = "c_vid_4",
                title = "Kun Anta (كن أنت) - Slowe...",
                type = ContentType.VIDEO,
                duration = "4:40",
                publishedTimeAgo = "250 days ago",
                views = 27,
                likes = 2,
                thumbnailTitle = "كن أنت Slowed"
            )
        )
    )

    var contentShorts by mutableStateOf(
        listOf(
            VideoItem(
                id = "s_1",
                title = "#unfrezzmyacc...",
                type = ContentType.SHORT,
                duration = "0:15",
                publishedTimeAgo = "177 days ago",
                views = 2,
                likes = 0,
                thumbnailTitle = "Boy in Red"
            ),
            VideoItem(
                id = "s_2",
                title = "26 March 2026",
                type = ContentType.SHORT,
                duration = "0:12",
                publishedTimeAgo = "209 days ago",
                views = 6,
                likes = 0,
                thumbnailTitle = "Striped Tee"
            ),
            VideoItem(
                id = "s_3",
                title = "#Gaming#gami...",
                type = ContentType.SHORT,
                duration = "0:30",
                publishedTimeAgo = "150 days ago",
                views = 8,
                likes = 0,
                thumbnailTitle = "White Pigeon Green Parrot"
            ),
            VideoItem(
                id = "s_4",
                title = "#Gaming#gami...",
                type = ContentType.SHORT,
                duration = "0:25",
                publishedTimeAgo = "140 days ago",
                views = 4,
                likes = 0,
                thumbnailTitle = "Pigeon on Train Track"
            ),
            VideoItem(
                id = "s_5",
                title = "#Gaming#ga...",
                type = ContentType.SHORT,
                duration = "0:20",
                publishedTimeAgo = "130 days ago",
                views = 5,
                likes = 0,
                thumbnailTitle = "Flying White Rabbit"
            )
        )
    )

    var contentLiveStreams by mutableStateOf(
        listOf(
            VideoItem(
                id = "l_1",
                title = "free fire live",
                type = ContentType.LIVE,
                duration = "12:08",
                publishedTimeAgo = "300 days ago",
                views = 1,
                likes = 0,
                thumbnailTitle = "FF LIVE"
            ),
            VideoItem(
                id = "l_2",
                title = "Free Fire Chalenge Custom...",
                type = ContentType.LIVE,
                duration = "16:04",
                publishedTimeAgo = "310 days ago",
                views = 3,
                likes = 0,
                thumbnailTitle = "AJJU BHAI VS AMIT BHAI"
            )
        )
    )

    // Analytics: Overview Tab (reactive)
    var overviewViewsMetric by mutableStateOf(
        MetricCardData(
            title = "Views",
            value = "812",
            subtitle = "612 more than usual",
            isPositive = true,
            showArrow = true,
            trendData = listOf(30f, 32f, 40f, 62f, 54f, 48f, 40f, 62f, 45f, 25f, 28f, 20f, 26f, 24f, 18f, 22f, 20f, 32f, 20f, 25f, 28f),
            yMinLabel = "0",
            yMidLabel = "30",
            yMaxLabel = "90",
            startDateLabel = "24 Sept",
            endDateLabel = "21 Oct"
        )
    )

    var overviewWatchTimeMetric by mutableStateOf(
        MetricCardData(
            title = "Watch time (hours)",
            value = "1.8",
            subtitle = "About the same as usual",
            isPositive = true,
            showArrow = false,
            isCheckmark = true,
            trendData = listOf(0.02f, 0.05f, 0.08f, 0.06f, 0.07f, 0.04f, 0.09f, 0.07f, 0.03f, 0.03f, 0.04f, 0.06f, 0.02f, 0.01f, 0.03f, 0.02f, 0.04f, 0.03f, 0.05f, 0.04f),
            yMinLabel = "0.0",
            yMidLabel = "",
            yMaxLabel = "0.1",
            startDateLabel = "24 Sept",
            endDateLabel = "21 Oct"
        )
    )

    var overviewSubscribersMetric by mutableStateOf(
        MetricCardData(
            title = "Subscribers",
            value = "-2",
            subtitle = "60% more than previous 28 days",
            isPositive = true,
            showArrow = true,
            trendData = listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, -1f, 0f, -1f, 0f, 0f, 0f, 0f),
            yMinLabel = "-3",
            yMidLabel = "-1",
            yMaxLabel = "0",
            startDateLabel = "24 Sept",
            endDateLabel = "21 Oct"
        )
    )

    var overviewEstimatedRevenueMetric by mutableStateOf(
        MetricCardData(
            title = "Estimated revenue",
            value = "$1,247.32",
            subtitle = "Simulated demo data",
            isPositive = true,
            showArrow = true,
            trendData = listOf(25f, 30f, 35f, 45f, 42f, 50f, 48f, 55f, 52f, 40f, 44f, 48f, 52f, 58f, 62f, 59f, 65f, 68f, 70f, 75f),
            yMinLabel = "$0",
            yMidLabel = "$50",
            yMaxLabel = "$100",
            startDateLabel = "24 Sept",
            endDateLabel = "21 Oct",
            isSimulatedDemo = true
        )
    )

    val topContentList = listOf(
        VideoItem("tc_1", "80 HAZAR KE MERE SHOES 👟...", ContentType.SHORT, "0:15", "28 days", 735, 0),
        VideoItem("tc_2", "Mohabbat Mohabbat Mohabbat...", ContentType.SHORT, "0:12", "28 days", 10, 0),
        VideoItem("tc_3", "Abhi ke Abhi Ke Jake Peheli Tic...", ContentType.SHORT, "0:20", "28 days", 8, 0),
        VideoItem("tc_4", "MC STAN - Mere Haath Mein X...", ContentType.VIDEO, "3:20", "28 days", 6, 0),
        VideoItem("tc_5", "Mc Stan - Tujhe Bhula Diya Ft.E...", ContentType.VIDEO, "3:10", "28 days", 6, 0)
    )

    val realtimeVideos = listOf(
        RealtimeVideo("80 HAZAR KE MERE ...", 42, listOf(0.1f, 0.3f, 0.8f, 0.4f, 0.2f, 0.5f, 0.9f, 0.2f)),
        RealtimeVideo("MC STAN - Mere Ha...", 3, listOf(0.0f, 0.0f, 0.2f, 0.0f, 0.5f, 0.0f)),
        RealtimeVideo("295 - Sidhu Moose...", 2, listOf(0.0f, 0.2f, 0.0f, 0.3f, 0.0f, 0.0f)),
        RealtimeVideo("Abhi ke Abhi Ke Jak...", 1, listOf(0.0f, 0.0f, 0.1f, 0.0f, 0.0f, 0.0f)),
        RealtimeVideo("Phir Hera Pheri X M...", 1, listOf(0.0f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f))
    )

    val drilldownTopContentViews = listOf(
        DrilldownContentItem("80 HAZAR KE MERE SHOES 👟...", "735", 0.90f, "80 HAZAR", true),
        DrilldownContentItem("Mohabbat Mohabbat Mohabbat Hui...", "10", 0.12f, "Mohabbat", true),
        DrilldownContentItem("Abhi ke Abhi Ke Jake Peheli Ticket...", "8", 0.10f, "Abhi ke Abhi", true),
        DrilldownContentItem("MC STAN - Mere Haath Mein X Vija...", "6", 0.08f, "MC STAN", false),
        DrilldownContentItem("Mc Stan - Tujhe Bhula Diya Ft.Emiw...", "6", 0.08f, "Tujhe Bhula", false),
        DrilldownContentItem("VTEN - MAHA SHIVARATRI MASHU...", "5", 0.06f, "VTEN", false),
        DrilldownContentItem("Bride's Entry Dance for her Groom ||...", "4", 0.05f, "Bride", true),
        DrilldownContentItem("Tablig ejtema mosali#mosali #man...", "3", 0.04f, "Tablig", true),
        DrilldownContentItem("Phir Hera Pheri X MC STAN | Drill R...", "3", 0.04f, "Phir Hera Pheri", false),
        DrilldownContentItem("Cute Shayari Status in Girl Voice Fr...", "2", 0.03f, "Shayari", true),
        DrilldownContentItem("lanta photo 🔥#divine #freefire...", "2", 0.03f, "lanta", true),
        DrilldownContentItem("MC Stan Remix: Soniye Heeriye X P...", "2", 0.03f, "Remix", false),
        DrilldownContentItem("295 - Sidhu Moose Wala | New so...", "2", 0.03f, "295", false),
        DrilldownContentItem("Mc Stan X Bewafa Ft.Imran khan X...", "1", 0.02f, "Bewafa", false),
        DrilldownContentItem("Jamal Jamaloo - Bobby Deol Entr...", "1", 0.02f, "Jamal", false)
    )

    val drilldownTopContentWatchTime = listOf(
        DrilldownContentItem("80 HAZAR KE MERE SHOES 👟...", "1.3", 0.72f, "80 HAZAR", true),
        DrilldownContentItem("Mc Stan - Tujhe Bhula Diya Ft.Emiw...", "0.1", 0.10f, "Tujhe Bhula", false)
    )

    val drilldownTopContentSubscribers = listOf(
        DrilldownContentItem("shayari ho jailfree fire funny whatsa...", "0", 0.0f, "shayari", true),
        DrilldownContentItem("free Fire funny shayari 😂 #ffshorts...", "0", 0.0f, "free Fire", true),
        DrilldownContentItem("Abhi ke Abhi Ke Jake Peheli Ticket...", "0", 0.0f, "Abhi ke Abhi", true),
        DrilldownContentItem("Mc stan X Bewafa Ft.Imran khan X...", "0", 0.0f, "Bewafa", false),
        DrilldownContentItem("Cute Shayari Status in Girl Voice Fr...", "0", 0.0f, "Shayari", true),
        DrilldownContentItem("Jamal Jamaloo - Bobby Deol Entr...", "0", 0.0f, "Jamal", false),
        DrilldownContentItem("EMIWAY BANTAI - \"MUJHKO YAAD...", "0", 0.0f, "EMIWAY", false),
        DrilldownContentItem("JANAM JANAM FT.Vijay Dk X Mc sta...", "0", 0.0f, "JANAM", false)
    )

    val drilldownTopContentRevenue = listOf(
        DrilldownContentItem("80 HAZAR KE MERE SHOES 👟...", "$980.50", 0.78f, "80 HAZAR", true),
        DrilldownContentItem("Mc Stan - Tujhe Bhula Diya Ft.Emiw...", "$145.20", 0.12f, "Tujhe Bhula", false),
        DrilldownContentItem("VTEN - MAHA SHIVARATRI MASHU...", "$78.40", 0.06f, "VTEN", false),
        DrilldownContentItem("MC STAN - Mere Haath Mein X Vija...", "$43.22", 0.04f, "MC STAN", false)
    )

    // Analytics: Content Tab (Videos filter)
    val contentVideosViewsMetric = MetricCardData(
        title = "Views",
        value = "34",
        subtitle = "16 fewer than usual",
        isPositive = false,
        showArrow = true,
        trendData = listOf(1f, 0.5f, 2f, 1f, 1f, 1f, 3f, 3f, 1f, 0f, 0f, 3f, 2f, 2f, 1f, 0f, 0f, 1f, 0f, 1f, 1f, 4f, 3f, 1f),
        yMinLabel = "0",
        yMidLabel = "2",
        yMaxLabel = "6"
    )

    val contentVideosImpressionsMetric = MetricCardData(
        title = "Thumbnail impressions",
        value = "231",
        subtitle = "27% more than previous 28 days",
        isPositive = true,
        showArrow = true,
        trendData = listOf(8f, 12f, 18f, 5f, 8f, 14f, 11f, 12f, 7f, 3f, 8f, 11f, 14f, 15f, 1f, 3f, 10f, 7f, 13f, 8f, 11f, 10f, 26f),
        yMinLabel = "0",
        yMidLabel = "9",
        yMaxLabel = "27"
    )

    val contentVideosCtrMetric = MetricCardData(
        title = "Thumbnail click-through rate",
        value = "6.5%",
        subtitle = "",
        isPositive = true,
        showArrow = false,
        trendData = listOf(0.1f, 0.15f, 0.25f, 0.12f, 0.15f, 0.3f, 0.2f, 0.22f, 0.12f, 0.14f, 0.28f, 0.45f, 0.12f, 0.15f, 0.5f, 0.15f, 0.16f, 0.35f, 0.28f),
        yMinLabel = "0.0%",
        yMidLabel = "20.0%",
        yMaxLabel = "60.0%"
    )

    val contentVideosAvgDurationMetric = MetricCardData(
        title = "Average view duration",
        value = "0:56",
        subtitle = "",
        isPositive = true,
        showArrow = false,
        trendData = listOf(0.1f, 0.15f, 0.1f, 0.1f, 0.2f, 0.18f, 0.65f, 0.1f, 0.1f, 0.12f, 0.45f, 0.2f, 0.1f, 0.1f, 0.1f, 0.15f, 0.1f, 0.5f, 0.15f, 0.3f, 0.4f, 0.35f),
        yMinLabel = "0:00",
        yMidLabel = "1:09",
        yMaxLabel = "3:27"
    )

    val trafficSources = listOf(
        TrafficSourceItem("YouTube search", 58.8f),
        TrafficSourceItem("Browse features", 20.6f),
        TrafficSourceItem("Other YouTube features", 8.8f)
    )

    // Analytics: Audience Tab
    val audienceMonthlyMetric = MetricCardData(
        title = "Monthly audience",
        value = "368",
        subtitle = "",
        isPositive = true,
        showArrow = false,
        trendData = listOf(170f, 210f, 260f, 310f, 350f, 380f, 410f, 440f, 460f, 470f, 490f, 500f, 510f, 500f, 505f, 490f, 440f, 380f, 368f),
        yMinLabel = "0",
        yMidLabel = "170",
        yMaxLabel = "510"
    )

    val deviceBreakdown = listOf(
        DeviceTypeItem("Mobile", 91.8f),
        DeviceTypeItem("TV", 5.0f),
        DeviceTypeItem("Computer", 1.6f),
        DeviceTypeItem("Tablet", 1.6f)
    )

    val geographyList = listOf(
        SubtitleItem("India", 88.4f)
    )

    val subtitlesList = listOf(
        SubtitleItem("No subtitles/CC", 97.7f),
        SubtitleItem("Hindi", 2.0f),
        SubtitleItem("English", 0.1f)
    )

    // Trends Tab
    val topSearches = listOf(
        ExploreTopic("bengali bangladeshi short film", 4),
        ExploreTopic("bengali reels", 3)
    )

    val trendRecentVideos = listOf(
        TrendVideo(
            title = "বিজয়া দশমী ❤️ | #bonggos #ytshorts #shortvideo #shortsfeed...",
            channelName = "Bong Gos",
            viewsText = "8.2 lakh views",
            timeAgo = "17 hours ago",
            duration = "1:20"
        ),
        TrendVideo(
            title = "💔 বাংলা স্যাড স্ট্যাটাস | Shayari | Heart Touching Sad Status",
            channelName = "MR TOHID 44",
            viewsText = "4.1 lakh views",
            timeAgo = "1 day ago",
            duration = "0:59"
        )
    )

    /**
     * Synchronizes live data from Supabase PostgREST API.
     * If offline, misconfigured, or tables do not exist yet,
     * falls back gracefully without modifying default values.
     */
    suspend fun syncWithSupabase() {
        if (isSyncing) return
        isSyncing = true
        lastSyncError = null

        try {
            // 1. Channel Profile
            val profile = SupabaseClient.fetchChannelProfile()
            if (profile != null) {
                danielProfile = profile
                currentProfile = profile
                isConnectedToSupabase = true
                Log.d(TAG, "Synced channel profile: ${profile.name}, subs: ${profile.subscribersCount}")
            }

            // 2. Dashboard 2x2 Metrics
            val metrics = SupabaseClient.fetchDashboardMetrics()
            if (metrics != null) {
                dashboardViews = metrics.views
                dashboardWatchTime = metrics.watchTime
                dashboardSubscribers = metrics.subscribers
                dashboardRevenue = metrics.revenue
                dashboardPeriodLabel = metrics.periodLabel
                isConnectedToSupabase = true
                Log.d(TAG, "Synced dashboard metrics: views=$dashboardViews, revenue=$dashboardRevenue")
            }

            // 3. Videos & Content
            val remoteVideos = SupabaseClient.fetchVideos()
            if (!remoteVideos.isNullOrEmpty()) {
                val videos = remoteVideos.filter { it.type == ContentType.VIDEO }
                val shorts = remoteVideos.filter { it.type == ContentType.SHORT }
                val lives = remoteVideos.filter { it.type == ContentType.LIVE }

                if (videos.isNotEmpty()) contentVideos = videos
                if (shorts.isNotEmpty()) contentShorts = shorts
                if (lives.isNotEmpty()) contentLiveStreams = lives

                // Update latest video if flagged
                val latest = remoteVideos.find { it.isExpanded } ?: remoteVideos.firstOrNull()
                if (latest != null) {
                    dashboardLatestVideo = latest
                }
                isConnectedToSupabase = true
                Log.d(TAG, "Synced ${remoteVideos.size} videos from Supabase")
            }

            // 4. Analytics Overview
            val remoteAnalytics = SupabaseClient.fetchAnalyticsOverview()
            if (!remoteAnalytics.isNullOrEmpty()) {
                remoteAnalytics.find { it.title.equals("Views", ignoreCase = true) }?.let {
                    overviewViewsMetric = it
                }
                remoteAnalytics.find { it.title.contains("Watch", ignoreCase = true) }?.let {
                    overviewWatchTimeMetric = it
                }
                remoteAnalytics.find { it.title.equals("Subscribers", ignoreCase = true) }?.let {
                    overviewSubscribersMetric = it
                }
                remoteAnalytics.find { it.title.contains("Revenue", ignoreCase = true) }?.let {
                    overviewEstimatedRevenueMetric = it
                }
                isConnectedToSupabase = true
            }

        } catch (e: Exception) {
            Log.e(TAG, "Supabase synchronization error", e)
            lastSyncError = e.message
        } finally {
            isSyncing = false
        }
    }
}
