package com.cbcds.polishpal.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.cbcds.polishpal.app.navigation.AppNavigationBar
import com.cbcds.polishpal.app.navigation.MainTab
import com.cbcds.polishpal.core.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {
    AppTheme {
        TabNavigator(MainTab) {
            Scaffold(
                content = { padding ->
                    Surface(modifier = Modifier.padding(padding)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    AppNavigationBar()
                }
            )
        }
    }
}
