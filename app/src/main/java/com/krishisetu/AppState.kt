package com.krishisetu

object AppState {
    var selectedLanguageDisplay: String = "हिंदी"
    var selectedLanguageCode: String    = "hi-IN"
    var userLocation: String            = "Detecting location..."

    private val langMap = mapOf(
        "हिंदी"   to "hi-IN",
        "English" to "en-IN",
        "मराठी"  to "mr-IN",
        "ਪੰਜਾਬੀ" to "pa-IN",
        "বাংলা"  to "bn-IN",
        "తెలుగు" to "te-IN",
        "தமிழ்"  to "ta-IN"
    )

    fun setLanguage(display: String) {
        selectedLanguageDisplay = display
        selectedLanguageCode    = langMap[display] ?: "hi-IN"
    }
}