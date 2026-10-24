package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoItem
import com.example.ui.components.StudioBottomNav
import com.example.ui.components.StudioHeader
import com.example.ui.components.StudioTab
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.data.SupabaseClient.init(this)
        com.example.data.StudioRepository.init(this)
        setContent {
            MyApplicationTheme {
                StudioApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioApp() {
    var currentTab by remember { mutableStateOf(StudioTab.DASHBOARD) }
    var isAccountScreenOpen by remember { mutableStateOf(false) }
    var isAdminPanelOpen by remember { mutableStateOf(false) }
    var isAdminLoggedIn by remember { mutableStateOf(com.example.data.local.LocalAdminStore.isLoggedIn()) }
    var selectedVideoForDetail by remember { mutableStateOf<VideoItem?>(null) }
    var showCreateSheet by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }

    val colors = StudioTheme.colors

    // Sync live data from Supabase and listen for Realtime WebSocket updates
    LaunchedEffect(Unit) {
        com.example.data.StudioRepository.syncWithSupabase()
        com.example.data.StudioRepository.startRealtimeListener()
    }

    // Hardware back navigation handler
    BackHandler(enabled = isAdminPanelOpen || isAccountScreenOpen || selectedVideoForDetail != null || currentTab != StudioTab.DASHBOARD) {
        when {
            isAdminPanelOpen -> isAdminPanelOpen = false
            selectedVideoForDetail != null -> selectedVideoForDetail = null
            isAccountScreenOpen -> isAccountScreenOpen = false
            currentTab != StudioTab.DASHBOARD -> currentTab = StudioTab.DASHBOARD
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("studio_root_scaffold"),
        containerColor = colors.background,
        topBar = {
            if (!isAccountScreenOpen && !isAdminPanelOpen) {
                StudioHeader(
                    onAvatarClick = { isAccountScreenOpen = true },
                    onCreateClick = { showCreateSheet = true },
                    onNotificationClick = { showNotificationsDialog = true }
                )
            }
        },
        bottomBar = {
            if (!isAccountScreenOpen && !isAdminPanelOpen) {
                StudioBottomNav(
                    selectedTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isAdminPanelOpen) {
                if (!isAdminLoggedIn) {
                    com.example.ui.screens.admin.LocalAdminLoginScreen(
                        onLoginSuccess = { isAdminLoggedIn = true },
                        onClose = { isAdminPanelOpen = false }
                    )
                } else {
                    com.example.ui.screens.admin.LocalAdminDashboardScreen(
                        onClose = { isAdminPanelOpen = false },
                        onLogout = {
                            com.example.data.local.LocalAdminStore.logout()
                            isAdminLoggedIn = false
                            isAdminPanelOpen = false
                        }
                    )
                }
            } else if (isAccountScreenOpen) {
                AccountScreen(
                    onBackClick = { isAccountScreenOpen = false },
                    onChannelSelected = { }
                )
            } else {
                when (currentTab) {
                    StudioTab.DASHBOARD -> DashboardScreen(
                        onVideoClick = { selectedVideoForDetail = it },
                        onAnalyticsClick = { currentTab = StudioTab.ANALYTICS }
                    )
                    StudioTab.CONTENT -> ContentScreen(
                        onVideoClick = { selectedVideoForDetail = it }
                    )
                    StudioTab.ANALYTICS -> AnalyticsScreen(
                        onVideoClick = { selectedVideoForDetail = it }
                    )
                    StudioTab.COMMUNITY -> CommunityScreen()
                    StudioTab.EARN -> EarnScreen(
                        onOpenAdminPanel = { isAdminPanelOpen = true }
                    )
                }
            }
        }
    }

    // Video detail bottom sheet
    selectedVideoForDetail?.let { video ->
        VideoDetailModal(
            video = video,
            onDismiss = { selectedVideoForDetail = null }
        )
    }

    // Create Modal Bottom Sheet
    if (showCreateSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCreateSheet = false },
            containerColor = colors.cardBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Create",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = null, tint = colors.textPrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Upload a video", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = colors.textPrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Create a Short", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Upload, contentDescription = null, tint = colors.textPrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Go live", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
                }
            }
        }
    }

    // Notifications Dialog
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            containerColor = colors.cardBackground,
            title = { Text("Notifications", fontWeight = FontWeight.Bold, color = colors.textPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Your Short reached 749 views!", color = colors.textPrimary)
                    Text("• @gautamm24 commented on your video.", color = colors.textPrimary)
                    Text("• Check your monthly performance summary.", color = colors.textPrimary)
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Dismiss", color = colors.accentRed, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
