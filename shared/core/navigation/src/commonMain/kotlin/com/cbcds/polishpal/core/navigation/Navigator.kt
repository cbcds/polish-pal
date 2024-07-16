package com.cbcds.polishpal.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator

private val currentScreen = mutableStateOf<AppScreen?>(null)

@Composable
fun rememberCurrentScreen(): State<AppScreen?> {
    return remember { currentScreen }
}

@Composable
fun NestedNavigator(screen: AppScreen) {
    Navigator(screen) {
        val navigator = LocalNavigator.current
        navigator.updateCurrentScreen()
        CurrentScreen()
    }
}

private fun Navigator?.updateCurrentScreen() {
    currentScreen.value = this?.lastItem as? AppScreen
}
