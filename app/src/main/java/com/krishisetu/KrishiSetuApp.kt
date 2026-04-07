package com.krishisetu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.krishisetu.navigation.KrishiBottomBar
import com.krishisetu.navigation.KrishiNavGraph
import com.krishisetu.navigation.NavRoutes

@Composable
fun KrishiSetuApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Screens where bottom bar should NOT show
    val noBottomBarRoutes = listOf(
        NavRoutes.Auth.route,
        NavRoutes.KrishiAI.route,
        NavRoutes.ColdChain.route,
        NavRoutes.ExportPortal.route,
        NavRoutes.FpoHub.route
    )

    val showBottomBar = currentRoute !in noBottomBarRoutes

    Scaffold(
        modifier  = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                KrishiBottomBar(navController = navController)
            }
        }
    ) { _ ->
        KrishiNavGraph(navController = navController)
    }
}