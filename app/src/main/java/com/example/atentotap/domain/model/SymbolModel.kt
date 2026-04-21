package com.example.atentotap.domain.model

enum class ShapeType {
    CIRCLE,
    SQUARE,
    TRIANGLE,
    DIAMOND,
    STAR,
    HEXAGON,
    CROSS,
    PILL,
}

enum class SymbolColor {
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    CYAN,
    BLUE,
    PURPLE,
    PINK,
}

enum class FillStyle {
    FILLED,
    STROKE,
}

enum class DecorationType {
    NONE,
    DOT,
    LINE,
    PLUS,
}

data class SymbolModel(
    val id: String,
    val shapeType: ShapeType,
    val color: SymbolColor,
    val fillStyle: FillStyle,
    val rotationDegrees: Float,
    val decoration: DecorationType,
)

