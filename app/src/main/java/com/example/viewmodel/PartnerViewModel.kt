package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PartnerViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: PartnerRepository

  val tasks: StateFlow<List<TaskEntity>>
  val projects: StateFlow<List<OngoingProjectEntity>>
  val achievements: StateFlow<List<AchievementEntity>>
  val notes: StateFlow<List<NoteEntity>>
  val focusSessions: StateFlow<List<FocusSessionEntity>>

  init {
    val dao = PartnerDatabase.getDatabase(application).partnerDao()
    repository = PartnerRepository(dao)

    tasks = repository.allTasks.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    projects = repository.allProjects.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    achievements = repository.allAchievements.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    notes = repository.allNotes.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    focusSessions = repository.allFocusSessions.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    // Seed initial demo data if empty
    viewModelScope.launch {
      // We can check if tasks is empty and seed
    }
  }

  fun addTask(
    title: String,
    description: String,
    startDate: String,
    deadline: String,
    startTime: String,
    endTime: String,
    priority: String,
    category: String,
    isDailyPriority: Boolean,
    reminderMinutes: Int,
    recurrence: String
  ) {
    viewModelScope.launch {
      repository.insertTask(
        TaskEntity(
          title = title,
          description = description,
          startDate = startDate,
          deadline = deadline,
          startTime = startTime,
          endTime = endTime,
          priority = priority,
          category = category,
          isDailyPriority = isDailyPriority,
          reminderMinutes = reminderMinutes,
          recurrence = recurrence
        )
      )
    }
  }

  fun updateTask(task: TaskEntity) {
    viewModelScope.launch {
      repository.updateTask(task)
      if (task.isCompleted) {
        // Automatically create achievement
        repository.insertAchievement(
          AchievementEntity(
            title = "Completed: ${task.title}",
            category = task.category,
            date = "Today",
            description = task.description.ifEmpty { "Successfully finished task on time." }
          )
        )
      }
    }
  }

  fun deleteTask(task: TaskEntity) {
    viewModelScope.launch {
      repository.deleteTask(task)
    }
  }

  fun addProject(
    title: String,
    description: String,
    startDate: String,
    targetDate: String,
    category: String,
    progressPercentage: Int,
    status: String,
    isPinned: Boolean
  ) {
    viewModelScope.launch {
      repository.insertProject(
        OngoingProjectEntity(
          title = title,
          description = description,
          startDate = startDate,
          targetDate = targetDate,
          category = category,
          progressPercentage = progressPercentage,
          status = status,
          isPinned = isPinned
        )
      )
    }
  }

  fun updateProject(project: OngoingProjectEntity) {
    viewModelScope.launch {
      repository.updateProject(project)
      if (project.status == "Completed" && project.completedAt == null) {
        val updated = project.copy(completedAt = System.currentTimeMillis(), progressPercentage = 100)
        repository.updateProject(updated)
        repository.insertAchievement(
          AchievementEntity(
            title = "Project Completed: ${project.title}",
            category = project.category,
            date = "Today",
            description = "Finished ongoing project successfully."
          )
        )
      }
    }
  }

  fun deleteProject(project: OngoingProjectEntity) {
    viewModelScope.launch {
      repository.deleteProject(project)
    }
  }

  fun addAchievement(title: String, category: String, date: String, description: String) {
    viewModelScope.launch {
      repository.insertAchievement(
        AchievementEntity(
          title = title,
          category = category,
          date = date,
          description = description
        )
      )
    }
  }

  fun deleteAchievement(achievement: AchievementEntity) {
    viewModelScope.launch {
      repository.deleteAchievement(achievement)
    }
  }

  fun addNote(title: String, content: String, category: String, tags: String, isPinned: Boolean) {
    viewModelScope.launch {
      repository.insertNote(
        NoteEntity(
          title = title,
          content = content,
          category = category,
          tags = tags,
          isPinned = isPinned
        )
      )
    }
  }

  fun updateNote(note: NoteEntity) {
    viewModelScope.launch {
      repository.updateNote(note.copy(updatedAt = System.currentTimeMillis()))
    }
  }

  fun deleteNote(note: NoteEntity) {
    viewModelScope.launch {
      repository.deleteNote(note)
    }
  }

  fun addFocusSession(taskTitle: String, durationMinutes: Int) {
    viewModelScope.launch {
      repository.insertFocusSession(
        FocusSessionEntity(
          taskTitle = taskTitle,
          durationMinutes = durationMinutes
        )
      )
      repository.insertAchievement(
        AchievementEntity(
          title = "Focus Session: $taskTitle",
          category = "Productivity",
          date = "Today",
          description = "Completed $durationMinutes minutes of focused work."
        )
      )
    }
  }
}
