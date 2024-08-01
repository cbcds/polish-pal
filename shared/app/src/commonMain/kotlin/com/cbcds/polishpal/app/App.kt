package com.cbcds.polishpal.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.cbcds.polishpal.app.navigation.AppNavigationBar
import com.cbcds.polishpal.app.navigation.MainTab
import com.cbcds.polishpal.core.navigation.rememberCurrentScreen
import com.cbcds.polishpal.core.ui.component.LocalSnackbarHostState
import com.cbcds.polishpal.core.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun App() {
    val viewModel = koinViewModel<AppViewModel>()

    val state = viewModel.uiState.collectAsState().value

    if (state is AppUiState.Loaded) {
        AppTheme(darkTheme = state.darkThemeEnabled ?: isSystemInDarkTheme()) {
            TabNavigator(MainTab) {
                AppScaffold()
            }
        }
    }
}

@Composable
private fun AppScaffold() {
    val currentScreen by rememberCurrentScreen()
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        Scaffold(
            content = { padding ->
                Surface(Modifier.padding(padding)) {
                    CurrentTab()
                }
            },
            bottomBar = {
                if (currentScreen?.fullscreen != true) {
                    AppNavigationBar()
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        )
    }
}
