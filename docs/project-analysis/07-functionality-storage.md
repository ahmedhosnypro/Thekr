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
