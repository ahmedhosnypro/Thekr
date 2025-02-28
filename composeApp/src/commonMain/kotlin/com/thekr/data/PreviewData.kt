package com.thekr.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.thekr.data.thekr.thekr.ThekrDetails

@Composable
fun thekrCardListPreviewData(): SnapshotStateList<ThekrDetails> {
    return remember {
        mutableStateListOf(
            ThekrDetails(
                text = "سبحان الله وبحمده سبحان الله العظيم",
            ),
            ThekrDetails(
                text = "الحمد لله",
            ),
            ThekrDetails(
                text = "لا إله إلا الله",
            ),
            ThekrDetails(
                text = "الله أكبر",
            ),
        )
    }
}


@Composable
fun emptyThekrCardListPreviewData() = remember {
    mutableStateListOf<ThekrDetails>()
}

@Composable
fun trueState() = remember {
    mutableStateOf(true)
}
