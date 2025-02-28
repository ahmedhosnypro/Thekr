package com.thekr.ui.home.tab.sebha

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.thekr.values.Dimensions.small
import org.jetbrains.compose.resources.stringResource
import com.thekr.resources.Res
import com.thekr.resources.add_thekr


/** Use to add new Thekr or category */
@Composable
fun SebhaAddNewButton(
    onCLick: () -> Unit,
    text: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = onCLick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = ButtonDefaults.buttonElevation(
                focusedElevation = 1.dp,
                pressedElevation = 1.dp,
                hoveredElevation = 1.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(small)
            ) {
                Text(text = text)
                Icon(
                    Icons.Filled.Create,
                    contentDescription = stringResource(Res.string.add_thekr),
                )
            }
        }
    }
}