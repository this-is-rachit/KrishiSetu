package com.krishisetu.data.fake

import com.krishisetu.model.UserRole
import com.krishisetu.model.FpoMember
import com.krishisetu.model.PooledProduce
import com.krishisetu.model.FpoListing
import com.krishisetu.model.FpoStats
import com.krishisetu.model.ExportDocument
import com.krishisetu.model.GlobalBuyer
import com.krishisetu.model.ComplianceRule
import com.krishisetu.model.ColdStorage
import com.krishisetu.model.StorageBooking
import com.krishisetu.model.ChatMessage
import com.krishisetu.model.SuggestedQuery
import com.krishisetu.model.FarmerCrop
import com.krishisetu.model.FarmerStats
import com.krishisetu.model.SettingItem
import com.krishisetu.model.TraceBatch
import com.krishisetu.model.TraceEvent
import com.krishisetu.model.Listing
import com.krishisetu.model.MandiPrice
import com.krishisetu.model.QuickAction
import com.krishisetu.navigation.NavRoutes
import com.krishisetu.model.ProcessingEquipment
import com.krishisetu.model.ProcessingGuide
val fakeMandiPrices = listOf(
    MandiPrice(
        cropName      = "Wheat",
        cropNameHindi = "गेहूं",
        mandiName     = "Azadpur Mandi",
        modalPrice    = 2150,
        previousPrice = 2100,
        emoji         = "🌾"
    ),
    MandiPrice(
        cropName      = "Tomato",
        cropNameHindi = "टमाटर",
        mandiName     = "Lucknow Mandi",
        modalPrice    = 1840,
        previousPrice = 1900,
        emoji         = "🍅"
    ),
    MandiPrice(
        cropName      = "Onion",
        cropNameHindi = "प्याज़",
        mandiName     = "Lasalgaon Mandi",
        modalPrice    = 2800,
        previousPrice = 2650,
        emoji         = "🧅"
    ),
    MandiPrice(
        cropName      = "Rice",
        cropNameHindi = "चावल",
        mandiName     = "Hapur Mandi",
        modalPrice    = 3200,
        previousPrice = 3175,
        emoji         = "🍚"
    ),
    MandiPrice(
        cropName      = "Potato",
        cropNameHindi = "आलू",
        mandiName     = "Agra Mandi",
        modalPrice    = 1200,
        previousPrice = 1215,
        emoji         = "🥔"
    ),
    MandiPrice(
        cropName      = "Soybean",
        cropNameHindi = "सोयाबीन",
        mandiName     = "Indore Mandi",
        modalPrice    = 4600,
        previousPrice = 4515,
        emoji         = "🫘"
    ),
)

val fakeQuickActions = listOf(
    QuickAction(
        label      = "Mandi",
        labelHindi = "मंडी भाव",
        emoji      = "📊",
        route      = NavRoutes.Home.route
    ),
    QuickAction(
        label      = "Cold Store",
        labelHindi = "कोल्ड स्टोर",
        emoji      = "❄️",
        route      = NavRoutes.ColdChain.route
    ),
    QuickAction(
        label      = "Export",
        labelHindi = "निर्यात",
        emoji      = "✈️",
        route      = NavRoutes.ExportPortal.route
    ),
    QuickAction(
        label      = "Krishi AI",
        labelHindi = "कृषि AI",
        emoji      = "🤖",
        route      = NavRoutes.KrishiAI.route
    ),
)

