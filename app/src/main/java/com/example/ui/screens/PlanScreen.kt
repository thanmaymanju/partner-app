package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.OngoingProjectEntity
import com.example.data.TaskEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PlanScreen(
  tasks: List<TaskEntity>,
  projects: List<OngoingProjectEntity>,
  onTaskCheckChanged: (TaskEntity, Boolean) -> Unit,
  onTaskClick: (TaskEntity) -> Unit,
  onAddTaskClick: () -> Unit
) {
  var selectedDate by remember { mutableStateOf("Today") }

  val calendar = Calendar.getInstance()
  val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
  val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
  val numFormat = SimpleDateFormat("d", Locale.getDefault())

  val datePills = remember {
    val list = mutableListOf<Triple<String, String, String>>()
    for (i in 0..14) {
      val dayStr = dayFormat.format(calendar.time)
      val numStr = numFormat.format(calendar.time)
      val fullStr = dateFormat.format(calendar.time)
      list.add(Triple(dayStr, numStr, if (i == 0) "Today" else fullStr))
      calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    list
  }

  val filteredTasks = tasks.filter { task ->
    if (selectedDate == "Today") true else task.deadline == selectedDate || task.startDate == selectedDate
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MainBackground)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Plan & Schedule",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryText
        )
        Text(
          text = "Calendar, deadlines & goals",
          style = MaterialTheme.typography.bodyMedium,
          color = SecondaryText
        )
      }

      FloatingActionButton(
        onClick = onAddTaskClick,
        containerColor = PrimaryOrange,
        contentColor = Color.White,
        modifier = Modifier
          .size(48.dp)
          .testTag("fab_add_task")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
      }
    }

    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(datePills) { (day, num, full) ->
        val selected = selectedDate == full
        val bg = if (selected) PrimaryOrange else CardBackground
        val fg = if (selected) Color.White else PrimaryText

        Surface(
          modifier = Modifier
            .width(64.dp)
            .height(76.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { selectedDate = full },
          color = bg,
          border = BorderStroke(1.dp, if (selected) PrimaryOrange else BorderColor)
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Text(text = day, style = MaterialTheme.typography.labelSmall, color = if (selected) Color.White.copy(alpha = 0.8f) else SecondaryText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = num, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = fg)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Text(
          text = "Duration Goals",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryText
        )
      }

      items(projects) { project ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = CardBackground),
          border = BorderStroke(1.dp, BorderColor)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
              )
              PriorityBadge(priority = project.category)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = project.description,
              style = MaterialTheme.typography.bodySmall,
              color = SecondaryText
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Target: ${project.targetDate}",
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryText
              )
              Text(
                text = "${project.progressPercentage}% completed",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryOrange
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
              progress = { project.progressPercentage / 100f },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = PrimaryOrange,
              trackColor = SubtleSurface
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Scheduled Activities ($selectedDate)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryText
        )
      }

      if (filteredTasks.isEmpty()) {
        item {
          EmptyStateCard(
            message = "No activities on this date",
            submessage = "Tap the + button above to schedule a new task or exam.",
            icon = Icons.Default.EventAvailable
          )
        }
      } else {
        items(filteredTasks) { task ->
          TaskCard(task = task, onCheckChanged = onTaskCheckChanged, onTaskClick = onTaskClick)
        }
      }
    }
  }
}
