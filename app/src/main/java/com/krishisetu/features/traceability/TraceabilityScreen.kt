package com.krishisetu.features.traceability

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.data.fake.fakeBatches
import com.krishisetu.model.TraceBatch
import com.krishisetu.model.TraceEvent
import com.krishisetu.ui.theme.*

@Composable
fun TraceabilityScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("My Batches", "Scan & Verify", "New Batch")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TraceHeader()

        TraceTabRow(
            selectedTab   = selectedTab,
            tabs          = tabs,
            onTabSelected = { selectedTab = it }
        )

        when (selectedTab) {
            0 -> MyBatchesTab()
            1 -> ScanVerifyTab()
            2 -> NewBatchTab()
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun TraceHeader() {
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
                text       = "Traceability",
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 26.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "खेत से बाज़ार तक पारदर्शिता",
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
                imageVector = Icons.Filled.QrCodeScanner,
                contentDescription = null,
                tint = Green800,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Tab Row ───────────────────────────────────────────────────────────────────
@Composable
private fun TraceTabRow(
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) White else Color.Transparent)
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = tab,
                    fontFamily = Poppins,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize   = 12.sp,
                    color      = if (isSelected) Green800 else Gray400
                )
            }
        }
    }
    Spacer(Modifier.height(16.dp))
}

// ── My Batches Tab ────────────────────────────────────────────────────────────
@Composable
private fun MyBatchesTab() {
    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Stats row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TraceStatCard("📦", "12", "Total Batches", Modifier.weight(1f))
                TraceStatCard("✅", "10", "Verified", Modifier.weight(1f))
                TraceStatCard("🔗", "8", "On-chain", Modifier.weight(1f))
            }
            Spacer(Modifier.height(4.dp))
        }

        items(fakeBatches, key = { it.batchId }) { batch ->
            BatchCard(batch = batch)
        }
    }
}

// ── Batch Card ────────────────────────────────────────────────────────────────
@Composable
private fun BatchCard(batch: TraceBatch) {
    var expanded by remember { mutableStateOf(false) }
    val completedEvents = batch.events.count { it.isCompleted }
    val totalEvents = batch.events.size
    val progress = completedEvents.toFloat() / totalEvents.toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, Gray200, RoundedCornerShape(16.dp))
    ) {
        // Main card content
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
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
                        Text(batch.emoji, fontSize = 26.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text       = batch.cropNameHindi,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp,
                            color      = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text       = "Batch: ${batch.batchId}",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Gray400
                        )
                    }
                }

                Icon(
                    imageVector = if (expanded)
                        Icons.Filled.KeyboardArrowUp
                    else
                        Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Gray400,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BatchInfoItem("👨‍🌾", batch.farmerName)
                BatchInfoItem("📍", batch.farmLocation)
                BatchInfoItem("⚖️", "${batch.quantity} qtl")
            }

            Spacer(Modifier.height(12.dp))

            // Progress bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text       = "Journey Progress",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Gray600
                    )
                    Text(
                        text       = "$completedEvents/$totalEvents steps",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 12.sp,
                        color      = Green800
                    )
                }
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress      = { progress },
                    modifier      = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color         = Green800,
                    trackColor    = Green100
                )
            }

            Spacer(Modifier.height(12.dp))

            // Badges + QR row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TraceBadge(
                        text      = "Grade ${batch.grade}",
                        bg        = Color(0xFFEFF6FF),
                        textColor = Color(0xFF1D4ED8)
                    )
                    if (batch.isOrganic) {
                        TraceBadge(
                            text      = "🌿 Organic",
                            bg        = Color(0xFFDCFCE7),
                            textColor = Color(0xFF15803D)
                        )
                    }
                }

                // QR button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Green800)
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.QrCode,
                            null,
                            tint     = White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text       = "View QR",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 12.sp,
                            color      = White
                        )
                    }
                }
            }
        }

        // Expanded timeline
        AnimatedVisibility(
            visible = expanded,
            enter   = expandVertically(),
            exit    = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Gray100.copy(alpha = 0.5f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                HorizontalDivider(color = Gray200, thickness = 0.5.dp)
                Spacer(Modifier.height(12.dp))
                Text(
                    text       = "Farm-to-Fork Journey",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(12.dp))

                batch.events.forEachIndexed { index, event ->
                    TimelineEvent(
                        event    = event,
                        isLast   = index == batch.events.lastIndex
                    )
                }
            }
        }
    }
}

// ── Timeline Event ────────────────────────────────────────────────────────────
@Composable
private fun TimelineEvent(event: TraceEvent, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Timeline line + dot
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (event.isCompleted) Green800 else Gray200
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(event.emoji, fontSize = 12.sp)
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(48.dp)
                        .background(
                            if (event.isCompleted) Green800 else Gray200
                        )
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Event details
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 8.dp)) {
            Text(
                text       = event.eventTypeHindi,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 13.sp,
                color      = if (event.isCompleted)
                    MaterialTheme.colorScheme.onBackground
                else Gray400
            )
            Text(
                text       = event.actorName,
                fontFamily = Poppins,
                fontSize   = 11.sp,
                color      = Gray600
            )
            Text(
                text       = "${event.location} · ${event.timestamp}",
                fontFamily = Poppins,
                fontSize   = 11.sp,
                color      = Gray400
            )
        }
    }
}

