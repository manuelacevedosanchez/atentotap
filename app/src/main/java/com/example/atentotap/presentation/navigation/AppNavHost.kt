package com.example.atentotap.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.atentotap.domain.model.Winner
import com.example.atentotap.presentation.screens.game.GameRoute
import com.example.atentotap.presentation.screens.howto.HowToPlayScreen
import com.example.atentotap.presentation.screens.menu.MenuScreen
import com.example.atentotap.presentation.screens.mode.ModeSelectionScreen
import com.example.atentotap.presentation.screens.mode.ModeTypeUi
import com.example.atentotap.presentation.screens.result.ResultScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestination.Menu.route,
        modifier = modifier,
    ) {
        composable(route = AppDestination.Menu.route) {
            MenuScreen(
                onPlayClick = { navController.navigate(AppDestination.ModeSelection.route) },
                onHowToPlayClick = { navController.navigate(AppDestination.HowToPlay.route) },
            )
        }

        composable(route = AppDestination.ModeSelection.route) {
            ModeSelectionScreen(
                onStartMatch = { mode, value ->
                    val modeArg = if (mode == ModeTypeUi.TIME) "time" else "score"
                    navController.navigate(AppDestination.Game.createRoute(modeArg, value))
                },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(route = AppDestination.HowToPlay.route) {
            HowToPlayScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = AppDestination.Game.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("value") { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val modeArg = backStackEntry.arguments?.getString("mode") ?: "score"
            val valueArg = backStackEntry.arguments?.getInt("value") ?: 5

            GameRoute(
                modeArg = modeArg,
                valueArg = valueArg,
                onBackToModeSelection = { navController.popBackStack() },
                onGameFinished = { player1Score, player2Score, winner ->
                    navController.navigate(
                        AppDestination.Result.createRoute(
                            player1Score = player1Score,
                            player2Score = player2Score,
                            winner = winner.name,
                            mode = modeArg,
                            value = valueArg,
                        )
                    ) {
                        popUpTo(AppDestination.Game.route) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = AppDestination.Result.route,
            arguments = listOf(
                navArgument("p1") { type = NavType.IntType },
                navArgument("p2") { type = NavType.IntType },
                navArgument("winner") { type = NavType.StringType },
                navArgument("mode") { type = NavType.StringType },
                navArgument("value") { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val player1Score = backStackEntry.arguments?.getInt("p1") ?: 0
            val player2Score = backStackEntry.arguments?.getInt("p2") ?: 0
            val winner = backStackEntry.arguments?.getString("winner")?.let {
                Winner.valueOf(it)
            } ?: Winner.DRAW
            val modeArg = backStackEntry.arguments?.getString("mode") ?: "score"
            val valueArg = backStackEntry.arguments?.getInt("value") ?: 5

            ResultScreen(
                winnerText = when (winner) {
                    Winner.PLAYER_1 -> "Player 1 wins"
                    Winner.PLAYER_2 -> "Player 2 wins"
                    Winner.DRAW -> "Draw"
                },
                scoreText = "Final score: $player1Score - $player2Score",
                onPlayAgain = {
                    navController.navigate(AppDestination.Game.createRoute(modeArg, valueArg)) {
                        popUpTo(AppDestination.ModeSelection.route)
                    }
                },
                onBackToMenu = {
                    navController.navigate(AppDestination.Menu.route) {
                        popUpTo(AppDestination.Menu.route) { inclusive = true }
                    }
                },
            )
        }
    }
}