val fakeListings = listOf(
    Listing(
        id              = "L1",
        sellerName      = "Ramesh Patel",
        sellerLocation  = "Nashik, Maharashtra",
        cropName        = "Onion",
        cropNameHindi   = "प्याज़",
        variety         = "Lal Pyaz",
        quantity        = 200,
        pricePerQuintal = 2750,
        grade           = "A",
        isOrganic       = false,
        isExportReady   = true,
        postedHoursAgo  = 2,
        emoji           = "🧅",
        totalBids       = 7
    ),
    Listing(
        id              = "L2",
        sellerName      = "Sunita Devi",
        sellerLocation  = "Hapur, UP",
        cropName        = "Wheat",
        cropNameHindi   = "गेहूं",
        variety         = "Sharbati",
        quantity        = 500,
        pricePerQuintal = 2100,
        grade           = "A",
        isOrganic       = true,
        isExportReady   = false,
        postedHoursAgo  = 5,
        emoji           = "🌾",
        totalBids       = 3
    ),
    Listing(
        id              = "L3",
        sellerName      = "Gurpreet Singh",
        sellerLocation  = "Amritsar, Punjab",
        cropName        = "Rice",
        cropNameHindi   = "चावल",
        variety         = "Basmati 1121",
        quantity        = 300,
        pricePerQuintal = 3800,
        grade           = "A",
        isOrganic       = false,
        isExportReady   = true,
        postedHoursAgo  = 1,
        emoji           = "🍚",
        totalBids       = 12
    ),
    Listing(
        id              = "L4",
        sellerName      = "Meena Kumari",
        sellerLocation  = "Indore, MP",
        cropName        = "Soybean",
        cropNameHindi   = "सोयाबीन",
        variety         = "JS 335",
        quantity        = 150,
        pricePerQuintal = 4500,
        grade           = "B",
        isOrganic       = false,
        isExportReady   = false,
        postedHoursAgo  = 8,
        emoji           = "🫘",
        totalBids       = 2
    ),
    Listing(
        id              = "L5",
        sellerName      = "Arjun Reddy",
        sellerLocation  = "Kurnool, AP",
        cropName        = "Tomato",
        cropNameHindi   = "टमाटर",
        variety         = "Hybrid",
        quantity        = 80,
        pricePerQuintal = 1800,
        grade           = "A",
        isOrganic       = true,
        isExportReady   = false,
        postedHoursAgo  = 3,
        emoji           = "🍅",
        totalBids       = 5
    ),
    Listing(
        id              = "L6",
        sellerName      = "Harish Sharma",
        sellerLocation  = "Agra, UP",
        cropName        = "Potato",
        cropNameHindi   = "आलू",
        variety         = "Kufri Jyoti",
        quantity        = 400,
        pricePerQuintal = 1150,
        grade           = "B",
        isOrganic       = false,
        isExportReady   = false,
        postedHoursAgo  = 12,
        emoji           = "🥔",
        totalBids       = 1
    ),
)

val fakeEquipment = listOf(
    ProcessingEquipment(
        id           = "E1",
        name         = "Mini Rice Mill",
        nameHindi    = "मिनी राइस मिल",
        category     = "Milling",
        emoji        = "⚙️",
        buyPrice     = 85000,
        rentPerDay   = 800,
        powerKW      = 3.5,
        district     = "Lucknow",
        isAvailable  = true,
        rating       = 4.5f,
        suitableFor  = listOf("Rice", "Wheat")
    ),
    ProcessingEquipment(
        id           = "E2",
        name         = "Cold Press Oil Extractor",
        nameHindi    = "कोल्ड प्रेस तेल मशीन",
        category     = "Oil Extraction",
        emoji        = "🫙",
        buyPrice     = 120000,
        rentPerDay   = 1200,
        powerKW      = 5.0,
        district     = "Kanpur",
        isAvailable  = true,
        rating       = 4.8f,
        suitableFor  = listOf("Soybean", "Mustard", "Groundnut")
    ),
    ProcessingEquipment(
        id           = "E3",
        name         = "Fruit Pulping Machine",
        nameHindi    = "फ्रूट पल्पर",
        category     = "Pulping",
        emoji        = "🍅",
        buyPrice     = 65000,
        rentPerDay   = 600,
        powerKW      = 2.2,
        district     = "Lucknow",
        isAvailable  = false,
        rating       = 4.2f,
        suitableFor  = listOf("Tomato", "Mango", "Guava")
    ),
    ProcessingEquipment(
        id           = "E4",
        name         = "Spice Grinder",
        nameHindi    = "मसाला ग्राइंडर",
        category     = "Grinding",
        emoji        = "🌶️",
        buyPrice     = 45000,
        rentPerDay   = 400,
        powerKW      = 1.5,
        district     = "Agra",
        isAvailable  = true,
        rating       = 4.6f,
        suitableFor  = listOf("Chilli", "Turmeric", "Coriander")
    ),
    ProcessingEquipment(
        id           = "E5",
        name         = "Solar Dryer",
        nameHindi    = "सोलर ड्रायर",
        category     = "Drying",
        emoji        = "☀️",
        buyPrice     = 35000,
        rentPerDay   = 300,
        powerKW      = 0.0,
        district     = "Lucknow",
        isAvailable  = true,
        rating       = 4.3f,
        suitableFor  = listOf("All Crops")
    ),
    ProcessingEquipment(
        id           = "E6",
        name         = "Dal Mill",
        nameHindi    = "दाल मिल",
        category     = "Milling",
        emoji        = "🫘",
        buyPrice     = 95000,
        rentPerDay   = 900,
        powerKW      = 4.0,
        district     = "Kanpur",
        isAvailable  = true,
        rating       = 4.4f,
        suitableFor  = listOf("Soybean", "Lentil", "Chickpea")
    ),
)

