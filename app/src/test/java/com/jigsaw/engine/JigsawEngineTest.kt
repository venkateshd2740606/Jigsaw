package com.jigsaw.engine

import com.jigsaw.domain.model.Difficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class JigsawEngineTest {

    @Test
    fun tutorialLevel_isValid() {
        val level = TutorialLevels.getTutorialLevel(0)!!
        assertTrue(JigsawEngine.validateLevel(level))
    }

    @Test
    fun canSlide_allowsAdjacentTileOnly() {
        val level = TutorialLevels.getTutorialLevel(0)!!
        val game = JigsawEngine.createInitialGame(level)
        val empty = JigsawEngine.emptyIndex(game.tiles)
        val size = level.gridSize
        val adjacent = listOf(
            empty - size,
            empty + size,
            if (empty % size > 0) empty - 1 else -1,
            if (empty % size < size - 1) empty + 1 else -1
        ).filter { it in game.tiles.indices && game.tiles[it] != 0 }
        assertTrue(adjacent.isNotEmpty())
        adjacent.forEach { index ->
            assertTrue(JigsawEngine.canSlide(game, index))
        }
        val nonAdjacent = game.tiles.indices.first { it !in adjacent && game.tiles[it] != 0 }
        assertFalse(JigsawEngine.canSlide(game, nonAdjacent))
    }

    @Test
    fun slide_movesTileIntoEmptySlot() {
        val level = TutorialLevels.getTutorialLevel(0)!!
        val game = JigsawEngine.createInitialGame(level)
        val movable = game.tiles.indices.first { JigsawEngine.canSlide(game, it) }
        val emptyBefore = JigsawEngine.emptyIndex(game.tiles)
        val tileValue = game.tiles[movable]
        val updated = JigsawEngine.slide(game, movable)
        assertEquals(1, updated.moves)
        assertEquals(0, updated.tiles[movable])
        assertEquals(tileValue, updated.tiles[emptyBefore])
    }

    @Test
    fun solvedState_matchesOrderedTiles() {
        val solved = JigsawEngine.solvedTiles(3)
        assertTrue(JigsawEngine.isSolved(solved, 3))
        val shuffled = solved.toMutableList()
        shuffled[0] = shuffled[1].also { shuffled[1] = shuffled[0] }
        assertFalse(JigsawEngine.isSolved(shuffled, 3))
    }

    @Test
    fun generator_usesCampaignGridSizes() {
        val easy = JigsawGenerator.generate(1L, 1, Difficulty.EASY)
        val medium = JigsawGenerator.generate(2L, 1, Difficulty.MEDIUM)
        val expert = JigsawGenerator.generate(3L, 1, Difficulty.EXPERT)
        assertEquals(3, easy.gridSize)
        assertEquals(4, medium.gridSize)
        assertEquals(5, expert.gridSize)
    }

    @Test
    fun generatedLevel_isValid() {
        val level = JigsawGenerator.generate(12345L, 1, Difficulty.EASY)
        assertTrue(JigsawEngine.validateLevel(level))
    }
}
