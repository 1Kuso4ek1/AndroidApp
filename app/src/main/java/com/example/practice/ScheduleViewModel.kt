package com.example.practice

import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime

class ScheduleViewModel : ViewModel() {
    var formUiState by mutableStateOf(FormUiState())
        private set

    fun onFormEvent(formEvent: FormUiEvent) {
        when (formEvent) {
            is FormUiEvent.OnGroupChange -> {
                formUiState = formUiState.copy(group = formEvent.group.uppercase())
            }
            is FormUiEvent.ViewScheduleClicked -> {
                saveSharedPreferences()
                makeRequest()

                formEvent.navController.navigate(ScheduleTableScreen)
                //formEvent.navController.navigate(OrderSucceeded("${formUiState.name} - ${formUiState.phone}"))
            }
            is FormUiEvent.OnDateChange -> {
                var dateTime = formEvent.date
                if(dateTime.toLocalDate() == LocalDate.now())
                    dateTime = LocalDateTime.now()
                formUiState = formUiState.copy(selectedDate = dateTime)
                makeRequest()
            }
            is FormUiEvent.OnApplicationContextChange -> {
                if(formUiState.sharedPreferences == null) {
                    formUiState = formUiState.copy(
                        sharedPreferences = formEvent.applicationContext.getSharedPreferences(
                            "sharedPreferences",
                            Context.MODE_PRIVATE
                        )
                    )
                    loadSharedPreferences()
                }
            }
        }
    }

    private fun makeRequest() {
        formUiState.api?.get()?.enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                if(response.isSuccessful) {
                    formUiState = formUiState.copy(response = response.body()!!.copy())
                }
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                println("Error: ${t.message}")
                //formEvent.navController.navigate(ScheduleTableScreen(response.body()!!))
            }
        })
    }

    private fun saveSharedPreferences() {
        val prefs = formUiState.sharedPreferences
        if(prefs != null) {
            val editor = prefs.edit()
            editor.putString("group", formUiState.group)

            editor.apply()
        }
    }

    private fun loadSharedPreferences() {
        val prefs = formUiState.sharedPreferences
        if (prefs != null) {
            formUiState = prefs.getString("group", "")?.let { formUiState.copy(group = it) }!!
        }
    }
}