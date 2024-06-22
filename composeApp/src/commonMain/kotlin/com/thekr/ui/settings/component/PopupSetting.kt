package com.thekr.ui.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thekr.R
import com.thekr.ui.theme.AppTheme

@Composable
fun PopupSetting(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: @Composable () -> Unit = {},
    onClick: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(
        horizontal = dimensionResource(id = R.dimen.padding_normal),
        vertical = dimensionResource(id = R.dimen.padding_medium),
    ),
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier
            .clickable {
                onClick()
            }
            .padding(paddingValues)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            // setting option title and description
            Column(
                Modifier
                    .weight(.75f)
                    .requiredHeight(48.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)

            ) {
                Text(
                    text = title, style = MaterialTheme.typography.titleMedium
                )
                if (summary != null) {
                    Text(
                        text = summary, style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        content()
    }
}

@Preview
@Composable
fun PreviewPopupSetting() {
    AppTheme {
        Surface {
            PopupSetting(title = "Title",
                summary = "Summary",
                icon = {},
                onClick = {},
                content = {})
        }
    }
}