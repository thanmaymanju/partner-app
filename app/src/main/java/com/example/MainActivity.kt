package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.PartnerNavigationBar
import com.example.ui.screens.*
import com.example.ui.theme.PartnerTheme
import com.example.viewmodel.PartnerViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: PartnerViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      PartnerTheme {
        var currentTab by remember { mutableStateOf("updates") }
        var showQuickAdd by remember { mutableStateOf(false) }
        var focusTaskTitle by remember { mutableStateOf("") }

        val tasks by viewModel.tasks.collectAsStateWithLifecycle()
        val projects by viewModel.projects.collectAsStateWithLifecycle()
        val achievements by viewModel.achievements.collectAsStateWithLifecycle()
        val notes by viewModel.notes.collectAsStateWithLifecycle()

        // Handle back button when not on updates tab
        BackHandler(enabled = currentTab != "updates" && currentTab != "focus" && currentTab != "settings") {
          currentTab = "updates"
        }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = {
            if (currentTab != "focus" && currentTab != "settings") {
              PartnerNavigationBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
              )
            }
          },
          floatingActionButton = {
            if (currentTab != "focus" && currentTab != "settings") {
              FloatingActionButton(
                onClick = { showQuickAdd = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Quick Add")
              }
            }
          }
        ) { innerPadding ->
          Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
              "updates" -> UpdatesScreen(
                tasks = tasks,
                projects = projects,
                onTaskCheckChanged = { task, checked ->
                  viewModel.updateTask(task.copy(isCompleted = checked))
                },
                onTaskClick = { task ->
                  viewModel.updateTask(task.copy(isCompleted = !task.isCompleted))
                },
                onStartNowClick = { task ->
                  focusTaskTitle = task.title
                  currentTab = "focus"
                },
                onSettingsClick = { currentTab = "settings" },
                onFocusClick = {
                  focusTaskTitle = ""
                  currentTab = "focus"
                }
              )

              "plan" -> PlanScreen(
                tasks = tasks,
                projects = projects,
                onTaskCheckChanged = { task, checked ->
                  viewModel.updateTask(task.copy(isCompleted = checked))
                },
                onTaskClick = { task ->
                  viewModel.updateTask(task.copy(isCompleted = !task.isCompleted))
                },
                onAddTaskClick = { showQuickAdd = true }
              )

              "ongoing" -> OngoingScreen(
                projects = projects,
                onUpdateProject = { proj -> viewModel.updateProject(proj) },
                onDeleteProject = { proj -> viewModel.deleteProject(proj) },
                onAddProjectClick = { showQuickAdd = true }
              )

              "achievements" -> AchievementsScreen(
                achievements = achievements,
                onDeleteAchievement = { ach -> viewModel.deleteAchievement(ach) }
              )

              "notes" -> NotesScreen(
                notes = notes,
                onAddNote = { title, content, cat, tags, pinned ->
                  viewModel.addNote(title, content, cat, tags, pinned)
                },
                onUpdateNote = { note -> viewModel.updateNote(note) },
                onDeleteNote = { note -> viewModel.deleteNote(note) }
              )

              "focus" -> FocusScreen(
                taskTitle = focusTaskTitle,
                onSaveSession = { title, duration ->
                  viewModel.addFocusSession(title, duration)
                },
                onBack = { currentTab = "updates" }
              )

              "settings" -> SettingsScreen(
                onBack = { currentTab = "updates" }
              )
            }
          }
        }

        if (showQuickAdd) {
          QuickAddBottomSheet(
            onDismiss = { showQuickAdd = false },
            onAddTask = { title, desc, start, deadline, sTime, eTime, priority, category, isDaily, reminder, recurrence ->
              viewModel.addTask(title, desc, start, deadline, sTime, eTime, priority, category, isDaily, reminder, recurrence)
            },
            onAddProject = { title, desc, start, target, category, prog, status, pinned ->
              viewModel.addProject(title, desc, start, target, category, prog, status, pinned)
            }
          )
        }
      }
    }
  }
}
