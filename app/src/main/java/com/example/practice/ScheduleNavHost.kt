package com.example.practice

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                animationSpec = tween(1000, easing = LinearOutSlowInEasing)
            ) + slideIntoContainer(
                animationSpec = tween(500, easing = EaseOutCubic),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(500, easing = LinearOutSlowInEasing)
            ) + slideOutOfContainer(
                animationSpec = tween(500, easing = EaseOutCubic),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {
        composable<StartupScreen> {
            val context = LocalContext.current.applicationContext
            viewModel.onFormEvent(FormUiEvent.OnApplicationContextChange(context))
            StartupScreen(navController, viewModel)
        }
        composable<ScheduleTableScreen> {
            ScheduleTableScreen(navController, viewModel)
        }
    }
}
