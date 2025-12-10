package com.thekr.ui.home


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.add_thekr

@Preview
@Composable
fun AddCounterFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    FloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurface,
        onClick = { onClick() },
    ) {
        Icon(Icons.Filled.Create, stringResource(Res.string.add_thekr))
    }
}
