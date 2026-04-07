package com.krishisetu.features.processing

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.data.fake.fakeEquipment
import com.krishisetu.data.fake.fakeGuides
import com.krishisetu.model.ProcessingEquipment
import com.krishisetu.model.ProcessingGuide
import com.krishisetu.ui.theme.*

@Composable
fun ProcessingScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Equipment", "Guides", "Calculator")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        ProcessingHeader()

        // Tab row
        ProcessingTabRow(
            selectedTab = selectedTab,
            tabs        = tabs,
            onTabSelected = { selectedTab = it }
        )

        // Content
        when (selectedTab) {
            0 -> EquipmentTab()
            1 -> GuidesTab()
            2 -> CalculatorTab()
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun ProcessingHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text       = "Processing",
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 26.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "कच्चे से पक्के दाम तक",
                fontFamily = Poppins,
                fontSize   = 13.sp,
                color      = Gray400
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Green50)
                .border(1.dp, Green100, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Factory,
                contentDescription = null,
                tint = Green800,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Tab Row ───────────────────────────────────────────────────────────────────
@Composable
private fun ProcessingTabRow(
    selectedTab: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selectedTab == index
            val bgColor by animateColorAsState(
                targetValue   = if (isSelected) White else Color.Transparent,
                animationSpec = tween(200),
                label         = "tabBg"
            )
            val textColor by animateColorAsState(
                targetValue   = if (isSelected) Green800 else Gray400,
                animationSpec = tween(200),
                label         = "tabText"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor)
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = tab,
                    fontFamily = Poppins,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize   = 13.sp,
                    color      = textColor
                )
            }
        }
    }
    Spacer(Modifier.height(16.dp))
}

// ── Equipment Tab ─────────────────────────────────────────────────────────────
@Composable
private fun EquipmentTab() {
    val categories = listOf("All", "Milling", "Oil Extraction", "Pulping", "Grinding", "Drying")
    var selectedCategory by remember { mutableStateOf("All") }

    val filtered = if (selectedCategory == "All") fakeEquipment
    else fakeEquipment.filter { it.category == selectedCategory }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    ProcessingChip(
                        label    = cat,
                        selected = selectedCategory == cat,
                        onClick  = { selectedCategory = cat }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        items(filtered, key = { it.id }) { equipment ->
            EquipmentCard(equipment = equipment)
        }
    }
}

// ── Equipment Card ────────────────────────────────────────────────────────────
@Composable
private fun EquipmentCard(equipment: ProcessingEquipment) {
    var isRenting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp,
                if (isRenting) Green800 else Gray200,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        // Top row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Green50)
                        .border(1.dp, Green100, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(equipment.emoji, fontSize = 26.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text       = equipment.nameHindi,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = equipment.name,
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Gray400
                    )
                }
            }

            // Availability badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (equipment.isAvailable) Color(0xFFDCFCE7)
                        else Color(0xFFFFE4E4)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text       = if (equipment.isAvailable) "Available" else "Booked",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 10.sp,
                    color      = if (equipment.isAvailable) Color(0xFF15803D)
                    else Color(0xFFDC2626)
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = Gray200, thickness = 0.5.dp)
        Spacer(Modifier.height(12.dp))

        // Info row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EquipmentInfo("⚡", "${equipment.powerKW} kW")
            EquipmentInfo("📍", equipment.district)
            EquipmentInfo(
                "⭐",
                "${equipment.rating}",
            )
        }

        Spacer(Modifier.height(12.dp))

        // Suitable for chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(equipment.suitableFor) { crop ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Green50)
                        .border(1.dp, Green100, RoundedCornerShape(50.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = crop,
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        color      = Green700
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Price + action row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text       = "₹${equipment.rentPerDay}",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 20.sp,
                        color      = Green800
                    )
                    Text(
                        text       = "/day",
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        color      = Gray400,
                        modifier   = Modifier.padding(bottom = 2.dp, start = 2.dp)
                    )
                }
                Text(
                    text       = "Buy: ₹${equipment.buyPrice}",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Info button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Green50)
                        .border(1.dp, Green100, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Info, null,
                        tint     = Green700,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Rent button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (!equipment.isAvailable) Gray200
                            else if (isRenting) Green100
                            else Green800
                        )
                        .clickable(enabled = equipment.isAvailable) {
                            isRenting = !isRenting
                        }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                ) {
                    Text(
                        text       = when {
                            !equipment.isAvailable -> "Unavailable"
                            isRenting              -> "✓ Booked"
                            else                   -> "Book Now"
                        },
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 13.sp,
                        color      = when {
                            !equipment.isAvailable -> Gray400
                            isRenting              -> Green800
                            else                   -> White
                        }
                    )
                }
            }
        }
    }
}

// ── Guides Tab ────────────────────────────────────────────────────────────────
@Composable
private fun GuidesTab() {
    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Value addition banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Green800)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text       = "Value Addition",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp,
                            color      = White
                        )
                        Text(
                            text       = "कच्चे माल को बनाओ\nबड़े दाम का उत्पाद",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Green100,
                            lineHeight = 18.sp
                        )
                    }
                    Text("🏭", fontSize = 44.sp)
                }
            }
            Spacer(Modifier.height(4.dp))
        }

        items(fakeGuides, key = { it.id }) { guide ->
            GuideCard(guide = guide)
        }
    }
}

