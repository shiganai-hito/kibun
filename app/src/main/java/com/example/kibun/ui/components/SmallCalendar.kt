package com.example.kibun.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kibun.data.KibunEntry
import com.example.kibun.data.KibunPlan
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SmallCalendar(
    entries: List<KibunEntry>,
    plans: List<KibunPlan>,
    themeColor: Color,
    accentColor: Color,
    selectedDate: Calendar? = null,
    onDateSelected: (Calendar) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var calendar by remember { mutableStateOf(Calendar.getInstance()) }
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    // 当月の最初の日
    val firstDayOfMonth = (calendar.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) // 1: Sunday, 2: Monday...
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val monthName = SimpleDateFormat("yyyy年 M月", Locale.JAPAN).format(calendar.time)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        calendar = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                    },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Prev", tint = themeColor.copy(alpha = 0.6f))
                }
                
                Text(
                    text = monthName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = themeColor,
                    letterSpacing = (-0.3).sp
                )
                
                IconButton(
                    onClick = {
                        calendar = (calendar.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
                    },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = themeColor.copy(alpha = 0.6f))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Days of week
            Row(modifier = Modifier.fillMaxWidth()) {
                val daysOfWeek = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColor.copy(alpha = 0.3f),
                        letterSpacing = 0.5.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Calendar Grid
            val totalCells = 42 // 6 rows * 7 days
            for (row in 0 until 6) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayOfMonth = cellIndex - (firstDayOfWeek - 1) + 1
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .clickable {
                                    if (dayOfMonth in 1..daysInMonth) {
                                        val selected = (calendar.clone() as Calendar).apply {
                                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                        }
                                        onDateSelected(selected)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (dayOfMonth in 1..daysInMonth) {
                                val isToday = isToday(currentYear, currentMonth, dayOfMonth)
                                val isSelected = selectedDate?.let {
                                    it.get(Calendar.YEAR) == currentYear &&
                                    it.get(Calendar.MONTH) == currentMonth &&
                                    it.get(Calendar.DAY_OF_MONTH) == dayOfMonth
                                } ?: false
                                
                                val hasEntry = hasEntryOnDate(entries, currentYear, currentMonth, dayOfMonth)
                                val hasPlan = hasPlanOnDate(plans, currentYear, currentMonth, dayOfMonth)
                                
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            when {
                                                isSelected -> accentColor.copy(alpha = 0.4f)
                                                isToday -> accentColor.copy(alpha = 0.15f)
                                                else -> Color.Transparent
                                            }
                                        )
                                ) {
                                    Text(
                                        text = dayOfMonth.toString(),
                                        fontSize = 14.sp,
                                        fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isToday || isSelected) themeColor else themeColor.copy(alpha = 0.6f)
                                    )
                                    
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (hasEntry) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .clip(CircleShape)
                                                    .background(accentColor)
                                            )
                                        }
                                        if (hasPlan) {
                                            if (hasEntry) Spacer(modifier = Modifier.width(2.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .clip(CircleShape)
                                                    .background(themeColor.copy(alpha = 0.2f))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if ((row + 1) * 7 - (firstDayOfWeek - 2) > daysInMonth) break
            }
        }
    }
}

private fun isToday(year: Int, month: Int, day: Int): Boolean {
    val today = Calendar.getInstance()
    return today.get(Calendar.YEAR) == year &&
            today.get(Calendar.MONTH) == month &&
            today.get(Calendar.DAY_OF_MONTH) == day
}

private fun hasEntryOnDate(entries: List<KibunEntry>, year: Int, month: Int, day: Int): Boolean {
    return entries.any { entry ->
        val cal = Calendar.getInstance().apply { timeInMillis = entry.date }
        cal.get(Calendar.YEAR) == year &&
                cal.get(Calendar.MONTH) == month &&
                cal.get(Calendar.DAY_OF_MONTH) == day
    }
}

private fun hasPlanOnDate(plans: List<KibunPlan>, year: Int, month: Int, day: Int): Boolean {
    return plans.any { plan ->
        val cal = Calendar.getInstance().apply { timeInMillis = plan.date }
        cal.get(Calendar.YEAR) == year &&
                cal.get(Calendar.MONTH) == month &&
                cal.get(Calendar.DAY_OF_MONTH) == day
    }
}
