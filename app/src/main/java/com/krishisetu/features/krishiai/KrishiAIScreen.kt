package com.krishisetu.features.krishiai

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krishisetu.data.fake.fakeSuggestedQueries
import com.krishisetu.model.ChatMessage
import com.krishisetu.model.SuggestedQuery
import com.krishisetu.ui.theme.*
import kotlinx.coroutines.delay

val languageOptions = mapOf(
    "hi-IN" to "हिंदी",
    "en-IN" to "English",
    "mr-IN" to "मराठी",
    "pa-IN" to "ਪੰਜਾਬੀ",
    "bn-IN" to "বাংলা",
    "te-IN" to "తెలుగు",
    "ta-IN" to "தமிழ்"
)

@Composable
fun KrishiAIScreen(
    viewModel: KrishiAIViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var showLanguagePicker by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            delay(100)
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    if (showLanguagePicker) {
        LanguagePickerSheet(
            currentLang = state.selectedLanguage,
            options     = languageOptions,
            onSelect    = {
                viewModel.setLanguage(it)
                showLanguagePicker = false
            },
            onDismiss = { showLanguagePicker = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        KrishiAIHeader(
            isSpeaking       = state.isSpeaking,
            selectedLanguage = languageOptions[state.selectedLanguage] ?: "हिंदी",
            onLanguageClick  = { showLanguagePicker = true },
            onClearChat      = { viewModel.clearChat() }
        )

        // Error banner
        AnimatedVisibility(visible = state.error != null) {
            state.error?.let { error ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFEE2E2))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.Error, null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                    Text(text = error, fontFamily = Poppins, fontSize = 12.sp, color = Color(0xFFDC2626))
                }
            }
        }

        LazyColumn(
            state           = listState,
            modifier        = Modifier.weight(1f),
            contentPadding  = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.messages, key = { it.id }) { message ->
                ChatBubble(
                    message = message,
                    onSpeak = { viewModel.speakMessage(it) }
                )
            }

            if (state.isTyping) {
                item { TypingIndicator() }
            }

            if (state.messages.size == 1 && !state.isTyping) {
                item {
                    SuggestedQueriesRow(
                        queries  = fakeSuggestedQueries,
                        onSelect = { viewModel.sendMessage(it) }
                    )
                }
            }
        }

        ChatInputBar(
            text         = inputText,
            isListening  = state.isListening,
            isSpeaking   = state.isSpeaking,
            onTextChange = { inputText = it },
            onSend       = {
                viewModel.sendMessage(inputText)
                inputText = ""
            },
            onVoiceStart = { viewModel.startListening() },
            onVoiceStop  = { viewModel.stopListening() }
        )
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun KrishiAIHeader(
    isSpeaking: Boolean,
    selectedLanguage: String,
    onLanguageClick: () -> Unit,
    onClearChat: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Green800),
                contentAlignment = Alignment.Center
            ) {
                Text("🤖", fontSize = 22.sp)
            }
            Column {
                Text(
                    text       = "Krishi AI",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (isSpeaking) Color(0xFFF59E0B) else Color(0xFF22C55E))
                    )
                    Text(
                        text       = if (isSpeaking) "Speaking..." else "Gemini 1.5 Flash · Sarvam AI",
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        color      = Gray400
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Green50)
                    .border(1.dp, Green100, RoundedCornerShape(10.dp))
                    .clickable(onClick = onLanguageClick)
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                Text(
                    text       = "🌐 $selectedLanguage",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 12.sp,
                    color      = Green800
                )
            }
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Green50)
                    .border(1.dp, Green100, CircleShape)
                    .clickable(onClick = onClearChat),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Delete, null, tint = Green800, modifier = Modifier.size(18.dp))
            }
        }
    }
    HorizontalDivider(color = Gray200, thickness = 0.5.dp)
}

// ── Chat Bubble ───────────────────────────────────────────────────────────────
@Composable
private fun ChatBubble(
    message: ChatMessage,
    onSpeak: (String) -> Unit
) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment     = Alignment.Bottom
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Green800),
                contentAlignment = Alignment.Center
            ) {
                Text("🌱", fontSize = 14.sp)
            }
            Spacer(Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier            = Modifier.widthIn(max = 280.dp)
        ) {
            if (message.isVoice) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier              = Modifier.padding(bottom = 4.dp)
                ) {
                    Icon(Icons.Filled.Mic, null, tint = Gray400, modifier = Modifier.size(12.dp))
                    Text("Voice message", fontFamily = Poppins, fontSize = 10.sp, color = Gray400)
                }
            }

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart    = 16.dp,
                            topEnd      = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd   = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .background(
                        if (isUser) Green800 else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text       = message.text,
                    fontFamily = Poppins,
                    fontSize   = 14.sp,
                    lineHeight = 22.sp,
                    color      = if (isUser) White else MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                modifier              = Modifier.padding(top = 4.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text       = message.timestamp,
                    fontFamily = Poppins,
                    fontSize   = 10.sp,
                    color      = Gray400
                )
                if (!isUser) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Green50)
                            .border(1.dp, Green100, CircleShape)
                            .clickable { onSpeak(message.text) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.VolumeUp,
                            contentDescription = "Speak",
                            tint     = Green800,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }

        if (isUser) {
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Green100),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "R",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = Green800
                )
            }
        }
    }
}

