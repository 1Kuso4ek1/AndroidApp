package com.example.practice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

data class FormUiState(
    val group: String = "",
    val retrofit: Retrofit = Retrofit.Builder()
                            .baseUrl("https://run.mocky.io")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build(),
    var selectedDate: LocalDateTime = LocalDateTime.now(),
    val api: Schedule? = retrofit.create(Schedule::class.java),
    var response: ScheduleResponse? = null
)

val FormUiState.successValidated: Boolean get() = group.length in 4..6
