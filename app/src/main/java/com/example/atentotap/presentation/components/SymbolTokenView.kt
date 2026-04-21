package com.example.atentotap.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.atentotap.domain.model.DecorationType
import com.example.atentotap.domain.model.FillStyle
import com.example.atentotap.domain.model.ShapeType
import com.example.atentotap.domain.model.SymbolColor
import com.example.atentotap.domain.model.SymbolModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun SymbolTokenView(
    symbol: SymbolModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = center
            val drawSize = min(size.width, size.height) * 0.72f
            val strokeWidth = drawSize * 0.12f
            val drawStyle: DrawStyle = if (symbol.fillStyle == FillStyle.FILLED) {
                Fill
            } else {
                Stroke(width = strokeWidth)
            }
            val baseColor = symbol.color.toComposeColor()

            rotate(degrees = symbol.rotationDegrees, pivot = center) {
                when (symbol.shapeType) {
                    ShapeType.CIRCLE -> drawCircle(
                        color = baseColor,
                        radius = drawSize / 2f,
                        center = center,
                        style = drawStyle,
                    )

                    ShapeType.SQUARE -> drawRect(
                        color = baseColor,
                        topLeft = Offset(center.x - drawSize / 2f, center.y - drawSize / 2f),
                        size = Size(drawSize, drawSize),
                        style = drawStyle,
                    )

                    ShapeType.TRIANGLE -> drawPath(
                        path = trianglePath(center, drawSize),
                        color = baseColor,
                        style = drawStyle,
                    )

                    ShapeType.DIAMOND -> drawPath(
                        path = diamondPath(center, drawSize),
                        color = baseColor,
                        style = drawStyle,
                    )

                    ShapeType.STAR -> drawPath(
                        path = starPath(center, drawSize / 2f, drawSize / 4f),
                        color = baseColor,
                        style = drawStyle,
                    )

                    ShapeType.HEXAGON -> drawPath(
                        path = polygonPath(center, drawSize / 2f, 6),
                        color = baseColor,
                        style = drawStyle,
                    )

                    ShapeType.CROSS -> {
                        val thickness = drawSize * 0.28f
                        drawRoundRect(
                            color = baseColor,
                            topLeft = Offset(center.x - thickness / 2f, center.y - drawSize / 2f),
                            size = Size(thickness, drawSize),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(thickness / 2f),
                            style = drawStyle,
                        )
                        drawRoundRect(
                            color = baseColor,
                            topLeft = Offset(center.x - drawSize / 2f, center.y - thickness / 2f),
                            size = Size(drawSize, thickness),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(thickness / 2f),
                            style = drawStyle,
                        )
                    }

                    ShapeType.PILL -> drawRoundRect(
                        color = baseColor,
                        topLeft = Offset(center.x - drawSize / 2f, center.y - drawSize * 0.26f),
                        size = Size(drawSize, drawSize * 0.52f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(drawSize * 0.3f),
                        style = drawStyle,
                    )
                }

                drawDecoration(symbol, drawSize)
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDecoration(
    symbol: SymbolModel,
    drawSize: Float,
) {
    if (symbol.decoration == DecorationType.NONE) return

    val markerColor = when (symbol.fillStyle) {
        FillStyle.FILLED -> Color.White
        FillStyle.STROKE -> symbol.color.toComposeColor()
    }

    when (symbol.decoration) {
        DecorationType.NONE -> Unit
        DecorationType.DOT -> drawCircle(
            color = markerColor,
            radius = drawSize * 0.08f,
            center = center,
        )

        DecorationType.LINE -> drawLine(
            color = markerColor,
            start = Offset(center.x - drawSize * 0.24f, center.y),
            end = Offset(center.x + drawSize * 0.24f, center.y),
            strokeWidth = drawSize * 0.08f,
            cap = StrokeCap.Round,
        )

        DecorationType.PLUS -> {
            drawLine(
                color = markerColor,
                start = Offset(center.x - drawSize * 0.2f, center.y),
                end = Offset(center.x + drawSize * 0.2f, center.y),
                strokeWidth = drawSize * 0.08f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = markerColor,
                start = Offset(center.x, center.y - drawSize * 0.2f),
                end = Offset(center.x, center.y + drawSize * 0.2f),
                strokeWidth = drawSize * 0.08f,
                cap = StrokeCap.Round,
            )
        }
    }
}

private fun SymbolColor.toComposeColor(): Color = when (this) {
    SymbolColor.RED -> Color(0xFFFF5A5F)
    SymbolColor.ORANGE -> Color(0xFFFFA552)
    SymbolColor.YELLOW -> Color(0xFFFFE066)
    SymbolColor.GREEN -> Color(0xFF71DD8A)
    SymbolColor.CYAN -> Color(0xFF49D6FF)
    SymbolColor.BLUE -> Color(0xFF6798FF)
    SymbolColor.PURPLE -> Color(0xFFAE82FF)
    SymbolColor.PINK -> Color(0xFFFF79C6)
}

private fun trianglePath(center: Offset, size: Float): Path {
    val half = size / 2f
    return Path().apply {
        moveTo(center.x, center.y - half)
        lineTo(center.x - half, center.y + half)
        lineTo(center.x + half, center.y + half)
        close()
    }
}

private fun diamondPath(center: Offset, size: Float): Path {
    val half = size / 2f
    return Path().apply {
        moveTo(center.x, center.y - half)
        lineTo(center.x - half, center.y)
        lineTo(center.x, center.y + half)
        lineTo(center.x + half, center.y)
        close()
    }
}

private fun polygonPath(center: Offset, radius: Float, sides: Int): Path {
    val path = Path()
    path.fillType = PathFillType.EvenOdd
    for (index in 0 until sides) {
        val angle = ((2.0 * PI / sides) * index) - (PI / 2.0)
        val x = center.x + (cos(angle) * radius).toFloat()
        val y = center.y + (sin(angle) * radius).toFloat()
        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    return path
}

private fun starPath(center: Offset, outerRadius: Float, innerRadius: Float): Path {
    val path = Path()
    val points = 10
    for (index in 0 until points) {
        val radius = if (index % 2 == 0) outerRadius else innerRadius
        val angle = ((2.0 * PI / points) * index) - (PI / 2.0)
        val x = center.x + (cos(angle) * radius).toFloat()
        val y = center.y + (sin(angle) * radius).toFloat()
        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    return path
}

