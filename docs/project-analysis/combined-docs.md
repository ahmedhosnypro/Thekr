# Thekr - Project Overview

## What is Thekr?

**Thekr** (ذكر - Arabic for "remembrance") is a digital Islamic remembrance and prayer counter application. The app helps Muslims track and maintain their daily spiritual practices, specifically:

- **Dhikr/Thekr** - Remembrance of Allah through repeated phrases
- **Azkar** (أذكار) - Islamic supplications and remembrances for various occasions
- **Dua** - Personal prayers and supplications
- **Mesbaha** (مسبحة) - Digital prayer beads/counter

## Primary Purpose

The application serves as a modern, digital alternative to traditional prayer beads (tasbih/mesbaha), allowing users to:

1. Count their daily remembrances (dhikr)
2. Track progress toward spiritual goals
3. Access a library of authentic Islamic supplications
4. Maintain statistics of their worship activities

## Target Audience

- Muslims who want to maintain regular dhikr practice
- Users seeking a convenient way to track prayer counts
- People looking for authentic Islamic supplications (from "Hesn Al-Muslim" - Fortress of the Muslim)

## App Name Meaning

The Arabic name "واصبر نفسك" (Wasbir Nafsak) translates to "Be patient with yourself" - a phrase from the Quran encouraging spiritual perseverance.

---
*Analysis continues in subsequent files...*
# Thekr - Features Analysis

## Main Features

### 1. Digital Prayer Counter (Mesbaha/Sebha)
The core feature - a digital tasbih (prayer beads) that allows users to:
- Tap to count remembrances
- Track session counts
- Set daily/weekly/monthly/yearly goals
- View counting history and statistics

### 2. Content Library
The app provides four main content categories:

#### a) User Custom Thekr
- Users can create their own custom remembrances
- Personalized counters with custom text
- Editable and manageable

#### b) Hesn Al-Muslim (حصن المسلم)
- "Fortress of the Muslim" - a famous collection of authentic Islamic supplications
- Organized by categories (morning, evening, prayer, travel, etc.)
- Pre-loaded content from the authentic Islamic source

#### c) Knooz (كنوز - Treasures)
- Collection of valuable dhikr and remembrances
- Curated spiritual content

#### d) Dua (الدعاء - Supplications)
- Personal prayers and supplications
- Organized collection of duas

### 3. Statistics & Progress Tracking
- Daily, weekly, monthly, and yearly statistics
- Goal completion tracking
- Session history
- Visual progress indicators

### 4. Customization Options
- Multiple theme modes (Light/Dark/System)
- Material You support (Android)
- Multiple Arabic fonts for authentic display
- Adjustable font sizes
- Screen always-on option during counting

### 5. Accessibility Features
- Vibration feedback
- Sound feedback on count
- Voice alerts for count values
- Volume key support for counting
- Fingerprint sensor support for counting (Android)

## Content Categories (Azkar Types)
Based on the icons, the app covers these life situations:
- Morning remembrances (أذكار الصباح)
- Evening remembrances (أذكار المساء)
- Prayer-related (الصلاة)
- Sleep/waking (النوم)
- Eating/drinking (الطعام)
- Travel (السفر)
- Home (المنزل)
- Mosque (المسجد)
- Sickness (المرض)
- Fear/anxiety (الخوف)
- Social interactions (الاجتماعية)
- And many more...

---
*Analysis continues in subsequent files...*
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
# Thekr - Content Library Analysis

## Content Sources

The app comes pre-loaded with authentic Islamic content from:

1. **Hesn Al-Muslim (حصن المسلم)** - "Fortress of the Muslim"
   - A famous compilation by Sa'id bin Ali bin Wahf Al-Qahtani
   - Contains authentic supplications from Quran and Sunnah

2. **Knooz (كنوز)** - "Treasures"
   - Curated collection of valuable remembrances

3. **Dua Collection**
   - Duas from Quran (أدعية من القرآن)
   - Duas from Sunnah (أدعية من السنة)

## Category Structure (147 Categories)

### Main Categories
| ID | Arabic Name | English Translation |
|----|-------------|---------------------|
| 1 | المسبحة | Prayer Beads (User Custom) |
| 2 | حصن المسلم | Fortress of the Muslim |
| 3 | كنوز | Treasures |
| 4 | الدعاء | Supplications |

### Life Situations Covered

