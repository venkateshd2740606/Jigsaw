package com.jigsaw.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jigsaw.R
import com.jigsaw.domain.model.JigsawGame
import com.jigsaw.engine.JigsawEngine

private val tileColors = listOf(
    Color(0xFFEF5350),
    Color(0xFF42A5F5),
    Color(0xFF66BB6A),
    Color(0xFFFFA726),
    Color(0xFFAB47BC),
    Color(0xFF26A69A),
    Color(0xFF5C6BC0),
    Color(0xFFEC407A),
    Color(0xFF8D6E63),
    Color(0xFF78909C),
    Color(0xFFFF7043),
    Color(0xFF9CCC65),
    Color(0xFF29B6F6),
    Color(0xFFBA68C8),
    Color(0xFFFFCA28),
    Color(0xFF26C6DA),
    Color(0xFF7E57C2),
    Color(0xFFFF8A65),
    Color(0xFF66BB6A),
    Color(0xFF42A5F5),
    Color(0xFFEF5350),
    Color(0xFFFFB74D),
    Color(0xFF4DB6AC),
    Color(0xFFA1887F),
    Color(0xFF90A4AE)
)

@Composable
fun JigsawBoard(
    game: JigsawGame,
    reducedMotion: Boolean,
    onTileClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val boardDescription = stringResource(R.string.color_sort)
    val size = game.level.gridSize

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
    canSlide: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (value == 0) {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.15f)
                } else {
                    tileColors[(value - 1) % tileColors.size]
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
            ),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = value.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
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
