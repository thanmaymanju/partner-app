package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PartnerNavigationBar(
  currentTab: String,
  onTabSelected: (String) -> Unit
) {
  val tabs = listOf(
    Triple("updates", "Updates", Icons.Default.Home),
    Triple("plan", "Plan", Icons.Default.CalendarToday),
    Triple("ongoing", "Ongoing", Icons.Default.HourglassTop),
    Triple("achievements", "Wins", Icons.Default.EmojiEvents),
    Triple("notes", "Notes", Icons.Default.Book)
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    color = CardBackground,
    tonalElevation = 0.dp,
    border = BorderStroke(1.dp, BorderColor)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      tabs.forEach { (id, label, icon) ->
        val selected = currentTab == id
        val containerColor = if (selected) LightOrange else Color.Transparent
        val contentColor = if (selected) PrimaryOrange else SecondaryText

        Box(
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(containerColor)
            .clickable { onTabSelected(id) }
            .testTag("nav_tab_$id"),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = label,
              tint = contentColor,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = label,
              style = MaterialTheme.typography.labelSmall,
              color = contentColor,
              fontSize = 11.sp,
              fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
          }
        }
      }
    }
  }
}

@Composable
fun PartnerHeader(
  title: String,
  subtitle: String,
  onSettingsClick: () -> Unit,
  onFocusClick: () -> Unit
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
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = PrimaryText,
        fontSize = 26.sp
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = SecondaryText,
        fontSize = 14.sp
      )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      IconButton(
        onClick = onFocusClick,
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(LightOrange)
          .testTag("focus_mode_button")
      ) {
        Icon(
          imageVector = Icons.Default.Timer,
          contentDescription = "Focus Mode",
          tint = PrimaryOrange
        )
      }

      IconButton(
        onClick = onSettingsClick,
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(SubtleSurface)
          .testTag("settings_button")
      ) {
        Icon(
          imageVector = Icons.Default.Settings,
          contentDescription = "Settings",
          tint = PrimaryText
        )
      }
    }
  }
}

@Composable
fun PriorityBadge(priority: String) {
  val (bg, fg) = when (priority.lowercase()) {
    "high" -> Pair(Color(0xFFFFDAD6), Color(0xFF93000A))
    "medium" -> Pair(LightOrange, PrimaryOrange)
    else -> Pair(SubtleSurface, SecondaryText)
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bg,
    modifier = Modifier.padding(vertical = 2.dp)
  ) {
    Text(
      text = priority,
      style = MaterialTheme.typography.labelSmall,
      color = fg,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      fontWeight = FontWeight.SemiBold
    )
  }
}

@Composable
fun EmptyStateCard(message: String, submessage: String, icon: ImageVector) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = CardBackground),
    border = BorderStroke(1.dp, BorderColor)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(36.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(68.dp)
          .clip(CircleShape)
          .background(LightOrange),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = PrimaryOrange,
          modifier = Modifier.size(32.dp)
        )
      }
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = message,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = PrimaryText,
        fontSize = 18.sp
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = submessage,
        style = MaterialTheme.typography.bodyMedium,
        color = SecondaryText,
        fontSize = 14.sp
      )
    }
  }
}
