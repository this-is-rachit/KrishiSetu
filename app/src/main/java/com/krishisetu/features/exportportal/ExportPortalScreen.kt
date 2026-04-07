package com.krishisetu.features.exportportal

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishisetu.data.fake.fakeComplianceRules
import com.krishisetu.data.fake.fakeExportDocuments
import com.krishisetu.data.fake.fakeGlobalBuyers
import com.krishisetu.model.ComplianceRule
import com.krishisetu.model.ExportDocument
import com.krishisetu.model.GlobalBuyer
import com.krishisetu.ui.theme.*

@Composable
fun ExportPortalScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Documents", "Global Buyers", "Compliance")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ExportHeader()
        ExportTabRow(
            selectedTab   = selectedTab,
            tabs          = tabs,
            onTabSelected = { selectedTab = it }
        )
        when (selectedTab) {
            0 -> DocumentsTab()
            1 -> GlobalBuyersTab()
            2 -> ComplianceTab()
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun ExportHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text       = "Export Portal",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 26.sp,
                        color      = White
                    )
                    Text(
                        text       = "भारतीय उपज, वैश्विक बाज़ार",
                        fontFamily = Poppins,
                        fontSize   = 13.sp,
                        color      = Color(0xFF94A3B8)
                    )
                }
                Text("✈️", fontSize = 32.sp)
            }

            Spacer(Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExportStatPill("🌍", "180+", "Countries", Modifier.weight(1f))
                ExportStatPill("💰", "\$45B", "India Exports", Modifier.weight(1f))
                ExportStatPill("📦", "6 Docs", "Required", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ExportStatPill(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E293B))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 16.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            text       = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize   = 13.sp,
            color      = White
        )
        Text(
            text       = label,
            fontFamily = Poppins,
            fontSize   = 10.sp,
            color      = Color(0xFF94A3B8)
        )
    }
}

// ── Tab Row ───────────────────────────────────────────────────────────────────
@Composable
private fun ExportTabRow(
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
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize   = 12.sp,
                    color      = if (isSelected) Color(0xFF0F172A) else Gray400
                )
            }
        }
    }
}

// ── Documents Tab ─────────────────────────────────────────────────────────────
@Composable
private fun DocumentsTab() {
    val completedCount = fakeExportDocuments.count { it.isCompleted }
    val totalCount = fakeExportDocuments.size

    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Progress card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text       = "Export Readiness",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp,
                            color      = White
                        )
                        Text(
                            text       = "निर्यात तैयारी स्कोर",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Color(0xFF94A3B8)
                        )
                    }
                    Text(
                        text       = "$completedCount/$totalCount",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 28.sp,
                        color      = if (completedCount >= 4) Color(0xFF22C55E)
                        else Color(0xFFF59E0B)
                    )
                }

                LinearProgressIndicator(
                    progress   = { completedCount.toFloat() / totalCount },
                    modifier   = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color      = if (completedCount >= 4) Color(0xFF22C55E)
                    else Color(0xFFF59E0B),
                    trackColor = Color(0xFF1E293B)
                )

                Text(
                    text       = if (completedCount >= 4)
                        "✅ You are export ready for basic shipments!"
                    else
                        "⚠️ Complete pending documents to start exporting",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = if (completedCount >= 4) Color(0xFF22C55E)
                    else Color(0xFFF59E0B)
                )
            }
        }

        // Section label
        item {
            Text(
                text       = "Required Documents",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 16.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text       = "आवश्यक दस्तावेज़",
                fontFamily = Poppins,
                fontSize   = 12.sp,
                color      = Gray400
            )
        }

        items(fakeExportDocuments, key = { it.id }) { doc ->
            DocumentCard(doc = doc)
        }
    }
}

