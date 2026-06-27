package com.jigsaw.domain.repository

import com.jigsaw.domain.model.Achievement
import com.jigsaw.domain.model.ChallengeRecord
import com.jigsaw.domain.model.ChallengeType
import com.jigsaw.domain.model.JigsawGame
import com.jigsaw.domain.model.JigsawLevel
import com.jigsaw.domain.model.Difficulty
import com.jigsaw.domain.model.EconomyState
import com.jigsaw.domain.model.PuzzleProfile
import com.jigsaw.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun createNewGame(difficulty: Difficulty, levelNumber: Int): JigsawGame
    suspend fun createGameFromSeed(seed: Long, levelNumber: Int, difficulty: Difficulty): JigsawGame
    suspend fun createTutorialGame(tutorialIndex: Int): JigsawGame?
    suspend fun createEndlessGame(wave: Int): JigsawGame
    suspend fun saveGame(game: JigsawGame): Long
    suspend fun getGame(gameId: Long): JigsawGame?
    suspend fun getInProgressGame(): JigsawGame?
    fun observeInProgressGame(): Flow<JigsawGame?>
    suspend fun completeGame(game: JigsawGame): JigsawGame
    suspend fun abandonGame(gameId: Long)
    suspend fun getLevel(seed: Long, levelNumber: Int, difficulty: Difficulty): JigsawLevel
}

interface ChallengeRepository {
    suspend fun getChallenge(type: ChallengeType, key: String): ChallengeRecord?
    suspend fun createChallenge(type: ChallengeType, key: String, difficulty: Difficulty): ChallengeRecord
    suspend fun resolveActiveChallenge(type: ChallengeType): ChallengeRecord
    fun observeActiveChallenge(type: ChallengeType): Flow<ChallengeRecord?>
    suspend fun completeChallenge(record: ChallengeRecord, timeSeconds: Long, moves: Int): ChallengeRecord
    fun observeChallengeHistory(type: ChallengeType): Flow<List<ChallengeRecord>>
    suspend fun getCurrentStreak(type: ChallengeType): Int
    suspend fun getChallengeGame(record: ChallengeRecord): JigsawGame
}

interface ProgressionRepository {
    fun observeStats(): Flow<UserStats>
    suspend fun getStats(): UserStats
    suspend fun updateStatsAfterGame(game: JigsawGame)
    suspend fun grantChallengeRewards(rewardCoins: Int, rewardXp: Int)
    fun observePuzzleProfile(): Flow<PuzzleProfile>
    suspend fun getPuzzleProfile(): PuzzleProfile
    fun observeAchievements(): Flow<List<Achievement>>
    suspend fun checkAndUnlockAchievements(
        game: JigsawGame,
        sameDevicePlayed: Boolean = false
    ): List<Achievement>
    fun observeEconomy(): Flow<EconomyState>
    suspend fun getEconomy(): EconomyState
    suspend fun spendCoins(amount: Int): Boolean
    suspend fun earnCoins(amount: Int)
    suspend fun unlockTheme(themeId: String): Boolean
}

interface PreferencesRepository {
    fun getUserPreferences(): Flow<com.jigsaw.domain.model.UserPreferences>
    suspend fun updatePreferences(transform: (com.jigsaw.domain.model.UserPreferences) -> com.jigsaw.domain.model.UserPreferences)
    suspend fun getCampaignLevel(difficulty: Difficulty): Int
    suspend fun advanceCampaignLevel(difficulty: Difficulty): Int
}
