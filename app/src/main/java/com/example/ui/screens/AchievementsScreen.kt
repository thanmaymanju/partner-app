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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AchievementEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.theme.*

@Composable
fun AchievementsScreen(
  achievements: List<AchievementEntity>,
  onDeleteAchievement: (AchievementEntity) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  val filteredAchievements = achievements.filter {
    it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
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
          text = "Accomplishments",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryText
        )
        Text(
          text = "Every small win matters ✨",
          style = MaterialTheme.typography.bodyMedium,
          color = SecondaryText
        )
      }
    }

    // Search bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      placeholder = { Text("Search wins & milestones...") },
      leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SecondaryText) },
      shape = RoundedCornerShape(14.dp),
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PrimaryOrange,
        unfocusedBorderColor = BorderColor,
        focusedContainerColor = SubtleSurface,
        unfocusedContainerColor = SubtleSurface,
        focusedTextColor = PrimaryText,
        unfocusedTextColor = PrimaryText
      )
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Stats summary card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = LightOrange),
      border = BorderStroke(1.dp, Peach)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "${achievements.size}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = PrimaryOrange)
          Text(text = "Total Wins", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
        }
        Divider(modifier = Modifier
          .height(36.dp)
          .width(1.dp), color = Peach)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "Look how far you've come!", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = PrimaryText)
          Text(text = "Progress is progress.", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(20.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (filteredAchievements.isEmpty()) {
        item {
          EmptyStateCard(
            message = "No achievements recorded yet",
            submessage = "Complete tasks, finish study goals, or complete focus sessions to record your wins here.",
            icon = Icons.Default.EmojiEvents
          )
        }
      } else {
        items(filteredAchievements) { achievement ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            border = BorderStroke(1.dp, BorderColor)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                  Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                  )
                }
                if (achievement.description.isNotEmpty()) {
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "${achievement.category} • ${achievement.date}",
                  style = MaterialTheme.typography.labelSmall,
                  color = SecondaryText
                )
              }

              IconButton(onClick = { onDeleteAchievement(achievement) }) {
                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFFBA1A1A))
              }
            }
          }
        }
      }
    }
  }
}
