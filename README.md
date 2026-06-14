# 🛡️ Shield Core - AI Call Fraud Detection Interceptor

An advanced Jetpack Compose prototype designed to intercept, analyze, and classify social engineering and financial scams using live voice telemetry.

---

## 🚀 Key Features

* **Google Voice Integration:** Live audio stream mapping utilizing a high-stability native voice subsystem.
* **Multi-Criteria NLP Engine:** Dynamic evaluation and sorting of incoming text parameters into three distinct threat vectors:
  * **Financial Fraud:** Detects ongoing requests for critical banking tokens, cards, or OTPs.
  * **Account Suspension Threat:** Flags hostile profiles forcing credential updates under fake account lock panic.
  * **Urgency & Impersonation:** Identifies authority spoofing, fabricated legal threats, or random lottery rewards.
* **Cyber Shield UI:** A high-fidelity, real-time dark mode interceptor dashboard built entirely with modern Jetpack Compose.
* **Instant Security Overlay:** Blasts a prominent red warning banner immediately upon triggering dynamic scam parameters to protect the user.

---

## 🛠️ Tech Stack & Architecture

* **UI Framework:** Jetpack Compose (Kotlin)
* **Architecture:** ComponentActivity with reactive state management (`mutableStateOf`)
* **Animation:** Smooth hardware-accelerated transitions via `animateColorAsState`
* **API Subsystem:** Native Android `RecognizerIntent` for high-accuracy speech-to-text processing

---

## 🔮 Production Roadmap (Android Limitations)

> **Architectural Note:** Due to current Android sandbox security architectures, third-party applications are programmatically restricted from intercepting native carrier-call audio streams to ensure user privacy. 
> 
> In a production environment, this system is designed to deploy either as a **Pre-installed System Carrier Application** or integrate directly via the **Google Dialer Framework Extension API**.

---

## 👨‍💻 Installation & Demo Run

1. Clone this repository.
2. Open the project in **Android Studio Jellyfish / Ladybug (2024+)**.
3. Sync the Gradle files and build the debug APK.
4. Run the application on a device with Google Speech Services active.
5. Tap **LAUNCH GOOGLE MIC INTERFACE** and speak test phrases like *"Give me your OTP"* to trigger the real-time interceptor simulation.
