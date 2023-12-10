package me.dl33.wordplay

class StubDictionary : Dictionary {
    override fun findMatches(line: String, suffix: String): Set<String> {
        return (line + suffix).let { s -> List(10) { s.takeLast(suffix.length + 1 + it) }.toSet() }
    }
}
