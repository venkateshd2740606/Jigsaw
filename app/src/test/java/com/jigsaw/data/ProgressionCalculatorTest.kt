package com.jigsaw.data

import com.jigsaw.domain.model.Difficulty
import com.jigsaw.domain.model.GameStatus
import com.jigsaw.engine.JigsawEngine
import com.jigsaw.engine.JigsawGenerator
import com.jigsaw.util.ProgressionCalculator
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressionCalculatorTest {

    @Test
    fun xpForCompletedGame_isPositive() {
        val level = JigsawGenerator.generate(1L, 1, Difficulty.EASY)
        val game = JigsawEngine.createInitialGame(level).copy(status = GameStatus.COMPLETED)
        assertTrue(ProgressionCalculator.xpForGame(game) > 0)
    }

    @Test
    fun xpForGame_withHints_isLowerThanWithoutHints() {
        val level = JigsawGenerator.generate(1L, 1, Difficulty.EASY)
        val withHints = JigsawEngine.createInitialGame(level).copy(hintsUsed = 2, status = GameStatus.COMPLETED)
        val noHints = JigsawEngine.createInitialGame(level).copy(hintsUsed = 0, status = GameStatus.COMPLETED)
        assertTrue(ProgressionCalculator.xpForGame(noHints) >= ProgressionCalculator.xpForGame(withHints))
    }
}
