package com.jigsaw.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jigsaw.data.local.database.dao.AchievementDao
import com.jigsaw.data.local.database.dao.ChallengeDao
import com.jigsaw.data.local.database.dao.EconomyDao
import com.jigsaw.data.local.database.dao.GameDao
import com.jigsaw.data.local.database.dao.ProfileDao
import com.jigsaw.data.local.database.dao.StatsDao
import com.jigsaw.data.local.database.entity.ProfileEntity
import com.jigsaw.data.local.database.entity.AchievementEntity
import com.jigsaw.data.local.database.entity.ChallengeEntity
import com.jigsaw.data.local.database.entity.EconomyEntity
import com.jigsaw.data.local.database.entity.GameEntity
import com.jigsaw.data.local.database.entity.StatsEntity

@Database(
    entities = [
        GameEntity::class,
        StatsEntity::class,
        AchievementEntity::class,
        ChallengeEntity::class,
        EconomyEntity::class,
        ProfileEntity::class
    ],
    version = 3,
    exportSchema = true
)
abstract class JigsawDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun statsDao(): StatsDao
    abstract fun achievementDao(): AchievementDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun economyDao(): EconomyDao
    abstract fun profileDao(): ProfileDao
}
