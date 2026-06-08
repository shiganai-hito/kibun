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
fun AfternoonHomeScreen(
    entries: List<KibunEntry>,
    plans: List<KibunPlan>,
    onNavigateToDiary: () -> Unit,
    onNavigateToView: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val accentColor = Color(0xFF8E9775) // 韓国風のセージグリーン
    val themeColor = Color(0xFF333333) // 深いチャコール

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
            .background(Color(0xFFFAF9F6)) // アラバスター（非常に淡いベージュ白）
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            
            AfternoonHeader(currentTime, themeColor, onNavigateToSettings)
            
            Spacer(modifier = Modifier.height(40.dp))
            
            AfternoonDateCard(themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            AfternoonQuickActions(onNavigateToDiary, onNavigateToView, themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "カレンダー",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = themeColor.copy(alpha = 0.7f),
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
            AfternoonBottomNav(themeColor, accentColor) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }
    }
}

@Composable
private fun AfternoonHeader(currentTime: Calendar, themeColor: Color, onNavigateToSettings: () -> Unit) {
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
                .background(Color.White, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = themeColor.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun AfternoonDateCard(themeColor: Color, accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    text = "木曜日 午後",
                    fontSize = 14.sp,
                    color = themeColor.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.1f)
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Sunny",
                    modifier = Modifier.padding(10.dp),
                    tint = accentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun AfternoonQuickActions(onNavigateToDiary: () -> Unit, onNavigateToView: () -> Unit, themeColor: Color, accentColor: Color) {
    Column {
        Text(
            text = "クイックアクション",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = themeColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AfternoonActionCard(
                icon = Icons.Default.Edit,
                label = "日記を書く",
                color = accentColor.copy(alpha = 0.1f),
                themeColor = themeColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToDiary
            )
            AfternoonActionCard(
                icon = Icons.Default.Add,
                label = "予定を入れる",
                color = Color.White,
                themeColor = themeColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToView
            )
        }
    }
}

@Composable
private fun AfternoonActionCard(
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
private fun AfternoonBottomNav(themeColor: Color, accentColor: Color, onViewClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AfternoonNavItem(Icons.Default.Home, "ホーム", true, themeColor, accentColor) { }
            AfternoonNavItem(Icons.Default.List, "見る", false, themeColor, accentColor, onViewClick)
            AfternoonNavItem(Icons.Default.Favorite, "お気に入り", false, themeColor, accentColor) { }
        }
    }
}

@Composable
private fun AfternoonNavItem(
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
            tint = if (selected) accentColor else themeColor.copy(alpha = 0.2f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (selected) accentColor else themeColor.copy(alpha = 0.2f)
        )
    }
}
