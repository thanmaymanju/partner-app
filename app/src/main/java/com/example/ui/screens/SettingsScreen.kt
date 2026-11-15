package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
  onBack: () -> Unit
) {
  var notificationsEnabled by remember { mutableStateOf(true) }
  var quietHoursEnabled by remember { mutableStateOf(true) }

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
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        IconButton(onClick = onBack) {
          Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
        }
        Text(
          text = "Settings",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryText
        )
      }
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentPadding = PaddingValues(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Text(text = "Preferences", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryOrange)
      }

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = CardBackground),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(text = "Push Notifications", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = PrimaryText)
                Text(text = "Remind me about deadlines and daily goals", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
              }
              Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryOrange)
              )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = BorderColor)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(text = "Quiet Hours (10 PM - 7 AM)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = PrimaryText)
                Text(text = "Silence reminder notifications during rest time", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
              }
              Switch(
                checked = quietHoursEnabled,
                onCheckedChange = { quietHoursEnabled = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryOrange)
              )
            }
          }
        }
      }

      item {
        Text(text = "About Partner", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryOrange)
      }

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = CardBackground),
          border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "PARTNER — Personal Productivity Companion", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = PrimaryText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Version 1.0 • Built with White & Soft Orange Material 3 & ADHD-friendly design.", style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "\"You don't have to remember everything. PARTNER is here to help.\"", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = PrimaryOrange)
          }
        }
      }
    }
  }
}
