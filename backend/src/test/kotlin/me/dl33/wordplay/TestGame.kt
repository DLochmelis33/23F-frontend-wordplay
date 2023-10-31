package me.dl33.wordplay

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.seconds

class TestGame {

    private val dict = object : Dictionary {
        private val words = setOf("aabb", "bbaa", "aacc")
        override fun findMatches(line: String, suffix: String): Set<String> {
            val s = line + suffix
            return buildSet {
                for (i in 1..line.length) {
                    val t = s.takeLast(i + suffix.length)
                    if (t in words) add(t)
                }
            }
        }
    }

    @Test
    fun full() {
        val g = Game(2, 1.seconds, 3, dict, "a")
        assertEquals(0, g.moveCount)
        assertEquals(true, g.isOngoing)
        assertEquals(listOf(0, 0), g.playerScores())

        val m1 = g.tryMove(0, "abb")
        assertIs<MoveResult.Ok>(m1)
        assertEquals("aabb", m1.matchedWord)
        assertEquals(1, m1.scoreAdded)
        assertEquals(1, g.moveCount)
        assertEquals(1, g.currentPlayerIdx)
        assertEquals(true, g.isOngoing)
        assertEquals(listOf(1, 0), g.playerScores())

        val me1 = g.tryMove(0, "x")
        assertIs<MoveResult.NotYourTurn>(me1)
        val me2 = g.tryMove(1, "")
        assertIs<MoveResult.NoWordsMatch>(me2)
        val me3 = g.tryMove(1, "x")
        assertIs<MoveResult.NoWordsMatch>(me3)

        assertEquals(true, g.isOngoing)
        assertEquals(1, g.moveCount)
        assertEquals(listOf(1, 0), g.playerScores())

        val m2 = g.tryMove(1, "aa")
        assertEquals(MoveResult.Ok("bbaa", 2), m2)
        assertEquals(listOf(1, 2), g.playerScores())
        assertEquals(0, g.currentPlayerIdx)

        val me5 = g.tryMove(0, "bb")
        assertIs<MoveResult.AlreadyUsed>(me5)

        val m3 = g.tryMove(0, "cc")
        assertIs<MoveResult.Ok>(m3)
        assertEquals(listOf(3, 2), g.playerScores())

        assertEquals(3, g.moveCount)
        assertEquals(false, g.isOngoing)
        val me6 = g.tryMove(0, "x")
        assertIs<MoveResult.GameIsOver>(me6)
    }

}