// ── Document Card ─────────────────────────────────────────────────────────────
@Composable
private fun DocumentCard(doc: ExportDocument) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp,
                if (doc.isCompleted) Color(0xFF86EFAC) else Gray200,
                RoundedCornerShape(16.dp)
            )
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (doc.isCompleted) Color(0xFFDCFCE7)
                            else Color(0xFFFFF7ED)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(doc.emoji, fontSize = 22.sp)
                }

                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text       = doc.name,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 14.sp,
                            color      = MaterialTheme.colorScheme.onBackground
                        )
                        if (doc.isMandatory) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFEE2E2))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text       = "Required",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 9.sp,
                                    color      = Color(0xFFDC2626)
                                )
                            }
                        }
                    }
                    Text(
                        text       = doc.issuingBody,
                        fontFamily = Poppins,
                        fontSize   = 11.sp,
                        color      = Gray400
                    )
                }
            }

            // Status icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (doc.isCompleted) Color(0xFFDCFCE7) else Gray100
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (doc.isCompleted)
                        Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                    contentDescription = null,
                    tint     = if (doc.isCompleted) Color(0xFF16A34A) else Gray400,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Expanded description
        if (expanded) {
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Gray200, thickness = 0.5.dp)
            Spacer(Modifier.height(12.dp))

            Text(
                text       = doc.description,
                fontFamily = Poppins,
                fontSize   = 13.sp,
                color      = Gray600,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (!doc.isCompleted) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A))
                            .padding(horizontal = 16.dp, vertical = 9.dp)
                    ) {
                        Text(
                            text       = "Apply Now →",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 13.sp,
                            color      = White
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Gray100)
                        .border(1.dp, Gray200, RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                ) {
                    Text(
                        text       = "Learn More",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 13.sp,
                        color      = Gray600
                    )
                }
            }
        }
    }
}

// ── Global Buyers Tab ─────────────────────────────────────────────────────────
@Composable
private fun GlobalBuyersTab() {
    val cropFilters = listOf("All", "Rice", "Onion", "Spices", "Mango")
    var selectedCrop by remember { mutableStateOf("All") }

    val filtered = if (selectedCrop == "All") fakeGlobalBuyers
    else fakeGlobalBuyers.filter { buyer ->
        buyer.interestedIn.any {
            it.contains(selectedCrop, ignoreCase = true)
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Banner
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text       = "Global Buyers",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp,
                            color      = White
                        )
                        Text(
                            text       = "Verified international importers\nready to buy your produce",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Color(0xFF94A3B8),
                            lineHeight = 18.sp
                        )
                    }
                    Text("🌍", fontSize = 36.sp)
                }
            }
        }

        // Filter chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cropFilters) { crop ->
                    ExportChip(
                        label    = crop,
                        selected = selectedCrop == crop,
                        onClick  = { selectedCrop = crop }
                    )
                }
            }
        }

        // Count
        item {
            Text(
                text       = "${filtered.size} buyers interested",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onBackground,
                modifier   = Modifier.padding(horizontal = 20.dp)
            )
        }

        // Buyer cards
        items(filtered, key = { it.id }) { buyer ->
            BuyerCard(buyer = buyer)
        }
    }
}

// ── Buyer Card ────────────────────────────────────────────────────────────────
@Composable
private fun BuyerCard(buyer: GlobalBuyer) {
    var contacted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp,
                if (contacted) Color(0xFF0F172A) else Gray200,
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
                // Flag circle
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Gray100)
                        .border(1.dp, Gray200, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(buyer.countryFlag, fontSize = 28.sp)
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text       = buyer.companyName,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 14.sp,
                            color      = MaterialTheme.colorScheme.onBackground,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis
                        )
                        if (buyer.isVerified) {
                            Icon(
                                Icons.Filled.Verified,
                                null,
                                tint     = Color(0xFF1D4ED8),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text       = buyer.country,
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Gray400
                    )
                }
            }

            // Price
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text       = "\$${buyer.priceUSD}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize   = 20.sp,
                    color      = Color(0xFF0F172A)
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

        // Interested in
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text       = "Interested in:",
                fontFamily = Poppins,
                fontSize   = 11.sp,
                color      = Gray400
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(buyer.interestedIn) { crop ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFF0FDF4))
                            .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(50.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text       = crop,
                            fontFamily = Poppins,
                            fontSize   = 11.sp,
                            color      = Color(0xFF15803D)
                        )
                    }
                }
            }
        }

        // Min order + contact row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text       = "Min. Order",
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400
                )
                Text(
                    text       = "${buyer.minOrderMT} Metric Tons",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Gray100)
                        .border(1.dp, Gray200, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Email, null,
                        tint     = Gray600,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (contacted) Color(0xFFDCFCE7) else Color(0xFF0F172A)
                        )
                        .clickable { contacted = !contacted }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                ) {
                    Text(
                        text       = if (contacted) "✓ Contacted" else "Connect",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 13.sp,
                        color      = if (contacted) Color(0xFF15803D) else White
                    )
                }
            }
        }
    }
}

