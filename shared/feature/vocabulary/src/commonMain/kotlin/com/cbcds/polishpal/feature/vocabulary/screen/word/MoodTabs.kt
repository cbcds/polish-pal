package com.cbcds.polishpal.feature.vocabulary.screen.word

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MoodTabs(
    tabsData: List<MoodTabData>,
    pagerState: PagerState,
) {
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = AppTheme.colorScheme.surfaceColorAtElevation(3.dp),
        contentColor = AppTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            if (pagerState.currentPage < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .padding(horizontal = 32.dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                )
            }
        },
        divider = {},
        modifier = Modifier.fillMaxWidth(),
    ) {
        tabsData.forEachIndexed { index, data ->
            Tab(
                selected = pagerState.currentPage == index,
                text = { Text(stringResource(data.titleRes)) },
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}