val fakeGuides = listOf(
    ProcessingGuide(
        id              = "G1",
        title           = "Tomato Puree Making",
        titleHindi      = "टमाटर प्यूरी बनाएं",
        emoji           = "🍅",
        category        = "Vegetables",
        valueMultiplier = "6x",
        steps           = 6,
        durationMins    = 45
    ),
    ProcessingGuide(
        id              = "G2",
        title           = "Cold Pressed Mustard Oil",
        titleHindi      = "सरसों का तेल निकालें",
        emoji           = "🫙",
        category        = "Oil",
        valueMultiplier = "4x",
        steps           = 5,
        durationMins    = 60
    ),
    ProcessingGuide(
        id              = "G3",
        title           = "Rice Milling & Packaging",
        titleHindi      = "चावल मिलिंग करें",
        emoji           = "🍚",
        category        = "Grains",
        valueMultiplier = "3x",
        steps           = 4,
        durationMins    = 30
    ),
    ProcessingGuide(
        id              = "G4",
        title           = "Mango Pickle Processing",
        titleHindi      = "आम का अचार बनाएं",
        emoji           = "🥭",
        category        = "Fruits",
        valueMultiplier = "8x",
        steps           = 7,
        durationMins    = 90
    ),
)

val fakeBatches = listOf(
    TraceBatch(
        batchId       = "KS-2024-001",
        cropName      = "Onion",
        cropNameHindi = "प्याज़",
        emoji         = "🧅",
        farmerName    = "Ramesh Patel",
        farmLocation  = "Nashik, Maharashtra",
        harvestDate   = "28 Mar 2024",
        quantity      = 200,
        grade         = "A",
        isOrganic     = false,
        events        = listOf(
            TraceEvent(
                eventType      = "Harvested",
                eventTypeHindi = "कटाई",
                actorName      = "Ramesh Patel",
                location       = "Nashik Farm",
                timestamp      = "28 Mar, 6:00 AM",
                emoji          = "🌾",
                isCompleted    = true
            ),
            TraceEvent(
                eventType      = "Graded",
                eventTypeHindi = "ग्रेडिंग",
                actorName      = "APMC Inspector",
                location       = "Lasalgaon APMC",
                timestamp      = "28 Mar, 2:00 PM",
                emoji          = "✅",
                isCompleted    = true
            ),
            TraceEvent(
                eventType      = "Processed",
                eventTypeHindi = "प्रसंस्करण",
                actorName      = "Nashik Agro Unit",
                location       = "Nashik Processing Hub",
                timestamp      = "29 Mar, 10:00 AM",
                emoji          = "🏭",
                isCompleted    = true
            ),
            TraceEvent(
                eventType      = "Dispatched",
                eventTypeHindi = "भेजा गया",
                actorName      = "FastLogistics Pvt Ltd",
                location       = "Nashik Warehouse",
                timestamp      = "30 Mar, 8:00 AM",
                emoji          = "🚚",
                isCompleted    = true
            ),
            TraceEvent(
                eventType      = "Delivered",
                eventTypeHindi = "पहुंचाया गया",
                actorName      = "Delhi Buyer",
                location       = "Azadpur Mandi, Delhi",
                timestamp      = "31 Mar, 6:00 AM",
                emoji          = "📦",
                isCompleted    = false
            ),
        )
    ),
    TraceBatch(
        batchId       = "KS-2024-002",
        cropName      = "Basmati Rice",
        cropNameHindi = "बासमती चावल",
        emoji         = "🍚",
        farmerName    = "Gurpreet Singh",
        farmLocation  = "Amritsar, Punjab",
        harvestDate   = "15 Mar 2024",
        quantity      = 300,
        grade         = "A",
        isOrganic     = false,
        events        = listOf(
            TraceEvent(
                eventType      = "Harvested",
                eventTypeHindi = "कटाई",
                actorName      = "Gurpreet Singh",
                location       = "Amritsar Farm",
                timestamp      = "15 Mar, 7:00 AM",
                emoji          = "🌾",
                isCompleted    = true
            ),
            TraceEvent(
                eventType      = "Milled",
                eventTypeHindi = "मिलिंग",
                actorName      = "Punjab Rice Mills",
                location       = "Amritsar Mill",
                timestamp      = "16 Mar, 9:00 AM",
                emoji          = "⚙️",
                isCompleted    = true
            ),
            TraceEvent(
                eventType      = "Packaged",
                eventTypeHindi = "पैकेजिंग",
                actorName      = "AgroPackage Co.",
                location       = "Ludhiana",
                timestamp      = "17 Mar, 11:00 AM",
                emoji          = "📦",
                isCompleted    = true
            ),
            TraceEvent(
                eventType      = "Export Ready",
                eventTypeHindi = "निर्यात तैयार",
                actorName      = "APEDA Officer",
                location       = "Delhi Export Hub",
                timestamp      = "18 Mar, 3:00 PM",
                emoji          = "✈️",
                isCompleted    = false
            ),
        )
    ),
)

