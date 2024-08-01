package com.cbcds.polishpal.feature.vocabulary.screen.words

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.component.CheckableFilterChip
import com.cbcds.polishpal.core.ui.component.DropdownFilterChip
import com.cbcds.polishpal.data.model.words.Aspect
import com.cbcds.polishpal.shared.core.grammar.title_aspect
import com.cbcds.polishpal.shared.feature.vocabulary.Res
import com.cbcds.polishpal.shared.feature.vocabulary.filter_aspect_all
import com.cbcds.polishpal.shared.feature.vocabulary.filter_aspect_imperfective
import com.cbcds.polishpal.shared.feature.vocabulary.filter_aspect_perfective
import com.cbcds.polishpal.shared.feature.vocabulary.filter_favorites
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import com.cbcds.polishpal.shared.core.grammar.Res as gramRes

@Composable
internal fun WordFilters(
    onlyFavorite: Boolean,
    onOnlyFavoriteChange: (Boolean) -> Unit,
    aspect: Aspect?,
    onAspectChange: (Aspect?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        FavoritesFilter(onlyFavorite, onOnlyFavoriteChange)
        AspectFilter(aspect, onAspectChange)
    }
}

@Composable
private fun FavoritesFilter(
    onlyFavorite: Boolean,
    onOnlyFavoriteChange: (Boolean) -> Unit,
) {
    CheckableFilterChip(
        selected = onlyFavorite,
        label = stringResource(Res.string.filter_favorites),
        onClick = { onOnlyFavoriteChange(!onlyFavorite) },
    )
}

@Composable
private fun AspectFilter(
    aspect: Aspect?,
    onAspectChange: (Aspect?) -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    DropdownFilterChip(
        selected = aspect != null,
        label = stringResource(aspect.toFilterStringResource()),
        onClick = { showDialog = true },
    )

    if (showDialog) {
        AspectChooserDialog(
            initialSelectedAspect = aspect,
            onDismiss = { selectedAspect ->
                showDialog = false
                onAspectChange(selectedAspect)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AspectChooserDialog(
    initialSelectedAspect: Aspect?,
    onDismiss: (Aspect?) -> Unit,
) {
    var selectedAspect by rememberSaveable { mutableStateOf(initialSelectedAspect) }

    val onDismissRequest = { onDismiss(selectedAspect) }
    val onAspectSelected: (Aspect?) -> Unit = { aspect ->
        selectedAspect = aspect
        onDismissRequest()
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        contentWindowInsets = { BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Bottom) },
    ) {
        val aspects = remember { Aspect.entries.toTypedArray<Aspect?>() + null }

        Column(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .padding(horizontal = 16.dp),
        ) {
            for (aspect in aspects) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .clickable(
                            role = Role.Button,
                            onClick = { onAspectSelected(aspect) },
                        )
                        .fillMaxWidth(),
                ) {
                    RadioButton(
                        selected = aspect == selectedAspect,
                        onClick = { onAspectSelected(aspect) },
                    )
                    Text(stringResource(aspect.toFilterDialogStringResource()))
                }
            }
        }
    }
}

private fun Aspect?.toFilterStringResource(): StringResource {
    return when (this) {
        Aspect.IMPERFECTIVE -> Res.string.filter_aspect_imperfective
        Aspect.PERFECTIVE -> Res.string.filter_aspect_perfective
        null -> gramRes.string.title_aspect
    }
}

private fun Aspect?.toFilterDialogStringResource(): StringResource {
    return when (this) {
        Aspect.IMPERFECTIVE -> Res.string.filter_aspect_imperfective
        Aspect.PERFECTIVE -> Res.string.filter_aspect_perfective
        null -> Res.string.filter_aspect_all
    }
}
