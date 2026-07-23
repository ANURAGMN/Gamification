package com.anurag.eduai.uikit.avatar.animation

import com.anurag.eduai.uikit.avatar.core.MouthShape
import com.anurag.eduai.uikit.avatar.core.Viseme

/**
 * Ported from Eduapp LipSync.html — English G2P + viseme mapping.
 */
object EduappVisemeMapper {

    data class VisemeFrame(val viseme: Viseme, val durationMs: Long)

    /**
     * A viseme timeline plus the native-time offset (ms from the start) at which each
     * whitespace-delimited word begins. Word offsets let the lip-sync controller re-anchor
     * the timeline to real TTS word-boundary callbacks.
     */
    data class VisemeTimeline(
        val frames: List<VisemeFrame>,
        val wordStartMs: List<Long>
    )

    fun visemeTimelineForText(text: String, speechRate: Float = 0.75f): List<VisemeFrame> =
        visemeTimelineWithWords(text, speechRate).frames

    fun visemeTimelineWithWords(text: String, speechRate: Float = 0.75f): VisemeTimeline {
        val speedMultiplier = 1f / speechRate.coerceIn(0.5f, 1.5f)
        val words = text.split(Regex("\\s+")).filter { it.isNotBlank() }
        val frames = mutableListOf<VisemeFrame>()
        val wordStartMs = mutableListOf<Long>()
        var cumMs = 0L
        words.forEachIndexed { index, word ->
            // Offset (native ms, pre-compression cumulative) where this word begins. Merging
            // same-viseme frames in compressConsecutive never shifts these cumulative offsets.
            wordStartMs.add(cumMs)
            val tokens = englishG2P(word)
            tokens.forEach { token ->
                val (viseme, duration) = englishPhonemeToViseme(token)
                val d = (duration * speedMultiplier).toLong()
                frames.add(VisemeFrame(viseme, d))
                cumMs += d
            }
            // Brief closed mouth between words — avoids a permanently open jaw.
            if (index < words.lastIndex) {
                val d = (70 * speedMultiplier).toLong()
                frames.add(VisemeFrame(Viseme.Closed, d))
                cumMs += d
            }
        }
        return VisemeTimeline(compressConsecutive(frames), wordStartMs)
    }

    fun mouthShapeToViseme(shape: MouthShape): Viseme = when (shape) {
        MouthShape.Closed -> Viseme.Closed
        MouthShape.A -> Viseme.Open
        MouthShape.E -> Viseme.Wide
        MouthShape.I -> Viseme.Wide
        MouthShape.O -> Viseme.Round
        MouthShape.U -> Viseme.Round
        MouthShape.Smile -> Viseme.Rest
    }

    private fun englishG2P(text: String): List<String> {
        val s = text.lowercase().replace(Regex("[^a-z\\s]"), " ")
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < s.length) {
            if (s[i] == ' ') {
                i++
                continue
            }
            val two = s.substring(i, minOf(i + 2, s.length))
            if (two in listOf("ch", "sh", "th", "ng", "ph", "qu", "wh")) {
                tokens.add(two)
                i += 2
            } else {
                tokens.add(s[i].toString())
                i++
            }
        }
        return tokens
    }

    private fun englishPhonemeToViseme(p: String): Pair<Viseme, Int> = when {
        p in listOf("p", "b", "m") -> Viseme.Closed to 140
        p in listOf("f", "v") -> Viseme.FV to 140
        p == "th" -> Viseme.Th to 140
        p in "aeiou" -> when (p) {
            "o", "u" -> Viseme.Round to 220
            "e", "i" -> Viseme.Wide to 200
            else -> Viseme.Open to 240
        }
        else -> Viseme.Rest to 100
    }

    private fun compressConsecutive(frames: List<VisemeFrame>): List<VisemeFrame> {
        if (frames.isEmpty()) return frames
        val result = mutableListOf<VisemeFrame>()
        for (frame in frames) {
            val last = result.lastOrNull()
            if (last != null && last.viseme == frame.viseme) {
                result[result.lastIndex] = last.copy(durationMs = last.durationMs + frame.durationMs)
            } else {
                result.add(frame)
            }
        }
        return result
    }
}