val fakeFarmerCrops = listOf(
    FarmerCrop(
        name       = "Wheat",
        nameHindi  = "गेहूं",
        emoji      = "🌾",
        areaSown   = 4.5,
        season     = "Rabi",
        sowingDate = "Nov 2024"
    ),
    FarmerCrop(
        name       = "Tomato",
        nameHindi  = "टमाटर",
        emoji      = "🍅",
        areaSown   = 2.0,
        season     = "Kharif",
        sowingDate = "Jun 2024"
    ),
    FarmerCrop(
        name       = "Onion",
        nameHindi  = "प्याज़",
        emoji      = "🧅",
        areaSown   = 1.5,
        season     = "Rabi",
        sowingDate = "Oct 2024"
    ),
)

val fakeFarmerStats = FarmerStats(
    totalSales      = "₹2.4L",
    batchesCreated  = 12,
    activeListings  = 3,
    savedAmount     = "₹18K"
)

val fakeSettings = listOf(
    SettingItem(
        label       = "Price Alerts",
        labelHindi  = "मूल्य अलर्ट",
        emoji       = "🔔",
        hasToggle   = true,
        toggleValue = true
    ),
    SettingItem(
        label       = "Cold Chain Alerts",
        labelHindi  = "कोल्ड चेन अलर्ट",
        emoji       = "❄️",
        hasToggle   = true,
        toggleValue = true
    ),
    SettingItem(
        label       = "Language",
        labelHindi  = "भाषा",
        emoji       = "🌐",
        hasToggle   = false
    ),
    SettingItem(
        label       = "KYC & Documents",
        labelHindi  = "KYC दस्तावेज़",
        emoji       = "📄",
        hasToggle   = false
    ),
    SettingItem(
        label       = "Bank Account",
        labelHindi  = "बैंक खाता",
        emoji       = "🏦",
        hasToggle   = false
    ),
    SettingItem(
        label       = "Help & Support",
        labelHindi  = "सहायता",
        emoji       = "🤝",
        hasToggle   = false
    ),
    SettingItem(
        label       = "About KrishiSetu",
        labelHindi  = "हमारे बारे में",
        emoji       = "🌱",
        hasToggle   = false
    ),
)

val fakeSuggestedQueries = listOf(
    SuggestedQuery(
        text      = "Should I sell wheat today?",
        textHindi = "क्या आज गेहूं बेचूं?",
        emoji     = "🌾"
    ),
    SuggestedQuery(
        text      = "My tomato crop has yellow spots",
        textHindi = "टमाटर में पीले धब्बे हैं",
        emoji     = "🍅"
    ),
    SuggestedQuery(
        text      = "Which scheme gives money for dryer?",
        textHindi = "ड्रायर के लिए कौन सी योजना?",
        emoji     = "💰"
    ),
    SuggestedQuery(
        text      = "How to export onion to UAE?",
        textHindi = "UAE को प्याज़ निर्यात कैसे करें?",
        emoji     = "✈️"
    ),
    SuggestedQuery(
        text      = "Best price for rice this week",
        textHindi = "इस हफ्ते चावल का बेस्ट भाव",
        emoji     = "📊"
    ),
)

