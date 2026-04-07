package com.krishisetu.features.coldchain

import androidx.compose.animation.core.*
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
import com.krishisetu.data.fake.fakeActiveBookings
import com.krishisetu.data.fake.fakeColdStorages
import com.krishisetu.model.ColdStorage
import com.krishisetu.model.StorageBooking
import com.krishisetu.ui.theme.*

@Composable
fun ColdChainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Find Storage", "My Bookings", "IoT Monitor")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ColdChainHeader()
        ColdChainTabRow(
            selectedTab   = selectedTab,
            tabs          = tabs,
            onTabSelected = { selectedTab = it }
        )
        when (selectedTab) {
            0 -> FindStorageTab()
            1 -> MyBookingsTab()
            2 -> IoTMonitorTab()
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun ColdChainHeader() {
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
                text       = "Cold Chain",
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 26.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "फसल ताज़ा, नुकसान कम",
                fontFamily = Poppins,
                fontSize   = 13.sp,
                color      = Gray400
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFF6FF))
                .border(1.dp, Color(0xFFBFDBFE), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("❄️", fontSize = 20.sp)
        }
    }
}

// ── Tab Row ───────────────────────────────────────────────────────────────────
@Composable
private fun ColdChainTabRow(
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

// ── Find Storage Tab ──────────────────────────────────────────────────────────
@Composable
private fun FindStorageTab() {
    val cropFilters = listOf("All", "Potato", "Onion", "Mango", "Tomato")
    var selectedCrop by remember { mutableStateOf("All") }

    val filtered = if (selectedCrop == "All") fakeColdStorages
    else fakeColdStorages.filter { storage ->
        storage.supportedCrops.any {
            it.contains(selectedCrop, ignoreCase = true) || it == "All Crops"
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Stats
        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ColdStatCard("🏭", "240+",  "Storages",   Modifier.weight(1f))
                ColdStatCard("📦", "12K MT","Available",  Modifier.weight(1f))
                ColdStatCard("💰", "₹6-10", "Per MT/Day", Modifier.weight(1f))
            }
            Spacer(Modifier.height(4.dp))
        }

        // Filter chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cropFilters) { crop ->
                    ColdChip(
                        label    = crop,
                        selected = selectedCrop == crop,
                        onClick  = { selectedCrop = crop }
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
        }

        // Storage cards
        items(filtered, key = { it.id }) { storage ->
            StorageCard(storage = storage)
        }
    }
}

// ── Storage Card ──────────────────────────────────────────────────────────────
@Composable
private fun StorageCard(storage: ColdStorage) {
    var bookingRequested by remember { mutableStateOf(false) }
    val isFull = storage.availableSlotsMT == 0

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp,
                if (bookingRequested) Color(0xFF3B82F6) else Gray200,
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
                        .background(Color(0xFFEFF6FF))
                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(storage.emoji, fontSize = 26.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text       = storage.name,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 15.sp,
                            color      = MaterialTheme.colorScheme.onBackground
                        )
                        if (storage.isNHBCertified) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text       = "NHB",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 9.sp,
                                    color      = Color(0xFF15803D)
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.LocationOn, null,
                            tint     = Gray400,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text       = "${storage.district}, ${storage.state}",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Gray400
                        )
                        Text("·", color = Gray400, fontSize = 12.sp)
                        Text(
                            text       = "${storage.distanceKm} km",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Green700
                        )
                    }
                }
            }

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("⭐", fontSize = 12.sp)
                Text(
                    text       = "${storage.rating}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = Gray200, thickness = 0.5.dp)
        Spacer(Modifier.height(12.dp))

        // Temp + Humidity + Capacity row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StorageInfoItem(
                emoji = "🌡️",
                label = "Temp Range",
                value = "${storage.minTempCelsius}°C to ${storage.maxTempCelsius}°C"
            )
            StorageInfoItem(
                emoji = "💧",
                label = "Humidity",
                value = "${storage.currentHumidity}%"
            )
            StorageInfoItem(
                emoji = "📦",
                label = "Available",
                value = if (isFull) "Full" else "${storage.availableSlotsMT} MT"
            )
        }

        Spacer(Modifier.height(12.dp))

        // Capacity bar
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text       = "Capacity Used",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
                val usedPercent = ((storage.totalCapacityMT - storage.availableSlotsMT)
                    .toFloat() / storage.totalCapacityMT * 100).toInt()
                Text(
                    text       = "$usedPercent%",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 11.sp,
                    color      = if (isFull) ErrorRed else Green800
                )
            }
            Spacer(Modifier.height(4.dp))
            val progress = (storage.totalCapacityMT - storage.availableSlotsMT)
                .toFloat() / storage.totalCapacityMT
            LinearProgressIndicator(
                progress       = { progress },
                modifier       = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color          = if (isFull) ErrorRed else Color(0xFF3B82F6),
                trackColor     = Color(0xFFDBEAFE)
            )
        }

        Spacer(Modifier.height(12.dp))

        // Supported crops
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(storage.supportedCrops) { crop ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFEFF6FF))
                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(50.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = crop,
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        color      = Color(0xFF1D4ED8)
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Price + Book row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text       = "₹${storage.pricePerMTPerDay}",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 22.sp,
                        color      = Color(0xFF1D4ED8)
                    )
                    Text(
                        text       = "/MT/day",
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        color      = Gray400,
                        modifier   = Modifier.padding(bottom = 2.dp, start = 3.dp)
                    )
                }
                Text(
                    text       = "Total ${storage.totalCapacityMT} MT capacity",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFEFF6FF))
                        .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Phone, null,
                        tint     = Color(0xFF1D4ED8),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when {
                                isFull         -> Gray200
                                bookingRequested -> Color(0xFFDCFCE7)
                                else           -> Color(0xFF1D4ED8)
                            }
                        )
                        .clickable(enabled = !isFull) {
                            bookingRequested = !bookingRequested
                        }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                ) {
                    Text(
                        text = when {
                            isFull           -> "Storage Full"
                            bookingRequested -> "✓ Requested"
                            else             -> "Book Slot"
                        },
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 13.sp,
                        color      = when {
                            isFull           -> Gray400
                            bookingRequested -> Color(0xFF15803D)
                            else             -> White
                        }
                    )
                }
            }
        }
    }
}

