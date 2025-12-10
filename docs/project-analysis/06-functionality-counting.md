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
