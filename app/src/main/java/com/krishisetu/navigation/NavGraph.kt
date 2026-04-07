package com.krishisetu.navigation

import com.krishisetu.features.fpohub.FpoHubScreen
import com.krishisetu.features.exportportal.ExportPortalScreen
import com.krishisetu.features.coldchain.ColdChainScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.krishisetu.features.home.HomeScreen
import com.krishisetu.features.marketplace.MarketplaceScreen
import com.krishisetu.features.processing.ProcessingScreen
import com.krishisetu.features.traceability.TraceabilityScreen
import com.krishisetu.features.profile.ProfileScreen
import com.krishisetu.features.krishiai.KrishiAIScreen
import com.krishisetu.features.auth.AuthScreen
@Composable
fun KrishiNavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = NavRoutes.Auth.route
    ) {
        composable(NavRoutes.Home.route)         { HomeScreen(navController = navController) }
        composable(NavRoutes.Marketplace.route)  { MarketplaceScreen(navController = navController) }
        composable(NavRoutes.Processing.route)   { ProcessingScreen() }
        composable(NavRoutes.Traceability.route) { TraceabilityScreen() }
        composable(NavRoutes.Profile.route)      { ProfileScreen() }
        composable(NavRoutes.KrishiAI.route)     { KrishiAIScreen() }
        composable(NavRoutes.ColdChain.route) { ColdChainScreen() }
        composable(NavRoutes.ExportPortal.route) { ExportPortalScreen() }
        composable(NavRoutes.FpoHub.route) { FpoHubScreen() }
        composable(NavRoutes.Auth.route) { AuthScreen(navController = navController) }
    }
}