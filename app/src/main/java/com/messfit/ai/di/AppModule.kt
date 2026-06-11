package com.messfit.ai.di

import android.content.Context
import androidx.room.Room
import com.messfit.ai.data.local.MessFitDatabase
import com.messfit.ai.data.local.dao.DailyCompletionDao
import com.messfit.ai.data.local.dao.UserProfileDao
import com.messfit.ai.data.local.dao.WeeklyMenuDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MessFitDatabase =
        Room.databaseBuilder(context, MessFitDatabase::class.java, "messfit_db").build()

    @Provides fun provideUserProfileDao(db: MessFitDatabase): UserProfileDao = db.userProfileDao()
    @Provides fun provideWeeklyMenuDao(db: MessFitDatabase): WeeklyMenuDao = db.weeklyMenuDao()
    @Provides fun provideDailyCompletionDao(db: MessFitDatabase): DailyCompletionDao = db.dailyCompletionDao()
}
