package com.krishisetu.features.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.krishisetu.AppState
import com.krishisetu.data.fake.fakeUserRoles
import com.krishisetu.model.UserRole
import com.krishisetu.navigation.NavRoutes
import com.krishisetu.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
enum class AuthStep { PHONE, OTP, ROLE, FARMER_ID, DONE }

@Composable
fun AuthScreen(navController: NavController) {
    var currentStep by remember { mutableStateOf(AuthStep.PHONE) }
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var farmerId by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D3D2B),
                        Color(0xFF1A6B3C),
                        Color(0xFF228B50)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AuthTopSection(currentStep = currentStep)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    },
                    label = "authStep"
                ) { step ->
                    when (step) {
                        AuthStep.PHONE -> PhoneStep(
                            phoneNumber   = phoneNumber,
                            onPhoneChange = { if (it.length <= 10) phoneNumber = it },
                            onNext        = { if (phoneNumber.length == 10) currentStep = AuthStep.OTP }
                        )
                        AuthStep.OTP -> OtpStep(
                            phoneNumber = phoneNumber,
                            otpCode     = otpCode,
                            onOtpChange = { if (it.length <= 6) otpCode = it },
                            onVerify    = { if (otpCode.length == 6) currentStep = AuthStep.ROLE },
                            onResend    = { otpCode = "" },
                            onBack      = { currentStep = AuthStep.PHONE }
                        )
                        AuthStep.ROLE -> RoleStep(
                            selectedRole = selectedRole,
                            onRoleSelect = { selectedRole = it },
                            onNext       = {
                                if (selectedRole != null) {
                                    currentStep = if (selectedRole?.id == "farmer")
                                        AuthStep.FARMER_ID else AuthStep.DONE
                                }
                            }
                        )
                        AuthStep.FARMER_ID -> FarmerIdStep(
                            farmerId   = farmerId,
                            onIdChange = { farmerId = it },
                            onLink     = { currentStep = AuthStep.DONE },
                            onSkip     = { currentStep = AuthStep.DONE }
                        )
                        AuthStep.DONE -> DoneStep(
                            onEnter = {
                                navController.navigate(NavRoutes.Home.route) {
                                    popUpTo(NavRoutes.Auth.route) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthTopSection(currentStep: AuthStep) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(White.copy(alpha = 0.15f))
                .border(1.dp, White.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("🌱", fontSize = 36.sp)
        }
        Text(
            text       = "KrishiSetu",
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize   = 28.sp,
            color      = White
        )
        Text(
            text       = "कृषि सेतु",
            fontFamily = Poppins,
            fontSize   = 14.sp,
            color      = White.copy(alpha = 0.7f)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            listOf(AuthStep.PHONE, AuthStep.OTP, AuthStep.ROLE, AuthStep.FARMER_ID)
                .forEach { step ->
                    val isActive = currentStep == step
                    val isPast   = currentStep.ordinal > step.ordinal
                    Box(
                        modifier = Modifier
                            .width(if (isActive) 24.dp else 8.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when {
                                    isPast   -> White.copy(alpha = 0.6f)
                                    isActive -> White
                                    else     -> White.copy(alpha = 0.3f)
                                }
                            )
                    )
                }
        }
    }
}

// ── Phone Step ────────────────────────────────────────────────────────────────
@Composable
private fun PhoneStep(
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    onNext: () -> Unit
) {
    // ← Stateful language selection
    var selectedLang by remember { mutableStateOf<String>(AppState.selectedLanguageDisplay) }
    LazyColumn(
        modifier       = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 32.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text       = "Enter your mobile number",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 24.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text       = "अपना मोबाइल नंबर दर्ज करें",
                    fontFamily = Poppins,
                    fontSize   = 14.sp,
                    color      = Gray400
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text       = "Mobile Number",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            1.dp,
                            if (phoneNumber.length == 10) Green800
                            else MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Green800.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text       = "🇮🇳 +91",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp,
                            color      = Green500
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(MaterialTheme.colorScheme.outline)
                    )
                    BasicTextField(
                        value           = phoneNumber,
                        onValueChange   = onPhoneChange,
                        modifier        = Modifier.weight(1f),
                        textStyle       = TextStyle(
                            fontFamily    = Poppins,
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 20.sp,
                            color         = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 2.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        cursorBrush     = SolidColor(Green800),
                        decorationBox   = { inner ->
                            if (phoneNumber.isEmpty()) {
                                Text(
                                    text          = "XXXXXXXXXX",
                                    fontFamily    = Poppins,
                                    fontWeight    = FontWeight.Bold,
                                    fontSize      = 20.sp,
                                    color         = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    letterSpacing = 2.sp
                                )
                            }
                            inner()
                        }
                    )
                    if (phoneNumber.length == 10) {
                        Icon(
                            Icons.Filled.CheckCircle, null,
                            tint     = Green800,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text       = "We'll send a 6-digit OTP to verify your number",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Gray400,
                    lineHeight = 18.sp
                )
            }
        }

        // ── Language selector — NOW FUNCTIONAL ────────────────────────────────
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text       = "Select your language / भाषा चुनें",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("हिंदी", "English", "मराठी", "ਪੰਜਾਬੀ").forEach { lang ->
                        val isSelected = selectedLang == lang
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) Green800
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    1.5.dp,
                                    if (isSelected) Green800
                                    else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedLang = lang
                                    AppState.setLanguage(lang) // ← updates global state
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text       = lang,
                                fontFamily = Poppins,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize   = 13.sp,
                                color      = if (isSelected) White
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("বাংলা", "తెలుగు", "தமிழ்").forEach { lang ->
                        val isSelected = selectedLang == lang
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) Green800
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    1.5.dp,
                                    if (isSelected) Green800
                                    else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedLang = lang
                                    AppState.setLanguage(lang)
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text       = lang,
                                fontFamily = Poppins,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize   = 13.sp,
                                color      = if (isSelected) White
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Selected language confirmation
                if (selectedLang != "हिंदी") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle, null,
                            tint     = Green800,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text       = "AI will respond in $selectedLang",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Green800
                        )
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (phoneNumber.length == 10) Green800 else Gray200)
                        .clickable(enabled = phoneNumber.length == 10, onClick = onNext)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = "Send OTP →",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = if (phoneNumber.length == 10) White else Gray400
                    )
                }
                Text(
                    text       = "By continuing, you agree to our Terms of Service and Privacy Policy",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400,
                    textAlign  = TextAlign.Center,
                    lineHeight = 16.sp,
                    modifier   = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ── OTP Step ──────────────────────────────────────────────────────────────────
@Composable
private fun OtpStep(
    phoneNumber: String,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
    onBack: () -> Unit
) {
    var secondsLeft by remember { mutableStateOf(30) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            while (secondsLeft > 0) { delay(1000); secondsLeft-- }
        }
    }

    LazyColumn(
        modifier       = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 32.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack, null,
                    tint     = Gray600,
                    modifier = Modifier.size(20.dp).clickable(onClick = onBack)
                )
                Column {
                    Text(
                        text       = "Verify OTP",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 24.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = "OTP सत्यापित करें",
                        fontFamily = Poppins,
                        fontSize   = 14.sp,
                        color      = Gray400
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Green800.copy(alpha = 0.1f))
                    .border(1.dp, Green800.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.Sms, null, tint = Green800, modifier = Modifier.size(20.dp))
                    Column {
                        Text("OTP sent to", fontFamily = Poppins, fontSize = 12.sp, color = Gray600)
                        Text(
                            text       = "+91 $phoneNumber",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 15.sp,
                            color      = Green800
                        )
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text       = "Enter 6-digit OTP",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                OtpInputRow(otpCode = otpCode, onOtpChange = onOtpChange)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = if (secondsLeft > 0) "Resend OTP in ${secondsLeft}s"
                        else "Didn't receive OTP?",
                        fontFamily = Poppins,
                        fontSize   = 13.sp,
                        color      = Gray400
                    )
                    if (secondsLeft == 0) {
                        Text(
                            text       = "Resend",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 13.sp,
                            color      = Green800,
                            modifier   = Modifier.clickable {
                                onResend()
                                secondsLeft = 30
                            }
                        )
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFF7ED))
                    .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text       = "💡 For testing: enter any 6 digits to proceed",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Color(0xFF92400E)
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (otpCode.length == 6) Green800 else Gray200)
                    .clickable(enabled = otpCode.length == 6, onClick = onVerify)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "Verify & Continue →",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = if (otpCode.length == 6) White else Gray400
                )
            }
        }
    }
}

