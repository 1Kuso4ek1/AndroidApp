package com.example.practice

import android.content.Context
import android.content.SharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class FormUiState(
    val group: String = "",
    val retrofit: Retrofit = Retrofit.Builder()
                            .baseUrl("https://run.mocky.io")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build(),

    var selectedDate: LocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 20)),
    val api: Schedule? = retrofit.create(Schedule::class.java),
    var response: ScheduleResponse? = null,

    val sharedPreferences: SharedPreferences? = null,

    val timetable: List<LocalDateTime> = listOf(
        LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 30)),
        LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 10)),
        LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 40)),
        LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 40)),
        LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 20)),
        LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0)),
        LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 40))
    )
)

val FormUiState.successValidated: Boolean get() = group.length in 4..6
