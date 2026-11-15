package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun FocusScreen(
  taskTitle: String,
  onSaveSession: (String, Int) -> Unit,
  onBack: () -> Unit
) {
  var selectedDurationMinutes by remember { mutableIntStateOf(25) }
  var timeLeftSeconds by remember { mutableIntStateOf(25 * 60) }
  var isRunning by remember { mutableStateOf(false) }
  var isBreak by remember { mutableStateOf(false) }

  LaunchedEffect(isRunning, timeLeftSeconds) {
    if (isRunning && timeLeftSeconds > 0) {
      delay(1000L)
      timeLeftSeconds -= 1
    } else if (isRunning && timeLeftSeconds == 0) {
      isRunning = false
      if (!isBreak) {
        onSaveSession(taskTitle.ifEmpty { "Focused Work Session" }, selectedDurationMinutes)
      }
    }
  }

  fun setTimer(minutes: Int, breakMode: Boolean) {
    selectedDurationMinutes = minutes
    timeLeftSeconds = minutes * 60
    isRunning = false
    isBreak = breakMode
  }

  val minutes = timeLeftSeconds / 60
  val seconds = timeLeftSeconds % 60
  val timeString = String.format(java.util.Locale.getDefault(), "%02d:%02d", minutes, seconds)
  val progress = 1f - (timeLeftSeconds.toFloat() / (selectedDurationMinutes * 60).toFloat())

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MainBackground)
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack, modifier = Modifier.testTag("focus_back_button")) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
      }
      Text(
        text = if (isBreak) "Break Time ☕" else "Focus Mode 🎯",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = PrimaryText
      )
      Spacer(modifier = Modifier.size(48.dp))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = if (taskTitle.isNotEmpty()) taskTitle else "One task at a time",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = PrimaryOrange
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Eliminate distractions and focus completely.",
        style = MaterialTheme.typography.bodyMedium,
        color = SecondaryText
      )
    }

    Box(
      modifier = Modifier.size(260.dp),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxSize(),
        strokeWidth = 12.dp,
        color = PrimaryOrange,
        trackColor = SubtleSurface
      )
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = timeString,
          style = MaterialTheme.typography.displayLarge,
          fontWeight = FontWeight.Bold,
          fontSize = 54.sp,
          color = PrimaryText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = if (isBreak) "Resting" else "Remaining",
          style = MaterialTheme.typography.bodyMedium,
          color = SecondaryText
        )
      }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      OutlinedButton(onClick = { setTimer(25, false) }, shape = RoundedCornerShape(12.dp)) { Text("25m Focus", color = PrimaryText) }
      OutlinedButton(onClick = { setTimer(5, true) }, shape = RoundedCornerShape(12.dp)) { Text("5m Break", color = PrimaryText) }
      OutlinedButton(onClick = { setTimer(50, false) }, shape = RoundedCornerShape(12.dp)) { Text("50m Focus", color = PrimaryText) }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = { isRunning = !isRunning },
        modifier = Modifier
          .height(56.dp)
          .width(160.dp)
          .testTag("focus_toggle_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
      ) {
        Icon(imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = if (isRunning) "Pause" else "Start", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
      }

      OutlinedButton(
        onClick = {
          isRunning = false
          timeLeftSeconds = selectedDurationMinutes * 60
        },
        modifier = Modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = PrimaryText)
      }
    }

    Spacer(modifier = Modifier.height(8.dp))
  }
}
