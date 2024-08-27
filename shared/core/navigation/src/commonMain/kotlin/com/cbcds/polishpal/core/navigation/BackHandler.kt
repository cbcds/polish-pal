package com.cbcds.polishpal.core.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(onBack: () -> Unit)
