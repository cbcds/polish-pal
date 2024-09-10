package com.cbcds.polishpal.feature.settings.screen.about

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.cbcds.polishpal.core.ui.animation.SlideHorizontallyTransition
import com.cbcds.polishpal.core.ui.component.TopAppBar
import com.cbcds.polishpal.core.ui.theme.AppTheme
import com.cbcds.polishpal.feature.settings.model.AppInfo
import com.cbcds.polishpal.shared.feature.settings.Res
import com.cbcds.polishpal.shared.feature.settings.acknowledgement_sjp
import com.cbcds.polishpal.shared.feature.settings.acknowledgement_sjp_link
import com.cbcds.polishpal.shared.feature.settings.acknowledgements
import com.cbcds.polishpal.shared.feature.settings.app_version
import com.cbcds.polishpal.shared.feature.settings.laptop
import com.cbcds.polishpal.shared.feature.settings.setting_about_app
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.get

@Composable
internal fun AboutAppScreen(
    appInfo: AppInfo = get(),
) {
    val visibleState = remember { MutableTransitionState(initialState = false) }

    SlideHorizontallyTransition(visibleState) {
        Scaffold(
            topBar = {
                val navigator = LocalNavigator.current
                TopAppBar(
                    onBackClick = { navigator?.pop() }
                )
            },
            content = { padding ->
                AboutAppContent(
                    appInfo = appInfo,
                    modifier = Modifier.padding(padding),
                )
            },
        )
    }

    LaunchedEffect(true) {
        visibleState.targetState = true
    }
}

@Composable
private fun AboutAppContent(
    appInfo: AppInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "${stringResource(Res.string.app_version)} ${appInfo.applicationVersion}",
            style = AppTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(horizontal = 12.dp),
        )

        Text(
            text = stringResource(Res.string.acknowledgements),
            style = AppTheme.typography.titleMedium,
            modifier = Modifier
                .padding(top = 48.dp)
                .padding(horizontal = 12.dp),
        )
        Text(
            text = getSJPAcknowledgementText(),
            style = AppTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 28.dp)
                .padding(horizontal = 12.dp),
        )

        Image(
            painter = painterResource(Res.drawable.laptop),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 52.dp, bottom = 12.dp)
                .height(132.dp),
        )
    }
}

@Composable
private fun getSJPAcknowledgementText(): AnnotatedString {
    return buildAnnotatedString {
        append("• ${stringResource(Res.string.acknowledgement_sjp)} ")

        val link = stringResource(Res.string.acknowledgement_sjp_link)
        pushLink(LinkAnnotation.Url(link))
        withStyle(SpanStyle(color = AppTheme.colorScheme.primary)) {
            append(link)
        }
        pop()
    }
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
