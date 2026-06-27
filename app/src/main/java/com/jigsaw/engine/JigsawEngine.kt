package com.jigsaw.engine

import com.jigsaw.domain.model.Difficulty
import com.jigsaw.domain.model.GameStatus
import com.jigsaw.domain.model.JigsawGame
import com.jigsaw.domain.model.JigsawLevel
import kotlin.math.abs
import kotlin.random.Random

object JigsawEngine {

    fun createInitialGame(level: JigsawLevel): JigsawGame = JigsawGame(
        level = level,
        tiles = level.initialTiles
    )

    fun solvedTiles(gridSize: Int): List<Int> {
        val count = gridSize * gridSize
        return (1 until count).toList() + 0
    }

    fun emptyIndex(tiles: List<Int>): Int = tiles.indexOf(0)

    fun canSlide(game: JigsawGame, tileIndex: Int): Boolean {
        val size = game.level.gridSize
        if (tileIndex !in game.tiles.indices) return false
        if (game.tiles[tileIndex] == 0) return false
        val empty = emptyIndex(game.tiles)
        val tileRow = tileIndex / size
        val tileCol = tileIndex % size
        val emptyRow = empty / size
        val emptyCol = empty % size
        return (tileRow == emptyRow && abs(tileCol - emptyCol) == 1) ||
            (tileCol == emptyCol && abs(tileRow - emptyRow) == 1)
    }

    fun slide(game: JigsawGame, tileIndex: Int): JigsawGame {
        if (!canSlide(game, tileIndex)) return game
        val tiles = game.tiles.toMutableList()
        val empty = emptyIndex(tiles)
        tiles[empty] = tiles[tileIndex]
        tiles[tileIndex] = 0
        val won = isSolved(tiles, game.level.gridSize)
        return game.copy(
            tiles = tiles,
            moves = game.moves + 1,
            status = if (won) GameStatus.COMPLETED else game.status,
            completedAt = if (won) System.currentTimeMillis() else game.completedAt,
            lastPlayedAt = System.currentTimeMillis()
        )
    }

    fun onTileSelected(game: JigsawGame, tileIndex: Int): JigsawGame = slide(game, tileIndex)

    fun isSolved(tiles: List<Int>, gridSize: Int): Boolean = tiles == solvedTiles(gridSize)

    fun isSolved(game: JigsawGame): Boolean = isSolved(game.tiles, game.level.gridSize)

    fun isWon(game: JigsawGame): Boolean = game.isCompleted || isSolved(game)

    fun shuffle(gridSize: Int, seed: Long, moves: Int): List<Int> {
        val random = Random(seed)
        var tiles = solvedTiles(gridSize)
        var game = JigsawGame(
            level = JigsawLevel(seed = seed, levelNumber = 1, difficulty = Difficulty.EASY, gridSize = gridSize, initialTiles = tiles),
            tiles = tiles
        )
        repeat(moves) {
            val legal = tiles.indices.filter { canSlide(game, it) }
            if (legal.isEmpty()) return@repeat
            val pick = legal[random.nextInt(legal.size)]
            game = slide(game, pick)
            tiles = game.tiles
        }
        if (isSolved(tiles, gridSize)) {
            return shuffle(gridSize, seed + 1, moves)
        }
        return tiles
    }

    fun getHintMove(game: JigsawGame): Int? {
        if (isWon(game)) return null
        val solved = solvedTiles(game.level.gridSize)
        val size = game.level.gridSize
        return game.tiles.indices.firstOrNull { index ->
            if (!canSlide(game, index)) return@firstOrNull false
            val trial = slide(game, index)
            manhattanDistance(trial.tiles, solved, size) < manhattanDistance(game.tiles, solved, size)
        } ?: game.tiles.indices.firstOrNull { canSlide(game, it) }
    }

    fun applyHint(game: JigsawGame): JigsawGame {
        val hint = getHintMove(game) ?: return game
        return slide(game, hint).copy(hintsUsed = game.hintsUsed + 1)
    }

    fun validateLevel(level: JigsawLevel): Boolean {
        if (level.gridSize !in 3..5) return false
        val expected = level.gridSize * level.gridSize
        if (level.initialTiles.size != expected) return false
        if (level.initialTiles.toSet().size != expected) return false
        if (!level.initialTiles.all { it in 0 until expected }) return false
        return !isSolved(level.initialTiles, level.gridSize)
    }

    fun optimalMoveCount(game: JigsawGame): Int {
        val solved = solvedTiles(game.level.gridSize)
        return manhattanDistance(game.tiles, solved, game.level.gridSize).coerceAtLeast(1)
    }

    fun gridSizeFor(difficulty: Difficulty): Int = when (difficulty) {
        Difficulty.BEGINNER, Difficulty.EASY -> 3
        Difficulty.MEDIUM, Difficulty.HARD -> 4
        Difficulty.EXPERT, Difficulty.MASTER, Difficulty.ENDLESS -> 5
    }

    fun shuffleMovesFor(difficulty: Difficulty): Int = when (difficulty) {
        Difficulty.BEGINNER -> 20
        Difficulty.EASY -> 40
        Difficulty.MEDIUM -> 80
        Difficulty.HARD -> 120
        Difficulty.EXPERT -> 160
        Difficulty.MASTER -> 200
        Difficulty.ENDLESS -> 100
    }

    private fun manhattanDistance(tiles: List<Int>, solved: List<Int>, size: Int): Int {
        var total = 0
        tiles.forEachIndexed { index, value ->
            if (value == 0) return@forEachIndexed
            val goalIndex = solved.indexOf(value)
            total += abs(index / size - goalIndex / size) + abs(index % size - goalIndex % size)
        }
        return total
    }
}