// ── Compliance Tab ────────────────────────────────────────────────────────────
@Composable
private fun ComplianceTab() {
    val countries = listOf("All", "UAE", "UK", "USA", "Japan", "EU")
    var selectedCountry by remember { mutableStateOf("All") }

    val filtered = if (selectedCountry == "All") fakeComplianceRules
    else fakeComplianceRules.filter {
        it.country.equals(selectedCountry, ignoreCase = true)
    }

    LazyColumn(
        contentPadding = PaddingValues(
            start  = 20.dp,
            end    = 20.dp,
            bottom = 100.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // MRL explanation
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFF7ED))
                    .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(14.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text("💡", fontSize = 20.sp)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text       = "What is MRL?",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 13.sp,
                        color      = Color(0xFF92400E)
                    )
                    Text(
                        text       = "Maximum Residue Limit — the highest amount of pesticide allowed in exported food. Exceeding limits causes shipment rejection.",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = Color(0xFF78350F),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Filter chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(countries) { country ->
                    ExportChip(
                        label    = country,
                        selected = selectedCountry == country,
                        onClick  = { selectedCountry = country }
                    )
                }
            }
        }

        // Legend
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusLegend(Color(0xFF16A34A), "Safe")
                StatusLegend(Color(0xFFF59E0B), "Caution")
                StatusLegend(Color(0xFFDC2626), "Restricted")
            }
        }

        // Compliance cards
        items(filtered) { rule ->
            ComplianceCard(rule = rule)
        }

        // Disclaimer
        item {
            Text(
                text       = "⚠️ Data is indicative. Always verify with APEDA or a licensed export agent before shipping.",
                fontFamily = Poppins,
                fontSize   = 11.sp,
                color      = Gray400,
                lineHeight = 16.sp,
                textAlign  = TextAlign.Center,
                modifier   = Modifier.fillMaxWidth()
            )
        }
    }
}

// ── Compliance Card ───────────────────────────────────────────────────────────
@Composable
private fun ComplianceCard(rule: ComplianceRule) {
    val statusColor = when (rule.status) {
        "Safe"       -> Color(0xFF16A34A)
        "Caution"    -> Color(0xFFF59E0B)
        "Restricted" -> Color(0xFFDC2626)
        else         -> Gray400
    }
    val statusBg = when (rule.status) {
        "Safe"       -> Color(0xFFDCFCE7)
        "Caution"    -> Color(0xFFFEF3C7)
        "Restricted" -> Color(0xFFFEE2E2)
        else         -> Gray100
    }

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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(rule.countryFlag, fontSize = 28.sp)
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text       = "${rule.country} · ${rule.crop}",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text       = "MRL: ${rule.mrlLimit}",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = Gray600
                )
                Text(
                    text       = rule.notes,
                    fontFamily = Poppins,
                    fontSize   = 11.sp,
                    color      = Gray400,
                    lineHeight = 16.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(statusBg)
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text       = rule.status,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 12.sp,
                color      = statusColor
            )
        }
    }
}

// ── Small Helpers ─────────────────────────────────────────────────────────────
@Composable
private fun ExportChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(if (selected) Color(0xFF0F172A) else Gray100)
            .border(
                1.dp,
                if (selected) Color(0xFF0F172A) else Gray200,
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
private fun StatusLegend(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text       = label,
            fontFamily = Poppins,
            fontSize   = 12.sp,
            color      = Gray600
        )
    }
}