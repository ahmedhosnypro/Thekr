package com.thekr.ui.counter.viewModel


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thekr.data.count.count.CountRepository
import com.thekr.data.count.miss.CountMissRepository
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.data.zekr.zekr.ZekrRepository
import com.thekr.ui.counter.viewModel.action.AntiSleep.killDetectSleepingJob
import com.thekr.ui.counter.viewModel.action.AntiSleep.stopDetectSleepingJob
import com.thekr.ui.counter.viewModel.action.configSleepJop

import com.thekr.ui.counter.viewModel.init.initCoolDown
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.HomeRoute
import com.thekr.ui.navigation.route.ZekrScreenRoute
import com.thekr.ui.viewmodel.AzkarStateHelper.azkarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ZekrCounterViewModel(
    savedStateHandle: SavedStateHandle,
    val zekrRepository: ZekrRepository,
    val countRepository: CountRepository,
    val countMissRepository: CountMissRepository,
) : ViewModel() {
    val categoryId: Long = checkNotNull(savedStateHandle[ZekrScreenRoute.CATEGORY_ID_ARG])

    val zekrId: Long = checkNotNull(savedStateHandle[ZekrScreenRoute.ZEKR_ID_ARG])
    val initialPage: Int = checkNotNull(
        savedStateHandle[ZekrScreenRoute.INITIAL_PAGE_ARG]
    )
    val pageCount: Int = checkNotNull(
        savedStateHandle[ZekrScreenRoute.PAGE_COUNT_ARG]
    )

//    val destination = savedStateHandle.toRoute<ZekrScreenRoute>()
//    val categoryId: Long = destination.categoryId
//    val zekrId: Long = destination.zekrId
//    val initialPage: Int = destination.initialPage
//    val pageCount: Int = destination.pageCount

    val mutableUiState = MutableStateFlow(CounterUiState(
        categoryDetails = azkarState.categoryList.first { it.value.id == categoryId }
    ))
    val uiState = mutableUiState.asStateFlow()

    var firstTime = true

//    val zekrSoundPlayer = ExoPlayer.Builder(appContext()).build()
//
//    private var fingerprintEventListener: FingerprintEventListener? = null

    init {
        initCoolDown(this)
        // todo: add validation for count items
        initClickSoundPlayer()
//        observePlayerEvents()
    }

    private fun initClickSoundPlayer() {
//        clickSoundPlayer = ExoPlayer.Builder(appContext()).build()
//        val soundFileName = "click_1.mp3"
//        val soundResourceId = SoundResourceHelper.getResourceIdFromFileName(soundFileName)
//        if (soundResourceId != null) {
//            val mediaItem =
//                MediaItem.fromUri("android.resource://${appContext().packageName}/$soundResourceId")
//
//            clickSoundPlayer?.setMediaItem(mediaItem)
//            clickSoundPlayer?.prepare()
//        }
    }

    fun setFingerprintListener(
        fingerPrintEnabled: Boolean,
    ) {
//        if (fingerprintEventListener != null) {
//            FingerprintEventDispatcher.removeListener(fingerprintEventListener!!)
//        }
//
//        if (fingerPrintEnabled) {
//            val listener = FingerprintEventListener { fingerprintEvent ->
//                if (fingerprintEvent.type == FingerprintEventType.TouchUp) {
//                    onZekrCounterCount()
//                }
//            }
//            this.fingerprintEventListener = listener
//            FingerprintEventDispatcher.addListener(listener)
//        }
    }

    fun delete() {
        if (uiState.value.currentZekrInstance.value.isProtected.not()) {
            viewModelScope.launch {
                zekrRepository.deleteIfNotProtected(uiState.value.currentZekrInstance.value.id)
            }
        }
    }

    fun onCounterDispose() {
        stopDetectSleepingJob()
//        stopPlayer(this)
    }

    fun onNavigateUp() {
//        NavigationActions.navigateUp(HomeRoute::class)
        NavigationActions.navigateUp(HomeRoute.route)
        killDetectSleepingJob()
    }

    fun updateCurrentZekrInstance(index: Int) {
        val zekrInstanceDetails =
            uiState.value.categoryDetails.value.zekrInstanceList.getOrNull(index)
        if (zekrInstanceDetails != null) {
            mutableUiState.update { currentState ->
                currentState.copy(
                    currentZekrInstance = zekrInstanceDetails
                )
            }
        }
    }


    fun showCategoryZekrListMenu() {
        mutableUiState.update { currentState ->
            currentState.copy(
                showCategoryZekrListMenu = true
            )
        }
        stopDetectSleepingJob()
    }

    fun hideCategoryZekrListMenu() {
        mutableUiState.update { currentState ->
            currentState.copy(
                showCategoryZekrListMenu = false
            )
        }
        configSleepJop(this)
    }

//    fun appContext(): Context = getApplication<Application>().applicationContext


    //    fun isPlayerPlaying() = zekrSoundPlayer.isPlaying
    fun isPlayerPlaying() = false

    fun getCurrentZekrInstance(): MutableState<ZekrInstanceDetails> =
        uiState.value.currentZekrInstance

    fun getZekrCount(tabIndex: Int): MutableState<ZekrCount> {
        val zekrInstance = uiState.value.categoryDetails.value.zekrInstanceList.getOrNull(tabIndex)
        return uiState.value.categoryDetails.value.countList.firstOrNull { it.value.zekrInstanceId == zekrInstance?.value?.zekrId }
            ?: mutableStateOf(ZekrCount())
    }

    fun getCurrentZekrCount(): MutableState<ZekrCount> {
        val currentZekrInstance = uiState.value.currentZekrInstance
        return uiState.value.categoryDetails.value.countList.firstOrNull { it.value.zekrInstanceId == currentZekrInstance.value.zekrId }
            ?: throw IllegalStateException("getCurrentZekrCount: can't find one")
    }

    fun getZekr(tabIndex: Int): MutableState<ZekrDetails> {
        val zekrInstance = uiState.value.categoryDetails.value.zekrInstanceList.getOrNull(tabIndex)
        return uiState.value.categoryDetails.value.zekrList.firstOrNull { it.value.id == zekrInstance?.value?.zekrId }
            ?: mutableStateOf(ZekrDetails())
    }

    fun getCurrentZekr(): MutableState<ZekrDetails> {
        val currentZekrInstance = uiState.value.currentZekrInstance
        return uiState.value.categoryDetails.value.zekrList.firstOrNull { it.value.id == currentZekrInstance.value.zekrId }
            ?: mutableStateOf(ZekrDetails())
    }

    fun getZekrInstance(tabIndex: Int): MutableState<ZekrInstanceDetails> {
        return uiState.value.categoryDetails.value.zekrInstanceList.getOrNull(tabIndex)
            ?: mutableStateOf(ZekrInstanceDetails())
    }

    fun updateUiState(counterUiState: CounterUiState) {
        mutableUiState.update {
            counterUiState
        }
    }

    fun hideStatistics() {
        mutableUiState.update {
            it.copy(
                showStatistics = false
            )
        }
        configSleepJop(this)
    }

    fun showStatistics() {
        mutableUiState.update {
            it.copy(
                showStatistics = true
            )
        }
        stopDetectSleepingJob()
    }

    fun updateOnCount() {
        var count by getCurrentZekrCount()
        count = count.copy(
            dailyCount = count.dailyCount + 1,
            weeklyCount = count.weeklyCount + 1,
            monthlyCount = count.monthlyCount + 1,
            yearlyCount = count.yearlyCount + 1,
            totalCount = count.totalCount + 1,
            timeUpdated = System.currentTimeMillis()
        )
    }

    companion object {
//        var clickSoundPlayer: ExoPlayer? = null
    }
}

fun interface SuspendRunnable {
    suspend fun run()
}