#### Daily Routine
- Morning remembrances (أذكار الصباح)
- Evening remembrances (أذكار المساء)
- Sleep & waking (أذكار النوم و اليقظة)
- Home entry/exit (أذكار البيت)

#### Worship
- Prayer-related (أذكار الأذان والصلاة)
- Mosque etiquette (أذكار المسجد)
- Purification (أذكار الطهارة)
- Fasting (أذكار الصيام)
- Hajj & Umrah (أذكار الحج و العمرة)

#### Life Events
- Marriage & family (أذكار الزواج و الأسرة)
- Sickness & hardship (أذكار المرض و الهم والكرب)
- Death & funeral (أذكار الموت والحياة)
- Travel (أذكار السفر)

#### Protection
- Evil eye & magic (أذكار التعوذ من السحر والعين)
- Fear & anxiety (أذكار الخوف والفزع)
- Ruqyah & protection (أذكار الرقية)

#### Social & Daily Life
- Eating & drinking (أذكار الأكل و الشرب)
- Clothing (أذكار اللباس و الزينة)
- Social interactions (الأذكار الاجتماعية)
- Business & trade (أذكار المال البيع و التجارة)

#### Spiritual Practice
- Repentance (الاستغفار والتوبة)
- Glorification (فضل التسبيح والتحميد)
- Prophet's method of dhikr (كيف كان النبي يسبح)

## Audio Content

The app includes audio recitations from multiple reciters:
- **Fasil Bin Gazyan** (فاصل بن غزيان)
- **Abdallah Al-Asmry** (عبدالله الأسمري)
- **Mishari Rashid** (مشاري راشد)

### Alert Sounds
Pre-recorded audio for common dhikr phrases:
- SubhanAllah (سبحان الله)
- Alhamdulillah (الحمد لله)
- La ilaha illallah (لا إله إلا الله)
- Allahu Akbar (الله أكبر)
- Salawat on the Prophet
- Istighfar (seeking forgiveness)

## Multi-Language Support

Content is available in multiple languages:
- Arabic (Primary)
- English translations
- Turkish (contentTr, vocalTr)
- French (contentFr)
- German (contentGr, vocalGr)
- Uyghur (contentUg, fadlUg)
- Indonesian (contentIn, fadlIn)

---
*Analysis continues in subsequent files...*
# Thekr - Executive Summary

## What is Thekr?

**Thekr** is a comprehensive Islamic remembrance (dhikr) application that serves as a digital companion for Muslims to maintain their daily spiritual practices.

## Core Value Proposition

The app replaces traditional prayer beads (tasbih/mesbaha) with a modern, feature-rich digital experience that:

1. **Tracks Progress** - Counts and records all remembrances with detailed statistics
2. **Provides Content** - Offers authentic supplications from trusted Islamic sources
3. **Sets Goals** - Helps users establish and achieve daily, weekly, monthly, and yearly spiritual goals
4. **Supports Multiple Platforms** - Works on Android and Desktop (with iOS/Web prepared)

## Key Differentiators

- **Authentic Content**: Pre-loaded with "Hesn Al-Muslim" (Fortress of the Muslim) - a widely trusted Islamic reference
- **Audio Support**: Multiple reciters for listening to proper pronunciation
- **Multi-Language**: Arabic primary with translations in 6+ languages
- **Comprehensive Categories**: 147 categories covering virtually every life situation
- **Accessibility**: Fingerprint, volume keys, vibration, and voice feedback options
- **Offline-First**: All content stored locally, no internet required

## Target Users

- Muslims seeking to maintain regular dhikr practice
- Users wanting to track their spiritual progress
- People learning authentic Islamic supplications
- Those preferring digital tools over physical prayer beads

## Business Model Indicators

The app appears to be:
- Free/open-source (no payment integrations visible)
- Privacy-focused (offline-first, local storage)
- Community-oriented (multi-language support)

---

## Report Files

1. [01-overview.md](./01-overview.md) - Project introduction and purpose
2. [02-features.md](./02-features.md) - Detailed feature analysis
3. [03-architecture.md](./03-architecture.md) - Technical architecture
4. [04-content.md](./04-content.md) - Content library analysis
5. [05-summary.md](./05-summary.md) - This executive summary
# Thekr - Counting Functionality Analysis

## How Counting Works

### User Interaction Flow

1. **Entry Point**: User selects a thekr (remembrance) from one of four tabs:
   - Mesbaha (custom user counters)
   - Hesn Al-Muslim (pre-loaded supplications)
   - Knooz (treasures collection)
   - Dua (supplications)

