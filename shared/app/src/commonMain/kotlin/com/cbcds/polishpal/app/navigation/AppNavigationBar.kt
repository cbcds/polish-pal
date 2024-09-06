package com.cbcds.polishpal.app.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.cbcds.polishpal.core.navigation.AppTab

@Composable
internal fun AppNavigationBar() {
    NavigationBar {
        TabNavigationItem(VocabularyTab)
        TabNavigationItem(MainTab)
        TabNavigationItem(SettingsTab)
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: AppTab) {
    val tabNavigator = LocalTabNavigator.current

    val selected = tabNavigator.current.key == tab.key

    NavigationBarItem(
        selected = selected,
        onClick = { tabNavigator.current = tab },
        icon = {
            val painter = if (selected) {
                tab.appTabOptions.selectedIcon
            } else {
                tab.appTabOptions.unselectedIcon
            }
            Icon(
                painter = painter,
                contentDescription = tab.appTabOptions.title,
                modifier = Modifier.size(28.dp),
            )
        },
        label = { Text(tab.appTabOptions.title) },
    )
}
