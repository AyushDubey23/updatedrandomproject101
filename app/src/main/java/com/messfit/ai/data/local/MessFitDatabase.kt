package com.messfit.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.messfit.ai.data.local.dao.DailyCompletionDao
import com.messfit.ai.data.local.dao.UserProfileDao
import com.messfit.ai.data.local.dao.WeeklyMenuDao
import com.messfit.ai.data.local.entity.DailyCompletionEntity
import com.messfit.ai.data.local.entity.UserProfileEntity
import com.messfit.ai.data.local.entity.WeeklyMenuEntity

@Database(
    entities = [UserProfileEntity::class, WeeklyMenuEntity::class, DailyCompletionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MessFitDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun weeklyMenuDao(): WeeklyMenuDao
    abstract fun dailyCompletionDao(): DailyCompletionDao
}
