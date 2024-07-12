package com.cbcds.polishpal.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cbcds.polishpal.app.MainScreen
import com.cbcds.polishpal.shared.app.Res
import com.cbcds.polishpal.shared.app.nav_title_main
import com.cbcds.polishpal.shared.app.nav_title_settings
import com.cbcds.polishpal.shared.app.nav_title_vocabulary
import com.cbcds.polishpal.shared.core.ui.ic_book_outline
import com.cbcds.polishpal.shared.core.ui.ic_home_outline
import com.cbcds.polishpal.shared.core.ui.ic_settings_outline
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

sealed class AppTab : Tab {

    @Composable
    protected fun getTabOptions(
        index: UShort,
        titleRes: StringResource,
        iconRes: DrawableResource,
    ): TabOptions {
        val title = stringResource(titleRes)
        val icon = rememberVectorPainter(vectorResource(iconRes))

        return remember {
            TabOptions(
                index = index,
                title = title,
                icon = icon,
            )
        }
    }
}

data object VocabularyTab : AppTab() {

    override val options: TabOptions
        @Composable
        get() = getTabOptions(
            index = 0u,
            titleRes = Res.string.nav_title_vocabulary,
            iconRes = uiRes.drawable.ic_book_outline,
        )

    @Composable
    override fun Content() {
        Text("Vocabulary screen")
    }
}

data object MainTab : AppTab() {

    override val options: TabOptions
        @Composable
        get() = getTabOptions(
            index = 1u,
            titleRes = Res.string.nav_title_main,
            iconRes = uiRes.drawable.ic_home_outline,
        )

    @Composable
    override fun Content() {
        MainScreen()
    }
}

data object SettingsTab : AppTab() {

    override val options: TabOptions
        @Composable
        get() = getTabOptions(
            index = 2u,
            titleRes = Res.string.nav_title_settings,
            iconRes = uiRes.drawable.ic_settings_outline,
        )

    @Composable
    override fun Content() {
        Text("Settings screen")
    }
}
