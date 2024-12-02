package com.example.practice

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

data class ScheduleResponse(
    @SerializedName("group")
    val group: String,
    @SerializedName("schedule")
    val schedule: List<DailySchedule>
)

data class DailySchedule(
    @SerializedName("day")
    val day: String,
    @SerializedName("lessons")
    val lessons: List<Lesson>
)

data class Lesson(
    @SerializedName("subject")
    val subject: String,
    @SerializedName("teacher")
    val teacher: String
)

interface Schedule {
    @GET("/v3/c239fcfa-cf45-404e-8856-f527e3b06914/table")
    fun get(): Call<ScheduleResponse>
}