// ── My Bookings Tab ───────────────────────────────────────────────────────────
@Composable
private fun MyBookingsTab() {
    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Alert banner if any active alert
        val hasAlert = fakeActiveBookings.any { it.isAlertActive }
        if (hasAlert) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFEF3C7))
                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("⚠️", fontSize = 24.sp)
                    Column {
                        Text(
                            text       = "Temperature Alert!",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 14.sp,
                            color      = Color(0xFF92400E)
                        )
                        Text(
                            text       = "Kisaan Cold Hub: Temp above threshold (8.9°C)",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Color(0xFF78350F)
                        )
                    }
                }
            }
        }

        items(fakeActiveBookings, key = { it.id }) { booking ->
            BookingCard(booking = booking)
        }

        // Past bookings hint
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Gray100)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "📋 View past bookings →",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 14.sp,
                    color      = Gray600
                )
            }
        }
    }
}

// ── Booking Card ──────────────────────────────────────────────────────────────
@Composable
private fun BookingCard(booking: StorageBooking) {
    val isAlert = booking.isAlertActive
    val borderColor = if (isAlert) Color(0xFFF59E0B) else Gray200

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(booking.cropEmoji, fontSize = 28.sp)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text       = booking.cropName,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = booking.storageName,
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Gray400,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isAlert) Color(0xFFFEF3C7) else Color(0xFFDCFCE7)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text       = if (isAlert) "⚠️ Alert" else "✅ ${booking.status}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 11.sp,
                    color      = if (isAlert) Color(0xFF92400E) else Color(0xFF15803D)
                )
            }
        }

        HorizontalDivider(color = Gray200, thickness = 0.5.dp)

        // Details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BookingDetailItem("📅", "From", booking.fromDate)
            BookingDetailItem("📅", "To", booking.toDate)
            BookingDetailItem("⚖️", "Qty", "${booking.quantityMT} MT")
        }

        // Live temp card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isAlert) Color(0xFFFFF7ED) else Color(0xFFEFF6FF)
                )
                .border(
                    1.dp,
                    if (isAlert) Color(0xFFFDE68A) else Color(0xFFBFDBFE),
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text       = "Live Temperature",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = if (isAlert) Color(0xFF92400E) else Color(0xFF1D4ED8)
                )
                Text(
                    text       = "${booking.currentTemp}°C",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 24.sp,
                    color      = if (isAlert) Color(0xFFF59E0B) else Color(0xFF1D4ED8)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = "Total Cost",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
                Text(
                    text       = "₹${booking.totalCost}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    color      = Green800
                )
            }
        }
    }
}

