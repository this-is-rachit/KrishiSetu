package com.krishisetu.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val labelHindi: String,
    val route: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        label       = "Home",
        labelHindi  = "होम",
        route       = NavRoutes.Home.route,
        icon        = Icons.Filled.Home
    ),
    BottomNavItem(
        label       = "Market",
        labelHindi  = "बाज़ार",
        route       = NavRoutes.Marketplace.route,
        icon        = Icons.Filled.Storefront
    ),
    BottomNavItem(
        label       = "Process",
        labelHindi  = "प्रसंस्करण",
        route       = NavRoutes.Processing.route,
        icon        = Icons.Filled.Factory
    ),
    BottomNavItem(
        label       = "Trace",
        labelHindi  = "ट्रेस",
        route       = NavRoutes.Traceability.route,
        icon        = Icons.Filled.QrCodeScanner
    ),
    BottomNavItem(
        label       = "Profile",
        labelHindi  = "प्रोफ़ाइल",
        route       = NavRoutes.Profile.route,
        icon        = Icons.Filled.Person
    ),
)