2. **Counter Screen**: Opens `ZekrScreen` with:
   - The thekr text displayed prominently
   - A horizontal pager to swipe between multiple thekrs in a category
   - Bottom sheet showing count statistics

3. **Counting Action**: User taps anywhere on the screen to count

### Count Trigger Methods

The app supports multiple input methods for counting:

```
┌─────────────────────────────────────────────┐
│           COUNT INPUT METHODS               │
├─────────────────────────────────────────────┤
│  1. Screen Tap      → Primary method        │
│  2. Volume Keys     → Optional (settings)   │
│  3. Fingerprint     → Android only (root)   │
└─────────────────────────────────────────────┘
```

### The Count Function (Core Logic)

Located in: `ui/counter/viewmodel/action/Count.kt`

```kotlin
fun ThekrCounterViewModel.onThekrCounterCount() {
    // 1. Reset anti-sleep timer
    restartSleepJop()
    
    // 2. Get current thekr data
    val thekrInstance = uiState.value.currentThekrInstance.value
    val count = getCurrentThekrCount().value
    val thekr = getCurrentThekr().value
    
    // 3. Check if clickable (cooldown not active)
    if (uiState.value.clickable) {
        count(thekr, thekrInstance, count, clickSound = true)
        
        // 4. Auto-advance if daily target reached
        if (count.dailyCount + 1 == thekrInstance.dailyTarget) {
            CounterHelper.scrollToNextThekr()
        }
    } else {
        // 5. Record missed count (during cooldown)
        miss(thekrInstance, count)
    }
}
```

### Count Processing Steps

When a valid count is registered:

