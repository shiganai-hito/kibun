package com.example.kibun.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kibun.ui.viewmodel.KibunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: KibunViewModel,
    onNavigateBack: () -> Unit
) {
    val themeOverride by viewModel.themeOverride.collectAsState()
    
    val themes = listOf(
        ThemeOption("MORNING", "朝 (爽やかなブルー)", Color(0xFF546E7A), Color(0xFFCFDEE7)),
        ThemeOption("AFTERNOON", "昼 (落ち着いたグレー)", Color(0xFF455A64), Color(0xFFF5F5F5)),
        ThemeOption("NIGHT", "夜 (静かなネイビー)", Color(0xFFECEFF1), Color(0xFF263238))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "テーマカラー設定",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "現在の時間に関わらず、お好みのテーマを選択できます。",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 自動（解除）オプション
            ThemeCard(
                label = "システム自動設定 (時間帯に合わせる)",
                isSelected = themeOverride == null,
                color = MaterialTheme.colorScheme.primaryContainer,
                onClick = { viewModel.setThemeOverride(null) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            themes.forEach { theme ->
                ThemeCard(
                    label = theme.label,
                    isSelected = themeOverride == theme.id,
                    color = theme.previewColor,
                    onClick = { viewModel.setThemeOverride(theme.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ThemeCard(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, color) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "選択済み",
                    tint = color
                )
            }
        }
    }
}

private data class ThemeOption(
    val id: String,
    val label: String,
    val textColor: Color,
    val previewColor: Color
)
