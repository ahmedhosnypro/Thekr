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
 * A third startup pair, [startupFreshInstallCompilationNone] /
 * [startupFreshInstallCompilationBaselineProfiles], measures the FIRST-RUN cold start:
 * app data is cleared before each measured start so the JSON seed import that populates
 * the database on a fresh install is inside the measured window. Compare it with the
 * steady-state [startupCompilationNone] / [startupCompilationBaselineProfiles] pair to
 * isolate the seeding cost from the pure cold-start cost.
 *
 * Both startup pairs use [StartupTimingMetric], which reports timeToInitialDisplay and —
 * since the app reports its fully-drawn moment (CounterApp's LoadScreen -> content
 * transition calls Activity.reportFullyDrawn) — a timeToFullyDrawn (TTFD) section per
 * iteration. TTFD is therefore captured by both the steady-state and the fresh-install
 * methods; before that anchor existed, only timeToInitialDisplay was reported. The
 * frame-timing journeys (counterSession / tabPaging) do not report TTFD — they measure
 * already-started frames, not startup.
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

    @Test
    fun startupFreshInstallCompilationNone() =
        freshInstallBenchmark(CompilationMode.None())

    @Test
    fun startupFreshInstallCompilationBaselineProfiles() =
        freshInstallBenchmark(CompilationMode.Partial(BaselineProfileMode.Require))

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

    /**
     * Cold-start benchmark of the FIRST-RUN path: app data is wiped with
     * `pm clear` immediately before each measured start, so the JSON seed
     * import (the first-run database population) is inside the measured
     * window instead of the steady-state, already-seeded start that
     * [benchmark] measures. The clear itself is not part of the timed window —
     * StartupTimingMetric measures from the activity start intent.
     *
     * The generated baseline profile is unaffected by the data clear (it is
     * dex-level), so the compilation-mode comparison remains meaningful for
     * the first-run path too. The list-wait timeout is longer than
     * [benchmark]'s because the seed import runs before content composes.
     */
    private fun freshInstallBenchmark(compilationMode: CompilationMode) {
        // The application id for the running build variant is read from the instrumentation arguments.
        val packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg")
        rule.measureRepeated(
            packageName = packageName,
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                // Reset to first-run state: no database, no settings — the next
                // cold start must re-import the JSON seed before content shows.
                device.executeShellCommand("pm clear $packageName")

                startActivityAndWait()

                // Wait for the home list — composed only after the first-run
                // JSON seed import has populated the database.
                device.wait(Until.hasObject(By.scrollable(true)), 15_000)
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
