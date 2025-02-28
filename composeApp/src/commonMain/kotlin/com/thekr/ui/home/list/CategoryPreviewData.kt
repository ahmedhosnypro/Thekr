package com.thekr.ui.home.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.thekr.category.CategoryDetails
import com.thekr.data.thekr.count.ThekrCount
import com.thekr.data.thekr.fadl.FadlDetails
import com.thekr.data.thekr.instance.ThekrInstanceDetails
import com.thekr.data.thekr.thekr.ThekrDetails
import com.thekr.model.ThekrTargetStatus

@Composable
fun categoryDetailsPreviewState(
    thekrList: SnapshotStateList<MutableState<ThekrDetails>> = thekrPreviewList(),
    thekrInstanceList: SnapshotStateList<MutableState<ThekrInstanceDetails>> = thekrInstanceList(),
    fadlList: SnapshotStateList<FadlDetails> = fadlPreviewList(),
    childCategories: SnapshotStateList<MutableState<CategoryDetails>> = categoryDetailsListPreviewState(),
    countList: SnapshotStateList<MutableState<ThekrCount>> = countPreviewList()
): MutableState<CategoryDetails> {
    return remember {
        mutableStateOf(
            CategoryDetails(
                thekrList = thekrList,
                thekrInstanceList = thekrInstanceList,
                fadlList = fadlList,
                childCategories = childCategories,
                countList = countList
            )
        )
    }
}


@Composable
fun thekrPreviewList() = remember {
    mutableStateListOf(
        mutableStateOf(
            ThekrDetails(
                id = 1,
                text = "سبحان الله وبحمده سبحان الله العظيم",
                editable = true,
                soundFileName = "sound.mp3",
            )
        ),
        mutableStateOf(
            ThekrDetails(
                id = 2,
                text = "اللهم صل وسلم على سيدنا محمد",
            )
        )
    )
}


@Composable
fun thekrInstanceList(): SnapshotStateList<MutableState<ThekrInstanceDetails>> = remember {
    mutableStateListOf(
        mutableStateOf(
            ThekrInstanceDetails(
                id = 1,
                thekrId = 1,
                categoryId = 1,
                dailyTarget = 100,
                dailyTargetStatus = ThekrTargetStatus.Enabled,
                weeklyTarget = 200,
                weeklyTargetStatus = ThekrTargetStatus.Enabled,
                monthlyTarget = 300,
                monthlyTargetStatus = ThekrTargetStatus.Enabled,
                yearlyTarget = 400,
                yearlyTargetStatus = ThekrTargetStatus.Enabled,

                )
        ),
        mutableStateOf(
            ThekrInstanceDetails(
                id = 2,
                thekrId = 2,
                categoryId = 1,
                dailyTarget = 100,
                dailyTargetStatus = ThekrTargetStatus.Enabled,
            )
        )
    )
}

@Composable
fun fadlPreviewList() = remember {
    mutableStateListOf(
        FadlDetails(
            id = 1,
            thekrId = 1,
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
fun countPreviewList(): SnapshotStateList<MutableState<ThekrCount>> = remember {
    mutableStateListOf(
        mutableStateOf(
            ThekrCount(
                thekrInstanceId = 1,
                dailyCount = 100,
                weeklyCount = 200,
                monthlyCount = 300,
                yearlyCount = 400,
                totalCount = 500,
            )
        ),
        mutableStateOf(
            ThekrCount(
                thekrInstanceId = 2,
                dailyCount = 100,
                weeklyCount = 200,
                monthlyCount = 300,
                yearlyCount = 400,
                totalCount = 500,
            )
        )
    )
}