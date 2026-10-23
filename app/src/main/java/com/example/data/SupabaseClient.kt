package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.example.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object SupabaseClient {
    private const val TAG = "SupabaseClient"
    const val SUPABASE_URL = "https://zzswtrpdjllmagjjmxbx.supabase.co"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    // Resolves publishable anon key from BuildConfig (.env) or runtime override
    var runtimeAnonKeyOverride: String = ""

    val anonKey: String
        get() {
            if (runtimeAnonKeyOverride.isNotBlank()) return runtimeAnonKeyOverride
            return try {
                BuildConfig.SUPABASE_ANON_KEY
            } catch (_: Exception) {
                ""
            }
        }

    suspend fun fetchChannelProfile(): ChannelProfile? = withContext(Dispatchers.IO) {
        val key = anonKey
        if (key.isBlank()) return@withContext null

        try {
            val url = "$SUPABASE_URL/rest/v1/channel_profile?select=*"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", key)
                .addHeader("Authorization", "Bearer $key")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w(TAG, "fetchChannelProfile failed with HTTP ${response.code}")
                return@withContext null
            }

            val body = response.body?.string() ?: return@withContext null
            val array = JSONArray(body)
            if (array.length() == 0) return@withContext null

            val obj = array.getJSONObject(0)
            return@withContext ChannelProfile(
                id = obj.optString("id", "channel_daniel"),
                name = obj.optString("name", "Daniel Dalen"),
                handle = obj.optString("handle", "@danieldalen"),
                subscribersCount = obj.optString("subscribers_count", "48,237"),
                email = obj.optString("email", "creator@gmail.com"),
                isMonetized = obj.optBoolean("is_monetized", true)
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching channel profile from Supabase", e)
            null
        }
    }

    suspend fun fetchDashboardMetrics(): DashboardMetricsResponse? = withContext(Dispatchers.IO) {
        val key = anonKey
        if (key.isBlank()) return@withContext null

        try {
            val url = "$SUPABASE_URL/rest/v1/dashboard_metrics?select=*"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", key)
                .addHeader("Authorization", "Bearer $key")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val body = response.body?.string() ?: return@withContext null
            val array = JSONArray(body)
            if (array.length() == 0) return@withContext null

            val obj = array.getJSONObject(0)
            return@withContext DashboardMetricsResponse(
                views = obj.optString("views", "887.7K"),
                watchTime = obj.optString("watch_time", "66.2K"),
                subscribers = obj.optString("subscribers", "+39.0K"),
                revenue = obj.optString("revenue", "$0"),
                viewsIsUp = obj.optBoolean("views_is_up", true),
                watchTimeIsUp = obj.optBoolean("watch_time_is_up", true),
                subscribersIsUp = obj.optBoolean("subscribers_is_up", true),
                revenueIsUp = obj.optBoolean("revenue_is_up", true),
                periodLabel = obj.optString("period_label", "Last 28 days")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching dashboard metrics from Supabase", e)
            null
        }
    }

    suspend fun fetchVideos(): List<VideoItem>? = withContext(Dispatchers.IO) {
        val key = anonKey
        if (key.isBlank()) return@withContext null

        try {
            val url = "$SUPABASE_URL/rest/v1/videos?select=*&order=display_order.asc"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", key)
                .addHeader("Authorization", "Bearer $key")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val body = response.body?.string() ?: return@withContext null
            val array = JSONArray(body)
            val result = mutableListOf<VideoItem>()

            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val typeStr = obj.optString("type", "VIDEO").uppercase()
                val contentType = when (typeStr) {
                    "SHORT" -> ContentType.SHORT
                    "LIVE" -> ContentType.LIVE
                    else -> ContentType.VIDEO
                }

                result.add(
                    VideoItem(
                        id = obj.optString("id", "vid_$i"),
                        title = obj.optString("title", "Untitled"),
                        type = contentType,
                        duration = obj.optString("duration", "0:00"),
                        publishedTimeAgo = obj.optString("published_time_ago", ""),
                        views = obj.optLong("views", 0),
                        likes = obj.optLong("likes", 0),
                        comments = obj.optLong("comments", 0),
                        rankingText = obj.optString("ranking_text", ""),
                        avgPercentageViewed = obj.optString("avg_percentage_viewed", ""),
                        realTimeViews = obj.optLong("real_time_views", 0),
                        thumbnailTitle = obj.optString("thumbnail_title", ""),
                        isExpanded = obj.optBoolean("is_expanded", false),
                        category = obj.optString("category", "Entertainment")
                    )
                )
            }
            return@withContext result
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching videos from Supabase", e)
            null
        }
    }

    suspend fun fetchAnalyticsOverview(): List<MetricCardData>? = withContext(Dispatchers.IO) {
        val key = anonKey
        if (key.isBlank()) return@withContext null

        try {
            val url = "$SUPABASE_URL/rest/v1/analytics_overview?select=*&order=display_order.asc"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", key)
                .addHeader("Authorization", "Bearer $key")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val body = response.body?.string() ?: return@withContext null
            val array = JSONArray(body)
            val result = mutableListOf<MetricCardData>()

            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val trendJson = obj.optJSONArray("trend_data")
                val trendList = mutableListOf<Float>()
                if (trendJson != null) {
                    for (j in 0 until trendJson.length()) {
                        trendList.add(trendJson.optDouble(j, 0.0).toFloat())
                    }
                }

                result.add(
                    MetricCardData(
                        title = obj.optString("title", "Metric"),
                        value = obj.optString("value", "0"),
                        subtitle = obj.optString("subtitle", ""),
                        isPositive = obj.optBoolean("is_positive", true),
                        showArrow = obj.optBoolean("show_arrow", true),
                        isCheckmark = obj.optBoolean("is_checkmark", false),
                        trendData = trendList,
                        yMinLabel = obj.optString("y_min_label", "0"),
                        yMidLabel = obj.optString("y_mid_label", ""),
                        yMaxLabel = obj.optString("y_max_label", ""),
                        startDateLabel = obj.optString("start_date_label", "24 Sept"),
                        endDateLabel = obj.optString("end_date_label", "21 Oct")
                    )
                )
            }
            return@withContext result
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching analytics from Supabase", e)
            null
        }
    }
}

data class DashboardMetricsResponse(
    val views: String,
    val watchTime: String,
    val subscribers: String,
    val revenue: String,
    val viewsIsUp: Boolean,
    val watchTimeIsUp: Boolean,
    val subscribersIsUp: Boolean,
    val revenueIsUp: Boolean,
    val periodLabel: String
)
