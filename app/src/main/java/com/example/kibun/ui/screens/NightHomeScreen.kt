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
fun NightHomeScreen(
    entries: List<KibunEntry>,
    onNavigateToDiary: () -> Unit,
    onNavigateToView: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val accentColor = Color(0xFFA8BDC9) // 共通の差し色：くすんだ水色
    val themeColor = Color(0xFFECEFF1) // テキスト用：ほぼ白に近いグレー

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF102027), // 非常に濃いネイビー
                        Color(0xFF263238)  // 少し明るいネイビー
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
            
            NightHeader(themeColor)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            NightDateCard(themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            NightQuickActions(onNavigateToDiary, onNavigateToView, themeColor, accentColor)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            EveningReflectionSection(themeColor, accentColor)

            Spacer(modifier = Modifier.height(80.dp))
            }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp, start = 24.dp, end = 24.dp)
        ) {
            NightBottomNav(themeColor, accentColor) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }
    }
}

@Composable
private fun NightHeader(themeColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "こんばんは",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = themeColor
            )
            Text(
                text = "今日も1日お疲れ様でした",
                fontSize = 15.sp,
                color = themeColor.copy(alpha = 0.5f)
            )
        }
        
        IconButton(onClick = { /* Settings */ }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = themeColor.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun NightDateCard(themeColor: Color, accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
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
                    text = "木曜日の夜",
                    fontSize = 13.sp,
                    color = themeColor.copy(alpha = 0.4f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.NightsStay,
                contentDescription = "Night",
                modifier = Modifier.size(40.dp),
                tint = accentColor.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun NightQuickActions(onNavigateToDiary: () -> Unit, onNavigateToView: () -> Unit, themeColor: Color, accentColor: Color) {
    Column {
        Text(
            text = "クイックアクション",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = themeColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NightActionCard(
                icon = Icons.Default.Edit,
                label = "日記を書く",
                color = accentColor.copy(alpha = 0.2f),
                themeColor = themeColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToDiary
            )
            NightActionCard(
                icon = Icons.Default.Add,
                label = "予定を入れる",
                color = Color.White.copy(alpha = 0.05f),
                themeColor = themeColor,
                modifier = Modifier.weight(1f),
                onClick = onNavigateToView
            )
        }
    }
}

@Composable
private fun NightActionCard(
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
                tint = themeColor.copy(alpha = 0.8f)
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
private fun EveningReflectionSection(themeColor: Color, accentColor: Color) {
    Column {
        Text(
            text = "今日の振り返り",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = themeColor.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.03f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReflectionItem(Icons.Default.CheckCircle, "完了したタスク", "5件", themeColor, accentColor)
                HorizontalDivider(color = themeColor.copy(alpha = 0.05f))
                ReflectionItem(Icons.Default.Star, "良かったこと", "3件", themeColor, accentColor)
                HorizontalDivider(color = themeColor.copy(alpha = 0.05f))
                ReflectionItem(Icons.Default.Lightbulb, "明日への目標", "2件", themeColor, accentColor)
            }
        }
    }
}

@Composable
private fun ReflectionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    count: String,
    themeColor: Color,
    accentColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = accentColor.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = themeColor
            )
        }
        Text(
            text = count,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = themeColor.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun NightBottomNav(themeColor: Color, accentColor: Color, onViewClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NightNavItem(Icons.Default.Home, "ホーム", true, themeColor, accentColor) { }
            NightNavItem(Icons.Default.List, "見る", false, themeColor, accentColor, onViewClick)
            NightNavItem(Icons.Default.Favorite, "お気に入り", false, themeColor, accentColor) { }
        }
    }
}

@Composable
private fun NightNavItem(
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
