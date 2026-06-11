package com.messfit.ai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.messfit.ai.data.local.entity.DailyCompletionEntity
import com.messfit.ai.data.local.entity.UserProfileEntity
import com.messfit.ai.data.local.entity.WeeklyMenuEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(profile: UserProfileEntity)
}

@Dao
interface WeeklyMenuDao {
    @Query("SELECT * FROM weekly_menu ORDER BY scannedAt DESC LIMIT 1")
    fun observeLatestMenu(): Flow<WeeklyMenuEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(menu: WeeklyMenuEntity): Long

    @Query("SELECT * FROM weekly_menu ORDER BY scannedAt DESC LIMIT 1")
    suspend fun getLatestMenu(): WeeklyMenuEntity?
}

@Dao
interface DailyCompletionDao {
    @Query("SELECT * FROM daily_completion WHERE date = :date")
    fun observeByDate(date: String): Flow<DailyCompletionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(completion: DailyCompletionEntity)

    @Query("SELECT * FROM daily_completion ORDER BY date DESC LIMIT 7")
    fun observeRecent(): Flow<List<DailyCompletionEntity>>
}
