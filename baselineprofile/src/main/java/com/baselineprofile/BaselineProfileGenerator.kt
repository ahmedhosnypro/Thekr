package com.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class generates a baseline profile for the target package, covering the app's
 * critical user journeys:
 *
 * 1. Cold start to the home screen (list load + scroll).
 * 2. Paging through the home tabs (Mesbaha / Hesn Al Muslim / Knooz / Dua) and back to Mesbaha.
 * 3. A counter (tasbih) session: open a category and tap the counter a few times.
 * 4. Navigate to the Settings screen and back.
 *
 * All navigation steps are best-effort (null-safe), so the generator does not fail when the
 * content state differs (e.g. fresh install with no categories). The Settings entry point is
 * matched by content description in English and Arabic, since the app labels are localized.
 *
 * You can run the generator with the "Generate Baseline Profile" run configuration in Android Studio or
 * the equivalent `generateBaselineProfile` gradle task:
 * ```
 * ./gradlew :composeApp:generateReleaseBaselineProfile
 * ```
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 *
 * Generation requires a connected physical device or emulator on API 33+ (or a rooted device
 * on API 28+); the [baselineprofile] module is configured with `useConnectedDevices = true`.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 *
 * The minimum required version of androidx.benchmark to generate a baseline profile is 1.2.0.
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        // The application id for the running build variant is read from the instrumentation arguments.
        rule.collect(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),

            // See: https://d.android.com/topic/performance/baselineprofiles/dex-layout-optimizations
            includeInStartupProfile = true
        ) {
            val displayWidth = device.displayWidth
            val displayHeight = device.displayHeight
            val centerX = displayWidth / 2
            val centerY = displayHeight / 2

            // 1. Start the default activity (home screen)
            pressHome()
            startActivityAndWait()

            // 2. Wait until the home list is asynchronously loaded from the database
            device.wait(Until.hasObject(By.scrollable(true)), 5_000)

            // 3. Scroll the home list down and back up
            device.findObject(By.scrollable(true))?.let { list ->
                list.fling(Direction.DOWN)
                list.fling(Direction.UP)
            }

            // 4. Page through the home tabs (HorizontalPager): Mesbaha -> Hesn Al Muslim -> Knooz -> Dua
            repeat(3) {
                device.swipe(centerX + centerX / 2, centerY, centerX / 2, centerY, 10)
                device.waitForIdle()
            }

            // 5. Swipe back to the Mesbaha (sebha) tab and open the first category card for a
            //    counter session: tap the counter a few times
            repeat(3) {
                device.swipe(centerX / 2, centerY, centerX + centerX / 2, centerY, 10)
                device.waitForIdle()
            }
            device.findObjects(By.clickable(true))
                .filter { it.visibleBounds.top > displayHeight / 4 }
                .minByOrNull { it.visibleBounds.top }
                ?.click()
            device.waitForIdle()
            repeat(3) {
                device.click(centerX, centerY)
                device.waitForIdle()
            }
            device.pressBack()
            device.waitForIdle()

            // 6. Navigate to the Settings screen and back
            val settingsIcon = device.findObject(By.desc("Settings"))
                ?: device.findObject(By.desc("الإعدادات"))
            settingsIcon?.click()
            device.waitForIdle()
            device.pressBack()
        }
    }
}
