package com.krishisetu.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.data.fake.fakeFarmerCrops
import com.krishisetu.data.fake.fakeFarmerStats
import com.krishisetu.data.fake.fakeSettings
import com.krishisetu.model.FarmerCrop
import com.krishisetu.model.SettingItem
import com.krishisetu.ui.theme.*

@Composable
fun ProfileScreen() {
    val languages = listOf("हिंदी", "English", "मराठी", "ਪੰਜਾਬੀ", "বাংলা", "తెలుగు", "தமிழ்")
    var selectedLanguage by remember { mutableStateOf("हिंदी") }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var settingToggles by remember {
        mutableStateOf(fakeSettings.map { it.toggleValue }.toMutableList())
    }

    // Wrap in Box so sheet can overlay at root level
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 140.dp)
        ) {
            item { ProfileHeader() }

            item {
                Spacer(Modifier.height(16.dp))
                FarmerCard(selectedLanguage = selectedLanguage)
            }

            item {
                Spacer(Modifier.height(20.dp))
                ProfileStatsRow()
            }

            item {
                Spacer(Modifier.height(24.dp))
                ProfileSectionTitle("My Crops", "मेरी फसलें")
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(fakeFarmerCrops) { crop -> CropCard(crop = crop) }
                    item { AddCropCard() }
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                SchemesBanner()
            }

            item {
                Spacer(Modifier.height(24.dp))
                ProfileSectionTitle("Settings", "सेटिंग्स")
                Spacer(Modifier.height(12.dp))
            }

            items(fakeSettings.size) { index ->
                val setting = fakeSettings[index]
                SettingRow(
                    setting     = setting,
                    toggleValue = settingToggles[index],
                    onToggle    = { value ->
                        val updated = settingToggles.toMutableList()
                        updated[index] = value
                        settingToggles = updated
                    },
                    onClick = {
                        if (setting.label == "Language") showLanguageSheet = true
                    }
                )
                if (index < fakeSettings.lastIndex) {
                    HorizontalDivider(
                        modifier  = Modifier.padding(horizontal = 20.dp),
                        color     = Gray200,
                        thickness = 0.5.dp
                    )
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, ErrorRed.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .clickable { }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Logout, null, tint = ErrorRed, modifier = Modifier.size(18.dp))
                        Text("Logout", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = ErrorRed)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text     = "KrishiSetu v1.0 · Made for Indian Farmers 🌱",
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color    = Gray400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
        }

        // ← Language sheet rendered at Box level, persists correctly
        if (showLanguageSheet) {
            LanguageBottomSheet(
                languages        = languages,
                selectedLanguage = selectedLanguage,
                onSelect         = { lang ->
                    selectedLanguage = lang
                    showLanguageSheet = false
                },
                onDismiss = { showLanguageSheet = false }
            )
        }
    }
}

@Composable
private fun ProfileHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Profile", fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, color = MaterialTheme.colorScheme.onBackground)
            Text("आपकी जानकारी", fontFamily = Poppins, fontSize = 13.sp, color = Gray400)
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Green50)
                .border(1.dp, Green100, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Edit, "Edit", tint = Green800, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun FarmerCard(selectedLanguage: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Green800)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(Green600),
                    contentAlignment = Alignment.Center
                ) {
                    Text("R", fontFamily = Poppins, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = White)
                }
                Spacer(Modifier.width(14.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Rachit Bansal", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = White)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.LocationOn, null, tint = Green200, modifier = Modifier.size(13.dp))
                        Text("Lucknow, UP", fontFamily = Poppins, fontSize = 13.sp, color = Green200)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Green700)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("🌐 $selectedLanguage", fontFamily = Poppins, fontSize = 11.sp, color = Green100)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF22C55E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Verified, null, tint = White, modifier = Modifier.size(22.dp))
                }
                Text("Verified", fontFamily = Poppins, fontSize = 10.sp, color = Green100)
            }
        }
    }
}

