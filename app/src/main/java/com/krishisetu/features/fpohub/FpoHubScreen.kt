package com.krishisetu.features.fpohub

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
import com.krishisetu.data.fake.fakeFpoListings
import com.krishisetu.data.fake.fakeFpoMembers
import com.krishisetu.data.fake.fakeFpoStats
import com.krishisetu.data.fake.fakePooledProduce
import com.krishisetu.model.FpoListing
import com.krishisetu.model.FpoMember
import com.krishisetu.model.PooledProduce
import com.krishisetu.ui.theme.*

@Composable
fun FpoHubScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("My FPO", "Pool Produce", "Collective Market")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FpoHeader()
        FpoTabRow(
            selectedTab   = selectedTab,
            tabs          = tabs,
            onTabSelected = { selectedTab = it }
        )
        when (selectedTab) {
            0 -> MyFpoTab()
            1 -> PoolProduceTab()
            2 -> CollectiveMarketTab()
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun FpoHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF7C3AED))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text       = "FPO Hub",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 26.sp,
                        color      = White
                    )
                    Text(
                        text       = "मिलकर बेचें, ज़्यादा कमाएं",
                        fontFamily = Poppins,
                        fontSize   = 13.sp,
                        color      = Color(0xFFDDD6FE)
                    )
                }
                // FPO badge
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6D28D9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👥", fontSize = 24.sp)
                    }
                    Text(
                        text       = "SFAC Reg.",
                        fontFamily = Poppins,
                        fontSize   = 10.sp,
                        color      = Color(0xFFDDD6FE)
                    )
                }
            }

            // FPO name card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF6D28D9))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text       = "Kisan Samridhi FPO",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 15.sp,
                            color      = White
                        )
                        Text(
                            text       = "Lucknow, Uttar Pradesh · Est. 2023",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Color(0xFFDDD6FE)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF22C55E))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text       = "✓ Active",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 11.sp,
                            color      = White
                        )
                    }
                }
            }
        }
    }
}

// ── Tab Row ───────────────────────────────────────────────────────────────────
@Composable
private fun FpoTabRow(
    selectedTab: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
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
                    fontWeight = if (isSelected) FontWeight.SemiBold
                    else FontWeight.Normal,
                    fontSize   = 11.sp,
                    color      = if (isSelected) Color(0xFF7C3AED) else Gray400
                )
            }
        }
    }
}

// ── My FPO Tab ────────────────────────────────────────────────────────────────
@Composable
private fun MyFpoTab() {
    val stats = fakeFpoStats

    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stats grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FpoStatCard(
                    "👥", "${stats.totalMembers}",
                    "Members", Modifier.weight(1f)
                )
                FpoStatCard(
                    "🌾", "${stats.totalLandAcres}",
                    "Total Acres", Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FpoStatCard(
                    "💰", stats.totalTradedCrore,
                    "Total Traded", Modifier.weight(1f)
                )
                FpoStatCard(
                    "📦", "${stats.activePools}",
                    "Active Pools", Modifier.weight(1f)
                )
            }
        }

        // Benefits banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F3FF))
                    .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text       = "💡 Why sell as FPO?",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 14.sp,
                        color      = Color(0xFF7C3AED)
                    )
                    listOf(
                        "Bulk quantity = better price negotiation",
                        "Access to large buyers & exporters",
                        "Shared logistics cost reduction",
                        "Government scheme benefits for FPOs"
                    ).forEach { benefit ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text  = "✓",
                                color = Color(0xFF7C3AED),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text       = benefit,
                                fontFamily = Poppins,
                                fontSize   = 12.sp,
                                color      = Color(0xFF4C1D95),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // Members section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text       = "Members",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 18.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = "सदस्य सूची",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Gray400
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF7C3AED))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.PersonAdd, null,
                            tint     = White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text       = "Add",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 13.sp,
                            color      = White
                        )
                    }
                }
            }
        }

        items(fakeFpoMembers, key = { it.id }) { member ->
            MemberCard(member = member)
        }

        // View all hint
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray100)
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "View all 48 members →",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 14.sp,
                    color      = Color(0xFF7C3AED)
                )
            }
        }
    }
}

