package com.example.kibun.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kibun.data.KibunEntry
import java.util.*

@Composable
fun SmallCalendar(
    themeColor: Color,
    entries: List<KibunEntry>,
    selectedDay: Int? = null,
    onDaySelected: (Int) -> Unit = {}
) {
    val accentColor = Color(0xFFA8BDC9) // 共通の差し色：くすんだ水色
    
    val calendar = Calendar.getInstance()
    val monthName = "${calendar.get(Calendar.MONTH) + 1}月"
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    
    val entryDays = remember(entries) {
        entries.filter { entry ->
            val entryCal = Calendar.getInstance().apply { timeInMillis = entry.date }
            entryCal.get(Calendar.MONTH) == currentMonth && 
            entryCal.get(Calendar.YEAR) == currentYear
        }.map { entry ->
            val entryCal = Calendar.getInstance().apply { timeInMillis = entry.date }
            entryCal.get(Calendar.DAY_OF_MONTH)
        }.toSet()
    }
    
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = (calendar.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }.get(Calendar.DAY_OF_WEEK)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = monthName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = themeColor
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                listOf("日", "月", "火", "水", "木", "金", "土").forEach { day ->
                    Text(
                        text = day,
                        fontSize = 10.sp,
                        color = themeColor.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            var dateToPrint = 1
            for (i in 0..5) {
                if (dateToPrint > daysInMonth) break
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (j in 1..7) {
                        if ((i == 0 && j < firstDayOfWeek) || dateToPrint > daysInMonth) {
                            Box(modifier = Modifier.size(28.dp))
                        } else {
                            val isToday = dateToPrint == currentDay
                            val isSelected = dateToPrint == selectedDay
                            val hasEntry = entryDays.contains(dateToPrint)
                            
                            val dayNumber = dateToPrint

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) accentColor.copy(alpha = 0.2f)
                                        else Color.Transparent
                                    )
                                    .clickable { onDaySelected(dayNumber) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = dateToPrint.toString(),
                                        fontSize = 12.sp,
                                        fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isSelected -> accentColor
                                            isToday -> themeColor
                                            else -> themeColor.copy(alpha = 0.8f)
                                        }
                                    )
                                    if (hasEntry) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 1.dp)
                                                .size(3.dp)
                                                .clip(CircleShape)
                                                .background(accentColor)
                                        )
                                    }
                                }
                            }
                            dateToPrint++
                        }
                    }
                }
            }
        }
    }
}
