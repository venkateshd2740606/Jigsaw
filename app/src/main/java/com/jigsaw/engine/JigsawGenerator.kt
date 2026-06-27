package com.jigsaw.engine

import com.jigsaw.domain.model.Difficulty
import com.jigsaw.domain.model.GenerationProfile
import com.jigsaw.domain.model.JigsawLevel

object JigsawGenerator {

    fun generate(
        seed: Long,
        levelNumber: Int,
        difficulty: Difficulty,
        generationProfile: GenerationProfile = GenerationProfile()
    ): JigsawLevel {
        val gridSize = gridSizeForCampaign(difficulty, generationProfile)
        val moves = JigsawEngine.shuffleMovesFor(difficulty) + generationProfile.colorCountModifier * 10
        val tiles = JigsawEngine.shuffle(gridSize, seed, moves.coerceAtLeast(10))
        return JigsawLevel(
            seed = seed,
            levelNumber = levelNumber,
            difficulty = difficulty,
            gridSize = gridSize,
            initialTiles = tiles,
            puzzleIndex = levelNumber,
            isEndless = difficulty == Difficulty.ENDLESS
        )
    }

    fun generateForChallenge(
        seed: Long,
        levelNumber: Int,
        difficulty: Difficulty
    ): JigsawLevel = generate(seed, levelNumber, difficulty)

    fun seedFromLevelNumber(levelNumber: Int, difficulty: Difficulty): Long {
        val difficultyOffset = difficulty.ordinal * 100_000L
        return levelNumber.toLong() * 9973L + difficultyOffset + 42L
    }

    fun formatShareText(seed: Long, levelNumber: Int, difficulty: Difficulty): String {
        val size = JigsawEngine.gridSizeFor(difficulty)
        return "Jigsaw Sliding Puzzle\nSeed: $seed\nLevel: $levelNumber\nGrid: ${size}x$size\nDifficulty: ${difficulty.name}"
    }

    private fun gridSizeForCampaign(difficulty: Difficulty, profile: GenerationProfile): Int {
        val base = JigsawEngine.gridSizeFor(difficulty)
        val modifier = profile.emptyTubeModifier.coerceIn(0, 1)
        return (base + modifier).coerceIn(3, 5)
    }
}