// ── Member Card ───────────────────────────────────────────────────────────────
@Composable
private fun MemberCard(member: FpoMember) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, Gray200, RoundedCornerShape(14.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F3FF))
                    .border(1.dp, Color(0xFFDDD6FE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(member.emoji, fontSize = 22.sp)
            }

            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text       = member.name,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text       = "${member.village} · ${member.landHoldingAcres} acres",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Gray400
                )
                Text(
                    text       = "Joined ${member.joinedDate}",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F3FF))
                    .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text       = "${member.shareCount} shares",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 11.sp,
                    color      = Color(0xFF7C3AED)
                )
            }
            Text(
                text       = "${member.contributedMT} MT",
                fontFamily = Poppins,
                fontSize   = 11.sp,
                color      = Gray400
            )
        }
    }
}

// ── Pool Produce Tab ──────────────────────────────────────────────────────────
@Composable
private fun PoolProduceTab() {
    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Explainer
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF7C3AED))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text       = "Pool Your Produce",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 15.sp,
                            color      = White
                        )
                        Text(
                            text       = "Combine your crop with other members to create a large bulk lot — get prices individual farmers can't negotiate.",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Color(0xFFDDD6FE),
                            lineHeight = 18.sp
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("🤝", fontSize = 40.sp)
                }
            }
        }

        // Section label
        item {
            Text(
                text       = "Active Pools",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "सक्रिय पूल — अभी जुड़ें",
                fontFamily = Poppins,
                fontSize   = 12.sp,
                color      = Gray400
            )
        }

        items(fakePooledProduce, key = { it.id }) { pool ->
            PoolCard(pool = pool)
        }

        // Add to pool button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF7C3AED))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.Add, null,
                        tint     = White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text       = "Add My Produce to a Pool",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 15.sp,
                        color      = White
                    )
                }
            }
        }
    }
}

// ── Pool Card ─────────────────────────────────────────────────────────────────
@Composable
private fun PoolCard(pool: PooledProduce) {
    var joined by remember { mutableStateOf(false) }
    val progress = (pool.totalQuantityMT / pool.targetQuantityMT).toFloat()
        .coerceIn(0f, 1f)
    val premiumPercent = ((pool.expectedPricePerMT - pool.currentMandiPricePerMT)
        .toFloat() / pool.currentMandiPricePerMT * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp,
                if (joined) Color(0xFF7C3AED) else Gray200,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF5F3FF))
                        .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(pool.emoji, fontSize = 26.sp)
                }
                Column {
                    Text(
                        text       = pool.cropNameHindi,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 17.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = "Grade ${pool.grade} · Closes ${pool.closingDate}",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Gray400
                    )
                }
            }

            // Premium badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F3FF))
                    .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text       = "+$premiumPercent%",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 14.sp,
                    color      = Color(0xFF7C3AED)
                )
            }
        }

        // Progress bar
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text       = "${pool.totalQuantityMT} MT collected",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Gray600
                )
                Text(
                    text       = "Target: ${pool.targetQuantityMT} MT",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 12.sp,
                    color      = Color(0xFF7C3AED)
                )
            }
            LinearProgressIndicator(
                progress       = { progress },
                modifier       = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color          = Color(0xFF7C3AED),
                trackColor     = Color(0xFFEDE9FE)
            )
            Text(
                text       = "${pool.contributorsCount} members contributing",
                fontFamily = Poppins,
                fontSize   = 11.sp,
                color      = Gray400
            )
        }

        HorizontalDivider(color = Gray200, thickness = 0.5.dp)

        // Price comparison
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text       = "Mandi Price",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
                Text(
                    text       = "₹${pool.currentMandiPricePerMT}/MT",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = Gray600
                )
            }

            Icon(
                Icons.Filled.ArrowForward, null,
                tint     = Color(0xFF7C3AED),
                modifier = Modifier.size(20.dp)
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = "Pool Price",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
                Text(
                    text       = "₹${pool.expectedPricePerMT}/MT",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 16.sp,
                    color      = Color(0xFF7C3AED)
                )
            }
        }

        // Join button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (joined) Color(0xFFF5F3FF) else Color(0xFF7C3AED)
                )
                .border(
                    1.dp,
                    Color(0xFF7C3AED),
                    RoundedCornerShape(12.dp)
                )
                .clickable { joined = !joined }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = if (joined) "✓ Joined this Pool" else "Join Pool →",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 14.sp,
                color      = if (joined) Color(0xFF7C3AED) else White
            )
        }
    }
}

