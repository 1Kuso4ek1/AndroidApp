package com.example.practice

import android.content.Context
import androidx.navigation.NavController
import java.time.LocalDateTime

sealed class FormUiEvent {
    data class OnGroupChange(val group: String): FormUiEvent()
    data class OnDateChange(val date: LocalDateTime): FormUiEvent()
    data class ViewScheduleClicked(val navController: NavController): FormUiEvent()
    data class OnApplicationContextChange(val applicationContext: Context): FormUiEvent()
}
