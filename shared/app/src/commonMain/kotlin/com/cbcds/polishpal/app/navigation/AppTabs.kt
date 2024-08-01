package com.cbcds.polishpal.app.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cbcds.polishpal.core.navigation.AppTab
import com.cbcds.polishpal.core.navigation.NestedNavigator
import com.cbcds.polishpal.feature.settings.navigation.SettingsScreen
import com.cbcds.polishpal.feature.vocabulary.navigation.VocabularyScreen
import com.cbcds.polishpal.shared.app.Res
import com.cbcds.polishpal.shared.app.nav_title_main
import com.cbcds.polishpal.shared.app.nav_title_settings
import com.cbcds.polishpal.shared.app.nav_title_vocabulary
import com.cbcds.polishpal.shared.core.ui.ic_book_outline
import com.cbcds.polishpal.shared.core.ui.ic_home_outline
import com.cbcds.polishpal.shared.core.ui.ic_settings_outline
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

internal data object VocabularyTab : AppTab() {

    override val options: TabOptions
        @Composable
        get() = getTabOptions(
            index = 0u,
            titleRes = Res.string.nav_title_vocabulary,
            iconRes = uiRes.drawable.ic_book_outline,
        )

    @Composable
    override fun Content() {
        NestedNavigator(VocabularyScreen)
    }
}

internal data object MainTab : AppTab() {

    override val options: TabOptions
        @Composable
        get() = getTabOptions(
            index = 1u,
            titleRes = Res.string.nav_title_main,
            iconRes = uiRes.drawable.ic_home_outline,
        )

    @Composable
    override fun Content() {
        NestedNavigator(MainScreen)
    }
}

internal data object SettingsTab : AppTab() {

    override val options: TabOptions
        @Composable
        get() = getTabOptions(
            index = 2u,
            titleRes = Res.string.nav_title_settings,
            iconRes = uiRes.drawable.ic_settings_outline,
        )

    @Composable
    override fun Content() {
        NestedNavigator(SettingsScreen)
    }
}
