package com.jigsaw.multiplayer

import com.jigsaw.domain.model.JigsawGame
import com.jigsaw.domain.model.Difficulty
import com.jigsaw.domain.model.MultiplayerMode
import com.jigsaw.domain.model.MultiplayerSession
import com.jigsaw.engine.JigsawEngine
import com.jigsaw.engine.JigsawGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PuzzleBotSession @Inject constructor() {
    private val _session = MutableStateFlow<MultiplayerSession?>(null)
    val session: StateFlow<MultiplayerSession?> = _session.asStateFlow()

    private var playerGame: JigsawGame? = null
    private var botGame: JigsawGame? = null
    private var playerName = "You"
    private val botName = "AI Bot"

    fun start(player: String, difficulty: Difficulty, seed: Long = System.currentTimeMillis()) {
        playerName = player
        val level = JigsawGenerator.generate(seed, 1, difficulty)
        val game = JigsawEngine.createInitialGame(level)
        playerGame = game
        botGame = game
        _session.value = MultiplayerSession(
            mode = MultiplayerMode.SAME_DEVICE,
            localPlayerName = playerName,
            remotePlayerName = botName,
            activePlayerName = playerName,
            isActive = true,
            seed = seed,
            difficulty = difficulty
        )
    }

    fun getPlayerGame(): JigsawGame? = playerGame

    fun applyPlayerTileClick(tileIndex: Int): JigsawGame? {
        val game = playerGame ?: return null
        val updated = JigsawEngine.onTileSelected(game, tileIndex)
        playerGame = updated
        botGame = updated
        return updated
    }

    fun applyBotMove(): JigsawGame? {
        val game = botGame ?: return null
        val hint = JigsawEngine.getHintMove(game) ?: return game
        val updated = JigsawEngine.onTileSelected(game, hint)
        playerGame = updated
        botGame = updated
        val session = _session.value
        if (session != null) {
            _session.value = session.copy(
                remoteScore = session.remoteScore + if (updated.isCompleted) 1 else 0,
                activePlayerName = playerName
            )
        }
        return updated
    }

    fun onPlayerWon() {
        val session = _session.value ?: return
        _session.value = session.copy(
            localScore = session.localScore + 1,
            activePlayerName = playerName
        )
        startNewRound(session)
    }

    fun onBotWon() {
        val session = _session.value ?: return
        _session.value = session.copy(
            remoteScore = session.remoteScore + 1,
            activePlayerName = playerName
        )
        startNewRound(session)
    }

    private fun startNewRound(session: MultiplayerSession) {
        val newSeed = session.seed + session.localScore + session.remoteScore
        val level = JigsawGenerator.generate(newSeed, session.localScore + session.remoteScore + 1, session.difficulty)
        val game = JigsawEngine.createInitialGame(level)
        playerGame = game
        botGame = game
    }

    fun isBotThinking(): Boolean = false

    fun end() {
        _session.value = null
        playerGame = null
        botGame = null
    }
}
