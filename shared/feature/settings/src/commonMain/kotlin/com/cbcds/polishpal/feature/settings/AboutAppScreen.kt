package com.cbcds.polishpal.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import com.cbcds.polishpal.core.ui.component.TopAppBar
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.shared.feature.settings.Res
import com.cbcds.polishpal.shared.feature.settings.setting_about_app
import org.jetbrains.compose.resources.stringResource

private const val ID = "1SB5lMyPmuk"
private const val LINK = "https://www.youtube.com/watch?v=$ID"

@Composable
internal fun AboutAppScreen() {
    val linkHandler = rememberYoutubeLinkHandler()
    val navigator = LocalNavigator.current

    Scaffold(
        topBar = {
            TopAppBar(
                onBackClick = { navigator?.pop() }
            )
        },
        content = { padding ->
            Text(
                text = LINK,
                modifier = Modifier
                    .padding(padding)
                    .clickable { linkHandler.handle(ID) },
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.setting_about_app),
                style = AppTheme.typography.titleMedium,
            )
        },
        onBackClick = onBackClick,
    )
}

internal interface YoutubeLinkHandler {

    fun handle(id: String)
}

@Composable
internal expect fun rememberYoutubeLinkHandler(): YoutubeLinkHandler
