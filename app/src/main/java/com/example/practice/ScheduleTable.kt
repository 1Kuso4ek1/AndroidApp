package com.example.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

fun isWeekend(date: LocalDateTime): Boolean {
    return date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
}

@Composable
fun ScheduleTable(data: ScheduleResponse, viewModel: ScheduleViewModel) {
    val dayOfWeek = viewModel.formUiState.selectedDate

    LazyColumn(
        modifier = Modifier.height(500.dp),
        contentPadding = PaddingValues(5.dp)
    ) {
        for(i in 0..<data.schedule.size) {
            val nextDay = dayOfWeek.plusDays(i.toLong())
            item {
                Box(
                    modifier = Modifier
                        .background(Color(MaterialTheme.colorScheme.surfaceContainerHighest.toArgb()))
                        .fillMaxWidth()
                ) {
                    Text(
                        /*data.schedule[i].day*/nextDay.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ru")).replaceFirstChar { c -> c.uppercase() }, fontSize = 20.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopCenter),
                        color = if(!isWeekend(nextDay)) Color(MaterialTheme.colorScheme.onSurface.toArgb()) else Color(0xFFFF0000)
                    )
                }
            }
            if(!isWeekend(nextDay)) {
                itemsIndexed(data.schedule[i].lessons) { index, lesson ->
                    var isHighlighted = false

                    if (index < viewModel.formUiState.timetable.size) {
                        val startTime = viewModel.formUiState.timetable[index]
                        val endTime = viewModel.formUiState.timetable[index + 1]

                        isHighlighted =
                            viewModel.formUiState.selectedDate.isAfter(startTime) &&
                                    viewModel.formUiState.selectedDate.isBefore(endTime) &&
                                    i == 0
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (index % 2 == 0 && !isHighlighted)
                                    Color(MaterialTheme.colorScheme.background.toArgb() - 0x101010)
                                else if (isHighlighted)
                                    Color(0x79FF0000)
                                else
                                    Color(MaterialTheme.colorScheme.background.toArgb() - 0x070707)
                            )
                    ) {
                        Text(
                            (index + data.schedule[i].firstLesson).toString(),
                            fontSize = 13.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.CenterStart),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            lesson.subject,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(35.dp, 8.dp)
                                .align(Alignment.CenterStart)
                        )
                        Text(
                            lesson.teacher,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.CenterEnd),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
