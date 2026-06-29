package com.example.kibun.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kibun.data.KibunEntry
import com.example.kibun.ui.viewmodel.KibunViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEntryScreen(
    viewModel: KibunViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("😊") }
    var selectedCategory by remember { mutableStateOf("日常") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> 
            if (uri != null) {
                // Android 12などで後から写真を表示するために、永続的なアクセス権を取得する
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    Log.w("DiaryEntryScreen", "永続的なURIパーミッションの取得に失敗しました", e)
                }
                selectedImageUri = uri 
            }
        }
    )

    val moods = listOf("😢", "😕", "😐", "😊", "🤩")
    val categories = listOf("日常", "旅行", "記念日", "仕事", "趣味", "グルメ", "その他")
    val accentColor = Color(0xFFE28E8E) // 韓国風のくすみピンク
    
    val themeOverride by viewModel.themeOverride.collectAsState()
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val (themeBrush, themeColor) = when (themeOverride) {
        "MORNING" -> Brush.verticalGradient(listOf(Color(0xFFFFF9F2), Color(0xFFFFEBD6))) to Color(0xFF4A4A4A)
        "AFTERNOON" -> Brush.verticalGradient(listOf(Color(0xFFFAF9F6), Color(0xFFFAF9F6))) to Color(0xFF333333)
        "NIGHT" -> Brush.verticalGradient(listOf(Color(0xFF1A1C1E), Color(0xFF2D2F31))) to Color(0xFFF0F0F0)
        else -> when {
            hour in 5..11 -> Brush.verticalGradient(listOf(Color(0xFFFFF9F2), Color(0xFFFFEBD6))) to Color(0xFF4A4A4A)
            hour in 12..17 -> Brush.verticalGradient(listOf(Color(0xFFFAF9F6), Color(0xFFFAF9F6))) to Color(0xFF333333)
            else -> Brush.verticalGradient(listOf(Color(0xFF1A1C1E), Color(0xFF2D2F31))) to Color(0xFFF0F0F0)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("日記を書く", fontWeight = FontWeight.ExtraBold, color = themeColor, fontSize = 22.sp, letterSpacing = (-0.5).sp) },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.padding(start = 8.dp).background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る", tint = themeColor.copy(alpha = 0.6f))
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "写真を撮る", tint = themeColor.copy(alpha = 0.6f))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (title.isNotBlank() || content.isNotBlank()) {
                                viewModel.insert(
                                    KibunEntry(
                                        title = title, 
                                        content = content, 
                                        date = System.currentTimeMillis(), 
                                        mood = selectedMood,
                                        category = selectedCategory,
                                        imageUri = selectedImageUri?.toString()
                                    )
                                )
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp).background(accentColor.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "保存", tint = accentColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(themeBrush)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = "今日の気分は？",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = themeColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    moods.forEach { mood ->
                        MoodEmoji(
                            emoji = mood,
                            isSelected = selectedMood == mood,
                            accentColor = accentColor,
                            onClick = { selectedMood = mood }
                        )
                    }
                }

                Text(
                    text = "カテゴリ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = themeColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        CategoryChip(
                            label = category,
                            isSelected = selectedCategory == category,
                            accentColor = accentColor,
                            themeColor = themeColor,
                            onClick = { selectedCategory = category }
                        )
                    }
                }

                if (selectedImageUri != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .padding(bottom = 20.dp),
                        shape = RoundedCornerShape(28.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("タイトル", color = themeColor.copy(alpha = 0.3f), fontWeight = FontWeight.Medium) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = themeColor,
                        unfocusedTextColor = themeColor.copy(alpha = 0.8f),
                        cursorColor = accentColor,
                        focusedContainerColor = Color.White.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f)
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("今日の出来事や気持ちを自由に書きましょう...", color = themeColor.copy(alpha = 0.3f), fontWeight = FontWeight.Medium) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    shape = RoundedCornerShape(28.dp),
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = themeColor,
                        unfocusedTextColor = themeColor.copy(alpha = 0.8f),
                        cursorColor = accentColor,
                        focusedContainerColor = Color.White.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f)
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        if (title.isNotBlank() || content.isNotBlank()) {
                            viewModel.insert(
                                KibunEntry(
                                    title = title, 
                                    content = content, 
                                    date = System.currentTimeMillis(), 
                                    mood = selectedMood,
                                    category = selectedCategory,
                                    imageUri = selectedImageUri?.toString()
                                )
                            )
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text("保存する", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
fun MoodEmoji(
    emoji: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) accentColor.copy(alpha = 0.4f)
                else Color.White.copy(alpha = 0.2f)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = if (isSelected) 28.sp else 22.sp
        )
    }
}

@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    themeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) accentColor.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.3f),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)) else null
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) themeColor else themeColor.copy(alpha = 0.6f)
        )
    }
}
