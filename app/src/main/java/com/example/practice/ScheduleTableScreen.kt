package com.example.practice

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun ScheduleTableScreen(navController: NavController, viewModel: ScheduleViewModel = viewModel()) {
    val data = viewModel.formUiState.response
    var selectDate by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.padding(20.dp))
        Text("Расписание", fontSize = 30.sp)
        Spacer(Modifier.padding(5.dp))

        Text(viewModel.formUiState.selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))

        Button(onClick = {
            selectDate = true
        }) {
            Text("Выбрать дату")
        }

        FilePickerDemo()

        if (data != null) {
            Spacer(Modifier.padding(10.dp))
            Text(data.group)
            Spacer(Modifier.padding(10.dp))

            if (selectDate) {
                DatePickerModal(
                    onDateSelected = {
                        selectDate = false
                        viewModel.onFormEvent(
                            FormUiEvent.OnDateChange(
                                Instant.ofEpochMilli(it!!).atZone(ZoneId.systemDefault())
                                    .toLocalDateTime()
                            )
                        )
                    }
                ) { selectDate = false }
            }

            TableExample(data, viewModel)

            Button(onClick = {
                navController.navigateUp()
            }) {
                Text("Назад")
            }
        }
    }
}

@Composable
fun TableExample(data: ScheduleResponse, viewModel: ScheduleViewModel) {
    //ScrollableTabRow(0) {
        LazyColumn(
            modifier = Modifier.height(300.dp),
            contentPadding = PaddingValues(5.dp)
        ) {
            item {
                Box(modifier = Modifier
                    .background(Color(MaterialTheme.colorScheme.surfaceContainerLowest.toArgb()))
                    .fillMaxWidth()
                ) {
                    Text(data.schedule[0].day, fontSize = 20.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopCenter))
                }
            }

            itemsIndexed(data.schedule[0].lessons) { index, lesson ->
                var isHighlighted = false

                if(index < viewModel.formUiState.timetable.size) {
                    val startTime = viewModel.formUiState.timetable[index]
                    val endTime = viewModel.formUiState.timetable[index + 1]

                    isHighlighted = viewModel.formUiState.selectedDate.isAfter(startTime) && viewModel.formUiState.selectedDate.isBefore(endTime)
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (index % 2 == 0 && !isHighlighted)
                            Color(MaterialTheme.colorScheme.background.toArgb() - 0x101010)
                        else if(isHighlighted)
                            Color(0xFF860000)
                        else
                            Color(MaterialTheme.colorScheme.background.toArgb() - 0x070707)
                    )) {
                    Text(
                        (index + 1).toString(),
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterStart),
                        color = MaterialTheme.colorScheme.inversePrimary
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
    //}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun FilePickerDemo() {
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedFile = uri
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                launcher.launch("application/*") // Вы можете указать конкретный тип файла, например "image/*"
            }
        ) {
            Text("Выбрать файл")
        }

        selectedFile?.let {
            Text("Выбранный файл: ${it.path}")
        }
    }
}
