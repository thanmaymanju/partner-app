package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddBottomSheet(
  onDismiss: () -> Unit,
  onAddTask: (String, String, String, String, String, String, String, String, Boolean, Int, String) -> Unit,
  onAddProject: (String, String, String, String, String, Int, String, Boolean) -> Unit
) {
  var selectedType by remember { mutableStateOf("Task") }
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var deadline by remember { mutableStateOf("Today") }
  var priority by remember { mutableStateOf("Medium") }
  var category by remember { mutableStateOf("College") }
  var isDailyPriority by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor = CardBackground
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(24.dp)
        .navigationBarsPadding(),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text(
        text = "Create New Entry",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = PrimaryText
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = selectedType == "Task",
          onClick = { selectedType = "Task" },
          label = { Text("Task / Exam") },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = LightOrange,
            selectedLabelColor = PrimaryOrange
          )
        )
        FilterChip(
          selected = selectedType == "Project",
          onClick = { selectedType = "Project" },
          label = { Text("Ongoing Goal") },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = LightOrange,
            selectedLabelColor = PrimaryOrange
          )
        )
      }

      OutlinedTextField(
        value = title,
        onValueChange = { title = it },
        label = { Text(if (selectedType == "Task") "Task Title" else "Project Title") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("input_title"),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = PrimaryOrange,
          unfocusedBorderColor = BorderColor,
          focusedContainerColor = SubtleSurface,
          unfocusedContainerColor = SubtleSurface
        )
      )

      OutlinedTextField(
        value = description,
        onValueChange = { description = it },
        label = { Text("Description & Details") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = PrimaryOrange,
          unfocusedBorderColor = BorderColor,
          focusedContainerColor = SubtleSurface,
          unfocusedContainerColor = SubtleSurface
        )
      )

      if (selectedType == "Task") {
        OutlinedTextField(
          value = deadline,
          onValueChange = { deadline = it },
          label = { Text("Deadline / Date (e.g. Today, Tomorrow)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryOrange,
            unfocusedBorderColor = BorderColor,
            focusedContainerColor = SubtleSurface,
            unfocusedContainerColor = SubtleSurface
          )
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Set as Daily Priority (Top 3)", fontWeight = FontWeight.Medium, color = PrimaryText)
          Switch(
            checked = isDailyPriority,
            onCheckedChange = { isDailyPriority = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryOrange)
          )
        }
      } else {
        OutlinedTextField(
          value = deadline,
          onValueChange = { deadline = it },
          label = { Text("Target Date (e.g. December 16)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryOrange,
            unfocusedBorderColor = BorderColor,
            focusedContainerColor = SubtleSurface,
            unfocusedContainerColor = SubtleSurface
          )
        )
      }

      Button(
        onClick = {
          if (title.isNotBlank()) {
            if (selectedType == "Task") {
              onAddTask(
                title,
                description,
                "Today",
                deadline,
                "09:00 AM",
                "05:00 PM",
                priority,
                category,
                isDailyPriority,
                0,
                "None"
              )
            } else {
              onAddProject(
                title,
                description,
                "Today",
                deadline,
                category,
                0,
                "In Progress",
                false
              )
            }
            onDismiss()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("btn_save_entry"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange, contentColor = Color.White)
      ) {
        Text("Save Entry", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      }
    }
  }
}
