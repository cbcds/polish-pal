package com.cbcds.polishpal.feature.settings.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.cbcds.polishpal.core.ui.component.ElevatedCard
import com.cbcds.polishpal.core.ui.component.Switch
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.data.model.settings.Locale
import com.cbcds.polishpal.data.model.settings.Theme
import com.cbcds.polishpal.feature.settings.navigation.AboutAppScreen
import com.cbcds.polishpal.feature.settings.notifications.AskForNotificationPermissions
import com.cbcds.polishpal.shared.core.ui.ic_bell_outline
import com.cbcds.polishpal.shared.core.ui.ic_globe_outline
import com.cbcds.polishpal.shared.core.ui.ic_message_square_outline
import com.cbcds.polishpal.shared.core.ui.ic_share_outline
import com.cbcds.polishpal.shared.core.ui.ic_smartphone_outline
import com.cbcds.polishpal.shared.core.ui.ic_star_otline
import com.cbcds.polishpal.shared.core.ui.ic_sun_outline
import com.cbcds.polishpal.shared.feature.settings.Res
import com.cbcds.polishpal.shared.feature.settings.setting_about_app
import com.cbcds.polishpal.shared.feature.settings.setting_language
import com.cbcds.polishpal.shared.feature.settings.setting_notifications
import com.cbcds.polishpal.shared.feature.settings.setting_notifiy_me_at
import com.cbcds.polishpal.shared.feature.settings.setting_rate_app
import com.cbcds.polishpal.shared.feature.settings.setting_send_feedback
import com.cbcds.polishpal.shared.feature.settings.setting_share_app
import com.cbcds.polishpal.shared.feature.settings.setting_theme
import com.cbcds.polishpal.shared.feature.settings.settings
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.androidx.compose.koinViewModel
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

@Composable
internal fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
    val state = viewModel.uiState.collectAsState().value
    val effect = viewModel.uiEffect.collectAsState().value

    val navigator = LocalNavigator.current

    if (state is SettingsUiState.Loaded) {
        val settings = state.settings
        val appActionsHandler = rememberAppActionsHandler(state.appInfo)

        Column(Modifier.verticalScroll(rememberScrollState())) {
            Image(
                painter = painterResource(Res.drawable.settings),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(132.dp),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 16.dp)
                    .padding(horizontal = 20.dp),
            ) {
                GeneralSettings(
                    theme = settings.theme,
                    onThemeChange = viewModel::setTheme,
                    locale = settings.locale,
                    onLocaleChange = viewModel::setLocale,
                    notificationsEnabled = settings.notificationsEnabled,
                    onNotificationsEnabledChange = viewModel::onNotificationsEnabledChange,
                    notificationsTime = settings.notificationsTime,
                    onNotificationsTimeChange = viewModel::onNotificationsTimeChange,
                )

                AppSettings(
                    onSendFeedbackClick = appActionsHandler::sendFeedback,
                    onShareAppClick = appActionsHandler::shareApp,
                    onRateAppClick = appActionsHandler::rateApp,
                    onAboutAppClick = { navigator?.push(AboutAppScreen) },
                )
            }
        }
    }

    if (effect is SettingsUiEffect.AskForNotificationsPermission) {
        AskForNotificationPermissions(viewModel::onNotificationPermissionsGrantedChange)
    }
}

@Composable
private fun GeneralSettings(
    theme: Theme,
    onThemeChange: (Theme) -> Unit,
    locale: Locale,
    onLocaleChange: (Locale) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    notificationsTime: LocalTime,
    onNotificationsTimeChange: (LocalTime) -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        SettingsItem(
            iconRes = uiRes.drawable.ic_sun_outline,
            titleRes = Res.string.setting_theme,
            control = {
                ThemeMenu(
                    currentTheme = theme,
                    onThemeChange = onThemeChange,
                )
            }
        )
        SettingsDivider()
        SettingsItem(
            iconRes = uiRes.drawable.ic_globe_outline,
            titleRes = Res.string.setting_language,
            control = {
                LanguageMenu(
                    currentLocale = locale,
                    onLocaleChange = onLocaleChange,
                )
            }
        )
        SettingsDivider()
        SettingsItem(
            iconRes = uiRes.drawable.ic_bell_outline,
            titleRes = Res.string.setting_notifications,
            control = {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = onNotificationsEnabledChange,
                )
            }
        )
        SettingsItem(
            iconRes = null,
            titleRes = Res.string.setting_notifiy_me_at,
            control = {
                NotificationsTimePicker(
                    currentNotificationsTime = notificationsTime,
                    onNotificationsTimeChange = onNotificationsTimeChange,
                    enabled = notificationsEnabled,
                )
            }
        )
    }
}

@Composable
private fun AppSettings(
    onSendFeedbackClick: () -> Unit,
    onShareAppClick: () -> Unit,
    onRateAppClick: () -> Unit,
    onAboutAppClick: () -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        SettingsItem(
            iconRes = uiRes.drawable.ic_message_square_outline,
            titleRes = Res.string.setting_send_feedback,
            onClick = onSendFeedbackClick,
        )
        SettingsDivider()
        SettingsItem(
            iconRes = uiRes.drawable.ic_share_outline,
            titleRes = Res.string.setting_share_app,
            onClick = onShareAppClick,
        )
        SettingsDivider()
        SettingsItem(
            iconRes = uiRes.drawable.ic_star_otline,
            titleRes = Res.string.setting_rate_app,
            onClick = onRateAppClick,
        )
        SettingsDivider()
        SettingsItem(
            iconRes = uiRes.drawable.ic_smartphone_outline,
            titleRes = Res.string.setting_about_app,
            onClick = onAboutAppClick,
        )
    }
}

@Composable
private fun SettingsItem(
    iconRes: DrawableResource?,
    titleRes: StringResource,
    onClick: (() -> Unit)? = null,
    control: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = if (onClick != null) {
            Modifier.clickable(
                role = Role.Button,
                onClick = onClick,
            )
        } else {
            Modifier
        }
            .padding(16.dp)
    ) {
        if (iconRes != null) {
            Icon(
                imageVector = vectorResource(iconRes),
                tint = AppTheme.colorScheme.onSurface,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        } else {
            Spacer(Modifier.size(24.dp))
        }

        Text(
            text = stringResource(titleRes),
            style = AppTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
        )

        control?.invoke()
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
}
