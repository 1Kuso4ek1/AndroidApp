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
    @SerializedName("firstLesson")
    val firstLesson: Int,
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
    @GET("/v3/2e70290c-b2a5-43c9-b672-8221136b938a")
    fun get(): Call<ScheduleResponse>
}