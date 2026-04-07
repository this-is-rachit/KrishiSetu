package com.krishisetu.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

object LocationHelper {

    @SuppressLint("MissingPermission")
    suspend fun detectLocation(context: Context): String = withContext(Dispatchers.IO) {
        try {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val providers = listOf(
                LocationManager.NETWORK_PROVIDER,
                LocationManager.GPS_PROVIDER
            )

            var best: android.location.Location? = null
            for (provider in providers) {
                if (!lm.isProviderEnabled(provider)) continue
                val loc = lm.getLastKnownLocation(provider) ?: continue
                if (best == null || loc.accuracy < best.accuracy) best = loc
            }

            if (best != null) {
                reverseGeocode(context, best.latitude, best.longitude)
            } else {
                "Lucknow, UP"
            }
        } catch (e: Exception) {
            "Lucknow, UP"
        }
    }

    private fun reverseGeocode(context: Context, lat: Double, lon: Double): String {
        return try {
            @Suppress("DEPRECATION")
            val addresses = Geocoder(context, Locale.getDefault())
                .getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val a     = addresses[0]
                val city  = a.locality ?: a.subAdminArea ?: a.adminArea ?: "Unknown"
                val state = a.adminArea ?: ""
                if (state.isNotEmpty() && state != city) "$city, $state" else city
            } else "Lucknow, UP"
        } catch (e: Exception) {
            "Lucknow, UP"
        }
    }
}