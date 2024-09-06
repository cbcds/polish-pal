package com.cbcds.polishpal.app.navigation

import androidx.compose.runtime.Composable
import com.cbcds.polishpal.core.navigation.AppTab
import com.cbcds.polishpal.core.navigation.NestedNavigator
import com.cbcds.polishpal.feature.settings.navigation.SettingsScreen
import com.cbcds.polishpal.feature.vocabulary.navigation.VocabularyScreen
import com.cbcds.polishpal.shared.app.Res
import com.cbcds.polishpal.shared.app.nav_title_main
import com.cbcds.polishpal.shared.app.nav_title_settings
import com.cbcds.polishpal.shared.app.nav_title_vocabulary
import com.cbcds.polishpal.shared.core.ui.ic_book_fill
import com.cbcds.polishpal.shared.core.ui.ic_book_outline
import com.cbcds.polishpal.shared.core.ui.ic_home_fill
import com.cbcds.polishpal.shared.core.ui.ic_home_outline
import com.cbcds.polishpal.shared.core.ui.ic_settings_fill
import com.cbcds.polishpal.shared.core.ui.ic_settings_outline
import com.cbcds.polishpal.shared.core.ui.Res as uiRes

internal data object VocabularyTab : AppTab() {

    override val index: UShort = 0u
    override val titleRes = Res.string.nav_title_vocabulary
    override val unselectedIconRes = uiRes.drawable.ic_book_outline
    override val selectedIconRes = uiRes.drawable.ic_book_fill

    @Composable
    override fun Content() {
        NestedNavigator(VocabularyScreen)
    }
}

internal data object MainTab : AppTab() {

    override val index: UShort = 1u
    override val titleRes = Res.string.nav_title_main
    override val unselectedIconRes = uiRes.drawable.ic_home_outline
    override val selectedIconRes = uiRes.drawable.ic_home_fill

    @Composable
    override fun Content() {
        NestedNavigator(MainScreen)
    }
}

internal data object SettingsTab : AppTab() {

    override val index: UShort = 2u
    override val titleRes = Res.string.nav_title_settings
    override val unselectedIconRes = uiRes.drawable.ic_settings_outline
    override val selectedIconRes = uiRes.drawable.ic_settings_fill

    @Composable
    override fun Content() {
        NestedNavigator(SettingsScreen)
    }
}