val fakeAIResponses = mapOf(
    "wheat" to "गेहूं का आज का मंडी भाव ₹2,150/quintal है। पिछले 7 दिनों में 2.4% की बढ़त हुई है। AI forecast: अगले 2 हफ्तों में भाव ₹2,200-2,300 तक जा सकता है। मेरी सलाह: 5-7 दिन रुकें, बेहतर दाम मिलेंगे। 📈",
    "tomato" to "टमाटर में पीले धब्बे Early Blight (अगेती झुलसा) के लक्षण हो सकते हैं। उपाय:\n1. Mancozeb 75% WP @ 2g/L पानी में मिलाकर spray करें\n2. रोगी पत्तियां तोड़कर जला दें\n3. सिंचाई कम करें\n\nअगर 3 दिन में सुधार न हो तो नजदीकी KVK से संपर्क करें। 🌿",
    "scheme" to "ड्रायर के लिए ये योजनाएं मिल सकती हैं:\n\n1. **PMKSY** - 50% subsidy on solar dryer\n2. **SFURTI** - FPO के लिए 90% subsidy\n3. **PMEGP** - ₹25 लाख तक loan, 35% subsidy\n\nApply करने के लिए agri.up.gov.in पर जाएं। 🏛️",
    "export" to "UAE को प्याज़ निर्यात के लिए:\n\n1. APEDA registration (apeda.gov.in)\n2. IEC code (DGFT से)\n3. Phytosanitary certificate\n4. FSSAI export license\n\nMinimum quantity: 5 MT\nCurrent UAE price: \$450/MT\n\nKrishiSetu Export Portal से direct buyers से connect करें! ✈️",
    "default" to "नमस्ते! मैं KrishiSetu का AI सहायक हूं। आप मुझसे पूछ सकते हैं:\n• मंडी भाव और बेचने का सही समय\n• फसल की बीमारी और उपचार\n• सरकारी योजनाएं\n• निर्यात की जानकारी\n• प्रसंस्करण की विधि\n\nहिंदी या English में पूछें! 🌱"
)

val fakeColdStorages = listOf(
    ColdStorage(
        id                = "CS1",
        name              = "AgroFreeze Lucknow",
        district          = "Lucknow",
        state             = "UP",
        distanceKm        = 4.2,
        totalCapacityMT   = 5000,
        availableSlotsMT  = 1200,
        pricePerMTPerDay  = 8,
        minTempCelsius    = 2,
        maxTempCelsius    = 8,
        currentTempCelsius = 4.5,
        currentHumidity   = 88,
        rating            = 4.6f,
        isNHBCertified    = true,
        supportedCrops    = listOf("Potato", "Onion", "Tomato", "Apple"),
        emoji             = "🏭"
    ),
    ColdStorage(
        id                = "CS2",
        name              = "Kisaan Cold Hub",
        district          = "Kanpur",
        state             = "UP",
        distanceKm        = 12.8,
        totalCapacityMT   = 3000,
        availableSlotsMT  = 450,
        pricePerMTPerDay  = 6,
        minTempCelsius    = 0,
        maxTempCelsius    = 5,
        currentTempCelsius = 2.1,
        currentHumidity   = 92,
        rating            = 4.3f,
        isNHBCertified    = true,
        supportedCrops    = listOf("Mango", "Banana", "Grapes", "Potato"),
        emoji             = "❄️"
    ),
    ColdStorage(
        id                = "CS3",
        name              = "FrostFarm Agra",
        district          = "Agra",
        state             = "UP",
        distanceKm        = 28.5,
        totalCapacityMT   = 8000,
        availableSlotsMT  = 3200,
        pricePerMTPerDay  = 10,
        minTempCelsius    = -2,
        maxTempCelsius    = 10,
        currentTempCelsius = 6.8,
        currentHumidity   = 85,
        rating            = 4.8f,
        isNHBCertified    = true,
        supportedCrops    = listOf("All Crops"),
        emoji             = "🌡️"
    ),
    ColdStorage(
        id                = "CS4",
        name              = "GreenChill Nashik",
        district          = "Nashik",
        state             = "Maharashtra",
        distanceKm        = 6.1,
        totalCapacityMT   = 2000,
        availableSlotsMT  = 0,
        pricePerMTPerDay  = 7,
        minTempCelsius    = 1,
        maxTempCelsius    = 7,
        currentTempCelsius = 3.2,
        currentHumidity   = 90,
        rating            = 4.4f,
        isNHBCertified    = false,
        supportedCrops    = listOf("Onion", "Grapes", "Tomato"),
        emoji             = "🧊"
    ),
)

