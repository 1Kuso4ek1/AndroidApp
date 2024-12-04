package com.example.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

        if (data != null) {
            Spacer(Modifier.padding(10.dp))
            Text(data.group, fontSize = 20.sp)
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

            ScheduleTable(data, viewModel)

            Button(onClick = {
                navController.navigateUp()
            }) {
                Text("Назад")
            }
        } else {
            Text("Загрузка...", fontSize = 30.sp, color = Color(0xFF545454))
        }
    }
}
