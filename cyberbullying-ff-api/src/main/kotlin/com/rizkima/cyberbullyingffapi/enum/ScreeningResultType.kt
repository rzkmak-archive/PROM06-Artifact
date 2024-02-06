package com.rizkima.cyberbullyingffapi.enum

enum class ScreeningResultType {
    GREEN,
    RED
}

enum class ScreeningResultTwitterType(
        val value: String,
        val type: ScreeningResultType
) {
    `OAG-GEN`("OAG-GEN", ScreeningResultType.RED),
    `OAG-NGEN`("OAG-NGEN", ScreeningResultType.RED),
    `NAG-GEN`("NAG-GEN", ScreeningResultType.GREEN),
    `NAG-NGEN`("NAG-NGEN", ScreeningResultType.GREEN),
    `CAG-GEN`("CAG-GEN", ScreeningResultType.RED),
    `CAG-NGEN`("CAG-NGEN", ScreeningResultType.RED)
}