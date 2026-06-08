package com.example.kibun.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.kibun.data.KibunEntry
import com.example.kibun.data.KibunPlan
import com.example.kibun.ui.components.SmallCalendar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MorningHomeScreen(
    entries: List<KibunEntry>,
    plans: List<KibunPlan>,
    onNavigateToDiary: () -> Unit,
    onNavigateToView: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val accentColor = Color(0xFFE28E8E) // 韓国風のくすみピンク
    val themeColor = Color(0xFF4A4A4A) // 柔らかいチャコール

    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }
    
    LaunchedEffect(Unit) {
        while(true) {
            currentTime = Calendar.getInstance()
            kotlinx.coroutines.delay(1000)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF9F2), // 非常に淡いクリーム
                        Color(0xFFFFEBD6)  // 暖かいペールピーチ
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            
            MorningHeader(currentTime, themeColor, onNavigateToSettings)
            
            Spacer(modifier = Modifier.height(40.dp))
            
            TodayDateCard(themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            QuickActionsGrid(onNavigateToDiary, onNavigateToView, themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "カレンダー",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = themeColor.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SmallCalendar(
                entries = entries, 
                plans = plans, 
                themeColor = themeColor, 
                accentColor = accentColor,
                modifier = Modifier.clip(RoundedCornerShape(28.dp))
            )

            Spacer(modifier = Modifier.height(100.dp))
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
private fun MorningHeader(currentTime: Calendar, themeColor: Color, onNavigateToSettings: () -> Unit) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.JAPAN)
    val timeStr = timeFormat.format(currentTime.time)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = timeStr,
            fontSize = 72.sp,
            fontWeight = FontWeight.ExtraBold,
            color = themeColor,
            letterSpacing = (-3).sp,
            modifier = Modifier.offset(y = 12.dp)
        )
        
        IconButton(
            onClick = onNavigateToSettings,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .background(Color.White.copy(alpha = 0.4f), CircleShape)
        ) {
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
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "3月26日",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColor
                )
                Text(
                    text = "木曜日",
                    fontSize = 14.sp,
                    color = themeColor.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.15f)
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Sunny",
                    modifier = Modifier.padding(10.dp),
                    tint = accentColor
                )
            }
        }
    }
}

@Composable
private fun QuickActionsGrid(onNavigateToDiary: () -> Unit, onNavigateToView: () -> Unit, themeColor: Color, accentColor: Color) {
    Column {
        Text(
            text = "クイックアクション",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = themeColor.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionCard(
                icon = Icons.Default.Edit,
                label = "日記を書く",
                color = accentColor.copy(alpha = 0.12f),
                themeColor = themeColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToDiary
            )
            QuickActionCard(
                icon = Icons.Default.Add,
                label = "予定を入れる",
                color = Color.White.copy(alpha = 0.6f),
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
            .height(110.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(32.dp),
                tint = themeColor.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = themeColor.copy(alpha = 0.7f)
            )
        }
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
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
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
