# KrishiSetu (कृषि सेतु)

> 🚧 **Work in Progress** — Core features functional, backend integration ongoing.

An end-to-end Android platform connecting Indian farmers to markets, AI advisory, cooperative networks, and global export chains — built for the **Agro Processing & Value Chain** problem statement.

---

## Core USPs

### 🔗 Blockchain Traceability
End-to-end crop journey tracking from farm to consumer. Every step — harvest, processing, storage, transport — is logged as an immutable record, enabling quality assurance and building buyer trust for premium and export markets.

### 🤝 FPO Hub
Dedicated module for Farmer Producer Organisations. Enables collective bargaining, bulk input procurement, shared cold storage coordination, and cooperative marketing — giving small farmers the negotiating power of large enterprises.

### 🌐 Export Portal
Direct gateway for farmers and FPOs to access export markets. Includes compliance checklists, documentation support, and buyer discovery — reducing dependence on middlemen for international trade.

### 🧊 Cold Chain Management
Real-time visibility into cold storage availability, temperature logs, and logistics routing to minimize post-harvest losses across the supply chain.

### 🤖 KrishiAI Assistant
Multilingual voice + text advisory powered by Gemini 1.5 Flash + Sarvam AI. Farmers can ask questions about crop pricing, weather, schemes, and market trends in their own language.

---

## All Features

| Feature | Description |
|---|---|
| 🔗 Blockchain Traceability | Immutable farm-to-market supply chain records |
| 🤝 FPO Hub | Cooperative tools for farmer collectives |
| 🌐 Export Portal | Export market access, compliance & buyer discovery |
| 🧊 Cold Chain | Storage availability, temperature tracking, logistics |
| 🤖 KrishiAI | Multilingual AI assistant (voice + text) |
| 📊 Mandi Prices | Live market rates via eNAM *(in progress)* |
| 🛒 Marketplace | Farmer-to-buyer listings and orders *(in progress)* |
| 🌍 Multilingual | 10+ Indian languages via Sarvam STT / TTS / Translate |
| 📍 Location-aware | GPS-based local mandi and weather data |
| 🌙 Dark Mode | Full Material 3 theming |
| 🔐 Phone Auth | Firebase OTP login *(real SMS in progress)* |

---

## Tech Stack

| Layer | Tech |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Language | Kotlin |
| Auth | Firebase Phone OTP |
| Database | Firestore + Room (offline-first) |
| AI | Gemini 1.5 Flash (REST API) |
| Voice | Sarvam AI — STT, TTS, Translate |
| Traceability | Blockchain ledger integration *(in progress)* |
| Notifications | Firebase Cloud Messaging |
| Background Sync | WorkManager |
| Build | Gradle (Kotlin DSL) |

---

## Project Status

| Step | Feature | Status |
|---|---|---|
| 1–13 | UI, auth flow, AI chat, multilingual, dark mode, GPS | ✅ Done |
| 14 | Firebase Phone OTP (real SMS) | 🔄 In Progress |
| 15 | eNAM live mandi prices | ⏳ Pending |
| 16 | Room DB offline-first | ⏳ Pending |
| 17 | Firestore listings & orders | ⏳ Pending |
| 18 | FCM push notifications | ⏳ Pending |
| 19 | WorkManager background sync | ⏳ Pending |
| 20 | Final polish & release build | ⏳ Pending |

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34+

### Setup

1. Clone the repo
   ```bash
   git clone https://github.com/this-is-rachit/KrishiSetu.git
   ```

2. Create `local.properties` in the project root and add your keys:
   ```
   GEMINI_API_KEY=your_gemini_key_here
   SARVAM_API_KEY=your_sarvam_key_here
   ```

3. Add `google-services.json` from your Firebase Console into `app/`

4. Build and run on a device or emulator (API 26+)

---

## Security

`local.properties` and `google-services.json` are gitignored and never committed. All API keys are accessed via `BuildConfig` at runtime.

---

## License

Apache 2.0
