package com.example.kibun.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kibun.data.KibunEntry
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FavoritePage(
    entries: List<KibunEntry>,
    themeColor: Color,
    accentColor: Color,
    onToggleFavorite: (KibunEntry) -> Unit
) {
    val favoriteEntries = entries.filter { it.isFavorite }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "お気に入り",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = themeColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (favoriteEntries.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "お気に入りの日記はまだありません",
                        fontSize = 14.sp,
                        color = themeColor.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    items(favoriteEntries) { entry ->
                        FavoriteDiaryItem(entry, themeColor, accentColor, onToggleFavorite)
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteDiaryItem(
    entry: KibunEntry,
    themeColor: Color,
    accentColor: Color,
    onToggleFavorite: (KibunEntry) -> Unit
) {
    val dateStr = SimpleDateFormat("M/d", Locale.JAPAN).format(Date(entry.date))

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
            // 気分の絵文字
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.title.ifEmpty { "無題" },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = dateStr,
                        fontSize = 12.sp,
                        color = themeColor.copy(alpha = 0.4f),
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Medium
                    )
                }
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

            // 画像があればサムネイルを表示
            if (entry.imageUri != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Card(
                    modifier = Modifier.size(56.dp),
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

            // お気に入り解除ボタン
            IconButton(onClick = { onToggleFavorite(entry) }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove Favorite",
                    tint = Color(0xFFE28E8E)
                )
            }
        }
    }
}
