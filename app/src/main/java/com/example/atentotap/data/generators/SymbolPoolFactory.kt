package com.example.atentotap.data.generators

import com.example.atentotap.domain.model.DecorationType
import com.example.atentotap.domain.model.FillStyle
import com.example.atentotap.domain.model.ShapeType
import com.example.atentotap.domain.model.SymbolColor
import com.example.atentotap.domain.model.SymbolModel

object SymbolPoolFactory {

    fun createPool(): List<SymbolModel> {
        val symbols = mutableListOf<SymbolModel>()
        val rotations = listOf(0f, 45f, 90f, 135f)

        for (shape in ShapeType.entries) {
            for (color in SymbolColor.entries) {
                for (fillStyle in FillStyle.entries) {
                    for (rotation in rotations) {
                        for (decoration in DecorationType.entries) {
                            val id = listOf(shape.name, color.name, fillStyle.name, rotation, decoration.name)
                                .joinToString("-")
                            symbols += SymbolModel(
                                id = id,
                                shapeType = shape,
                                color = color,
                                fillStyle = fillStyle,
                                rotationDegrees = rotation,
                                decoration = decoration,
                            )
                        }
                    }
                }
            }
        }

        return symbols
    }
}

