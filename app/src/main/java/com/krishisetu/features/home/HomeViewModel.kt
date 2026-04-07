package com.krishisetu.features.home
import com.krishisetu.BuildConfig
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishisetu.AppState
import com.krishisetu.data.fake.fakeMandiPrices
import com.krishisetu.data.fake.fakeQuickActions
import com.krishisetu.data.network.LocationHelper
import com.krishisetu.model.FarmerProfile
import com.krishisetu.model.MandiPrice
import com.krishisetu.model.QuickAction
import com.krishisetu.model.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val farmer: FarmerProfile          = FarmerProfile(),
    val weather: WeatherInfo           = WeatherInfo(),
    val mandiPrices: List<MandiPrice>  = emptyList(),
    val quickActions: List<QuickAction> = emptyList(),
    val isLoading: Boolean             = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        android.util.Log.d("KRISHI_DEBUG", "=== APP STARTED ===")
        android.util.Log.d("KRISHI_DEBUG", "Gemini key length: ${BuildConfig.GEMINI_API_KEY.length}")
        android.util.Log.d("KRISHI_DEBUG", "Gemini key first 5 chars: ${BuildConfig.GEMINI_API_KEY.take(5)}")
        android.util.Log.d("KRISHI_DEBUG", "Sarvam key length: ${BuildConfig.SARVAM_API_KEY.length}")
        loadData()
        refreshLocation()
    }

    private fun loadData() {
        val location = AppState.userLocation
        _uiState.value = HomeUiState(
            farmer       = FarmerProfile(name = "Rachit", location = location),
            weather      = WeatherInfo(
                temperature = 28,
                condition   = "Partly Cloudy",
                humidity    = 65,
                location    = location
            ),
            mandiPrices  = fakeMandiPrices,
            quickActions = fakeQuickActions,
            isLoading    = false
        )
    }

    private fun refreshLocation() {
        viewModelScope.launch {
            val location = LocationHelper.detectLocation(context)
            AppState.userLocation = location
            _uiState.value = _uiState.value.copy(
                farmer  = _uiState.value.farmer.copy(location = location),
                weather = _uiState.value.weather.copy(location = location)
            )
        }
    }
}