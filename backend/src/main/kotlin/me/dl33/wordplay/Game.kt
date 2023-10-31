package me.dl33.wordplay

import kotlin.time.Duration

class Game(
    val playerCount: Int,
    val timePerMove: Duration,
    val moveLimit: Int,
    private val dictionary: Dictionary,
    initialLine: String,
) {
    var line = initialLine
        private set
    private val usedWords = mutableSetOf<String>()
    var moveCount = 0
        private set
    var isOngoing = true
        private set
    val currentPlayerIdx get() = moveCount % playerCount
    private val playerScores = MutableList(playerCount) { 0 }

    fun playerScores(): List<Int> = playerScores

    fun tryMove(playerIdx: Int, suffix: String): MoveResult {
        if (!isOngoing) return MoveResult.GameIsOver
        if (currentPlayerIdx != playerIdx) return MoveResult.NotYourTurn

        if (suffix.isEmpty()) return MoveResult.NoWordsMatch
        val matchingWords = dictionary.findMatches(line, suffix)
        if (matchingWords.isEmpty()) return MoveResult.NoWordsMatch
        val availableWords = matchingWords subtract usedWords
        if (availableWords.isEmpty()) return MoveResult.AlreadyUsed

        val bestWord = availableWords.maxBy { it.length }
        usedWords += bestWord
        val scoreAdded = bestWord.length - suffix.length
        line += suffix
        playerScores[currentPlayerIdx] += scoreAdded
        moveCount++
        if (moveCount == moveLimit) end()

        return MoveResult.Ok(bestWord, scoreAdded)
    }

    fun end() {
        isOngoing = false
    }
}

sealed class MoveResult {
    data object GameIsOver : MoveResult()
    data object NotYourTurn : MoveResult()
    data object NoWordsMatch : MoveResult()
    data object AlreadyUsed : MoveResult()

    // NoWordsLeft would be tragic
    data class Ok(val matchedWord: String, val scoreAdded: Int) : MoveResult()
}
