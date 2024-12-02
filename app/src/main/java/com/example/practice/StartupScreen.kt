package com.example.practice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

//@Preview(showBackground = true)
@Composable
fun StartupScreen(navController: NavController, viewModel: ScheduleViewModel = viewModel()) {
    ContactInformation(
        group = viewModel.formUiState.group,
        onGroupChange = { viewModel.onFormEvent(FormUiEvent.OnGroupChange(it)) },
        fetchSchedule = { viewModel.onFormEvent(FormUiEvent.ViewScheduleClicked(navController)) },
        isValidGroup = viewModel.formUiState.successValidated
    )
}

@Composable
fun ContactInformation(
    group: String,
    onGroupChange: (String) -> Unit,
    fetchSchedule: () -> Unit,
    isValidGroup: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.padding(20.dp))
        Text("Расписание", fontSize = 30.sp)
        Spacer(Modifier.padding(5.dp))
        TextField(
            label = {
                Text("Группа")
            },
            value = group,
            onValueChange = onGroupChange
        )
        Spacer(Modifier.padding(5.dp))
        Button(
            onClick = {
                fetchSchedule()
            },
            enabled = isValidGroup
        ) {
            Text("Посмотреть расписание")
        }
    }
}