// ── Scan & Verify Tab ─────────────────────────────────────────────────────────
@Composable
private fun ScanVerifyTab() {
    var batchId by remember { mutableStateOf("") }
    val foundBatch = fakeBatches.find {
        it.batchId.equals(batchId.trim(), ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // QR Scanner placeholder
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Black)
                    .border(2.dp, Green800, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Scanner frame
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .border(2.dp, Green500, RoundedCornerShape(12.dp))
                    ) {
                        // Corner accents
                        Box(
                            Modifier.size(20.dp).align(Alignment.TopStart)
                                .border(
                                    width = 3.dp,
                                    color = Green500,
                                    shape = RoundedCornerShape(topStart = 8.dp)
                                )
                        )
                        Box(
                            Modifier.size(20.dp).align(Alignment.TopEnd)
                                .border(
                                    width = 3.dp,
                                    color = Green500,
                                    shape = RoundedCornerShape(topEnd = 8.dp)
                                )
                        )
                        Box(
                            Modifier.size(20.dp).align(Alignment.BottomStart)
                                .border(
                                    width = 3.dp,
                                    color = Green500,
                                    shape = RoundedCornerShape(bottomStart = 8.dp)
                                )
                        )
                        Box(
                            Modifier.size(20.dp).align(Alignment.BottomEnd)
                                .border(
                                    width = 3.dp,
                                    color = Green500,
                                    shape = RoundedCornerShape(bottomEnd = 8.dp)
                                )
                        )
                    }
                    Text(
                        text       = "Point camera at QR code",
                        fontFamily = Poppins,
                        fontSize   = 13.sp,
                        color      = White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // OR divider
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Gray200)
                Text(
                    text       = "OR enter batch ID",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Gray400
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Gray200)
            }
        }

        // Manual entry
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Gray100)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Search, null,
                    tint = Gray400,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                BasicTextField(
                    value         = batchId,
                    onValueChange = { batchId = it },
                    modifier      = Modifier.weight(1f),
                    textStyle     = TextStyle(
                        fontFamily = Poppins,
                        fontSize   = 14.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    ),
                    cursorBrush = SolidColor(Green800),
                    decorationBox = { inner ->
                        if (batchId.isEmpty()) {
                            Text(
                                "Enter batch ID e.g. KS-2024-001",
                                fontFamily = Poppins,
                                fontSize   = 14.sp,
                                color      = Gray400
                            )
                        }
                        inner()
                    }
                )
            }
        }

        // Result
        if (batchId.isNotEmpty()) {
            item {
                if (foundBatch != null) {
                    VerifiedBatchResult(batch = foundBatch)
                } else {
                    NotFoundResult()
                }
            }
        }
    }
}

// ── Verified Batch Result ─────────────────────────────────────────────────────
@Composable
private fun VerifiedBatchResult(batch: TraceBatch) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFDCFCE7))
            .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Verified,
                null,
                tint     = Color(0xFF15803D),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text       = "Verified ✓",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 16.sp,
                color      = Color(0xFF15803D)
            )
        }

        HorizontalDivider(color = Color(0xFF86EFAC), thickness = 0.5.dp)

        VerifyRow("Crop", "${batch.emoji} ${batch.cropNameHindi}")
        VerifyRow("Farmer", batch.farmerName)
        VerifyRow("Farm", batch.farmLocation)
        VerifyRow("Harvested", batch.harvestDate)
        VerifyRow("Quantity", "${batch.quantity} quintals")
        VerifyRow("Grade", batch.grade)
        VerifyRow(
            "Journey",
            "${batch.events.count { it.isCompleted }}/${batch.events.size} steps completed"
        )
    }
}

@Composable
private fun VerifyRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text       = label,
            fontFamily = Poppins,
            fontSize   = 13.sp,
            color      = Color(0xFF166534)
        )
        Text(
            text       = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 13.sp,
            color      = Color(0xFF15803D)
        )
    }
}

@Composable
private fun NotFoundResult() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFEE2E2))
            .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("❌", fontSize = 32.sp)
        Text(
            text       = "Batch not found",
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize   = 15.sp,
            color      = Color(0xFFDC2626)
        )
        Text(
            text       = "Try KS-2024-001 or KS-2024-002",
            fontFamily = Poppins,
            fontSize   = 12.sp,
            color      = Color(0xFFEF4444),
            textAlign  = TextAlign.Center
        )
    }
}