// ── OTP Input Row — FIXED for dark mode ───────────────────────────────────────
@Composable
private fun OtpInputRow(otpCode: String, onOtpChange: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Hidden capture field
        BasicTextField(
            value           = otpCode,
            onValueChange   = onOtpChange,
            modifier        = Modifier
                .focusRequester(focusRequester)
                .size(1.dp)
                .offset(x = (-1000).dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(6) { index ->
                val char      = otpCode.getOrNull(index)
                val isCurrent = otpCode.length == index

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        // ← KEY FIX: use surfaceVariant not Green50/Gray100
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            2.dp,
                            when {
                                char != null -> Green800
                                isCurrent    -> Green800
                                else         -> MaterialTheme.colorScheme.outline
                            },
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { focusRequester.requestFocus() },
                    contentAlignment = Alignment.Center
                ) {
                    if (char != null) {
                        Text(
                            text       = char.toString(),
                            fontFamily = Poppins,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 22.sp,
                            // ← KEY FIX: onSurface adapts to dark/light
                            color      = MaterialTheme.colorScheme.onSurface,
                            textAlign  = TextAlign.Center
                        )
                    } else if (isCurrent) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(24.dp)
                                .background(Green800)
                        )
                    }
                }
            }
        }
    }
}

// ── Role Step ─────────────────────────────────────────────────────────────────
@Composable
private fun RoleStep(
    selectedRole: UserRole?,
    onRoleSelect: (UserRole) -> Unit,
    onNext: () -> Unit
) {
    LazyColumn(
        modifier       = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 32.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text       = "Who are you?",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 24.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text       = "आप कौन हैं? — अपनी भूमिका चुनें",
                    fontFamily = Poppins,
                    fontSize   = 14.sp,
                    color      = Gray400
                )
            }
        }

        items(fakeUserRoles) { role ->
            RoleCard(
                role       = role,
                isSelected = selectedRole?.id == role.id,
                onSelect   = { onRoleSelect(role) }
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedRole != null) Green800 else Gray200)
                    .clickable(enabled = selectedRole != null, onClick = onNext)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "Continue as ${selectedRole?.label ?: "..."} →",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = if (selectedRole != null) White else Gray400
                )
            }
        }
    }
}

