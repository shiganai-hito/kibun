package com.example.kibun.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> 
            if (uri != null) {
                // Android 12などで後から写真を表示するために、永続的なアクセス権を取得する
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                selectedImageUri = uri 
            }
        }
    )

    val moods = listOf("😢", "😕", "😐", "😊", "🤩")
    val accentColor = Color(0xFFA8BDC9) // 共通の差し色
    
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val (themeBrush, themeColor) = when {
        hour in 5..11 -> Brush.verticalGradient(listOf(Color(0xFFE8F0F2), Color(0xFFCFDEE7))) to Color(0xFF546E7A)
        hour in 12..17 -> Brush.verticalGradient(listOf(Color(0xFFFCFCFC), Color(0xFFF5F5F5))) to Color(0xFF455A64)
        else -> Brush.verticalGradient(listOf(Color(0xFF102027), Color(0xFF263238))) to Color(0xFFECEFF1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("日記を書く", fontWeight = FontWeight.Bold, color = themeColor, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る", tint = themeColor.copy(alpha = 0.6f))
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "写真を撮る", tint = themeColor.copy(alpha = 0.6f))
                    }
                    IconButton(
                        onClick = {
                            if (title.isNotBlank() || content.isNotBlank()) {
                                viewModel.insert(
                                    KibunEntry(
                                        title = title, 
                                        content = content, 
                                        date = System.currentTimeMillis(), 
                                        mood = selectedMood,
                                        imageUri = selectedImageUri?.toString()
                                    )
                                )
                                onNavigateBack()
                            }
                        }
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
                    .padding(20.dp)
            ) {
                Text(
                    text = "今日の気分は？",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = themeColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
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

                if (selectedImageUri != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    placeholder = { Text("タイトル", color = themeColor.copy(alpha = 0.3f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = themeColor.copy(alpha = 0.1f),
                        focusedTextColor = themeColor,
                        unfocusedTextColor = themeColor.copy(alpha = 0.8f),
                        cursorColor = accentColor,
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("今日の出来事や気持ちを自由に書きましょう...", color = themeColor.copy(alpha = 0.3f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = themeColor.copy(alpha = 0.1f),
                        focusedTextColor = themeColor,
                        unfocusedTextColor = themeColor.copy(alpha = 0.8f),
                        cursorColor = accentColor,
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        if (title.isNotBlank() || content.isNotBlank()) {
                            viewModel.insert(KibunEntry(title = title, content = content, date = System.currentTimeMillis(), mood = selectedMood))
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text("保存する", fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
