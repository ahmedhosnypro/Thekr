package com.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
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
 * This test class benchmarks the speed of app startup.
 * Run this benchmark to verify how effective a Baseline Profile is.
 * It does this by comparing [CompilationMode.None], which represents the app with no Baseline
 * Profiles optimizations, and [CompilationMode.Partial], which uses Baseline Profiles.
 *
 * Both variants exercise the same critical user journey in the measure block: cold start,
 * waiting for the home list to load asynchronously, and scrolling it — the code paths the
 * generated baseline profile covers.
 *
 * Two additional journey pairs measure the frame timing of the counter (tasbih) session —
 * entering a category and tapping the thekr counter — and of paging through the home tabs
 * (Mesbaha / Hesn Al Muslim / Knooz / Dua) with the same compilation-mode comparison.
 *
 * Run this benchmark to see startup measurements and captured system traces for verifying
 * the effectiveness of your Baseline Profiles. You can run it directly from Android
 * Studio as an instrumentation test, or run all benchmarks for a variant, for example benchmarkRelease,
 * with this Gradle task:
 * ```
 * ./gradlew :baselineprofile:connectedBenchmarkReleaseAndroidTest
 * ```
 *
 * You should run the benchmarks on a physical device, not an Android emulator, because the
 * emulator doesn't represent real world performance and shares system resources with its host.
 * A physical device on API 33+ (or a rooted device on API 28+) is required so the
 * [CompilationMode.Partial] variant can install the generated baseline profile.
 *
 * For more information, see the [Macrobenchmark documentation](https://d.android.com/macrobenchmark#create-macrobenchmark)
 * and the [instrumentation arguments documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args).
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        benchmark(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    @Test
    fun counterSessionCompilationNone() =
        counterSession(CompilationMode.None())

    @Test
    fun counterSessionCompilationBaselineProfiles() =
        counterSession(CompilationMode.Partial(BaselineProfileMode.Require))

    @Test
    fun tabPagingCompilationNone() =
        tabPaging(CompilationMode.None())

    @Test
    fun tabPagingCompilationBaselineProfiles() =
        tabPaging(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun benchmark(compilationMode: CompilationMode) {
        // The application id for the running build variant is read from the instrumentation arguments.
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()

                // Wait until the home list is asynchronously loaded from the database,
                // then scroll it — the same journey the baseline profile covers.
                device.wait(Until.hasObject(By.scrollable(true)), 5_000)
                device.findObject(By.scrollable(true))?.let { list ->
                    list.fling(Direction.DOWN)
                    list.fling(Direction.UP)
                }
            },
        )
    }

    private fun counterSession(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            metrics = listOf(FrameTimingMetric()),
            compilationMode = compilationMode,
            iterations = 5,
            setupBlock = {
                pressHome()
                startActivityAndWait()
                device.wait(Until.hasObject(By.scrollable(true)), 5_000)

                // Enter the first category card to reach the thekr counting screen.
                val displayHeight = device.displayHeight
                device.findObjects(By.clickable(true))
                    .filter { it.visibleBounds.top > displayHeight / 4 }
                    .minByOrNull { it.visibleBounds.top }
                    ?.click()
                device.waitForIdle()
            },
            measureBlock = {
                repeat(3) {
                    device.click(device.displayWidth / 2, device.displayHeight / 2)
                    device.waitForIdle()
                }
            },
        )
    }

    private fun tabPaging(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
                ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
            metrics = listOf(FrameTimingMetric()),
            compilationMode = compilationMode,
            iterations = 5,
            setupBlock = {
                pressHome()
                startActivityAndWait()
                device.waitForIdle()
            },
            measureBlock = {
                // Page through the home tabs (HorizontalPager):
                // Mesbaha -> Hesn Al Muslim -> Knooz -> Dua, then back to Mesbaha (sebha).
                val centerX = device.displayWidth / 2
                val centerY = device.displayHeight / 2
                repeat(3) {
                    device.swipe(centerX + centerX / 2, centerY, centerX / 2, centerY, 10)
                    device.waitForIdle()
                }
                repeat(3) {
                    device.swipe(centerX / 2, centerY, centerX + centerX / 2, centerY, 10)
                    device.waitForIdle()
                }
            },
        )
    }
}