// ── Collective Market Tab ─────────────────────────────────────────────────────
@Composable
private fun CollectiveMarketTab() {
    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Power banner
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5F3FF))
                    .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💪", fontSize = 32.sp)
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text       = "Collective Bargaining Power",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 14.sp,
                        color      = Color(0xFF4C1D95)
                    )
                    Text(
                        text       = "FPO listings get 15-25% better prices than individual farmers",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Color(0xFF7C3AED),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Section label
        item {
            Text(
                text       = "FPO Listings",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "सामूहिक बिक्री सूची",
                fontFamily = Poppins,
                fontSize   = 12.sp,
                color      = Gray400
            )
        }

        items(fakeFpoListings, key = { it.id }) { listing ->
            FpoListingCard(listing = listing)
        }

        // Create new listing
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF7C3AED))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text       = "Create FPO Listing",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 15.sp,
                        color      = White
                    )
                    Text(
                        text       = "List pooled produce for bulk buyers",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Color(0xFFDDD6FE)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6D28D9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Add, null,
                        tint     = White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

// ── FPO Listing Card ──────────────────────────────────────────────────────────
@Composable
private fun FpoListingCard(listing: FpoListing) {
    var bidPlaced by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp,
                if (bidPlaced) Color(0xFF7C3AED) else Gray200,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF5F3FF))
                        .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(listing.emoji, fontSize = 26.sp)
                }
                Column {
                    Text(
                        text       = listing.cropNameHindi,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 17.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text       = "Grade ${listing.grade} · ${listing.quantityMT} MT",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Gray400
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = "₹${listing.askPricePerMT}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 20.sp,
                    color      = Color(0xFF7C3AED)
                )
                Text(
                    text       = "per MT",
                    fontFamily = Poppins,
                    fontSize   = 10.sp,
                    color      = Gray400
                )
            }
        }

        HorizontalDivider(color = Gray200, thickness = 0.5.dp)

        // Info row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FpoInfoItem("🔥", "${listing.totalBids} bids")
            FpoInfoItem("💰", "Highest: ₹${listing.highestBidPerMT}")
            FpoInfoItem("📅", "Closes ${listing.closingDate}")
        }

        // Badges
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FpoBadge(
                text      = "👥 FPO Verified",
                bg        = Color(0xFFF5F3FF),
                textColor = Color(0xFF7C3AED)
            )
            if (listing.isExportReady) {
                FpoBadge(
                    text      = "✈️ Export Ready",
                    bg        = Color(0xFFEFF6FF),
                    textColor = Color(0xFF1D4ED8)
                )
            }
        }

        // Bid row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // FPO tag
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF5F3FF))
                        .border(1.dp, Color(0xFFDDD6FE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👥", fontSize = 13.sp)
                }
                Text(
                    text       = "Kisan Samridhi FPO",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 12.sp,
                    color      = Gray600,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (bidPlaced) Color(0xFFF5F3FF) else Color(0xFF7C3AED)
                    )
                    .border(1.dp, Color(0xFF7C3AED), RoundedCornerShape(10.dp))
                    .clickable { bidPlaced = !bidPlaced }
                    .padding(horizontal = 16.dp, vertical = 9.dp)
            ) {
                Text(
                    text       = if (bidPlaced) "✓ Bidding" else "Place Bid",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = if (bidPlaced) Color(0xFF7C3AED) else White
                )
            }
        }
    }
}

// ── Small Helpers ─────────────────────────────────────────────────────────────
@Composable
private fun FpoStatCard(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F3FF))
            .border(1.dp, Color(0xFFDDD6FE), RoundedCornerShape(12.dp))
            .padding(vertical = 14.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 20.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            text       = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraBold,
            fontSize   = 18.sp,
            color      = Color(0xFF7C3AED)
        )
        Text(
            text       = label,
            fontFamily = Poppins,
            fontSize   = 11.sp,
            color      = Gray400
        )
    }
}

@Composable
private fun FpoInfoItem(emoji: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(emoji, fontSize = 12.sp)
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
private fun FpoBadge(text: String, bg: Color, textColor: Color) {
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