// ── IoT Monitor Tab ───────────────────────────────────────────────────────────
@Composable
private fun IoTMonitorTab() {
    // Animated temperature values
    val infiniteTransition = rememberInfiniteTransition(label = "iot")
    val tempOffset by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 0.3f,
        animationSpec = infiniteRepeatable(
            animation  = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "temp"
    )

    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text       = "Live IoT Dashboard",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "रियल-टाइम तापमान निगरानी",
                fontFamily = Poppins,
                fontSize   = 13.sp,
                color      = Gray400
            )
        }

        // AgroFreeze card
        item {
            IoTStorageCard(
                name        = "AgroFreeze Lucknow",
                emoji       = "🏭",
                currentTemp = 4.5 + tempOffset,
                humidity    = 88,
                minTemp     = 2,
                maxTemp     = 8,
                isAlert     = false,
                lastUpdated = "2 min ago"
            )
        }

        // Kisaan Cold Hub — alert state
        item {
            IoTStorageCard(
                name        = "Kisaan Cold Hub",
                emoji       = "❄️",
                currentTemp = 8.9 + tempOffset,
                humidity    = 92,
                minTemp     = 0,
                maxTemp     = 5,
                isAlert     = true,
                lastUpdated = "Just now"
            )
        }

        // Tips card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Green50)
                    .border(1.dp, Green100, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text       = "💡 Storage Tips",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = Green800
                )
                listOf(
                    "Potato: 4-8°C, 85-95% humidity",
                    "Onion: 0-5°C, dry conditions",
                    "Mango: 8-12°C, 85-90% humidity",
                    "Tomato: 8-12°C, 90% humidity"
                ).forEach { tip ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Green800)
                        )
                        Text(
                            text       = tip,
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Green700
                        )
                    }
                }
            }
        }
    }
}

// ── IoT Storage Card ──────────────────────────────────────────────────────────
@Composable
private fun IoTStorageCard(
    name: String,
    emoji: String,
    currentTemp: Double,
    humidity: Int,
    minTemp: Int,
    maxTemp: Int,
    isAlert: Boolean,
    lastUpdated: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.5.dp,
                if (isAlert) Color(0xFFF59E0B) else Color(0xFFBFDBFE),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(emoji, fontSize = 24.sp)
                Column {
                    Text(
                        text       = name,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 14.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = "Updated: $lastUpdated",
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        color      = Gray400
                    )
                }
            }

            // Live indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            if (isAlert) Color(0xFFF59E0B) else Color(0xFF22C55E)
                        )
                )
                Text(
                    text       = if (isAlert) "Alert" else "Live",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 11.sp,
                    color      = if (isAlert) Color(0xFFF59E0B) else Color(0xFF22C55E)
                )
            }
        }

        // Big temp display
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text       = String.format("%.1f°C", currentTemp),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 36.sp,
                    color      = if (isAlert) Color(0xFFF59E0B)
                    else Color(0xFF1D4ED8)
                )
                Text(
                    text       = "Temperature",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Gray400
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(60.dp)
                    .background(Gray200)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text       = "$humidity%",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 36.sp,
                    color      = Color(0xFF0891B2)
                )
                Text(
                    text       = "Humidity",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Gray400
                )
            }
        }

        // Temp range bar
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text       = "Safe Range: ${minTemp}°C - ${maxTemp}°C",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
                if (isAlert) {
                    Text(
                        text       = "⚠️ Above limit!",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 11.sp,
                        color      = Color(0xFFF59E0B)
                    )
                }
            }
            val tempRange = (maxTemp - minTemp).toFloat()
            val progress = ((currentTemp - minTemp) / tempRange)
                .coerceIn(0.0, 1.0).toFloat()
            LinearProgressIndicator(
                progress   = { progress },
                modifier   = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color      = if (isAlert) Color(0xFFF59E0B) else Color(0xFF1D4ED8),
                trackColor = Color(0xFFDBEAFE)
            )
        }
    }
}

// ── Small Helpers ─────────────────────────────────────────────────────────────
@Composable
private fun ColdStatCard(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEFF6FF))
            .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 18.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            text       = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize   = 13.sp,
            color      = Color(0xFF1D4ED8)
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
private fun ColdChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(if (selected) Color(0xFF1D4ED8) else Gray100)
            .border(
                1.dp,
                if (selected) Color(0xFF1D4ED8) else Gray200,
                RoundedCornerShape(50.dp)
            )
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
private fun StorageInfoItem(emoji: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 14.sp)
        Text(
            text       = label,
            fontFamily = Poppins,
            fontSize   = 10.sp,
            color      = Gray400
        )
        Text(
            text       = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 12.sp,
            color      = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun BookingDetailItem(emoji: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 14.sp)
        Text(
            text       = label,
            fontFamily = Poppins,
            fontSize   = 10.sp,
            color      = Gray400
        )
        Text(
            text       = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 12.sp,
            color      = MaterialTheme.colorScheme.onBackground
        )
    }
}