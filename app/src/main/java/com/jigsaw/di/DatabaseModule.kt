package com.jigsaw.di

import android.content.Context
import androidx.room.Room
import com.jigsaw.data.local.database.JigsawDatabase
import com.jigsaw.data.local.database.dao.AchievementDao
import com.jigsaw.data.local.database.dao.ChallengeDao
import com.jigsaw.data.local.database.dao.EconomyDao
import com.jigsaw.data.local.database.dao.GameDao
import com.jigsaw.data.local.database.dao.ProfileDao
import com.jigsaw.data.local.database.dao.StatsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JigsawDatabase =
        Room.databaseBuilder(context, JigsawDatabase::class.java, "jigsaw.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideGameDao(db: JigsawDatabase): GameDao = db.gameDao()
    @Provides fun provideStatsDao(db: JigsawDatabase): StatsDao = db.statsDao()
    @Provides fun provideAchievementDao(db: JigsawDatabase): AchievementDao = db.achievementDao()
    @Provides fun provideChallengeDao(db: JigsawDatabase): ChallengeDao = db.challengeDao()
    @Provides fun provideEconomyDao(db: JigsawDatabase): EconomyDao = db.economyDao()
    @Provides fun provideProfileDao(db: JigsawDatabase): ProfileDao = db.profileDao()
}
