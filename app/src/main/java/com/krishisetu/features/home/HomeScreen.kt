package com.krishisetu.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 8.dp,
            bottom = 120.dp)
    ) {
        // ── Header
        item {
            Spacer(Modifier.height(8.dp))
            HomeHeader(
                farmerName = state.farmer.name,
                location   = state.farmer.location,
                onNotificationClick = {}
            )
        }

        // ── Weather
        item {
            WeatherStrip(weather = state.weather)
        }

        // ── Quick Actions
        item {
            SectionHeader(
                title    = "Quick Actions",
                subtitle = "तेज़ शॉर्टकट",
                onSeeAllClick = {}
            )
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.quickActions) { action ->
                    QuickActionButton(action = action) {
                        navController.navigate(action.route)
                    }
                }
            }
        }

        // ── MSP Banner
        item {
            MspBanner()
        }

        // ── Mandi Prices
        item {
            SectionHeader(
                title    = "Today's Mandi Prices",
                subtitle = "आज के मंडी भाव",
                onSeeAllClick = {}
            )
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical   = 8.dp    // ← ADD vertical padding
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.mandiPrices) { price ->
                    MandiPriceCard(price = price)
                }
            }
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
    }
}