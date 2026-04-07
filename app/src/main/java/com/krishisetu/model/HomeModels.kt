package com.krishisetu.model

data class FarmerProfile(
    val name: String = "Rachit",
    val location: String = "Lucknow, UP",
    val cropCount: Int = 3
)

data class MandiPrice(
    val cropName: String,
    val cropNameHindi: String,
    val mandiName: String,
    val modalPrice: Int,
    val previousPrice: Int,
    val unit: String = "₹/quintal",
    val emoji: String
) {
    val priceChange: Int get() = modalPrice - previousPrice
    val isUp: Boolean get() = priceChange >= 0
}

data class QuickAction(
    val label: String,
    val labelHindi: String,
    val emoji: String,
    val route: String
)

data class WeatherInfo(
    val temperature: Int = 28,
    val condition: String = "Partly Cloudy",
    val humidity: Int = 65,
    val location: String = "Lucknow"
)

data class Listing(
    val id: String,
    val sellerName: String,
    val sellerLocation: String,
    val cropName: String,
    val cropNameHindi: String,
    val variety: String,
    val quantity: Int,
    val pricePerQuintal: Int,
    val grade: String,
    val isOrganic: Boolean,
    val isExportReady: Boolean,
    val postedHoursAgo: Int,
    val emoji: String,
    val totalBids: Int
)

data class ProcessingEquipment(
    val id: String,
    val name: String,
    val nameHindi: String,
    val category: String,
    val emoji: String,
    val buyPrice: Int,
    val rentPerDay: Int,
    val powerKW: Double,
    val district: String,
    val isAvailable: Boolean,
    val rating: Float,
    val suitableFor: List<String>
)

data class ProcessingGuide(
    val id: String,
    val title: String,
    val titleHindi: String,
    val emoji: String,
    val category: String,
    val valueMultiplier: String,
    val steps: Int,
    val durationMins: Int
)

data class TraceBatch(
    val batchId: String,
    val cropName: String,
    val cropNameHindi: String,
    val emoji: String,
    val farmerName: String,
    val farmLocation: String,
    val harvestDate: String,
    val quantity: Int,
    val grade: String,
    val isOrganic: Boolean,
    val events: List<TraceEvent>
)

data class TraceEvent(
    val eventType: String,
    val eventTypeHindi: String,
    val actorName: String,
    val location: String,
    val timestamp: String,
    val emoji: String,
    val isCompleted: Boolean
)

data class FarmerCrop(
    val name: String,
    val nameHindi: String,
    val emoji: String,
    val areaSown: Double,    // in acres
    val season: String,
    val sowingDate: String
)

data class FarmerStats(
    val totalSales: String,
    val batchesCreated: Int,
    val activeListings: Int,
    val savedAmount: String
)

data class SettingItem(
    val label: String,
    val labelHindi: String,
    val emoji: String,
    val hasToggle: Boolean = false,
    val toggleValue: Boolean = false
)

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: String,
    val isVoice: Boolean = false
)

data class SuggestedQuery(
    val text: String,
    val textHindi: String,
    val emoji: String
)

data class ColdStorage(
    val id: String,
    val name: String,
    val district: String,
    val state: String,
    val distanceKm: Double,
    val totalCapacityMT: Int,
    val availableSlotsMT: Int,
    val pricePerMTPerDay: Int,
    val minTempCelsius: Int,
    val maxTempCelsius: Int,
    val currentTempCelsius: Double,
    val currentHumidity: Int,
    val rating: Float,
    val isNHBCertified: Boolean,
    val supportedCrops: List<String>,
    val emoji: String
)

data class StorageBooking(
    val id: String,
    val storageName: String,
    val cropName: String,
    val cropEmoji: String,
    val quantityMT: Int,
    val fromDate: String,
    val toDate: String,
    val totalCost: Int,
    val status: String,
    val currentTemp: Double,
    val isAlertActive: Boolean
)

// ── EXPORT PORTAL ─────────────────────────────────────────────
data class ExportDocument(
    val id: String,
    val name: String,
    val nameHindi: String,
    val emoji: String,
    val issuingBody: String,
    val isCompleted: Boolean,
    val isMandatory: Boolean,
    val description: String
)

data class GlobalBuyer(
    val id: String,
    val companyName: String,
    val country: String,
    val countryFlag: String,
    val interestedIn: List<String>,
    val minOrderMT: Int,
    val priceUSD: Int,
    val isVerified: Boolean,
    val contactEmail: String
)

data class ComplianceRule(
    val country: String,
    val countryFlag: String,
    val crop: String,
    val mrlLimit: String,
    val status: String,
    val notes: String
)

// ── FPO HUB ───────────────────────────────────────────────────
data class FpoMember(
    val id: String,
    val name: String,
    val village: String,
    val landHoldingAcres: Double,
    val shareCount: Int,
    val joinedDate: String,
    val emoji: String,
    val contributedMT: Double
)

data class PooledProduce(
    val id: String,
    val cropName: String,
    val cropNameHindi: String,
    val emoji: String,
    val totalQuantityMT: Double,
    val targetQuantityMT: Double,
    val contributorsCount: Int,
    val expectedPricePerMT: Int,
    val currentMandiPricePerMT: Int,
    val closingDate: String,
    val grade: String
)

data class FpoListing(
    val id: String,
    val cropName: String,
    val cropNameHindi: String,
    val emoji: String,
    val quantityMT: Double,
    val askPricePerMT: Int,
    val grade: String,
    val isExportReady: Boolean,
    val totalBids: Int,
    val highestBidPerMT: Int,
    val closingDate: String
)

data class FpoStats(
    val totalMembers: Int,
    val totalLandAcres: Double,
    val totalTradedCrore: String,
    val activePools: Int
)

// ── AUTH ──────────────────────────────────────────────────────
data class UserRole(
    val id: String,
    val label: String,
    val labelHindi: String,
    val emoji: String,
    val description: String
)