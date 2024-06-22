package com.thekr.ui.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thekr.ui.component.DefaultVerticalDivider
import com.thekr.ui.theme.AppTheme
import com.thekr.ui.values.Dimensions.medium
import com.thekr.ui.values.Dimensions.normal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SwitchSetting(
    title: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = normal,
        vertical = medium,
    ),
    value: () -> Boolean = { false },
    summary: String? = null,
    onToggle: (Boolean) -> Unit = {},
    enabled: Boolean = true,
    icon: @Composable () -> Unit = { },
) {
    // setting option raw (tile, description and switch)
    Row(
        modifier
            .clickable {
                onToggle(value().not())
            }
            .fillMaxWidth()
            .padding(paddingValues)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // icon
        icon()

        // title and description
        Column(
            Modifier.weight(.75f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            if (summary != null) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        // switch
        DefaultVerticalDivider()
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = value(),
            onCheckedChange = {
                onToggle(it)
            },
            enabled = enabled
        )

    }
}

@Preview
@Composable
fun SwitchSettingPreview() {
    AppTheme {
        Surface {
            SwitchSetting(
                title = "Dark Mode",
                summary = "Enable dark mode",
                icon = {
                    Text(text = "🌙", modifier = Modifier.padding(end = 8.dp))
                },
                value = { true },
                onToggle = { }
            )
        }
    }
}