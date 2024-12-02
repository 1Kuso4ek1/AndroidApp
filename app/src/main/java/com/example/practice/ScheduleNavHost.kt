package com.example.practice

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.gson.Gson
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable data object StartupScreen
@Serializable data object ScheduleTableScreen

@Composable
fun ScheduleNavHost(navController: NavHostController, viewModel: ScheduleViewModel = viewModel()) {
    NavHost(
        navController = navController,
        startDestination = StartupScreen,
        enterTransition = {
            fadeIn(
                animationSpec = tween(300, easing = LinearOutSlowInEasing)
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseInCubic),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(300, easing = LinearOutSlowInEasing)
            ) + slideOutOfContainer(
                animationSpec = tween(300, easing = EaseOutCubic),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {
        composable<StartupScreen> {
            StartupScreen(navController, viewModel)
        }
        composable<ScheduleTableScreen> {
            ScheduleTableScreen(navController, viewModel)
        }
    }
}
