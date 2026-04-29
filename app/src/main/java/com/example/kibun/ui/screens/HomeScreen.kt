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
    onNavigateToDiary: () -> Unit
) {
    val entries by viewModel.allEntries.collectAsState()
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    
    val accentColor = Color(0xFFA8BDC9)
    val (themeColor, timeOfDay) = when {
        hour in 5..11 -> Color(0xFF546E7A) to TimeOfDay.MORNING
        hour in 12..17 -> Color(0xFF455A64) to TimeOfDay.AFTERNOON
        else -> Color(0xFFECEFF1) to TimeOfDay.NIGHT
    }

    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // 背景の描画
        val backgroundModifier = when (timeOfDay) {
            TimeOfDay.MORNING -> Modifier.background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color(0xFFE8F0F2), Color(0xFFCFDEE7))))
            TimeOfDay.AFTERNOON -> Modifier.background(Color(0xFFFCFCFC))
            TimeOfDay.NIGHT -> Modifier.background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color(0xFF102027), Color(0xFF263238))))
        }
        
        Box(modifier = Modifier.fillMaxSize().then(backgroundModifier)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        when (timeOfDay) {
                            TimeOfDay.MORNING -> MorningHomeScreen(entries, onNavigateToDiary, onNavigateToView = { scope.launch { pagerState.animateScrollToPage(1) } })
                            TimeOfDay.AFTERNOON -> AfternoonHomeScreen(entries, onNavigateToDiary, onNavigateToView = { scope.launch { pagerState.animateScrollToPage(1) } })
                            TimeOfDay.NIGHT -> NightHomeScreen(entries, onNavigateToDiary, onNavigateToView = { scope.launch { pagerState.animateScrollToPage(1) } })
                        }
                    }
                    1 -> DiaryHistoryPage(entries, themeColor, accentColor)
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
                        selected = false,
                        themeColor = themeColor,
                        accentColor = accentColor,
                        onClick = { }
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
