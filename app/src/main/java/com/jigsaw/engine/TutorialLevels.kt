package com.jigsaw.engine

import com.jigsaw.domain.model.Difficulty
import com.jigsaw.domain.model.JigsawLevel

object TutorialLevels {

    val all: List<JigsawLevel> = listOf(
        tutorialLevel(0, Difficulty.BEGINNER, 3, 15),
        tutorialLevel(1, Difficulty.BEGINNER, 3, 25),
        tutorialLevel(2, Difficulty.EASY, 3, 35),
        tutorialLevel(3, Difficulty.EASY, 4, 50),
        tutorialLevel(4, Difficulty.MEDIUM, 4, 70)
    )

    fun getTutorialLevel(index: Int): JigsawLevel? = all.getOrNull(index)

    private fun tutorialLevel(
        index: Int,
        difficulty: Difficulty,
        gridSize: Int,
        shuffleMoves: Int
    ): JigsawLevel {
        val tiles = JigsawEngine.shuffle(gridSize, index + 1L, shuffleMoves)
        return JigsawLevel(
            seed = index + 1L,
            levelNumber = index + 1,
            difficulty = difficulty,
            gridSize = gridSize,
            initialTiles = tiles,
            puzzleIndex = index + 1,
            isTutorial = true
        )
    }
}