// ── New Batch Tab ─────────────────────────────────────────────────────────────
@Composable
private fun NewBatchTab() {
    var cropName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("A") }
    var isOrganic by remember { mutableStateOf(false) }
    var batchCreated by remember { mutableStateOf(false) }
    var generatedId by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (batchCreated) {
            item { BatchCreatedSuccess(batchId = generatedId) }
        } else {
            item {
                Text(
                    text       = "Create New Batch",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text       = "नई फसल बैच बनाएं",
                    fontFamily = Poppins,
                    fontSize   = 13.sp,
                    color      = Gray400
                )
            }

            // Crop name input
            item {
                BatchInputField(
                    label       = "Crop Name / फसल का नाम",
                    value       = cropName,
                    placeholder = "e.g. Wheat, Tomato, Onion",
                    onValueChange = { cropName = it }
                )
            }

            // Quantity input
            item {
                BatchInputField(
                    label       = "Quantity (quintals) / मात्रा",
                    value       = quantity,
                    placeholder = "e.g. 100",
                    onValueChange = { quantity = it }
                )
            }

            // Grade selector
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text       = "Grade / ग्रेड",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf("A", "B", "C").forEach { g ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (grade == g) Green800 else Gray100
                                    )
                                    .border(
                                        1.dp,
                                        if (grade == g) Green800 else Gray200,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { grade = g }
                                    .padding(
                                        horizontal = 24.dp,
                                        vertical = 10.dp
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text       = "Grade $g",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize   = 13.sp,
                                    color      = if (grade == g) White else Gray600
                                )
                            }
                        }
                    }
                }
            }

            // Organic toggle
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .border(1.dp, Gray200, RoundedCornerShape(14.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text       = "Organic Produce",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp,
                            color      = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text       = "जैविक उत्पाद",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Gray400
                        )
                    }
                    Switch(
                        checked         = isOrganic,
                        onCheckedChange = { isOrganic = it },
                        colors          = SwitchDefaults.colors(
                            checkedThumbColor  = White,
                            checkedTrackColor  = Green800,
                            uncheckedThumbColor = White,
                            uncheckedTrackColor = Gray200
                        )
                    )
                }
            }

            // Create button
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (cropName.isNotEmpty() && quantity.isNotEmpty())
                                Green800 else Gray200
                        )
                        .clickable(
                            enabled = cropName.isNotEmpty() && quantity.isNotEmpty()
                        ) {
                            generatedId = "KS-2024-00${(3..9).random()}"
                            batchCreated = true
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = "🔗 Create Batch & Generate QR",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 15.sp,
                        color      = if (cropName.isNotEmpty() && quantity.isNotEmpty())
                            White else Gray400
                    )
                }
            }
        }
    }
}

// ── Batch Created Success ─────────────────────────────────────────────────────
@Composable
private fun BatchCreatedSuccess(batchId: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Success icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFDCFCE7)),
            contentAlignment = Alignment.Center
        ) {
            Text("✅", fontSize = 36.sp)
        }

        Text(
            text       = "Batch Created!",
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize   = 22.sp,
            color      = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text       = "बैच सफलतापूर्वक बनाई गई",
            fontFamily = Poppins,
            fontSize   = 14.sp,
            color      = Gray400
        )

        // Batch ID card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Green50)
                .border(1.dp, Green100, RoundedCornerShape(16.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text       = "Batch ID",
                fontFamily = Poppins,
                fontSize   = 13.sp,
                color      = Gray400
            )
            Text(
                text       = batchId,
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 24.sp,
                color      = Green800
            )

            Spacer(Modifier.height(8.dp))

            // QR placeholder
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(White)
                    .border(1.dp, Gray200, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.QrCode,
                        null,
                        tint     = Green800,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text       = "QR Code",
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        color      = Gray400
                    )
                }
            }

            Text(
                text       = "Scan to verify produce authenticity",
                fontFamily = Poppins,
                fontSize   = 12.sp,
                color      = Gray600,
                textAlign  = TextAlign.Center
            )
        }

        // Share button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Green800)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Filled.Share, null,
                    tint     = White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text       = "Share QR Code",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 15.sp,
                    color      = White
                )
            }
        }
    }
}

// ── Small Helpers ─────────────────────────────────────────────────────────────
@Composable
private fun TraceStatCard(emoji: String, value: String, label: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Green50)
            .border(1.dp, Green100, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 18.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            text       = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = Green800
        )
        Text(
            text       = label,
            fontFamily = Poppins,
            fontSize   = 10.sp,
            color      = Gray400
        )
    }
}

@Composable
private fun BatchInfoItem(emoji: String, text: String) {
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

@Composable
private fun TraceBadge(text: String, bg: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text       = text,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize   = 11.sp,
            color      = textColor
        )
    }
}

@Composable
private fun BatchInputField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text       = label,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 14.sp,
            color      = MaterialTheme.colorScheme.onBackground
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value         = value,
                onValueChange = onValueChange,
                modifier      = Modifier.fillMaxWidth(),
                textStyle     = TextStyle(
                    fontFamily = Poppins,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                ),
                cursorBrush = SolidColor(Green800),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text       = placeholder,
                            fontFamily = Poppins,
                            fontSize   = 14.sp,
                            color      = Gray400
                        )
                    }
                    inner()
                }
            )
        }
    }
}