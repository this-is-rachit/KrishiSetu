package com.krishisetu.features.marketplace

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.krishisetu.data.fake.fakeListings
import com.krishisetu.model.Listing
import com.krishisetu.ui.theme.*

@Composable
fun MarketplaceScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Grains", "Vegetables", "Organic", "Export Ready")

    val filtered = fakeListings.filter { listing ->
        val matchesSearch = searchQuery.isEmpty() ||
                listing.cropName.contains(searchQuery, ignoreCase = true) ||
                listing.cropNameHindi.contains(searchQuery) ||
                listing.sellerName.contains(searchQuery, ignoreCase = true)

        val matchesCategory = when (selectedCategory) {
            "Organic"      -> listing.isOrganic
            "Export Ready" -> listing.isExportReady
            "Vegetables"   -> listing.cropName in listOf("Tomato", "Onion", "Potato")
            "Grains"       -> listing.cropName in listOf("Wheat", "Rice", "Soybean")
            else           -> true
        }
        matchesSearch && matchesCategory
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // Header
            item {
                MarketHeader()
            }

            // Search
            item {
                Spacer(Modifier.height(12.dp))
                MarketSearchBar(query = searchQuery, onQueryChange = { searchQuery = it })
            }

            // Stats
            item {
                Spacer(Modifier.height(16.dp))
                MarketStatsRow()
            }

            // Filter chips
            item {
                Spacer(Modifier.height(16.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        MarketChip(
                            label    = cat,
                            selected = selectedCategory == cat,
                            onClick  = { selectedCategory = cat }
                        )
                    }
                }
            }

            // Count
            item {
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filtered.size}",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = Green800
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "listings available",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Gray400
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            // Listings
            if (filtered.isEmpty()) {
                item { MarketEmptyState() }
            } else {
                items(filtered, key = { it.id }) { listing ->
                    ListingCard(listing = listing)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        // FAB
        ExtendedFloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 24.dp),
            containerColor = Green800,
            contentColor   = White,
            shape = RoundedCornerShape(16.dp),
            icon = {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(20.dp))
            },
            text = {
                Text(
                    text = "फसल बेचें",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        )
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun MarketHeader() {
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
                text = "Marketplace",
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "सीधे किसान से खरीदें",
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = Gray400
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
                imageVector = Icons.Filled.Tune,
                contentDescription = "Filter",
                tint = Green800,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Search Bar ────────────────────────────────────────────────────────────────
@Composable
private fun MarketSearchBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, null, tint = Gray400, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value         = query,
            onValueChange = onQueryChange,
            modifier      = Modifier.weight(1f),
            textStyle     = TextStyle(
                fontFamily = Poppins,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(Green800),
            decorationBox = { inner ->
                if (query.isEmpty()) {
                    Text("Search crop, farmer, location...", fontFamily = Poppins, fontSize = 14.sp, color = Gray400)
                }
                inner()
            }
        )
        if (query.isNotEmpty()) {
            Icon(
                Icons.Filled.Close, null,
                tint = Gray400,
                modifier = Modifier.size(18.dp).clickable { onQueryChange("") }
            )
        }
    }
}

// ── Stats Row ─────────────────────────────────────────────────────────────────
@Composable
private fun MarketStatsRow() {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard("📦", "1,240", "Listings", Modifier.weight(1f))
        StatCard("👨‍🌾", "840",   "Farmers",  Modifier.weight(1f))
        StatCard("✅", "₹4.2Cr","Traded",   Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(emoji: String, value: String, label: String, modifier: Modifier) {
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
        Text(value, fontFamily = Poppins, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Green800)
        Text(label, fontFamily = Poppins, fontSize = 10.sp, color = Gray400)
    }
}

// ── Filter Chip ───────────────────────────────────────────────────────────────
@Composable
private fun MarketChip(label: String, selected: Boolean, onClick: () -> Unit) {
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

// ── Listing Card ──────────────────────────────────────────────────────────────
@Composable
private fun ListingCard(listing: Listing) {
    var bidPlaced by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue    = if (bidPlaced) Green800 else Gray200,
        animationSpec  = tween(300),
        label          = "border"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Top row — emoji + names + price
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
                    Text(listing.emoji, fontSize = 26.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text       = listing.cropNameHindi,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 17.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = "${listing.variety} · Grade ${listing.grade}",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Gray400
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = "₹${listing.pricePerQuintal}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 20.sp,
                    color      = Green800
                )
                Text(
                    text       = "per quintal",
                    fontFamily = Poppins,
                    fontSize   = 10.sp,
                    color      = Gray400
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
            InfoItem("⚖️", "${listing.quantity} qtl")
            InfoItem("📍", listing.sellerLocation)
            InfoItem("🕒", "${listing.postedHoursAgo}h ago")
        }

        Spacer(Modifier.height(12.dp))

        // Badges
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (listing.isOrganic) {
                Badge("🌿 Organic", Color(0xFFDCFCE7), Color(0xFF15803D))
            }
            if (listing.isExportReady) {
                Badge("✈️ Export", Color(0xFFEFF6FF), Color(0xFF1D4ED8))
            }
            Badge(
                "🔥 ${listing.totalBids} bids",
                Color(0xFFFFF7ED),
                Color(0xFFC2410C)
            )
        }

        Spacer(Modifier.height(14.dp))

        // Seller row + actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Seller avatar + name
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Green100),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = listing.sellerName.first().toString(),
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 13.sp,
                        color      = Green800
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text       = listing.sellerName,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 13.sp,
                    color      = Gray600,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Call button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Green50)
                        .border(1.dp, Green100, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Phone, null, tint = Green700, modifier = Modifier.size(16.dp))
                }

                // Bid button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (bidPlaced) Green100 else Green800)
                        .border(1.dp, if (bidPlaced) Green800 else Green800, RoundedCornerShape(10.dp))
                        .clickable { bidPlaced = !bidPlaced }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                ) {
                    Text(
                        text       = if (bidPlaced) "✓ Bidding" else "Place Bid",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 13.sp,
                        color      = if (bidPlaced) Green800 else White
                    )
                }
            }
        }
    }
}

// ── Small helpers ─────────────────────────────────────────────────────────────
@Composable
private fun InfoItem(emoji: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 12.sp)
        Spacer(Modifier.width(4.dp))
        Text(
            text       = text,
            fontFamily = Poppins,
            fontSize   = 11.sp,
            color      = Gray600,
            maxLines   = 1,
            overflow   = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun Badge(text: String, bg: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 11.sp, color = textColor)
    }
}

// ── Empty State ───────────────────────────────────────────────────────────────
@Composable
private fun MarketEmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔍", fontSize = 48.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            text       = "No listings found",
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize   = 18.sp,
            color      = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text       = "Try a different search or filter",
            fontFamily = Poppins,
            fontSize   = 14.sp,
            color      = Gray400
        )
    }
}