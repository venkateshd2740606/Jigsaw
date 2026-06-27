package com.jigsaw.presentation.ui.screens.stats

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jigsaw.R
import com.jigsaw.presentation.ui.util.localizedTitle
import com.jigsaw.presentation.viewmodel.StatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(onNavigateBack: () -> Unit, viewModel: StatsViewModel = hiltViewModel()) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.statistics)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatRow(stringResource(R.string.level_label), stringResource(R.string.level_display, stats.level))
            StatRow(stringResource(R.string.rank_label), stats.rank.localizedTitle())
            Text(stringResource(R.string.stat_summary_title), style = MaterialTheme.typography.titleMedium)
            StatRow(stringResource(R.string.games_played), stats.gamesPlayed.toString())
            StatRow(stringResource(R.string.games_won), stats.gamesWon.toString())
            StatRow(stringResource(R.string.win_rate), "${(stats.winRate * 100).toInt()}%")
            LinearProgressIndicator(
                progress = { stats.winRate.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
            Spacer(Modifier.height(8.dp))
            StatRow(stringResource(R.string.current_streak), stats.currentStreak.toString())
            StatRow(stringResource(R.string.longest_streak), stats.longestStreak.toString())
            StatRow(stringResource(R.string.perfect_games), stats.perfectGames.toString())
            StatRow(stringResource(R.string.pours_total), stats.poursTotal.toString())
            StatRow(stringResource(R.string.endless_high_score), stats.endlessHighScore.toString())
            StatRow(stringResource(R.string.total_xp), stats.xpPoints.toString())
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label)
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}
