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