val fakeActiveBookings = listOf(
    StorageBooking(
        id           = "B1",
        storageName  = "AgroFreeze Lucknow",
        cropName     = "Potato",
        cropEmoji    = "🥔",
        quantityMT   = 50,
        fromDate     = "1 Apr 2024",
        toDate       = "15 Apr 2024",
        totalCost    = 5600,
        status       = "Active",
        currentTemp  = 4.5,
        isAlertActive = false
    ),
    StorageBooking(
        id           = "B2",
        storageName  = "Kisaan Cold Hub",
        cropName     = "Onion",
        cropEmoji    = "🧅",
        quantityMT   = 30,
        fromDate     = "28 Mar 2024",
        toDate       = "10 Apr 2024",
        totalCost    = 2520,
        status       = "Active",
        currentTemp  = 8.9,
        isAlertActive = true
    ),
)

val fakeExportDocuments = listOf(
    ExportDocument(
        id           = "D1",
        name         = "APEDA Registration",
        nameHindi    = "APEDA पंजीकरण",
        emoji        = "📋",
        issuingBody  = "APEDA, New Delhi",
        isCompleted  = true,
        isMandatory  = true,
        description  = "Mandatory for all agricultural exports from India"
    ),
    ExportDocument(
        id           = "D2",
        name         = "IEC Code",
        nameHindi    = "आयात-निर्यात कोड",
        emoji        = "🔑",
        issuingBody  = "DGFT",
        isCompleted  = true,
        isMandatory  = true,
        description  = "Importer Exporter Code from Directorate General of Foreign Trade"
    ),
    ExportDocument(
        id           = "D3",
        name         = "Phytosanitary Certificate",
        nameHindi    = "फाइटोसैनिटरी प्रमाण",
        emoji        = "🌿",
        issuingBody  = "NPPO / State Dept of Agriculture",
        isCompleted  = false,
        isMandatory  = true,
        description  = "Certifies produce is free from pests and diseases"
    ),
    ExportDocument(
        id           = "D4",
        name         = "FSSAI Export License",
        nameHindi    = "FSSAI निर्यात लाइसेंस",
        emoji        = "🏥",
        issuingBody  = "FSSAI",
        isCompleted  = false,
        isMandatory  = true,
        description  = "Food safety certification for export"
    ),
    ExportDocument(
        id           = "D5",
        name         = "Certificate of Origin",
        nameHindi    = "उत्पत्ति प्रमाण पत्र",
        emoji        = "📜",
        issuingBody  = "Chamber of Commerce",
        isCompleted  = false,
        isMandatory  = false,
        description  = "Certifies goods are produced in India"
    ),
    ExportDocument(
        id           = "D6",
        name         = "RCMC Certificate",
        nameHindi    = "RCMC प्रमाण पत्र",
        emoji        = "🎖️",
        issuingBody  = "APEDA",
        isCompleted  = true,
        isMandatory  = false,
        description  = "Registration cum Membership Certificate for export benefits"
    ),
)

