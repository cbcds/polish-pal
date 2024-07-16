package com.cbcds.polishpal.core.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.shared.core.ui.Res
import com.cbcds.polishpal.shared.core.ui.ic_back_outline
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    onBackClick: () -> Unit,
) {
    TopAppBarDefaults.pinnedScrollBehavior()
    CenterAlignedTopAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_back_outline),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        windowInsets = WindowInsets(0.dp),
    )
}
