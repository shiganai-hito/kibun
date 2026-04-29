package com.example.kibun.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DiaryHistoryPage(
    entries: List<KibunEntry>,
    themeColor: Color,
    accentColor: Color
) {
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedDay by remember { mutableStateOf<Int?>(today) }
    var planText by remember { mutableStateOf("") }

    // 選択された日が過去かどうかを判定
    val isPastDate = selectedDay?.let { it < today } ?: false

    // 選択された日の日記をフィルタリング
    val selectedDateEntries = remember(selectedDay, entries) {
        if (selectedDay == null) emptyList()
        else {
            entries.filter { entry ->
                val entryCal = Calendar.getInstance().apply { timeInMillis = entry.date }
                entryCal.get(Calendar.DAY_OF_MONTH) == selectedDay &&
                entryCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                entryCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "記録を見る",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColor
                )
            }

            // カレンダー
            SmallCalendar(
                themeColor = themeColor, 
                entries = entries,
                selectedDay = selectedDay,
                onDaySelected = { selectedDay = it }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 選択された日付のタイトル
            selectedDay?.let { day ->
                Text(
                    text = "${calendar.get(Calendar.MONTH) + 1}月${day}日の記録",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColor.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // リスト表示（予定と日記）
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    // 予定セクション（現状はモック）
                    item {
                        SectionHeader("今日の予定", themeColor)
                    }
                    item {
                        PlanMockItem(themeColor, accentColor)
                    }

                    // 日記セクション
                    item {
                        SectionHeader("日記", themeColor)
                    }
                    
                    if (selectedDateEntries.isEmpty()) {
                        item {
                            Text(
                                text = "この日の日記はありません",
                                fontSize = 14.sp,
                                color = themeColor.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    } else {
                        items(selectedDateEntries) { entry ->
                            DiaryHistoryItem(entry, themeColor, accentColor)
                        }
                    }
                }
            }
        }

        // 入力欄（下部に固定。今日または未来の日付のみ表示）
        AnimatedVisibility(
            visible = selectedDay != null && !isPastDate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp, start = 24.dp, end = 24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = planText,
                        onValueChange = { planText = it },
                        placeholder = { Text("予定やメモを追加...", fontSize = 14.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (planText.isNotBlank()) {
                                    planText = ""
                                }
                            }
                        )
                    )
                    IconButton(
                        onClick = {
                            planText = ""
                        },
                        enabled = planText.isNotBlank()
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "保存", tint = accentColor)
                    }
                }
            }
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
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 気分の絵文字（左端）
            Text(
                text = entry.mood.ifEmpty { "😊" },
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.title.ifEmpty { "無題" },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = entry.content,
                    fontSize = 13.sp,
                    color = themeColor.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 画像があれば右側にサムネイルを表示
            if (entry.imageUri != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Card(
                    modifier = Modifier
                        .size(64.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    AsyncImage(
                        model = Uri.parse(entry.imageUri),
                        contentDescription = "Thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }
            }
        }
    }
}
