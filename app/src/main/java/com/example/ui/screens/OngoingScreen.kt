package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.example.data.OngoingProjectEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.theme.*

@Composable
fun OngoingScreen(
  projects: List<OngoingProjectEntity>,
  onUpdateProject: (OngoingProjectEntity) -> Unit,
  onDeleteProject: (OngoingProjectEntity) -> Unit,
  onAddProjectClick: () -> Unit
) {
  var selectedFilter by remember { mutableStateOf("All") }
  val filters = listOf("All", "In Progress", "On Hold", "Completed")

  val filteredProjects = projects.filter {
    if (selectedFilter == "All") true else it.status == selectedFilter
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
          text = "Ongoing Projects",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryText
        )
        Text(
          text = "Track your learning, habits & goals",
          style = MaterialTheme.typography.bodyMedium,
          color = SecondaryText
        )
      }

      FloatingActionButton(
        onClick = onAddProjectClick,
        containerColor = PrimaryOrange,
        contentColor = Color.White,
        modifier = Modifier
          .size(48.dp)
          .testTag("fab_add_project")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Project")
      }
    }

    // Filter Tabs
    ScrollableTabRow(
      selectedTabIndex = filters.indexOf(selectedFilter),
      edgePadding = 20.dp,
      containerColor = Color.Transparent,
      divider = {}
    ) {
      filters.forEach { filter ->
        Tab(
          selected = selectedFilter == filter,
          onClick = { selectedFilter = filter },
          text = { Text(filter, fontWeight = FontWeight.SemiBold, color = if (selectedFilter == filter) PrimaryOrange else SecondaryText) }
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      if (filteredProjects.isEmpty()) {
        item {
          EmptyStateCard(
            message = "No ongoing projects found",
            submessage = "Create an ongoing project or study goal to track your progress step by step.",
            icon = Icons.Default.HourglassTop
          )
        }
      } else {
        items(filteredProjects) { project ->
          OngoingProjectCard(
            project = project,
            onUpdate = onUpdateProject,
            onDelete = onDeleteProject
          )
        }
      }
    }
  }
}

@Composable
fun OngoingProjectCard(
  project: OngoingProjectEntity,
  onUpdate: (OngoingProjectEntity) -> Unit,
  onDelete: (OngoingProjectEntity) -> Unit
) {
  var currentProgress by remember { mutableFloatStateOf(project.progressPercentage.toFloat()) }

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
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = project.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
          )
          if (project.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = project.description,
              style = MaterialTheme.typography.bodySmall,
              color = SecondaryText
            )
          }
        }

        IconButton(onClick = {
          val newPinned = !project.isPinned
          onUpdate(project.copy(isPinned = newPinned))
        }) {
          Icon(
            imageVector = if (project.isPinned) Icons.Default.PushPin else Icons.Default.PinDrop,
            contentDescription = "Pin",
            tint = if (project.isPinned) PrimaryOrange else SecondaryText
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = when (project.status) {
            "Completed" -> Color(0xFFE5F4EC)
            "On Hold" -> Color(0xFFFFDAD6)
            else -> LightOrange
          }
        ) {
          Text(
            text = project.status,
            style = MaterialTheme.typography.labelSmall,
            color = when (project.status) {
              "Completed" -> SuccessGreen
              "On Hold" -> Color(0xFF93000A)
              else -> PrimaryOrange
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold
          )
        }

        Text(
          text = "${project.progressPercentage}%",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryOrange
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Slider(
        value = currentProgress,
        onValueChange = { currentProgress = it },
        onValueChangeFinished = {
          val newProg = currentProgress.toInt()
          val newStatus = if (newProg >= 100) "Completed" else "In Progress"
          onUpdate(project.copy(progressPercentage = newProg, status = newStatus))
        },
        valueRange = 0f..100f,
        steps = 10,
        colors = SliderDefaults.colors(thumbColor = PrimaryOrange, activeTrackColor = PrimaryOrange, inactiveTrackColor = SubtleSurface)
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Target: ${project.targetDate}",
          style = MaterialTheme.typography.bodySmall,
          color = SecondaryText
        )

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          if (project.status != "Completed") {
            TextButton(onClick = {
              onUpdate(project.copy(status = "Completed", progressPercentage = 100))
            }) {
              Text("Complete", fontWeight = FontWeight.Bold, color = PrimaryOrange)
            }
          }
          IconButton(onClick = { onDelete(project) }) {
            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFBA1A1A))
          }
        }
      }
    }
  }
}
