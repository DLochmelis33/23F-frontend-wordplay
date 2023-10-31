package me.dl33.wordplay

interface Dictionary {
    /**
     * Returns all words from the dictionary that are suffixes of line+suffix.
     * The set of all words is required, because the best one may be used.
     */
    fun findMatches(line: String, suffix: String): Set<String>
}
