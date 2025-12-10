# Thekr - Technical Architecture

## Platform Support

Thekr is built as a **Kotlin Multiplatform** application, supporting:

| Platform | Status |
|----------|--------|
| Android | ✅ Active |
| Desktop (JVM) | ✅ Active |
| iOS | 🔄 Prepared (commented out) |
| Web (WASM) | 🔄 Prepared (commented out) |

## Technology Stack

### Core Technologies
- **Kotlin Multiplatform (KMP)** - Shared business logic across platforms
- **Jetpack Compose Multiplatform** - Shared UI framework
- **Room Database** - Local data persistence
- **Kotlin Coroutines** - Asynchronous programming
- **Kotlin Serialization** - Data serialization

### UI & Navigation
- **Compose Material 3** - Modern Material Design components
- **Voyager** - Multiplatform navigation library
- **Coil** - Image loading
- **Vico** - Charts and statistics visualization

### Architecture Patterns
- **MVVM** (Model-View-ViewModel) architecture
- **Repository Pattern** for data access
- **Koin** for dependency injection
- **KStore** for key-value storage

## Data Layer

### Database Entities
1. **Thekr** - The remembrance text and metadata
2. **ThekrInstance** - User's instance of a thekr with personal settings
3. **Category** - Organizational categories for thekr
4. **Count** - Individual count records
5. **CountMiss** - Tracking missed counts
6. **Session** - Counting session data
7. **ThekrGoalCompletion** - Goal achievement records
8. **ThekrFadl** - Virtue/reward information for each thekr

### Settings Storage
- Uses KStore for persistent settings
- Supports theme, language, feedback preferences
- Tracks display and interaction preferences

## Android-Specific Features
- Text-to-Speech (TTS) integration
- Vibration feedback
- Wake lock for screen-on during counting
- Fingerprint sensor integration (via root/logcat)
- RTL (Right-to-Left) support for Arabic

---
*Analysis continues in subsequent files...*
