package com.example.kibun.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kibun.ui.viewmodel.KibunViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: KibunViewModel,
    onNavigateToDiary: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val entries by viewModel.allEntries.collectAsState()
    val plans by viewModel.allPlans.collectAsState()
    val themeOverride by viewModel.themeOverride.collectAsState()
    
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    
    val accentColor = Color(0xFFE28E8E) // 韓国風のくすみピンク
    
    val timeOfDay = when (themeOverride) {
        "MORNING" -> TimeOfDay.MORNING
        "AFTERNOON" -> TimeOfDay.AFTERNOON
        "NIGHT" -> TimeOfDay.NIGHT
        else -> {
            when {
                hour in 5..11 -> TimeOfDay.MORNING
                hour in 12..17 -> TimeOfDay.AFTERNOON
                else -> TimeOfDay.NIGHT
            }
        }
    }

    val themeColor = when (timeOfDay) {
        TimeOfDay.MORNING -> Color(0xFF4A4A4A)
        TimeOfDay.AFTERNOON -> Color(0xFF333333)
        TimeOfDay.NIGHT -> Color(0xFFF0F0F0)
    }

    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // 背景の描画
        val backgroundModifier = when (timeOfDay) {
            TimeOfDay.MORNING -> Modifier.background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color(0xFFFFF9F2), Color(0xFFFFEBD6))))
            TimeOfDay.AFTERNOON -> Modifier.background(Color(0xFFFAF9F6))
            TimeOfDay.NIGHT -> Modifier.background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color(0xFF1A1C1E), Color(0xFF2D2F31))))
        }
        
        Box(modifier = Modifier.fillMaxSize().then(backgroundModifier)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        when (timeOfDay) {
                            TimeOfDay.MORNING -> MorningHomeScreen(entries, plans, onNavigateToDiary, onNavigateToView = { scope.launch { pagerState.animateScrollToPage(1) } }, onNavigateToSettings = onNavigateToSettings)
                            TimeOfDay.AFTERNOON -> AfternoonHomeScreen(entries, plans, onNavigateToDiary, onNavigateToView = { scope.launch { pagerState.animateScrollToPage(1) } }, onNavigateToSettings = onNavigateToSettings)
                            TimeOfDay.NIGHT -> NightHomeScreen(entries, plans, onNavigateToDiary, onNavigateToView = { scope.launch { pagerState.animateScrollToPage(1) } }, onNavigateToSettings = onNavigateToSettings)
                        }
                    }
                    1 -> DiaryHistoryPage(
                        entries = entries,
                        plans = plans,
                        themeColor = themeColor,
                        accentColor = accentColor,
                        onAddPlan = { title, date ->
                            viewModel.insertPlan(
                                com.example.kibun.data.KibunPlan(
                                    title = title,
                                    date = date
                                )
                            )
                        },
                        onToggleFavorite = { viewModel.toggleFavorite(it) }
                    )
                    2 -> FavoritePage(
                        entries = entries,
                        themeColor = themeColor,
                        accentColor = accentColor,
                        onToggleFavorite = { viewModel.toggleFavorite(it) }
                    )
                }
            }
        }

        // 共通のボトムナビゲーション
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (timeOfDay == TimeOfDay.NIGHT) Color.White.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    BottomNavItem(
                        icon = Icons.Default.Home,
                        label = "ホーム",
                        selected = pagerState.currentPage == 0,
                        themeColor = themeColor,
                        accentColor = accentColor,
                        onClick = { scope.launch { pagerState.animateScrollToPage(0) } }
                    )
                    BottomNavItem(
                        icon = Icons.Default.List,
                        label = "見る",
                        selected = pagerState.currentPage == 1,
                        themeColor = themeColor,
                        accentColor = accentColor,
                        onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
                    )
                    BottomNavItem(
                        icon = Icons.Default.Favorite,
                        label = "お気に入り",
                        selected = pagerState.currentPage == 2,
                        themeColor = themeColor,
                        accentColor = accentColor,
                        onClick = { scope.launch { pagerState.animateScrollToPage(2) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    themeColor: Color,
    accentColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) accentColor else themeColor.copy(alpha = 0.3f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (selected) accentColor else themeColor.copy(alpha = 0.3f)
        )
    }
}

enum class TimeOfDay {
    MORNING, AFTERNOON, NIGHT
}