// ── Typing Indicator ──────────────────────────────────────────────────────────
@Composable
private fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    Row(
        verticalAlignment     = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Green800),
            contentAlignment = Alignment.Center
        ) {
            Text("🌱", fontSize = 14.sp)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val scale by infiniteTransition.animateFloat(
                        initialValue  = 0.6f,
                        targetValue   = 1f,
                        animationSpec = infiniteRepeatable(
                            animation          = tween(400),
                            repeatMode         = RepeatMode.Reverse,
                            initialStartOffset = StartOffset(index * 150)
                        ),
                        label = "dot$index"
                    )
                    Box(
                        modifier = Modifier
                            .scale(scale)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Gray400)
                    )
                }
            }
        }
    }
}

// ── Suggested Queries ─────────────────────────────────────────────────────────
@Composable
private fun SuggestedQueriesRow(
    queries: List<SuggestedQuery>,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text       = "Try asking...",
            fontFamily = Poppins,
            fontSize   = 12.sp,
            color      = Gray400,
            modifier   = Modifier.padding(start = 4.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding        = PaddingValues(horizontal = 4.dp)
        ) {
            items(queries) { query ->
                Column(
                    modifier = Modifier
                        .width(140.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .clickable { onSelect(query.text) }
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(query.emoji, fontSize = 20.sp)
                    Text(
                        text       = query.textHindi,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 12.sp,
                        color      = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 17.sp
                    )
                    Text(
                        text       = query.text,
                        fontFamily = Poppins,
                        fontSize   = 10.sp,
                        color      = Gray400,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

// ── Chat Input Bar ────────────────────────────────────────────────────────────
@Composable
private fun ChatInputBar(
    text: String,
    isListening: Boolean,
    isSpeaking: Boolean,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onVoiceStart: () -> Unit,
    onVoiceStop: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue  = 1f,
        targetValue   = 1.15f,
        animationSpec = infiniteRepeatable(
            animation  = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column {
        HorizontalDivider(color = Gray200, thickness = 0.5.dp)

        AnimatedVisibility(visible = isListening) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDCFCE7))
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .scale(pulseScale)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF16A34A))
                )
                Text(
                    text       = "Listening... बोलिए",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = Color(0xFF15803D)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text       = "Tap mic to stop",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Color(0xFF15803D)
                )
            }
        }

        AnimatedVisibility(visible = isSpeaking) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF7ED))
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.VolumeUp, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                Text(
                    text       = "Speaking response...",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = Color(0xFF92400E)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment     = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        1.dp,
                        if (isListening) Green800 else MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value         = text,
                    onValueChange = onTextChange,
                    modifier      = Modifier.weight(1f),
                    textStyle     = TextStyle(
                        fontFamily = Poppins,
                        fontSize   = 14.sp,
                        color      = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(Green800),
                    maxLines    = 4,
                    decorationBox = { inner ->
                        if (text.isEmpty()) {
                            Text(
                                text       = if (isListening) "सुन रहा हूं..."
                                else "हिंदी या English में पूछें...",
                                fontFamily = Poppins,
                                fontSize   = 14.sp,
                                color      = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        inner()
                    }
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .scale(if (isListening) pulseScale else 1f)
                    .clip(CircleShape)
                    .background(if (isListening) Color(0xFF16A34A) else Green50)
                    .border(1.dp, if (isListening) Color(0xFF16A34A) else Green100, CircleShape)
                    .clickable { if (isListening) onVoiceStop() else onVoiceStart() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = if (isListening) Icons.Filled.MicOff else Icons.Filled.Mic,
                    contentDescription = "Voice",
                    tint               = if (isListening) White else Green800,
                    modifier           = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (text.isNotEmpty()) Green800 else Gray200)
                    .clickable(enabled = text.isNotEmpty(), onClick = onSend),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Send, null,
                    tint     = if (text.isNotEmpty()) White else Gray400,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ── Language Picker Sheet ─────────────────────────────────────────────────────
@Composable
private fun LanguagePickerSheet(
    currentLang: String,
    options: Map<String, String>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(onClick = onDismiss)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(MaterialTheme.colorScheme.background)
                .clickable(enabled = false) {}
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Gray200)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text       = "AI Language / AI भाषा",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "AI will respond in your chosen language",
                fontFamily = Poppins,
                fontSize   = 13.sp,
                color      = Gray400
            )
            Spacer(Modifier.height(8.dp))

            options.forEach { (code, name) ->
                val isSelected = code == currentLang
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) Green50 else Color.Transparent)
                        .border(
                            1.dp,
                            if (isSelected) Green800 else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelect(code) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = name,
                        fontFamily = Poppins,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize   = 15.sp,
                        color      = if (isSelected) Green800 else MaterialTheme.colorScheme.onBackground
                    )
                    if (isSelected) {
                        Icon(Icons.Filled.Check, null, tint = Green800, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}