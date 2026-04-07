package com.krishisetu.data.model

data class Listing(
    val id: String,
    val sellerName: String,
    val sellerLocation: String,
    val cropName: String,
    val cropNameHindi: String,
    val variety: String,
    val quantity: Int,        // quintals
    val pricePerQuintal: Int, // ₹
    val grade: String,        // A, B, C
    val isOrganic: Boolean,
    val isExportReady: Boolean,
    val postedHoursAgo: Int,
    val emoji: String,
    val totalBids: Int
)