@Composable
private fun ProfileStatsRow() {
    val stats = fakeFarmerStats
    Row(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ProfileStatCard("💰", stats.totalSales,          "Total Sales", Modifier.weight(1f))
        ProfileStatCard("📦", "${stats.batchesCreated}", "Batches",     Modifier.weight(1f))
        ProfileStatCard("🛒", "${stats.activeListings}", "Active",      Modifier.weight(1f))
        ProfileStatCard("💚", stats.savedAmount,         "Saved",       Modifier.weight(1f))
    }
}

@Composable
private fun ProfileStatCard(emoji: String, value: String, label: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Green50)
            .border(1.dp, Green100, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 16.sp)
        Spacer(Modifier.height(2.dp))
        Text(value, fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Green800)
        Text(label, fontFamily = Poppins, fontSize = 9.sp, color = Gray400)
    }
}

@Composable
private fun ProfileSectionTitle(title: String, hindi: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(title, fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
        Text(hindi, fontFamily = Poppins, fontSize = 12.sp, color = Gray400)
    }
}

@Composable
private fun CropCard(crop: FarmerCrop) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, Gray200, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Green50).border(1.dp, Green100, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) { Text(crop.emoji, fontSize = 22.sp) }
        Text(crop.nameHindi, fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
        Text("${crop.areaSown} acres", fontFamily = Poppins, fontSize = 12.sp, color = Gray400)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (crop.season == "Rabi") Color(0xFFEFF6FF) else Color(0xFFFFF7ED))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                crop.season,
                fontFamily = Poppins,
                fontSize   = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color      = if (crop.season == "Rabi") Color(0xFF1D4ED8) else Color(0xFFC2410C)
            )
        }
        Text("Sown: ${crop.sowingDate}", fontFamily = Poppins, fontSize = 10.sp, color = Gray400)
    }
}

@Composable
private fun AddCropCard() {
    Column(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, Gray200, RoundedCornerShape(16.dp))
            .clickable { }
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Green50).border(1.dp, Green100, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Add, null, tint = Green800, modifier = Modifier.size(22.dp)) }
        Text("Add Crop", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Green800)
        Text("फसल जोड़ें", fontFamily = Poppins, fontSize = 11.sp, color = Gray400)
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SchemesBanner() {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFBEB))
            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(16.dp))
            .clickable { }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Government Schemes", fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF92400E))
            Text("3 schemes you're eligible for", fontFamily = Poppins, fontSize = 12.sp, color = Color(0xFF78350F))
            Text("PM-KISAN · PMFBY · PKVY", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color(0xFF92400E))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🏛️", fontSize = 32.sp)
            Text("Check →", fontFamily = Poppins, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Color(0xFF92400E))
        }
    }
}

@Composable
private fun SettingRow(
    setting: SettingItem,
    toggleValue: Boolean,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(Green50).border(1.dp, Green100, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) { Text(setting.emoji, fontSize = 18.sp) }
            Column {
                Text(setting.label, fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
                Text(setting.labelHindi, fontFamily = Poppins, fontSize = 11.sp, color = Gray400)
            }
        }
        if (setting.hasToggle) {
            Switch(
                checked         = toggleValue,
                onCheckedChange = onToggle,
                colors          = SwitchDefaults.colors(
                    checkedThumbColor   = White,
                    checkedTrackColor   = Green800,
                    uncheckedThumbColor = White,
                    uncheckedTrackColor = Gray200
                )
            )
        } else {
            Icon(Icons.Filled.ChevronRight, null, tint = Gray400, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun LanguageBottomSheet(
    languages: List<String>,
    selectedLanguage: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Full screen overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
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
                text       = "Select Language / भाषा चुनें",
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

            languages.forEach { lang ->
                val isSelected = lang == selectedLanguage
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
                        .clickable { onSelect(lang) }   // ← direct call, no lambda capture issue
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = lang,
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