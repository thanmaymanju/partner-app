package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.NoteEntity
import com.example.ui.components.EmptyStateCard
import com.example.ui.theme.*

@Composable
fun NotesScreen(
  notes: List<NoteEntity>,
  onAddNote: (String, String, String, String, Boolean) -> Unit,
  onUpdateNote: (NoteEntity) -> Unit,
  onDeleteNote: (NoteEntity) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("All") }
  var showAddDialog by remember { mutableStateOf(false) }
  var editingNote by remember { mutableStateOf<NoteEntity?>(null) }

  val categories = listOf("All", "Personal Thoughts", "Startup Ideas", "College", "Business Ideas", "Important Notes")

  val filteredNotes = notes.filter { note ->
    val matchesSearch = note.title.contains(searchQuery, ignoreCase = true) || note.content.contains(searchQuery, ignoreCase = true)
    val matchesCategory = selectedCategory == "All" || note.category == selectedCategory
    matchesSearch && matchesCategory
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
          text = "Notes & Ideas",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = PrimaryText
        )
        Text(
          text = "Your thoughts, organized in one place.",
          style = MaterialTheme.typography.bodyMedium,
          color = SecondaryText
        )
      }

      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = PrimaryOrange,
        contentColor = Color.White,
        modifier = Modifier
          .size(48.dp)
          .testTag("fab_add_note")
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
      }
    }

    // Search bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
      placeholder = { Text("Search notes and ideas...") },
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

    Spacer(modifier = Modifier.height(12.dp))

    // Categories
    ScrollableTabRow(
      selectedTabIndex = categories.indexOf(selectedCategory),
      edgePadding = 20.dp,
      containerColor = Color.Transparent,
      divider = {}
    ) {
      categories.forEach { cat ->
        Tab(
          selected = selectedCategory == cat,
          onClick = { selectedCategory = cat },
          text = { Text(cat, fontWeight = FontWeight.SemiBold, color = if (selectedCategory == cat) PrimaryOrange else SecondaryText) }
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (filteredNotes.isEmpty()) {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(20.dp)
      ) {
        item {
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            EmptyStateCard(
              message = "Your ideas start here.",
              submessage = "Save your thoughts, plans and ideas in one place.",
              icon = Icons.Default.Book
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = { showAddDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange, contentColor = Color.White),
              shape = RoundedCornerShape(14.dp)
            ) {
              Text("Create your first note", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    } else {
      LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp
      ) {
        items(filteredNotes) { note ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { editingNote = note },
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
                  text = note.title,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = PrimaryText,
                  maxLines = 1
                )
                IconButton(
                  onClick = { onUpdateNote(note.copy(isPinned = !note.isPinned)) },
                  modifier = Modifier.size(24.dp)
                ) {
                  Icon(
                    imageVector = if (note.isPinned) Icons.Default.PushPin else Icons.Default.PinDrop,
                    contentDescription = "Pin",
                    tint = if (note.isPinned) PrimaryOrange else SecondaryText,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryText,
                maxLines = 5
              )
              Spacer(modifier = Modifier.height(12.dp))
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = LightOrange
              ) {
                Text(
                  text = note.category,
                  style = MaterialTheme.typography.labelSmall,
                  color = PrimaryOrange,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }

  // Add/Edit Note Dialog
  if (showAddDialog || editingNote != null) {
    val noteToEdit = editingNote
    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var content by remember { mutableStateOf(noteToEdit?.content ?: "") }
    var category by remember { mutableStateOf(noteToEdit?.category ?: "Personal Thoughts") }

    AlertDialog(
      onDismissRequest = {
        showAddDialog = false
        editingNote = null
      },
      title = { Text(if (noteToEdit == null) "New Note" else "Edit Note", color = PrimaryText) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryOrange, unfocusedBorderColor = BorderColor)
          )
          OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content / Thoughts") },
            modifier = Modifier
              .fillMaxWidth()
              .height(140.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryOrange, unfocusedBorderColor = BorderColor)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank()) {
              if (noteToEdit == null) {
                onAddNote(title, content, category, "", false)
              } else {
                onUpdateNote(noteToEdit.copy(title = title, content = content, category = category))
              }
              showAddDialog = false
              editingNote = null
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange, contentColor = Color.White)
        ) {
          Text("Save")
        }
      },
      dismissButton = {
        Row {
          if (noteToEdit != null) {
            TextButton(onClick = {
              onDeleteNote(noteToEdit)
              editingNote = null
            }) {
              Text("Delete", color = Color(0xFFBA1A1A))
            }
          }
          TextButton(onClick = {
            showAddDialog = false
            editingNote = null
          }) {
            Text("Cancel", color = SecondaryText)
          }
        }
      },
      containerColor = CardBackground
    )
  }
}
