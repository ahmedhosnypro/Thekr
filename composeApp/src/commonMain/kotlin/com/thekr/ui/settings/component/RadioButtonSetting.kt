package com.thekr.ui.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thekr.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import thekr.composeapp.generated.resources.Res
import thekr.composeapp.generated.resources.arabic
import thekr.composeapp.generated.resources.english
import thekr.composeapp.generated.resources.local_ar
import thekr.composeapp.generated.resources.local_en

@Composable
fun RadioButtonSetting(
    selected: Boolean,
    text: String,
    description: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            }
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp
            )
            if (description != null) {
                Text(
                    text = description,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun OptionRadioButtonPreview() {
    AppTheme {
        Surface {
            RadioButtonSetting(
                selected = true,
                text = stringResource(Res.string.english),
                description = stringResource(Res.string.local_en),
            )
        }
    }
}

@Preview
@Composable
fun OptionRadioButtonPreviewRtl() {
    AppTheme {
        Surface {
            RadioButtonSetting(
                selected = false,
                text = stringResource(Res.string.arabic),
                description = stringResource(Res.string.local_ar),
            )
        }
    }
}