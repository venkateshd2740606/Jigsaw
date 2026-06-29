package com.jigsaw.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jigsaw.R
import com.jigsaw.domain.model.JigsawGame
import com.jigsaw.engine.JigsawEngine
import com.jigsaw.engine.PuzzleImageCatalog

@Composable
fun JigsawBoard(
    game: JigsawGame,
    reducedMotion: Boolean,
    onTileClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val boardDescription = stringResource(R.string.color_sort)
    val size = game.level.gridSize
    val puzzleIndex = game.level.puzzleIndex
    val imageName = PuzzleImageCatalog.displayNameFor(puzzleIndex)
    val imageRes = PuzzleImageCatalog.drawableFor(puzzleIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { contentDescription = boardDescription },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.sliding_puzzle_hint, size, size),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stringResource(R.string.photo_jigsaw_scene, imageName),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(6.dp)
        ) {
            for (row in 0 until size) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (col in 0 until size) {
                        val index = row * size + col
                        val value = game.tiles[index]
                        val canSlide = JigsawEngine.canSlide(game, index)
                        TileCell(
                            value = value,
                            gridSize = size,
                            imageRes = imageRes,
                            canSlide = canSlide,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(2.dp)
                                .then(
                                    if (value != 0 && canSlide) {
                                        Modifier.clickable { onTileClick(index) }
                                    } else {
                                        Modifier
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TileCell(
    value: Int,
    gridSize: Int,
    imageRes: Int,
    canSlide: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (value == 0) {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .border(
                width = if (canSlide && value != 0) 2.dp else 1.dp,
                color = if (canSlide && value != 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .graphicsLayer { clip = true },
        contentAlignment = Alignment.TopStart
    ) {
        if (value != 0) {
            val solvedRow = (value - 1) / gridSize
            val solvedCol = (value - 1) % gridSize
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val tileW = maxWidth
                val tileH = maxHeight
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(tileW * gridSize, tileH * gridSize)
                        .offset(x = -tileW * solvedCol, y = -tileH * solvedRow)
                )
            }
        }
    }
}

@Composable
fun GameStatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
    )
}
