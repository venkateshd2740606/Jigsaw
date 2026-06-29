package com.jigsaw.engine

import com.jigsaw.R

data class PuzzleImageEntry(
    val drawableResId: Int,
    val displayName: String
)

object PuzzleImageCatalog {

    const val IMAGE_COUNT = 8

    private val catalog: List<PuzzleImageEntry> = listOf(
        PuzzleImageEntry(R.drawable.puzzle_sunset, "Sunset"),
        PuzzleImageEntry(R.drawable.puzzle_mountain, "Mountain"),
        PuzzleImageEntry(R.drawable.puzzle_ocean, "Ocean"),
        PuzzleImageEntry(R.drawable.puzzle_forest, "Forest"),
        PuzzleImageEntry(R.drawable.puzzle_flower, "Flower"),
        PuzzleImageEntry(R.drawable.puzzle_city, "City"),
        PuzzleImageEntry(R.drawable.puzzle_tiger, "Tiger"),
        PuzzleImageEntry(R.drawable.puzzle_peacock, "Peacock")
    )

    fun entryFor(puzzleIndex: Int): PuzzleImageEntry =
        catalog[puzzleIndex.coerceIn(0, catalog.lastIndex)]

    fun drawableFor(puzzleIndex: Int): Int = entryFor(puzzleIndex).drawableResId

    fun displayNameFor(puzzleIndex: Int): String = entryFor(puzzleIndex).displayName
}
