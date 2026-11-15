package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.data.OngoingProjectEntity
import com.example.data.TaskEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.PartnerHeader
import com.example.ui.components.PriorityBadge
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun UpdatesScreen(
  tasks: List<TaskEntity>,
  projects: List<OngoingProjectEntity>,
  onTaskCheckChanged: (TaskEntity, Boolean) -> Unit,
  onTaskClick: (TaskEntity) -> Unit,
  onStartNowClick: (TaskEntity) -> Unit,
  onSettingsClick: () -> Unit,
  onFocusClick: () -> Unit
) {
  val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
  val currentDateStr = dateFormat.format(Date())

  val activeTasks = tasks.filter { !it.isCompleted }
  val dailyPriorities = activeTasks.filter { it.isDailyPriority }
  val pinnedProjects = projects.filter { it.isPinned && it.status != "Completed" }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MainBackground)
  ) {
    PartnerHeader(
      title = "Good morning! ☀️",
      subtitle = "Let's take today one step at a time. • $currentDateStr",
      onSettingsClick = onSettingsClick,
      onFocusClick = onFocusClick
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Start Now Banner
      if (activeTasks.isNotEmpty()) {
        item {
          val topTask = activeTasks.first()
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("start_now_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LightOrange),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, Peach)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    tint = PrimaryOrange,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Start Now",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange
                  )
                }
                PriorityBadge(priority = topTask.priority)
              }
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = topTask.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
              )
              if (topTask.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = topTask.description,
                  style = MaterialTheme.typography.bodyMedium,
                  color = SecondaryText
                )
              }
              Spacer(modifier = Modifier.height(16.dp))
              Button(
                onClick = { onStartNowClick(topTask) },
                colors = ButtonDefaults.buttonColors(
                  containerColor = PrimaryOrange,
                  contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("btn_start_now")
              ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Focus on this now", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // Daily Three Priorities
      if (dailyPriorities.isNotEmpty()) {
        item {
          Text(
            text = "Daily 3 Priorities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
          )
        }
        items(dailyPriorities) { task ->
          TaskCard(task = task, onCheckChanged = onTaskCheckChanged, onTaskClick = onTaskClick)
        }
      }

      // Pinned Ongoing Projects
      if (pinnedProjects.isNotEmpty()) {
        item {
          Text(
            text = "Pinned Ongoing Projects",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
          )
        }
        items(pinnedProjects) { project ->
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
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = LightOrange
                ) {
                  Text(
                    text = "${project.progressPercentage}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = PrimaryOrange,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                  )
                }
              }
              Spacer(modifier = Modifier.height(10.dp))
              LinearProgressIndicator(
                progress = { project.progressPercentage / 100f },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(8.dp)
                  .clip(RoundedCornerShape(4.dp)),
                color = PrimaryOrange,
                trackColor = SubtleSurface,
              )
            }
          }
        }
      }

      // Today's Tasks
      item {
        Text(
          text = "Today's Tasks & Schedule",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryText
        )
      }

      if (activeTasks.isEmpty()) {
        item {
          EmptyStateCard(
            message = "All caught up!",
            submessage = "You have no pending tasks right now. Take a well-deserved break!",
            icon = Icons.Default.CheckCircle
          )
        }
      } else {
        items(activeTasks) { task ->
          TaskCard(task = task, onCheckChanged = onTaskCheckChanged, onTaskClick = onTaskClick)
        }
      }
    }
  }
}

@Composable
fun TaskCard(
  task: TaskEntity,
  onCheckChanged: (TaskEntity, Boolean) -> Unit,
  onTaskClick: (TaskEntity) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onTaskClick(task) },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = CardBackground),
    border = BorderStroke(1.dp, BorderColor)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Checkbox(
        checked = task.isCompleted,
        onCheckedChange = { checked -> onCheckChanged(task, checked) },
        colors = CheckboxDefaults.colors(checkedColor = PrimaryOrange, uncheckedColor = MutedText),
        modifier = Modifier.testTag("checkbox_task_${task.id}")
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = task.title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.SemiBold,
          color = if (task.isCompleted) SecondaryText else PrimaryText,
          textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
        )
        if (task.description.isNotEmpty()) {
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = task.description,
            style = MaterialTheme.typography.bodySmall,
            color = SecondaryText,
            maxLines = 1
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
          if (task.deadline.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(imageVector = Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = SecondaryText)
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = task.deadline, style = MaterialTheme.typography.bodySmall, color = SecondaryText)
            }
          }
          PriorityBadge(priority = task.priority)
        }
      }
    }
  }
}
