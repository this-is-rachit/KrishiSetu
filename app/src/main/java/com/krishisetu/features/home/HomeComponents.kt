package com.krishisetu.features.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.model.MandiPrice
import com.krishisetu.model.QuickAction
import com.krishisetu.model.WeatherInfo
import com.krishisetu.ui.theme.*

// ── TOP HEADER ───────────────────────────────────────────────
@Composable
fun HomeHeader(
    farmerName: String,
    location: String,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "नमस्ते, $farmerName 👋",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "📍 ",
                    fontSize = 12.sp
                )
                Text(
                    text = location,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Notification bell
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Green50)
                .border(1.dp, Green100, CircleShape)
                .clickable { onNotificationClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = Green800,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ── WEATHER STRIP ────────────────────────────────────────────
@Composable
fun WeatherStrip(weather: WeatherInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Green50)
            .border(1.dp, Green100, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("⛅", fontSize = 28.sp)
            Column {
                Text(
                    text = "${weather.temperature}°C · ${weather.condition}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Green900
                )
                Text(
                    text = weather.location,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = Green700
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(
                imageVector = Icons.Filled.WaterDrop,
                contentDescription = "Humidity",
                tint = Green600,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "${weather.humidity}%",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Green800
            )
        }
    }
}

// ── SECTION HEADER ───────────────────────────────────────────
@Composable
fun SectionHeader(title: String, subtitle: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TextButton(onClick = onSeeAllClick) {
            Text(
                text = "See All →",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Green800
            )
        }
    }
}

// ── MANDI PRICE CARD ─────────────────────────────────────────
@Composable
fun MandiPriceCard(price: MandiPrice) {
    val priceColor by animateColorAsState(
        targetValue = if (price.isUp) Color(0xFF16A34A) else ErrorRed,
        animationSpec = tween(300),
        label = "priceColor"
    )

    Card(
        modifier = Modifier.width(170.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Green100)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Emoji + crop name row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Green50),
                    contentAlignment = Alignment.Center
                ) {
                    Text(price.emoji, fontSize = 20.sp)
                }
                Column {
                    Text(
                        text = price.cropName,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = price.cropNameHindi,
                        fontFamily = Poppins,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Divider
            HorizontalDivider(color = Green100, thickness = 0.8.dp)

            // Price
            Text(
                text = "₹${price.modalPrice}",
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = price.unit,
                fontFamily = Poppins,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Change badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (price.isUp) Color(0xFFDCFCE7) else Color(0xFFFFE4E4))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = if (price.isUp) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = priceColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "₹${Math.abs(price.priceChange)}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = priceColor
                )
            }

            // Mandi name
            Text(
                text = price.mandiName,
                fontFamily = Poppins,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ── QUICK ACTION BUTTON ───────────────────────────────────────
@Composable
fun QuickActionButton(action: QuickAction, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Green50)
                .border(1.dp, Green100, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(action.emoji, fontSize = 24.sp)
        }
        Text(
            text = action.label,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ── MSP BANNER ───────────────────────────────────────────────
@Composable
fun MspBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
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
                    text = "MSP 2024-25",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = White
                )
                Text(
                    text = "Wheat: ₹2,275/quintal",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Green100
                )
                Text(
                    text = "Govt. guaranteed minimum",
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    color = Green400
                )
            }
            Text("🏛️", fontSize = 40.sp)
        }
    }
}