val fakeGlobalBuyers = listOf(
    GlobalBuyer(
        id            = "B1",
        companyName   = "Al Dahra Trading LLC",
        country       = "UAE",
        countryFlag   = "🇦🇪",
        interestedIn  = listOf("Onion", "Potato", "Rice"),
        minOrderMT    = 20,
        priceUSD      = 450,
        isVerified    = true,
        contactEmail  = "trade@aldahra.ae"
    ),
    GlobalBuyer(
        id            = "B2",
        companyName   = "Tesco Global Sourcing",
        country       = "United Kingdom",
        countryFlag   = "🇬🇧",
        interestedIn  = listOf("Basmati Rice", "Spices", "Mango"),
        minOrderMT    = 50,
        priceUSD      = 820,
        isVerified    = true,
        contactEmail  = "sourcing@tesco.co.uk"
    ),
    GlobalBuyer(
        id            = "B3",
        companyName   = "Little India Foods",
        country       = "USA",
        countryFlag   = "🇺🇸",
        interestedIn  = listOf("Basmati Rice", "Lentils", "Spices"),
        minOrderMT    = 10,
        priceUSD      = 950,
        isVerified    = true,
        contactEmail  = "buy@littleindia.com"
    ),
    GlobalBuyer(
        id            = "B4",
        companyName   = "FairPrice Singapore",
        country       = "Singapore",
        countryFlag   = "🇸🇬",
        interestedIn  = listOf("Onion", "Tomato", "Wheat"),
        minOrderMT    = 15,
        priceUSD      = 680,
        isVerified    = false,
        contactEmail  = "imports@fairprice.sg"
    ),
    GlobalBuyer(
        id            = "B5",
        companyName   = "Carrefour MENA",
        country       = "Saudi Arabia",
        countryFlag   = "🇸🇦",
        interestedIn  = listOf("Rice", "Onion", "Dates"),
        minOrderMT    = 100,
        priceUSD      = 520,
        isVerified    = true,
        contactEmail  = "agri@carrefour.sa"
    ),
    GlobalBuyer(
        id            = "B6",
        companyName   = "Woolworths Australia",
        country       = "Australia",
        countryFlag   = "🇦🇺",
        interestedIn  = listOf("Spices", "Mango", "Basmati Rice"),
        minOrderMT    = 30,
        priceUSD      = 1100,
        isVerified    = true,
        contactEmail  = "produce@woolworths.com.au"
    ),
)

val fakeComplianceRules = listOf(
    ComplianceRule(
        country     = "UAE",
        countryFlag = "🇦🇪",
        crop        = "Onion",
        mrlLimit    = "0.1 ppm",
        status      = "Safe",
        notes       = "Current residue levels within limits"
    ),
    ComplianceRule(
        country     = "UAE",
        countryFlag = "🇦🇪",
        crop        = "Tomato",
        mrlLimit    = "0.5 ppm",
        status      = "Safe",
        notes       = "No restrictions"
    ),
    ComplianceRule(
        country     = "UK",
        countryFlag = "🇬🇧",
        crop        = "Basmati Rice",
        mrlLimit    = "0.01 ppm",
        status      = "Caution",
        notes       = "Check pesticide residue before shipping"
    ),
    ComplianceRule(
        country     = "USA",
        countryFlag = "🇺🇸",
        crop        = "Mango",
        mrlLimit    = "0.2 ppm",
        status      = "Safe",
        notes       = "Irradiation treatment required"
    ),
    ComplianceRule(
        country     = "Japan",
        countryFlag = "🇯🇵",
        crop        = "Rice",
        mrlLimit    = "0.01 ppm",
        status      = "Restricted",
        notes       = "Strict MRL limits — lab test mandatory"
    ),
    ComplianceRule(
        country     = "EU",
        countryFlag = "🇪🇺",
        crop        = "Spices",
        mrlLimit    = "0.05 ppm",
        status      = "Caution",
        notes       = "Aflatoxin testing required"
    ),
)

val fakeFpoStats = FpoStats(
    totalMembers      = 48,
    totalLandAcres    = 312.5,
    totalTradedCrore  = "₹1.8Cr",
    activePools       = 3
)

val fakeFpoMembers = listOf(
    FpoMember(
        id                 = "M1",
        name               = "Ramesh Kumar",
        village            = "Sultanpur",
        landHoldingAcres   = 4.5,
        shareCount         = 10,
        joinedDate         = "Jan 2023",
        emoji              = "👨‍🌾",
        contributedMT      = 12.0
    ),
    FpoMember(
        id                 = "M2",
        name               = "Sunita Devi",
        village            = "Barabanki",
        landHoldingAcres   = 2.0,
        shareCount         = 5,
        joinedDate         = "Mar 2023",
        emoji              = "👩‍🌾",
        contributedMT      = 6.5
    ),
    FpoMember(
        id                 = "M3",
        name               = "Mohan Singh",
        village            = "Hardoi",
        landHoldingAcres   = 7.0,
        shareCount         = 15,
        joinedDate         = "Jan 2023",
        emoji              = "👨‍🌾",
        contributedMT      = 18.0
    ),
    FpoMember(
        id                 = "M4",
        name               = "Geeta Patel",
        village            = "Unnao",
        landHoldingAcres   = 3.5,
        shareCount         = 8,
        joinedDate         = "Jun 2023",
        emoji              = "👩‍🌾",
        contributedMT      = 9.0
    ),
    FpoMember(
        id                 = "M5",
        name               = "Vijay Yadav",
        village            = "Raebareli",
        landHoldingAcres   = 5.0,
        shareCount         = 12,
        joinedDate         = "Feb 2023",
        emoji              = "👨‍🌾",
        contributedMT      = 14.5
    ),
)

