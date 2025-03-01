package com.thekr.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.thekr.data.proto.ThemeMode
import com.thekr.data.settings.SettingsDetails
import com.thekr.ui.component.bar.AppTopBar
import com.thekr.ui.component.DefaultHorizontalDivider
import com.thekr.ui.component.bar.HeaderText
import com.thekr.ui.navigation.NavigationActions
import com.thekr.ui.navigation.route.HomeRoute
import com.thekr.ui.settings.component.GroupTitle
import com.thekr.ui.settings.component.SwitchSetting
import com.thekr.ui.settings.language.LanguageSettings
import com.thekr.ui.settings.theme.ThemeModeSetting
import com.thekr.ui.theme.AppTheme
import com.thekr.values.Dimensions.medium
import com.thekr.values.Dimensions.small
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.thekr.resources.Res
import com.thekr.resources.click_sound
import com.thekr.resources.display
import com.thekr.resources.feedback
import com.thekr.resources.fingerprint
import com.thekr.resources.input
import com.thekr.resources.material_you
import com.thekr.resources.screen_always_on
import com.thekr.resources.settings
import com.thekr.resources.sound
import com.thekr.resources.speech_name
import com.thekr.resources.speech_value
import com.thekr.resources.vibration
import com.thekr.resources.volume_key
import com.thekr.ui.component.LocalizedApp
import com.thekr.util.changeLang

@Composable
fun SettingsScreen(
    settingsDetails: SettingsDetails,
    modifier: Modifier = Modifier,
) {
    SettingsBody(
        settingsDetails = settingsDetails, modifier = modifier,
        onSettingUpdate = remember { { SettingActions.update(it) } },
    )
}

@Composable
private fun SettingsBody(
    modifier: Modifier = Modifier,
    settingsDetails: SettingsDetails = SettingsDetails(),
    onSettingUpdate: (SettingsDetails) -> Unit = {},
) {

    Scaffold(
        topBar = {
            AppTopBar(
                title = { HeaderText(stringResource(Res.string.settings)) },
                navigationIcon = {
//                    IconButton(onClick = { NavigationActions.navigateUp(HomeRoute::class) }) {
                    IconButton(onClick = { NavigationActions.navigateUp(HomeRoute.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                settingsDetails = settingsDetails,
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            // display settings
            DisplaySettings(
                settingsDetails = settingsDetails,
                onSettingUpdate = onSettingUpdate,
            )

            // spacer between each category
            DefaultHorizontalDivider()
            Spacer(modifier = Modifier.height(32.dp))

            // output settings
            OutputSettings(
                settingsDetails = settingsDetails,
                onSettingUpdate = onSettingUpdate,
            )

            // spacer between each category
            DefaultHorizontalDivider()
            Spacer(modifier = Modifier.height(32.dp))

            // input settings
            InputSettings(
                settingsDetails = settingsDetails,
                onSettingUpdate = onSettingUpdate,
            )
        }
    }

}

@Composable
fun InputSettings(
    settingsDetails: SettingsDetails = SettingsDetails(),
    onSettingUpdate: (SettingsDetails) -> Unit = {},
) {
    GroupTitle(
        title = stringResource(Res.string.input),
        modifier = Modifier.padding(horizontal = medium)
    )
    Column(
        modifier = Modifier.padding(horizontal = small)
    ) {

        // volume control
        SwitchSetting(
            title = stringResource(Res.string.volume_key),
            value = { settingsDetails.volumeControl },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        volumeControl = !settingsDetails.volumeControl
                    )
                )
            },
        )
        DefaultHorizontalDivider()
        // fingerprint control
        SwitchSetting(
            title = stringResource(Res.string.fingerprint),
            value = { settingsDetails.fingerPrintControl },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        fingerPrintControl = !settingsDetails.fingerPrintControl
                    )
                )
            },
        )
    }
}

@Composable
fun OutputSettings(
    settingsDetails: SettingsDetails = SettingsDetails(),
    onSettingUpdate: (SettingsDetails) -> Unit = {},
) {
    GroupTitle(
        title = stringResource(Res.string.feedback),
        modifier = Modifier.padding(horizontal = medium)
    )
    Column(
        modifier = Modifier.padding(horizontal = small)
    ) {
        // vibration
        SwitchSetting(
            title = stringResource(Res.string.vibration),
            value = { settingsDetails.vibration },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        vibration = !settingsDetails.vibration
                    )
                )
            },
        )

        DefaultHorizontalDivider()
        // sound
        SwitchSetting(
            title = stringResource(Res.string.sound),
            value = { settingsDetails.sound },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        sound = !settingsDetails.sound
                    )
                )
            },
        )

        DefaultHorizontalDivider()
        // click sound
        SwitchSetting(
            title = stringResource(Res.string.click_sound),
            value = { settingsDetails.clickSound },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        clickSound = !settingsDetails.clickSound
                    )
                )
            },
            enabled = settingsDetails.sound,
        )

        DefaultHorizontalDivider()
        // speech value
        SwitchSetting(
            title = stringResource(Res.string.speech_value),
            value = { settingsDetails.speechValue },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        speechValue = !settingsDetails.speechValue
                    )
                )
            },
            enabled = settingsDetails.sound,
        )
        DefaultHorizontalDivider()
        // speech name
        SwitchSetting(
            title = stringResource(Res.string.speech_name),
            value = { settingsDetails.speechName },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        speechName = !settingsDetails.speechName
                    )
                )
            },
            enabled = settingsDetails.sound,
        )
    }
}

@Composable
fun DisplaySettings(
    settingsDetails: SettingsDetails = SettingsDetails(),
    onSettingUpdate: (SettingsDetails) -> Unit = {},
) {
    GroupTitle(
        title = stringResource(Res.string.display),
        modifier = Modifier.padding(horizontal = medium)
    )

    Column(
        modifier = Modifier.padding(horizontal = small)
    ) {
        LanguageSettings(
            selectedLanguage = { settingsDetails.language },
            onLanguageChange = { option ->
                val locale = try {
                    Locale(option.value)
                } catch (e: Exception) {
                    null
                }
                locale?.let {
                    changeLang(it.language)
                }
                onSettingUpdate(
                    settingsDetails.copy(
                        language = option.value
                    )
                )
            },
        )
        DefaultHorizontalDivider()
        // theme mode
        ThemeModeSetting(
            selectedTheme = { settingsDetails.themeMode },
            onThemeChange = {
                onSettingUpdate(
                    settingsDetails.copy(
                        themeMode = it.value
                    )
                )
            },
        )
        DefaultHorizontalDivider()
        // material you
        SwitchSetting(
            title = stringResource(Res.string.material_you),
            value = { settingsDetails.materialYou },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        materialYou = !settingsDetails.materialYou
                    )
                )
            },
        )
        // screen always on
        DefaultHorizontalDivider()
        SwitchSetting(
            title = stringResource(Res.string.screen_always_on),
            value = { settingsDetails.screenAlwaysOn },
            onToggle = {
                onSettingUpdate(
                    settingsDetails.copy(
                        screenAlwaysOn = !settingsDetails.screenAlwaysOn
                    )
                )
            },
        )
    }
}

@Preview
@Composable
fun SettingsBodyPreview() {
    AppTheme {
        Surface {
            LocalizedApp {
                SettingsBody()
            }
        }
    }
}

@Preview
@Composable
fun SettingsBodyPreviewDark() {
    AppTheme(ThemeMode.Dark) {
        Surface {
            LocalizedApp {
                SettingsBody(
                    settingsDetails = SettingsDetails(
                        themeMode = ThemeMode.Dark
                    )
                )
            }
        }
    }
}