```
┌──────────────────────────────────────────────────────────────┐
│                    COUNT PROCESSING                          │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  1. FEEDBACK                                                 │
│     ├── Play click sound (if enabled)                        │
│     ├── Vibrate device (if enabled, Android only)            │
│     └── Speak count/name (TTS, if enabled)                   │
│                                                              │
│  2. COOLDOWN                                                 │
│     ├── Set clickable = false                                │
│     ├── Wait for cooldown duration                           │
│     └── Set clickable = true                                 │
│                                                              │
│  3. DATABASE INSERT                                          │
│     └── Insert new Count record with:                        │
│         ├── thekrInstanceId                                  │
│         ├── thekrCategoryId                                  │
│         ├── value (total count + 1)                          │
│         └── timeCreated (current timestamp)                  │
│                                                              │
│  4. UI UPDATE                                                │
│     └── Increment all count displays:                        │
│         ├── dailyCount + 1                                   │
│         ├── weeklyCount + 1                                  │
│         ├── monthlyCount + 1                                 │
│         ├── yearlyCount + 1                                  │
│         └── totalCount + 1                                   │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### Cooldown System

Each thekr can have a configurable cooldown period to prevent accidental double-taps:

```kotlin
// In Count.kt - count() function
mutableUiState.update {
    it.copy(clickable = false)
}
viewModelScope.launch {
    delay(thekr.coolDown)  // Wait for cooldown duration
    mutableUiState.update {
        it.copy(clickable = true)
    }
}
```

**Cooldown Persistence**: If user exits and returns within cooldown period, the remaining cooldown is restored:

```kotlin
// In InitCoolDown.kt
val timeDiff = now - lastCountItemTime
if (timeDiff < cooldown - 500) {
    // Disable clicking
    delay(timeDiff - 500)
    // Re-enable clicking
}
```

### Missed Counts

When user taps during cooldown, a "miss" is recorded:

```kotlin
private fun ThekrCounterViewModel.miss(
    thekrInstance: ThekrInstanceDetails,
    count: ThekrCount
) {
    viewModelScope.launch {
        countMissRepository.insert(
            CountMiss(
                thekrInstanceId = thekrInstance.id,
                thekrCategoryId = categoryId,
                value = count.totalCount + 1,
                timeCreated = System.currentTimeMillis()
            )
        )
    }
}
```

This allows tracking user engagement patterns and potentially adjusting cooldown settings.

## Feedback System

### Sound Feedback

```kotlin
fun counterClickFeedBack(label: String, count: Long, clickSound: Boolean) {
    val currentSettings = currentSettings()
    
    if (clickSound && currentSettings.sound) {
        if (currentSettings.clickSound) {
            ClickSoundPlayer.clickSound()  // Play click_1.mp3
        }
    }
    
    if (Platform.isAndroid && currentSettings.vibration) {
        vibrate()
    }
}
```

### Vibration Patterns

The app includes multiple vibration patterns (Android only):
- Heartbeat
- Double/Triple pulse
- Morse SOS
- Ascending/Descending patterns
- Peaceful pulse
- Energetic buzz
- And more...

### Audio Playback

Users can listen to thekr recitations from multiple sheikhs:
- Fasil Bin Gazyan
- Abdallah Al-Asmry
- Mishari Rashid

```kotlin
fun ThekrCounterViewModel.onPlayAudio() {
    val soundFileName = getCurrentThekr().value.soundFileName
    val filePath = "files/thekr/${currentSettings().currentSheikh}/${soundFileName}.mp3"
    // Load and play audio...
}
```

---
*Analysis continues in subsequent files...*
# Thekr - Data Storage & Persistence

## Database Architecture

### Room Database Schema

The app uses Room (SQLite) with 8 main entities:

```
┌─────────────────────────────────────────────────────────────────┐
│                    DATABASE SCHEMA                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────┐     ┌──────────────────┐     ┌─────────────┐  │
│  │   Thekr     │────▶│  ThekrInstance   │◀────│   Count     │  │
│  │  (content)  │     │ (user's copy)    │     │  (records)  │  │
│  └─────────────┘     └──────────────────┘     └─────────────┘  │
│        │                     │                      │          │
│        │                     │                      │          │
│        ▼                     ▼                      ▼          │
│  ┌─────────────┐     ┌──────────────────┐     ┌─────────────┐  │
│  │  Category   │     │  ThekrGoalCompl  │     │  CountMiss  │  │
│  │ (grouping)  │     │   (achievements) │     │  (misses)   │  │
│  └─────────────┘     └──────────────────┘     └─────────────┘  │
│                                                                 │
│  ┌─────────────┐     ┌──────────────────┐                      │
│  │  ThekrFadl  │     │     Session      │                      │
│  │  (virtues)  │     │  (usage track)   │                      │
│  └─────────────┘     └──────────────────┘                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Entity Details

#### 1. Thekr (The Remembrance Text)
```kotlin
@Entity(tableName = "thekr")
data class Thekr(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var categoryId: Long = 0,
    var categoryName: String = "",
    var content: String = "",           // Arabic text
    var dailyTarget: Int = 0,
    var description: String? = null,
    var fadl: String? = null,           // Virtue/reward info
    
    // Multi-language support
    var contentEn: String? = null,      // English
    var contentTr: String? = null,      // Turkish
    var contentFr: String? = null,      // French
    var contentGr: String? = null,      // German
    var contentUg: String? = null,      // Uyghur
    var contentIn: String? = null,      // Indonesian
    
    // Audio
    var soundFileName: String? = "",
    var shortSoundFileName: String? = null,
    
    // Behavior
    var coolDown: Long = 0,             // Milliseconds between counts
    var isProtected: Boolean = false,   // Can't be deleted
    var editable: Boolean = true,
    
    // Timestamps
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
)
```

#### 2. ThekrInstance (User's Personal Copy)
```kotlin
@Entity(tableName = "thekr_instance")
data class ThekrInstance(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var thekrId: Long = 1,              // Links to Thekr
    var categoryId: Long = 1,
    var isProtected: Boolean = false,
    var editable: Boolean = true,
    
    // Goal targets (each can be enabled/disabled)
    var dailyTarget: Long = 0,
    var dailyTargetStatus: ThekrTargetStatus = Disabled,
    var weeklyTarget: Long = 0,
    var weeklyTargetStatus: ThekrTargetStatus = Disabled,
    var monthlyTarget: Long = 0,
    var monthlyTargetStatus: ThekrTargetStatus = Disabled,
    var yearlyTarget: Long = 0,
    var yearlyTargetStatus: ThekrTargetStatus = Disabled,
    
    var timeCreated: Long = now(),
    var timeUpdated: Long = timeCreated,
)
```

#### 3. Count (Individual Count Records)
```kotlin
@Entity(tableName = "count")
data class Count(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var thekrCategoryId: Long = 1,
    var thekrInstanceId: Long,
    var value: Long = 0,                // Running total at time of count
    var timeCreated: Long = now(),      // Timestamp for statistics
)
```

#### 4. Category (Organization)
```kotlin
@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    var parent: Long = 0,               // Hierarchical structure
    var iconFileName: String? = null,
)
```

### Count Aggregation (ThekrCount)

The app maintains aggregated counts in memory for fast UI updates:

```kotlin
data class ThekrCount(
    val thekrInstanceId: Long = 0,
    val categoryId: Long = 0,
    var dailyCount: Long = 0,
    var weeklyCount: Long = 0,
    var monthlyCount: Long = 0,
    var yearlyCount: Long = 0,
    var totalCount: Long = 0,
    var timeUpdated: Long = now(),
)
```

### Count DAO (Data Access)

```kotlin
@Dao
interface CountDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(count: Count)
    
    // Flexible query with optional filters
    @Query("""
        SELECT * FROM count
        WHERE (:thekrInstanceId IS NULL OR thekrInstanceId = :thekrInstanceId)
        AND (:timeCreatedAfter IS NULL OR timeCreated > :timeCreatedAfter)
        AND (:timeCreatedBefore IS NULL OR timeCreated < :timeCreatedBefore)
    """)
    fun findCounts(
        thekrInstanceId: Long? = null,
        timeCreatedAfter: Long? = null,
        timeCreatedBefore: Long? = null
    ): Flow<List<Count>>
    
    // Count aggregation
    @Query("""
        SELECT COUNT(*) FROM count
        WHERE (:thekrInstanceId IS NULL OR thekrInstanceId = :thekrInstanceId)
        AND (:timeCreatedAfter IS NULL OR timeCreated > :timeCreatedAfter)
        AND (:timeCreatedBefore IS NULL OR timeCreated < :timeCreatedBefore)
    """)
    fun getCount(...): Flow<Int>
    
    // For cooldown restoration
    @Query("SELECT * FROM count WHERE thekrInstanceId = :thekrInstanceId 
            ORDER BY timeCreated DESC LIMIT 1")
    fun getLastCountByThekrInstanceId(thekrInstanceId: Long): Flow<Count?>
}
```

## Settings Storage

Settings are stored using KStore (key-value storage):

```kotlin
data class SettingsDetails(
    // Display
    val language: String = "ar",
    val themeMode: ThemeMode = ThemeMode.Light,
    val fontSize: Float = 16f,
    val materialYou: Boolean = false,
    val screenAlwaysOn: Boolean = false,
    
    // Feedback
    val vibration: Boolean = true,
    val sound: Boolean = true,
    val clickSound: Boolean = true,
    val speechValue: Boolean = false,
    val speechName: Boolean = false,
    
    // Input
    val volumeControl: Boolean = false,
    val fingerPrintControl: Boolean = false,
    
    // Audio
    val currentSheikh: String = "FasilBnGazyan",
    
    // Count visibility
    val showCount: Boolean = true,
    val showDailyCount: Boolean = true,
    val showWeeklyCount: Boolean = true,
    val showMonthlyCount: Boolean = true,
    val showYearlyCount: Boolean = true,
    val showTotalCount: Boolean = true,
    val showSessionCount: Boolean = true,
    
    // State
    val initialized: Boolean = false,
    val dbInitialized: Boolean = false,
)
```

## Pre-loaded Content

Initial data is loaded from JSON files in resources:

```
composeResources/files/database/json/
├── category.json      # 147 categories
├── thekr.json         # All thekr texts
└── thekr_instance.json # Default instances
```

Plus a pre-built SQLite database:
```
composeResources/files/database/init.sqlite
```

---
*Analysis continues in subsequent files...*
# Thekr - Statistics & Goal Tracking

## Statistics System

### Overview

The app provides detailed statistics visualization using the Vico charting library:

```
┌─────────────────────────────────────────────────────────────┐
│                   STATISTICS VIEWS                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │    Daily    │  │   Weekly    │  │   Monthly   │         │
│  │   Stats     │  │   Stats     │  │   Stats     │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│        │                │                │                  │
│        ▼                ▼                ▼                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │  By Hour    │  │  By Day of  │  │  By Week    │         │
│  │  By Minute  │  │    Week     │  │  of Month   │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Daily Statistics

Two visualization modes:

#### 1. Hourly View (Column Chart)
Groups counts by hour of day (0-23):

```kotlin
private fun hourDayStatistics(todayCountItems: List<Count>): StatisticsData {
    val todayCountByHour = mutableMapOf<Int, Int>()
    
    // Initialize all hours to 0
    (0..23).forEach { hour ->
        todayCountByHour[hour] = 0
    }
    
    // Group counts by hour
    todayCountItems.forEach { countItem ->
        calendar.timeInMillis = countItem.timeCreated
        val hour = calendar[Calendar.HOUR_OF_DAY]
        todayCountByHour[hour] = todayCountByHour.getOrDefault(hour, 0) + 1
    }
    
    return StatisticsData(
        partial = columnPartial(todayCountByHour),
        maxY = maxY(todayCountByHour)
    )
}
```

#### 2. Minute View (Line Chart)
Groups counts by minute of day (0-1440):

```kotlin
fun minuteDayStatistics(
    currentDayCountItems: List<Count>,
    currentDayMidnight: Long
): StatisticsData {
    val currentDayCountByMinute = mutableMapOf<Int, Int>()
    
    // Initialize all minutes to 0
    (0..24 * 60).forEach { minute ->
        currentDayCountByMinute[minute] = 0
    }
    
    // Group counts by minute
    currentDayCountItems.forEach { countItem ->
        calendar.timeInMillis = countItem.timeCreated
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minuteOfHour = calendar[Calendar.MINUTE]
        val minute = hour * 60 + minuteOfHour
        currentDayCountByMinute[minute] = currentDayCountByMinute.getOrDefault(minute, 0) + 1
    }
    
    return StatisticsData(
        partial = linePartial(currentDayCountByMinute),
        maxY = maxY(currentDayCountByMinute)
    )
}
```

### Weekly Statistics

Groups counts by day of week (Saturday-Friday, following Islamic week):

```kotlin
fun calcWeekStatistics(viewModel: ThekrCounterViewModel, time: Long): StatisticsData {
    val weekStart = calcWeekStart(time)
    val weekEnd = calcWeekEnd(time)
    
    // Query counts within week range
    val countItems = countRepository.findCounts(thekrId, weekStart, weekEnd)
    
    // Map to day of week (0=Saturday, 6=Friday)
    val countByDay = mutableMapOf<Int, Int>()
    (0..6).forEach { day -> countByDay[day] = 0 }
    
    countItems.forEach { countItem ->
        calendar.timeInMillis = countItem.timeCreated
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        val day = when (dayOfWeek) {
            Calendar.SATURDAY -> 0
            Calendar.SUNDAY -> 1
            Calendar.MONDAY -> 2
            Calendar.TUESDAY -> 3
            Calendar.WEDNESDAY -> 4
            Calendar.THURSDAY -> 5
            Calendar.FRIDAY -> 6
            else -> 0
        }
        countByDay[day] = countByDay.getOrDefault(day, 0) + 1
    }
    
    return StatisticsData(
        partial = columnPartial(countByDay),
        maxY = maxY(countByDay)
    )
}
```

### Statistics Data Structure

```kotlin
data class StatisticsData(
    val partial: CartesianLayerModel.Partial,  // Chart data
    val maxY: Double? = null,                   // Y-axis max for scaling
    val minY: Double? = null,                   // Y-axis min
)
```

## Goal Tracking System

### Goal Types

Each ThekrInstance can have four independent goals:

| Goal Type | Reset Period | Status Options |
|-----------|--------------|----------------|
| Daily     | Midnight     | Enabled/Disabled/Impossible |
| Weekly    | Saturday     | Enabled/Disabled/Impossible |
| Monthly   | 1st of month | Enabled/Disabled/Impossible |
| Yearly    | Jan 1st      | Enabled/Disabled/Impossible |

### Goal Status Enum

```kotlin
enum class ThekrTargetStatus {
    Enabled,     // Goal is active and tracked
    Disabled,    // Goal is not tracked
    Impossible   // Goal cannot be achieved (e.g., past deadline)
}
```

### Progress Calculation

```kotlin
// In ZekrCount.kt
fun progressState(target: Long, count: Long): ProgressState {
    return when {
        target == 0L -> ProgressState.TargetIsZero
        count == 0L -> ProgressState.ScoreIsZero
        count == target -> ProgressState.EQUAL
        count > target -> ProgressState.BIGGER
        else -> ProgressState.SMALLER
    }
}
```

### Visual Progress Indicators

#### Circular Progress (Main Counter)
```kotlin
CircularProgressIndicator(
    progress = { progressAnimate.value },  // count / target
    color = progressColor,                  // Green when complete
    strokeWidth = 8.dp,
)
```

#### Linear Progress (Detailed View)
```kotlin
LinearProgressIndicator(
    progress = { progressAnimate.value },
    color = progressColor,
    trackColor = Color.Transparent,
)
```

### Progress Colors

```kotlin
fun progressColor(colors: AppColors, progressState: ProgressState): Color {
    return when (progressState) {
        ProgressState.EQUAL, 
        ProgressState.BIGGER -> colors.successColor    // Green - goal met!
        ProgressState.SMALLER -> colors.progressColor  // Blue - in progress
        else -> MaterialTheme.colorScheme.onSurface    // Default
    }
}
```

### Auto-Advance on Goal Completion

When daily target is reached, automatically scroll to next thekr:

```kotlin
// In Count.kt
if (count.dailyCount + 1 == thekrInstance.dailyTarget) {
    CounterHelper.scrollToNextThekr()
}
```

## Count Display Options

Users can customize which counts are visible:

```kotlin
// In SettingsDetails
val showCount: Boolean = true,        // Master toggle
val showDailyCount: Boolean = true,
val showWeeklyCount: Boolean = true,
val showMonthlyCount: Boolean = true,
val showYearlyCount: Boolean = true,
val showTotalCount: Boolean = true,
val showSessionCount: Boolean = true,
```

### Detailed Count View

Shows all enabled count types with progress bars:

```
┌─────────────────────────────────────────────┐
│  Daily    ████████████░░░░░░░░  50 / 100   │
│  Weekly   ██████░░░░░░░░░░░░░░  150 / 500  │
│  Monthly  ████░░░░░░░░░░░░░░░░  300 / 2000 │
│  Yearly   █░░░░░░░░░░░░░░░░░░░  1200/10000 │
│  ─────────────────────────────────────────  │
│  Total                           12,450     │
└─────────────────────────────────────────────┘
```

---
*Analysis continues in subsequent files...*
# Thekr - User Flows & Navigation

## App Structure

```
┌─────────────────────────────────────────────────────────────────┐
│                        HOME SCREEN                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────┬──────────────┬──────────┬──────────┐             │
│  │ Mesbaha  │ Hesn Muslim  │  Knooz   │   Dua    │  ◀─ Tabs    │
│  └──────────┴──────────────┴──────────┴──────────┘             │
│                      │                                          │
│                      ▼                                          │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                                                         │   │
│  │              Category/Thekr List                        │   │
│  │                                                         │   │
│  │   ┌─────────────────────────────────────────────────┐   │   │
│  │   │  📿 أذكار الصباح (Morning Remembrances)         │   │   │
│  │   └─────────────────────────────────────────────────┘   │   │
│  │   ┌─────────────────────────────────────────────────┐   │   │
│  │   │  🌙 أذكار المساء (Evening Remembrances)         │   │   │
│  │   └─────────────────────────────────────────────────┘   │   │
│  │   ┌─────────────────────────────────────────────────┐   │   │
│  │   │  🕌 أذكار الصلاة (Prayer Remembrances)          │   │   │
│  │   └─────────────────────────────────────────────────┘   │   │
│  │                        ...                              │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## Main User Flows

### Flow 1: Counting with Pre-loaded Content

```
┌─────────┐    ┌──────────────┐    ┌─────────────┐    ┌───────────┐
│  Home   │───▶│ Select Tab   │───▶│  Category   │───▶│  Counter  │
│ Screen  │    │(Hesn/Knooz)  │    │   List      │    │  Screen   │
└─────────┘    └──────────────┘    └─────────────┘    └───────────┘
                                          │                  │
                                          ▼                  ▼
                                   ┌─────────────┐    ┌───────────┐
                                   │ Sub-Category│    │   TAP!    │
                                   │    List     │    │  to count │
                                   └─────────────┘    └───────────┘
```

### Flow 2: Custom Counter (Mesbaha Tab)

```
┌─────────┐    ┌──────────────┐    ┌─────────────┐    ┌───────────┐
│  Home   │───▶│ Mesbaha Tab  │───▶│ Create New  │───▶│  Counter  │
│ Screen  │    │              │    │  Category   │    │  Screen   │
└─────────┘    └──────────────┘    └─────────────┘    └───────────┘
                     │                                      │
                     ▼                                      ▼
              ┌──────────────┐                       ┌───────────┐
              │ Select User  │                       │   TAP!    │
              │   Category   │                       │  to count │
              └──────────────┘                       └───────────┘
                     │
                     ▼
              ┌──────────────┐
              │  Add Custom  │
              │    Thekr     │
              └──────────────┘
```

### Flow 3: Viewing Statistics

```
┌───────────┐    ┌──────────────┐    ┌─────────────────────────┐
│  Counter  │───▶│ Stats Button │───▶│    Statistics View      │
│  Screen   │    │  (Top Bar)   │    │                         │
└───────────┘    └──────────────┘    │  ┌─────┬───────┬─────┐  │
                                     │  │Daily│Weekly │Month│  │
                                     │  └─────┴───────┴─────┘  │
                                     │                         │
                                     │   📊 Chart Display      │
                                     │                         │
                                     └─────────────────────────┘
```

## Counter Screen Layout

```
┌─────────────────────────────────────────────────────────────────┐
│  ◀ Back    أذكار الصباح    ⚙️ 📊 🔊                    │ Top Bar │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                                                                 │
│                                                                 │
│                    بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ              │
│                                                                 │
│              ◀────────  Swipe for more  ────────▶               │
│                                                                 │
│                     [ TAP ANYWHERE TO COUNT ]                   │
│                                                                 │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                    ● ○ ○ ○ ○  (Page Indicator)                  │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Daily    ████████████░░░░░░░░  50 / 100               │   │
│  │  Weekly   ██████░░░░░░░░░░░░░░  150 / 500              │   │
│  │  Total                           12,450                 │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## Navigation Architecture

### Screen Routes

```kotlin
// Main routes
sealed class NavigationRoute {
    object HomeRoute : NavigationRoute()
    data class ThekrScreenRoute(
        val categoryId: Long,
        val thekrId: Long,
        val initialPage: Int,
        val pageCount: Int
    ) : NavigationRoute()
    object SettingsRoute : NavigationRoute()
}
```

### Tab Structure

```kotlin
enum class HomeTab {
    Mesbaha,      // Tab 0 - User custom counters
    HesnAlMuslim, // Tab 1 - Fortress of the Muslim
    Knooz,        // Tab 2 - Treasures
    Dua           // Tab 3 - Supplications
}
```

### Category Navigation Stack

Each tab maintains its own navigation stack for hierarchical categories:

```kotlin
// In AppState
val hesnAlmuslimStack: SnapshotStateList<MutableState<CategoryDetails>>
val knoozStack: SnapshotStateList<MutableState<CategoryDetails>>
val duaCategoryStack: SnapshotStateList<MutableState<CategoryDetails>>
```

Back navigation pops from the stack:
```kotlin
fun navigateToParentCategory(tabIndex: Int) {
    when (tabIndex) {
        HomeTab.HesnAlMuslim.tabIndex -> hesnAlmuslimStack.removeLast()
        HomeTab.Knooz.tabIndex -> knoozStack.removeLast()
        HomeTab.Dua.tabIndex -> duaCategoryStack.removeLast()
    }
}
```

## Interaction Patterns

### Screen Tap Counting

```kotlin
Box(
    modifier = modifier
        .clickable(
            interactionSource = NoRippleInteractionSource(),
            indication = LocalIndication.current,
            onClick = { CounterHelper.onCount() }
        )
)
```

### Volume Key Counting

```kotlin
.customOnKeyEvent(
    enabled = settingsDetails.volumeControl,
    focusRequester = focusRequester
)
```

### Horizontal Paging

```kotlin
HorizontalPager(
    state = pagerState,
    userScrollEnabled = counterUiState.lockEnabled.not()
) { tabIndex ->
    // Display thekr at tabIndex
}
```

### Back Handler

```kotlin
BackHandler(true) {
    if (counterUiState.lockEnabled) {
        // Show snackbar - can't go back while locked
    } else {
        CounterHelper.onNavigateUp()
    }
}
```

## Anti-Sleep System

Prevents screen from turning off during counting:

```kotlin
@Composable
fun KeepScreenOn(enabled: Boolean, content: @Composable () -> Unit) {
    if (enabled) {
        // Platform-specific wake lock
    }
    content()
}
```

Also detects user inactivity and can alert them:

```kotlin
// In AntiSleep.kt
fun configSleepJop(viewModel: ThekrCounterViewModel) {
    // Start job to detect sleeping
    // Alert user if inactive for too long
}

fun restartSleepJop() {
    // Reset inactivity timer on each count
}
```

---
*End of functionality analysis*
