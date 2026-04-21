package com.example.atentotap.presentation.navigation

sealed class AppDestination(val route: String) {
    data object Menu : AppDestination("menu")
    data object ModeSelection : AppDestination("mode")
    data object HowToPlay : AppDestination("how-to")

    data object Game : AppDestination("game/{mode}/{value}") {
        fun createRoute(mode: String, value: Int): String = "game/$mode/$value"
    }

    data object Result : AppDestination("result/{p1}/{p2}/{winner}/{mode}/{value}") {
        fun createRoute(
            player1Score: Int,
            player2Score: Int,
            winner: String,
            mode: String,
            value: Int,
        ): String = "result/$player1Score/$player2Score/$winner/$mode/$value"
    }
}

