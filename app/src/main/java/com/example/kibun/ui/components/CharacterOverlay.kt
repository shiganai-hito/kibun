package com.example.kibun.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kibun.R

@Composable
fun CharacterOverlay(
    modifier: Modifier = Modifier,
    currentRoute: String?
) {
    // キャラクターのメッセージをルートに応じて決定
    val message = when (currentRoute) {
        "home" -> "今日はどんな気分？ゆっくり教えてね。"
        "diary_entry" -> "日記を書いてるんだね。無理せず、今の気持ちを言葉にしてみて。"
        "settings" -> "設定を変えるのかな？使いやすいように調整してね。"
        else -> "いつでもここにいるよ。"
    }

    // 浮遊アニメーションの設定
    val infiniteTransition = rememberInfiniteTransition(label = "CharacterFloat")
    val translateY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Float"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(bottom = 80.dp) // ナビゲーションバー等との重なり防止
        ) {
            // 吹き出し
            AnimatedContent(
                targetState = message,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) togetherWith
                    fadeOut(animationSpec = tween(500))
                },
                label = "MessageAnimation"
            ) { targetMessage ->
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .widthIn(max = 200.dp)
                ) {
                    Text(
                        text = targetMessage,
                        fontSize = 12.sp,
                        color = Color(0xFF454545),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // キャラクター画像
            Image(
                painter = painterResource(id = R.drawable.app_character_base),
                contentDescription = "Guide Character",
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer {
                        translationY = translateY
                    }
            )
        }
    }
}
