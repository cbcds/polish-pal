package com.cbcds.polishpal.core.ui.component

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cbcds.polishpal.core.ui.theme.AppTheme

@Composable
fun DraggableVerticalScrollbar(
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
) {
    VerticalScrollbar(
        style = draggableScrollbarStyle(),
        adapter = rememberScrollbarAdapter(scrollState),
        interactionSource = remember { MutableInteractionSource() },
        modifier = modifier,
    )
}

@Composable
private fun draggableScrollbarStyle() = ScrollbarStyle(
    minimalHeight = 36.dp,
    thickness = 16.dp,
    shape = DraggableScrollbarShape(),
    hoverDurationMillis = 300,
    unhoverColor = AppTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.50f),
    hoverColor = AppTheme.colorScheme.secondary.copy(alpha = 0.90f),
    fixedHeight = true,
    hideWhenIdle = true,
)

@Suppress("MagicNumber")
private class DraggableScrollbarShape : Shape {

    private val arrowPath = Path()
    private val arrowRotationMatrix = Matrix().apply { scale(1f, -1f) }

    private val roundedCornerShape = RoundedCornerShape(8.dp)
    private val roundedRectPath = Path()

    private val scrollbarPath = Path()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        roundedRectPath.apply {
            reset()
            addOutline(roundedCornerShape.createOutline(size, layoutDirection, density))
        }

        val arrowOffsetX = (size.width - 5.5f * density.density) / 2
        val arrowOffsetY = 10f * density.density
        scrollbarPath.apply {
            reset()
            op(
                path1 = roundedRectPath,
                path2 = arrowPath.apply {
                    reset()
                    addArrow(directionUp = true, density.density)
                    translate(Offset(arrowOffsetX, arrowOffsetY))
                },
                operation = PathOperation.Difference,
            )
            op(
                path1 = this,
                path2 = arrowPath.apply {
                    reset()
                    addArrow(directionUp = false, density.density)
                    translate(Offset(arrowOffsetX, size.height - arrowOffsetY))
                },
                operation = PathOperation.Difference,
            )
        }

        return Outline.Generic(scrollbarPath)
    }

    private fun Path.addArrow(
        directionUp: Boolean,
        density: Float,
    ) {
        moveTo(0f * density, 0.7f * density)
        lineTo(2.75f * density, 3.45f * density)
        relativeLineTo(2.75f * density, -2.75f * density)
        lineTo(6.35f * density, 1.55f * density)
        relativeLineTo(-3.6f * density, 3.6f * density)
        relativeLineTo(-3.6f * density, -3.6f * density)
        relativeLineTo(0.85f * density, -0.85f * density)
        close()

        if (directionUp) {
            transform(arrowRotationMatrix)
        }
    }
}
