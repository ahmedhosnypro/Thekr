package com.thekr.ui.home.bar.top

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.theme.hacenTunisia
import org.jetbrains.compose.resources.stringResource
import thekr.composeapp.generated.resources.Res
import thekr.composeapp.generated.resources.app_name

@Composable
fun SearchUi(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
) {
    TopBarHeaderControls(
        settingsDetails = settingsDetails,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = hacenTunisia(),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

