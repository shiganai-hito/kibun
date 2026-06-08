package com.example.kibun.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kibun.data.KibunEntry
import com.example.kibun.data.KibunPlan
import com.example.kibun.ui.components.SmallCalendar
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DiaryHistoryPage(
    entries: List<KibunEntry>,
    plans: List<KibunPlan>,
    themeColor: Color,
    accentColor: Color,
    onAddPlan: (String, Long) -> Unit,
    onToggleFavorite: (KibunEntry) -> Unit
) {
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }
    val listState = rememberLazyListState()

    LaunchedEffect(selectedDate) {
        if (selectedDate != null) {
            // カレンダーの下（予定リストのヘッダー）までスクロール
            listState.animateScrollToItem(3)
        }
    }

    val filteredEntries = if (selectedDate == null) {
        entries
    } else {
        entries.filter { entry ->
            val cal = Calendar.getInstance().apply { timeInMillis = entry.date }
            cal.get(Calendar.YEAR) == selectedDate!!.get(Calendar.YEAR) &&
                    cal.get(Calendar.MONTH) == selectedDate!!.get(Calendar.MONTH) &&
                    cal.get(Calendar.DAY_OF_MONTH) == selectedDate!!.get(Calendar.DAY_OF_MONTH)
        }
    }

    val filteredPlans = if (selectedDate == null) {
        plans
    } else {
        plans.filter { plan ->
            val cal = Calendar.getInstance().apply { timeInMillis = plan.date }
            cal.get(Calendar.YEAR) == selectedDate!!.get(Calendar.YEAR) &&
                    cal.get(Calendar.MONTH) == selectedDate!!.get(Calendar.MONTH) &&
                    cal.get(Calendar.DAY_OF_MONTH) == selectedDate!!.get(Calendar.DAY_OF_MONTH)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 48.dp, bottom = 120.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "記録を見る",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColor
                    )
                    
                    if (selectedDate != null) {
                        TextButton(onClick = { selectedDate = null }) {
                            Text("選択を解除", color = accentColor, fontSize = 14.sp)
                        }
                    }
                }
            }

            // カレンダーを表示
            item {
                SmallCalendar(
                    entries = entries,
                    plans = plans,
                    themeColor = themeColor,
                    accentColor = accentColor,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    modifier = Modifier.clip(RoundedCornerShape(28.dp))
                )
            }
            
            // 予定追加バー
            item {
                PlanInputBar(
                    themeColor = themeColor, 
                    accentColor = accentColor, 
                    onAddPlan = { title ->
                        val date = selectedDate?.timeInMillis ?: System.currentTimeMillis()
                        onAddPlan(title, date)
                    }
                )
            }

            // 予定のリスト
            item {
                SectionHeader(
                    if (selectedDate == null) "予定のリスト" 
                    else "${selectedDate!!.get(Calendar.MONTH) + 1}/${selectedDate!!.get(Calendar.DAY_OF_MONTH)} の予定", 
                    themeColor
                )
            }
            
            if (filteredPlans.isEmpty()) {
                item {
                    PlanMockItem(themeColor, accentColor)
                }
            } else {
                items(filteredPlans) { plan ->
                    ActualPlanItem(plan, themeColor, accentColor)
                }
            }

            // 日記の履歴
            item {
                SectionHeader(
                    if (selectedDate == null) "日記の履歴" 
                    else "${selectedDate!!.get(Calendar.MONTH) + 1}/${selectedDate!!.get(Calendar.DAY_OF_MONTH)} の日記", 
                    themeColor
                )
            }
            
            if (filteredEntries.isEmpty()) {
                item {
                    Text(
                        text = if (selectedDate == null) "日記はまだありません" else "この日の日記はありません",
                        fontSize = 14.sp,
                        color = themeColor.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(filteredEntries) { entry ->
                    DiaryHistoryItem(entry, themeColor, accentColor, onToggleFavorite)
                }
            }
        }
    }
}

@Composable
private fun PlanInputBar(
    themeColor: Color,
    accentColor: Color,
    onAddPlan: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("新しい予定を追加...", fontSize = 14.sp, color = themeColor.copy(alpha = 0.4f)) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = themeColor,
                    unfocusedTextColor = themeColor
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (text.isNotBlank()) {
                        onAddPlan(text)
                        text = ""
                    }
                })
            )
            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onAddPlan(text)
                        text = ""
                    }
                },
                modifier = Modifier.background(accentColor.copy(alpha = 0.15f), CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "追加", tint = accentColor)
            }
        }
    }
}

@Composable
private fun ActualPlanItem(
    plan: KibunPlan,
    themeColor: Color,
    accentColor: Color
) {
    val dateStr = SimpleDateFormat("M/d", Locale.JAPAN).format(Date(plan.date))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.6f))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plan.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = themeColor
                )
            }
            Text(
                text = dateStr,
                fontSize = 12.sp,
                color = themeColor.copy(alpha = 0.4f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, themeColor: Color) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = themeColor.copy(alpha = 0.6f),
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun PlanMockItem(themeColor: Color, accentColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "予定はまだありません",
                fontSize = 14.sp,
                color = themeColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun DiaryHistoryItem(
    entry: KibunEntry,
    themeColor: Color,
    accentColor: Color,
    onToggleFavorite: (KibunEntry) -> Unit
) {
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
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 気分の絵文字（左端）
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = entry.mood.ifEmpty { "😊" },
                        fontSize = 24.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                if (entry.category.isNotEmpty()) {
                    Surface(
                        color = accentColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = entry.category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = entry.title.ifEmpty { "無題" },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = entry.content,
                    fontSize = 13.sp,
                    color = themeColor.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            // 画像があれば右側にサムネイルを表示
            if (entry.imageUri != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Card(
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    AsyncImage(
                        model = Uri.parse(entry.imageUri),
                        contentDescription = "Thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }
            }

            // お気に入りボタン
            IconButton(onClick = { onToggleFavorite(entry) }) {
                Icon(
                    imageVector = if (entry.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (entry.isFavorite) Color(0xFFE28E8E) else themeColor.copy(alpha = 0.2f)
                )
            }
        }
    }
}
