package com.example.practice

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleViewModel : ViewModel() {
    var formUiState by mutableStateOf(FormUiState())
        private set

    fun onFormEvent(formEvent: FormUiEvent) {
        when (formEvent) {
            is FormUiEvent.OnGroupChange -> {
                formUiState = formUiState.copy(group = formEvent.group.uppercase())
            }
            is FormUiEvent.ViewScheduleClicked -> {
                makeRequest()
                formEvent.navController.navigate(ScheduleTableScreen)
                //formEvent.navController.navigate(OrderSucceeded("${formUiState.name} - ${formUiState.phone}"))
            }
            is FormUiEvent.OnDateChange -> {
                formUiState = formUiState.copy(selectedDate = formEvent.date)
                makeRequest()
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
}