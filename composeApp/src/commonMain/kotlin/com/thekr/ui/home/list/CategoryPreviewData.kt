package com.thekr.ui.home.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.zekr.category.CategoryDetails
import com.thekr.data.zekr.count.ZekrCount
import com.thekr.data.zekr.fadl.FadlDetails
import com.thekr.data.zekr.instance.ZekrInstanceDetails
import com.thekr.data.zekr.zekr.ZekrDetails
import com.thekr.model.ZekrTargetStatus

@Composable
fun categoryDetailsPreviewState(
    zekrList: SnapshotStateList<MutableState<ZekrDetails>> = zekrPreviewList(),
    zekrInstanceList: SnapshotStateList<MutableState<ZekrInstanceDetails>> = zekrInstanceList(),
    fadlList: SnapshotStateList<FadlDetails> = fadlPreviewList(),
    childCategories: SnapshotStateList<MutableState<CategoryDetails>> = categoryDetailsListPreviewState(),
    countList: SnapshotStateList<MutableState<ZekrCount>> = countPreviewList()
): MutableState<CategoryDetails> {
    return remember {
        mutableStateOf(
            CategoryDetails(
                zekrList = zekrList,
                zekrInstanceList = zekrInstanceList,
                fadlList = fadlList,
                childCategories = childCategories,
                countList = countList
            )
        )
    }
}


@Composable
fun zekrPreviewList() = remember {
    mutableStateListOf(
        mutableStateOf(
            ZekrDetails(
                id = 1,
                text = "سبحان الله وبحمده سبحان الله العظيم",
                editable = true,
                soundFileName = "sound.mp3",
            )
        ),
        mutableStateOf(
            ZekrDetails(
                id = 2,
                text = "اللهم صل وسلم على سيدنا محمد",
            )
        )
    )
}


@Composable
fun zekrInstanceList(): SnapshotStateList<MutableState<ZekrInstanceDetails>> = remember {
    mutableStateListOf(
        mutableStateOf(
            ZekrInstanceDetails(
                id = 1,
                zekrId = 1,
                categoryId = 1,
                dailyTarget = 100,
                dailyTargetStatus = ZekrTargetStatus.Enabled,
                weeklyTarget = 200,
                weeklyTargetStatus = ZekrTargetStatus.Enabled,
                monthlyTarget = 300,
                monthlyTargetStatus = ZekrTargetStatus.Enabled,
                yearlyTarget = 400,
                yearlyTargetStatus = ZekrTargetStatus.Enabled,

                )
        ),
        mutableStateOf(
            ZekrInstanceDetails(
                id = 2,
                zekrId = 2,
                categoryId = 1,
                dailyTarget = 100,
                dailyTargetStatus = ZekrTargetStatus.Enabled,
            )
        )
    )
}

@Composable
fun fadlPreviewList() = remember {
    mutableStateListOf(
        FadlDetails(
            id = 1,
            zekrId = 1,
            fadl = "الفضل الأول",
        )
    )
}

@Composable
fun categoryDetailsListPreviewState(): SnapshotStateList<MutableState<CategoryDetails>> = remember {
    mutableStateListOf(
        mutableStateOf(
            CategoryDetails(
                id = 2,
                name = "أذكار الصباح"
            )
        ),
        mutableStateOf(
            CategoryDetails(
                id = 3,
                name = "أذكار المساء"
            )
        ),
        mutableStateOf(
            CategoryDetails(
                id = 4,
                name = "أذكار النوم"
            )
        )
    )
}

@Composable
fun countPreviewList(): SnapshotStateList<MutableState<ZekrCount>> = remember {
    mutableStateListOf(
        mutableStateOf(
            ZekrCount(
                zekrInstanceId = 1,
                dailyCount = 100,
                weeklyCount = 200,
                monthlyCount = 300,
                yearlyCount = 400,
                totalCount = 500,
            )
        ),
        mutableStateOf(
            ZekrCount(
                zekrInstanceId = 2,
                dailyCount = 100,
                weeklyCount = 200,
                monthlyCount = 300,
                yearlyCount = 400,
                totalCount = 500,
            )
        )
    )
}