@Composable
private fun RoleCard(role: UserRole, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) Green800.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surface
            )
            .border(
                2.dp,
                if (isSelected) Green800 else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onSelect)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) Green800.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(role.emoji, fontSize = 26.sp)
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = role.label,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = if (isSelected) Green800
                        else MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = role.labelHindi,
                        fontFamily = Poppins,
                        fontSize   = 13.sp,
                        color      = Gray400
                    )
                }
                Text(
                    text       = role.description,
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Gray400,
                    lineHeight = 17.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isSelected) Green800 else Color.Transparent)
                .border(2.dp, if (isSelected) Green800 else Gray200, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(Icons.Filled.Check, null, tint = White, modifier = Modifier.size(14.dp))
            }
        }
    }
}

// ── Farmer ID Step ────────────────────────────────────────────────────────────
@Composable
private fun FarmerIdStep(
    farmerId: String,
    onIdChange: (String) -> Unit,
    onLink: () -> Unit,
    onSkip: () -> Unit
) {
    LazyColumn(
        modifier       = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        contentPadding = PaddingValues(top = 32.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text       = "Link Farmer ID",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 24.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text       = "किसान ID लिंक करें — AgriStack",
                    fontFamily = Poppins,
                    fontSize   = 14.sp,
                    color      = Gray400
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Green800.copy(alpha = 0.08f))
                    .border(1.dp, Green800.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("🏛️ What is AgriStack?", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Green800)
                listOf(
                    "Government's digital farmer identity system",
                    "Links your land records automatically",
                    "Unlocks PM-KISAN and other scheme benefits",
                    "Makes loan and credit access faster"
                ).forEach { point ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                        Text("✓", color = Green800, fontWeight = FontWeight.Bold)
                        Text(text = point, fontFamily = Poppins, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), lineHeight = 18.sp)
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Farmer Registry ID", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, if (farmerId.isNotEmpty()) Green800 else MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Filled.Badge, null, tint = if (farmerId.isNotEmpty()) Green800 else Gray400, modifier = Modifier.size(20.dp))
                    BasicTextField(
                        value         = farmerId,
                        onValueChange = onIdChange,
                        modifier      = Modifier.weight(1f),
                        textStyle     = TextStyle(fontFamily = Poppins, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface),
                        cursorBrush   = SolidColor(Green800),
                        decorationBox = { inner ->
                            if (farmerId.isEmpty()) {
                                Text("e.g. FR-UP-2024-XXXXXX", fontFamily = Poppins, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                            }
                            inner()
                        }
                    )
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (farmerId.isNotEmpty()) Green800 else Gray200)
                        .clickable(enabled = farmerId.isNotEmpty(), onClick = onLink)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🔗 Link Farmer ID", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (farmerId.isNotEmpty()) White else Gray400)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .clickable(onClick = onSkip)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Skip for now →", fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Gray600)
                }
            }
        }
    }
}

// ── Done Step ─────────────────────────────────────────────────────────────────
@Composable
private fun DoneStep(onEnter: () -> Unit) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { scope.launch { delay(2000); onEnter() } }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp).clip(CircleShape).background(Green800.copy(alpha = 0.1f)).border(2.dp, Green800.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) { Text("🎉", fontSize = 48.sp) }
        Spacer(Modifier.height(24.dp))
        Text("Welcome to KrishiSetu!", fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("कृषि सेतु में आपका स्वागत है", fontFamily = Poppins, fontSize = 16.sp, color = Gray400, textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        listOf("✅ Phone number verified", "✅ Role selected", "✅ Profile created").forEach { item ->
            Text(item, fontFamily = Poppins, fontSize = 14.sp, color = Green800, modifier = Modifier.padding(vertical = 4.dp))
        }
        Spacer(Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Green800, strokeWidth = 2.dp)
            Text("Taking you to your dashboard...", fontFamily = Poppins, fontSize = 13.sp, color = Gray400)
        }
    }
}