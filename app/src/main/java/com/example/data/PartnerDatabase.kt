package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "tasks")
data class TaskEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val title: String,
  val description: String = "",
  val startDate: String = "",
  val deadline: String = "",
  val startTime: String = "",
  val endTime: String = "",
  val priority: String = "Medium", // High, Medium, Low
  val category: String = "College",
  val isCompleted: Boolean = false,
  val isPinned: Boolean = false,
  val isDailyPriority: Boolean = false,
  val reminderMinutes: Int = 0,
  val recurrence: String = "None", // None, Daily, Weekly, Monthly
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ongoing_projects")
data class OngoingProjectEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val title: String,
  val description: String = "",
  val startDate: String = "",
  val targetDate: String = "",
  val category: String = "Learning",
  val progressPercentage: Int = 0,
  val status: String = "In Progress", // Not Started, In Progress, On Hold, Completed
  val isPinned: Boolean = false,
  val createdAt: Long = System.currentTimeMillis(),
  val completedAt: Long? = null
)

@Entity(tableName = "achievements")
data class AchievementEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val title: String,
  val category: String = "General",
  val date: String = "",
  val description: String = "",
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notes")
data class NoteEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val title: String,
  val content: String,
  val category: String = "Personal Thoughts", // Personal Thoughts, Startup Ideas, College, Business Ideas, Future Plans, Random Ideas, Important Notes
  val tags: String = "",
  val isPinned: Boolean = false,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0L,
  val taskTitle: String,
  val durationMinutes: Int,
  val completedAt: Long = System.currentTimeMillis()
)

@Dao
interface PartnerDao {
  // Tasks
  @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, deadline ASC")
  fun getAllTasks(): Flow<List<TaskEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTask(task: TaskEntity): Long

  @Update
  suspend fun updateTask(task: TaskEntity)

  @Delete
  suspend fun deleteTask(task: TaskEntity)

  // Ongoing Projects
  @Query("SELECT * FROM ongoing_projects ORDER BY status ASC, targetDate ASC")
  fun getAllOngoingProjects(): Flow<List<OngoingProjectEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProject(project: OngoingProjectEntity): Long

  @Update
  suspend fun updateProject(project: OngoingProjectEntity)

  @Delete
  suspend fun deleteProject(project: OngoingProjectEntity)

  // Achievements
  @Query("SELECT * FROM achievements ORDER BY createdAt DESC")
  fun getAllAchievements(): Flow<List<AchievementEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAchievement(achievement: AchievementEntity): Long

  @Delete
  suspend fun deleteAchievement(achievement: AchievementEntity)

  // Notes
  @Query("SELECT * FROM notes ORDER BY isPinned DESC, updatedAt DESC")
  fun getAllNotes(): Flow<List<NoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNote(note: NoteEntity): Long

  @Update
  suspend fun updateNote(note: NoteEntity)

  @Delete
  suspend fun deleteNote(note: NoteEntity)

  // Focus Sessions
  @Query("SELECT * FROM focus_sessions ORDER BY completedAt DESC")
  fun getAllFocusSessions(): Flow<List<FocusSessionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFocusSession(session: FocusSessionEntity): Long
}

@Database(
  entities = [
    TaskEntity::class,
    OngoingProjectEntity::class,
    AchievementEntity::class,
    NoteEntity::class,
    FocusSessionEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class PartnerDatabase : RoomDatabase() {
  abstract fun partnerDao(): PartnerDao

  companion object {
    @Volatile private var INSTANCE: PartnerDatabase? = null

    fun getDatabase(context: Context): PartnerDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          PartnerDatabase::class.java,
          "partner_database"
        )
          .addCallback(DatabaseCallback())
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback : Callback() {
      override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
        super.onCreate(db)
        // Seed initial data if needed
      }
    }
  }
}

class PartnerRepository(private val dao: PartnerDao) {
  val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()
  val allProjects: Flow<List<OngoingProjectEntity>> = dao.getAllOngoingProjects()
  val allAchievements: Flow<List<AchievementEntity>> = dao.getAllAchievements()
  val allNotes: Flow<List<NoteEntity>> = dao.getAllNotes()
  val allFocusSessions: Flow<List<FocusSessionEntity>> = dao.getAllFocusSessions()

  suspend fun insertTask(task: TaskEntity) = dao.insertTask(task)
  suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)
  suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)

  suspend fun insertProject(project: OngoingProjectEntity) = dao.insertProject(project)
  suspend fun updateProject(project: OngoingProjectEntity) = dao.updateProject(project)
  suspend fun deleteProject(project: OngoingProjectEntity) = dao.deleteProject(project)

  suspend fun insertAchievement(achievement: AchievementEntity) = dao.insertAchievement(achievement)
  suspend fun deleteAchievement(achievement: AchievementEntity) = dao.deleteAchievement(achievement)

  suspend fun insertNote(note: NoteEntity) = dao.insertNote(note)
  suspend fun updateNote(note: NoteEntity) = dao.updateNote(note)
  suspend fun deleteNote(note: NoteEntity) = dao.deleteNote(note)

  suspend fun insertFocusSession(session: FocusSessionEntity) = dao.insertFocusSession(session)
}
