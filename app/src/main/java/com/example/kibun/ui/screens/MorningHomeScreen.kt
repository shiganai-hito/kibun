package com.example.kibun.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.rememberCoroutineScope
import com.example.kibun.data.KibunEntry
import kotlinx.coroutines.launch

@Composable
fun MorningHomeScreen(
    entries: List<KibunEntry>,
    onNavigateToDiary: () -> Unit,
    onNavigateToView: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val accentColor = Color(0xFFA8BDC9) // 朝のくすんだ水色（共通の差し色）
    val themeColor = Color(0xFF546E7A) // テキスト用：深いブルーグレー

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F0F2), // 淡い白に近い水色
                        Color(0xFFCFDEE7)  // 少し濃いくすみ水色
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(56.dp))
            
            MorningHeader(themeColor)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            TodayDateCard(themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            QuickActionsGrid(onNavigateToDiary, onNavigateToView, themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TodayPlanSection(themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            SmallCalendar(themeColor = themeColor, entries = entries)
            
            Spacer(modifier = Modifier.height(80.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            BottomNavigation(
                selectedItem = "home",
                themeColor = themeColor,
                accentColor = accentColor,
                onViewClick = {
                    scope.launch {
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            )
        }
    }
}

@Composable
private fun MorningHeader(themeColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "おはようございます",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = themeColor
            )
            Text(
                text = "今日も良い一日を！",
                fontSize = 15.sp,
                color = themeColor.copy(alpha = 0.6f)
            )
        }
        
        IconButton(onClick = { /* Settings */ }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = themeColor.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun TodayDateCard(themeColor: Color, accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "3月26日",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColor
                )
                Text(
                    text = "木曜日",
                    fontSize = 13.sp,
                    color = themeColor.copy(alpha = 0.5f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = "Sunny",
                modifier = Modifier.size(40.dp),
                tint = accentColor
            )
        }
    }
}

@Composable
private fun QuickActionsGrid(onNavigateToDiary: () -> Unit, onNavigateToView: () -> Unit, themeColor: Color, accentColor: Color) {
    Column {
        Text(
            text = "クイックアクション",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = themeColor.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                icon = Icons.Default.Edit,
                label = "日記を書く",
                color = accentColor.copy(alpha = 0.3f),
                themeColor = themeColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToDiary
            )
            QuickActionCard(
                icon = Icons.Default.Add,
                label = "予定を入れる",
                color = Color.White.copy(alpha = 0.5f),
                themeColor = themeColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToView
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    themeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = themeColor.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = themeColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun TodayPlanSection(themeColor: Color, accentColor: Color) {
    Column {
        Text(
            text = "今日の予定",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = themeColor.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.4f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                PlanItem(time = "09:00", title = "朝のミーティング", themeColor, accentColor)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = themeColor.copy(alpha = 0.05f))
                PlanItem(time = "14:00", title = "プロジェクトレビュー", themeColor, accentColor)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = themeColor.copy(alpha = 0.05f))
                PlanItem(time = "18:00", title = "夕食の準備", themeColor, accentColor)
            }
        }
    }
}

@Composable
private fun PlanItem(time: String, title: String, themeColor: Color, accentColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(accentColor)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = time,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = themeColor.copy(alpha = 0.5f),
            modifier = Modifier.width(50.dp)
        )
        Text(
            text = title,
            fontSize = 13.sp,
            color = themeColor.copy(alpha = 0.9f)
        )
    }
}

@Composable
private fun BottomNavigation(
    selectedItem: String,
    themeColor: Color,
    accentColor: Color,
    onViewClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(Icons.Default.Home, "ホーム", selectedItem == "home", themeColor, accentColor) { }
            BottomNavItem(Icons.Default.List, "見る", selectedItem == "view", themeColor, accentColor, onViewClick)
            BottomNavItem(Icons.Default.Favorite, "お気に入り", selectedItem == "favorite", themeColor, accentColor) { }
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
