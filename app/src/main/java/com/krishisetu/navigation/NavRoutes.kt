package com.krishisetu.navigation

sealed class NavRoutes(val route: String) {

    // Bottom nav tabs
    data object Home         : NavRoutes("home")
    data object Marketplace  : NavRoutes("marketplace")
    data object Processing   : NavRoutes("processing")
    data object Traceability : NavRoutes("traceability")
    data object Profile      : NavRoutes("profile")

    // Nested screens (added in later steps)
    data object MandiPrices  : NavRoutes("mandi_prices")
    data object ColdChain    : NavRoutes("cold_chain")
    data object ExportPortal : NavRoutes("export_portal")
    data object FpoHub       : NavRoutes("fpo_hub")
    data object KrishiAI     : NavRoutes("krishi_ai")
    data object Auth : NavRoutes("auth")
}