// ── Guide Card ────────────────────────────────────────────────────────────────
@Composable
private fun GuideCard(guide: ProcessingGuide) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, Gray200, RoundedCornerShape(16.dp))
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Green50)
                .border(1.dp, Green100, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(guide.emoji, fontSize = 28.sp)
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = guide.titleHindi,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 15.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = guide.title,
                fontFamily = Poppins,
                fontSize   = 12.sp,
                color      = Gray400
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GuideInfoChip("📋", "${guide.steps} steps")
                GuideInfoChip("⏱️", "${guide.durationMins} min")
            }
        }

        // Value multiplier badge
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFDCFCE7))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text       = guide.valueMultiplier,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 16.sp,
                    color      = Color(0xFF15803D)
                )
            }
            Text(
                text       = "value",
                fontFamily = Poppins,
                fontSize   = 10.sp,
                color      = Gray400
            )
        }
    }
}

@Composable
private fun GuideInfoChip(emoji: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 11.sp)
        Spacer(Modifier.width(3.dp))
        Text(
            text       = text,
            fontFamily = Poppins,
            fontSize   = 11.sp,
            color      = Gray600
        )
    }
}

// ── Calculator Tab ────────────────────────────────────────────────────────────
@Composable
private fun CalculatorTab() {
    var quantity by remember { mutableStateOf(100f) }
    var rawPrice by remember { mutableStateOf(1000f) }
    var processedMultiplier by remember { mutableStateOf(3f) }

    val rawRevenue = quantity * rawPrice
    val processedRevenue = rawRevenue * processedMultiplier
    val additionalEarning = processedRevenue - rawRevenue

    LazyColumn(
        modifier = Modifier.padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text       = "Value Addition Calculator",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "मूल्य वृद्धि कैलकुलेटर",
                fontFamily = Poppins,
                fontSize   = 13.sp,
                color      = Gray400
            )
        }

        // Quantity slider
        item {
            CalcSliderCard(
                label    = "Quantity",
                hindi    = "मात्रा",
                value    = quantity,
                min      = 10f,
                max      = 1000f,
                unit     = "quintals",
                onValueChange = { quantity = it }
            )
        }

        // Raw price slider
        item {
            CalcSliderCard(
                label    = "Raw Price",
                hindi    = "कच्चा भाव",
                value    = rawPrice,
                min      = 500f,
                max      = 10000f,
                unit     = "₹/quintal",
                onValueChange = { rawPrice = it }
            )
        }

        // Multiplier slider
        item {
            CalcSliderCard(
                label    = "Value Multiplier",
                hindi    = "मूल्य गुणक",
                value    = processedMultiplier,
                min      = 1.5f,
                max      = 10f,
                unit     = "x",
                onValueChange = { processedMultiplier = it }
            )
        }

        // Results card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Green800)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text       = "Your Earnings",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = White
                )

                ResultRow(
                    label = "Without processing",
                    value = "₹${formatAmount(rawRevenue)}",
                    color = Green100
                )
                ResultRow(
                    label = "After processing",
                    value = "₹${formatAmount(processedRevenue)}",
                    color = White
                )

                HorizontalDivider(color = Green600, thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Extra Earnings 🎉",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = Green100
                    )
                    Text(
                        text       = "+₹${formatAmount(additionalEarning)}",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 22.sp,
                        color      = Color(0xFF6DD99A)
                    )
                }
            }
        }
    }
}

@Composable
private fun CalcSliderCard(
    label: String,
    hindi: String,
    value: Float,
    min: Float,
    max: Float,
    unit: String,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, Gray200, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text       = label,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text       = hindi,
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
            }
            Text(
                text       = "${value.toInt()} $unit",
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 16.sp,
                color      = Green800
            )
        }
        Slider(
            value         = value,
            onValueChange = onValueChange,
            valueRange    = min..max,
            colors        = SliderDefaults.colors(
                thumbColor       = Green800,
                activeTrackColor = Green800,
                inactiveTrackColor = Green100
            )
        )
    }
}

@Composable
private fun ResultRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text       = label,
            fontFamily = Poppins,
            fontSize   = 13.sp,
            color      = Green200
        )
        Text(
            text       = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 14.sp,
            color      = color
        )
    }
}

private fun formatAmount(amount: Float): String {
    return when {
        amount >= 10000000 -> "${(amount / 10000000).toInt()} Cr"
        amount >= 100000  -> "${(amount / 100000).toInt()} L"
        amount >= 1000    -> "${(amount / 1000).toInt()}K"
        else              -> amount.toInt().toString()
    }
}

// ── Small helpers ─────────────────────────────────────────────────────────────
@Composable
private fun ProcessingChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(if (selected) Green800 else Gray100)
            .border(1.dp, if (selected) Green800 else Gray200, RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text       = label,
            fontFamily = Poppins,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize   = 13.sp,
            color      = if (selected) White else Gray600
        )
    }
}

@Composable
private fun EquipmentInfo(emoji: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 12.sp)
        Spacer(Modifier.width(4.dp))
        Text(
            text       = text,
            fontFamily = Poppins,
            fontSize   = 11.sp,
            color      = Gray600
        )
    }
}