val fakePooledProduce = listOf(
    PooledProduce(
        id                    = "P1",
        cropName              = "Wheat",
        cropNameHindi         = "गेहूं",
        emoji                 = "🌾",
        totalQuantityMT       = 340.0,
        targetQuantityMT      = 500.0,
        contributorsCount     = 28,
        expectedPricePerMT    = 22000,
        currentMandiPricePerMT = 21500,
        closingDate           = "15 Apr 2024",
        grade                 = "A"
    ),
    PooledProduce(
        id                    = "P2",
        cropName              = "Onion",
        cropNameHindi         = "प्याज़",
        emoji                 = "🧅",
        totalQuantityMT       = 85.0,
        targetQuantityMT      = 100.0,
        contributorsCount     = 12,
        expectedPricePerMT    = 28000,
        currentMandiPricePerMT = 26500,
        closingDate           = "10 Apr 2024",
        grade                 = "A"
    ),
    PooledProduce(
        id                    = "P3",
        cropName              = "Soybean",
        cropNameHindi         = "सोयाबीन",
        emoji                 = "🫘",
        totalQuantityMT       = 120.0,
        targetQuantityMT      = 200.0,
        contributorsCount     = 18,
        expectedPricePerMT    = 46000,
        currentMandiPricePerMT = 45000,
        closingDate           = "20 Apr 2024",
        grade                 = "B"
    ),
)

val fakeFpoListings = listOf(
    FpoListing(
        id               = "FL1",
        cropName         = "Wheat",
        cropNameHindi    = "गेहूं",
        emoji            = "🌾",
        quantityMT       = 500.0,
        askPricePerMT    = 22000,
        grade            = "A",
        isExportReady    = false,
        totalBids        = 8,
        highestBidPerMT  = 21800,
        closingDate      = "18 Apr 2024"
    ),
    FpoListing(
        id               = "FL2",
        cropName         = "Basmati Rice",
        cropNameHindi    = "बासमती चावल",
        emoji            = "🍚",
        quantityMT       = 200.0,
        askPricePerMT    = 42000,
        grade            = "A",
        isExportReady    = true,
        totalBids        = 14,
        highestBidPerMT  = 41500,
        closingDate      = "12 Apr 2024"
    ),
    FpoListing(
        id               = "FL3",
        cropName         = "Mustard",
        cropNameHindi    = "सरसों",
        emoji            = "🌻",
        quantityMT       = 150.0,
        askPricePerMT    = 55000,
        grade            = "A",
        isExportReady    = false,
        totalBids        = 5,
        highestBidPerMT  = 54000,
        closingDate      = "22 Apr 2024"
    ),
)

val fakeUserRoles = listOf(
    UserRole(
        id          = "farmer",
        label       = "Farmer",
        labelHindi  = "किसान",
        emoji       = "👨‍🌾",
        description = "Sell produce, track prices, access schemes"
    ),
    UserRole(
        id          = "processor",
        label       = "Processor",
        labelHindi  = "प्रसंस्करणकर्ता",
        emoji       = "🏭",
        description = "Buy raw produce, manage processing units"
    ),
    UserRole(
        id          = "trader",
        label       = "Trader",
        labelHindi  = "व्यापारी",
        emoji       = "🛒",
        description = "Buy and sell in bulk, manage inventory"
    ),
    UserRole(
        id          = "fpo",
        label       = "FPO Manager",
        labelHindi  = "FPO प्रबंधक",
        emoji       = "👥",
        description = "Manage farmer collectives, pool produce"
    ),
    UserRole(
        id          = "exporter",
        label       = "Exporter",
        labelHindi  = "निर्यातक",
        emoji       = "✈️",
        description = "Export Indian produce globally"
    ),
)