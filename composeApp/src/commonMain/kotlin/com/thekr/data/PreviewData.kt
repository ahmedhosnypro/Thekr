package com.thekr.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.zekr.zekr.ZekrDetails

@Composable
fun zekrCardListPreviewData(): SnapshotStateList<ZekrDetails> {
    return remember {
        mutableStateListOf(
            ZekrDetails(
                text = "سبحان الله وبحمده سبحان الله العظيم",
            ),
            ZekrDetails(
                text = "الحمد لله",
            ),
            ZekrDetails(
                text = "لا إله إلا الله",
            ),
            ZekrDetails(
                text = "الله أكبر",
            ),
        )
    }
}


@Composable
fun emptyZekrCardListPreviewData() = remember {
    mutableStateListOf<ZekrDetails>()
}

@Composable
fun trueState() = remember {
    mutableStateOf(true)
}
